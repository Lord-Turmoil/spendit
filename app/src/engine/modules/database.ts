/**
 * @module database
 *
 * This module provide the database operations for the application.
 */

import {
    DbMeta,
    DbTable,
    Entry,
    getDummyDbMeta,
    getDummyDbTable
} from '~/engine/models.js';
import { api, ApiResponse } from '~/extensions/api';
import { formatTimeISO } from '~/utils/format.js';
import { getNative, Native } from '~/utils/native.js';
import { EntryUpdateEvent, EntryUpdateTypes } from '~/engine/events.js';

/*
Below is the file layout of the database.

root/
  |- <user_id>/
  |    |- meta.json
  |    |- <entry_id>.json
  |    `- <entry_id>.json
    `- <user_id>/
         |- meta.json
         |- <entry_id>.json
         `- <entry_id>.json

meta.json:
{
    "entries": [
        "<entry_id>",
        "<entry_id>",
    ]
}

<entry_id>.json:
{
    timestamp:
    entries: [
        {
            ...
        }
    ]
}
*/

interface Hint {
    modify: string[];
    send: string[];
    receive: string[];
}

export class DatabaseModule {
    private meta: DbMeta = getDummyDbMeta();
    private tables: Map<string, DbTable> = new Map();
    private native: Native = getNative();
    private readonly userId: string;

    constructor(userId: string) {
        this.userId = userId;

        setInterval(
            () => {
                this.clearCache();
            },
            1000 * 60 * 5
        ); // clear the cache every 5 minutes
    }

    async init(): Promise<void> {
        await this.loadMeta();
    }

    /**
     * Get the table for the given timestamp (YYYY-MM-DD).
     * @param timestamp The timestamp of the table.
     */
    async getTable(timestamp: string): Promise<DbTable> {
        if (this.tables.has(timestamp)) {
            return Promise.resolve(this.tables.get(timestamp));
        }
        return await this.loadTable(timestamp);
    }

    /**
     * Update the entry with the given event.
     *
     * This is a reducer function that updates the entry based on the event.
     *
     * @param event The event to update the entry.
     */
    async update(event: EntryUpdateEvent): Promise<void> {
        switch (event.type) {
            case EntryUpdateTypes.CREATE:
                await this.handleCreate(event.entry);
                break;
            case EntryUpdateTypes.UPDATE:
                await this.handleUpdate(event.entry);
                break;
            case EntryUpdateTypes.DELETE:
                await this.handleDelete(event.entry);
                break;
            default:
                break;
        }
    }

    private async handleCreate(entry: Entry): Promise<void> {
        const table = await this.getTable(entry.date);
        for (const e of table.entries) {
            if (e.timestamp === entry.timestamp) {
                // prevent multiple entries with the same timestamp.
                console.error('Entry already exists:', e, entry);
                return;
            }
        }
        table.entries.push(entry);
        await this.saveTable(table);
    }

    private async handleUpdate(entry: Entry): Promise<void> {
        const table = await this.getTable(entry.date);
        table.entries = table.entries.map((e) =>
            e.timestamp === entry.timestamp ? entry : e
        );
        await this.saveTable(table);
    }

    private async handleDelete(entry: Entry): Promise<void> {
        const table = await this.getTable(entry.date);
        table.entries = table.entries.filter((e) => e.timestamp !== entry.timestamp);
        await this.saveTable(table);
    }

    private async loadMeta(): Promise<void> {
        let value = await this.native.loadFile(this.getFilePath('meta.json'));
        if (value === null) {
            value = JSON.stringify(getDummyDbMeta());
            await this.saveMeta();
        }
        this.meta = JSON.parse(value);
    }

    /**
     * Load the table from disk with the given timestamp. (YYYY-MM-DD)
     * @param timestamp The timestamp of the table.
     * @private
     */
    private async loadTable(timestamp: string): Promise<DbTable> {
        let table: DbTable;
        if (!this.meta.entries.includes(timestamp)) {
            table = getDummyDbTable(timestamp);
        } else {
            const path = this.getFilePath(`${timestamp}.json`);
            const value = await this.native.loadFile(path);
            if (value !== null) {
                table = JSON.parse(value);
            } else {
                table = getDummyDbTable(timestamp);
            }
        }

        this.tables.set(timestamp, table);

        return Promise.resolve(table);
    }

    private getFilePath(filename: string): string {
        return `${this.userId}/${filename}`;
    }

    /**
     * Save the metadata to disk.
     */
    private async saveMeta(): Promise<void> {
        const path = this.getFilePath('meta.json');
        await this.native.saveFile(path, JSON.stringify(this.meta));
    }

    /**
     * Save the table to disk, and updates the meta if necessary.
     *
     * @param table The table to save.
     * @param keepUpdated If true, the table will be saved to disk, but not updated in the meta.
     */
    private async saveTable(table: DbTable, keepUpdated: boolean = false): Promise<void> {
        // If the table is empty, remove it from the meta and disk.
        if (table.entries.length === 0) {
            this.meta.entries = this.meta.entries.filter((e) => e !== table.timestamp);
            await this.saveMeta();

            const path = this.getFilePath(`${table.timestamp}.json`);
            await this.native.deleteFile(path);

            return;
        }

        // update the meta if the table is not in the meta.
        if (!this.meta.entries.includes(table.timestamp)) {
            this.meta.entries.push(table.timestamp);
            this.meta.entries.sort();
            await this.saveMeta();
        }

        // save the table to disk.
        if (!keepUpdated) {
            table.updated = formatTimeISO(new Date());
        }
        const path = this.getFilePath(`${table.timestamp}.json`);
        await this.native.saveFile(path, JSON.stringify(table));
    }

    // ========================================================================
    // Sync
    // ========================================================================

    clearCache(): void {
        this.tables.clear();
    }

    async push(): Promise<void> {
        const entries = [];
        for (const timestamp of this.meta.entries) {
            const table = await this.getTable(timestamp);
            entries.push(table);
        }
        await api
            .post('/sync/entry/push', { entries: entries })
            .then((response: ApiResponse) => {
                if (response.status !== 200) {
                    throw new Error(response.message);
                }
            });
    }

    async pull(): Promise<void> {
        await api.get('/sync/entry/pull').then((response: ApiResponse) => {
            if (response.status !== 200) {
                throw new Error(response.message);
            }
            const entries = response.data.entries as DbTable[];
            this.meta.entries = [];
            for (const entry of entries) {
                this.saveTable(entry, true);
            }
        });
        this.clearCache();
    }

    async merge(): Promise<void> {
        const hint = await this.getMergeHint();
        const entries = [];
        for (const timestamp of hint.modify) {
            entries.push(await this.getTable(timestamp));
        }
        for (const timestamp of hint.send) {
            entries.push(await this.getTable(timestamp));
        }
        await api
            .post('/sync/entry/merge', { hint: hint, entries: entries })
            .then((response: ApiResponse) => {
                if (response.status !== 200) {
                    throw new Error(response.message);
                }
                const entries = response.data.entries as DbTable[];
                for (const entry of entries) {
                    this.saveTable(entry, true);
                }
            });
        this.clearCache();
    }

    async getMergeHint(): Promise<Hint> {
        const entries = [];
        for (const timestamp of this.meta.entries) {
            const table = await this.getTable(timestamp);
            entries.push({ timestamp: timestamp, updated: table.updated });
        }

        return await api
            .post('/sync/entry/merge/hint', { meta: { entries: entries } })
            .then((response: ApiResponse) => {
                if (response.status !== 200) {
                    throw new Error(response.message);
                }
                return response.data as Hint;
            });
    }
}
