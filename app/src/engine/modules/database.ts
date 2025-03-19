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
    getDummyDbTable,
    getDummyEntry
} from '~/engine/models.js';
import { formatTimeISO, formatTimestamp } from '~/utils/format.js';
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

export class DatabaseModule {
    private meta: DbMeta = getDummyDbMeta();
    private tables: Map<string, DbTable> = new Map();
    private native: Native = getNative();
    private readonly userId: string;

    constructor(userId: string) {
        this.userId = userId;
        this.loadMeta();
    }

    /**
     * Get the table for the given timestamp (YYYY-MM-DD).
     * @param timestamp The timestamp of the table.
     */
    getTable(timestamp: string): DbTable {
        if (this.tables.has(timestamp)) {
            return this.tables.get(timestamp);
        }
        return this.loadTable(timestamp);
    }

    /**
     * Create a new entry.
     *
     * @returns The new entry.
     */
    createEntry(): Entry {
        return getDummyEntry();
    }

    /**
     * Update the entry with the given event.
     *
     * This is a reducer function that updates the entry based on the event.
     *
     * @param event The event to update the entry.
     */
    update(event: EntryUpdateEvent): void {
        switch (event.type) {
            case EntryUpdateTypes.CREATE:
                this.handleCreate(event.entry);
                break;
            case EntryUpdateTypes.UPDATE:
                this.handleUpdate(event.entry);
                break;
            case EntryUpdateTypes.DELETE:
                this.handleDelete(event.entry);
                break;
            default:
                break;
        }
    }

    private handleCreate(entry: Entry): void {
        const table = this.getTable(entry.date);
        table.entries.push(entry);
        this.saveTable(table);
    }

    private handleUpdate(entry: Entry): void {
        const table = this.getTable(entry.date);
        table.entries = table.entries.map((e) =>
            e.timestamp === entry.timestamp ? entry : e
        );
        this.saveTable(table);
    }

    private handleDelete(entry: Entry): void {
        const table = this.getTable(entry.date);
        table.entries = table.entries.filter((e) => e.timestamp !== entry.timestamp);
        this.saveTable(table);
    }

    private loadMeta() {
        let value = this.native.loadFile(this.getFilePath('meta.json'));
        if (value === null) {
            value = JSON.stringify(getDummyDbMeta());
            this.saveMeta();
        }
        this.meta = JSON.parse(value);
    }

    /**
     * Load the table from disk with the given timestamp. (YYYY-MM-DD)
     * @param timestamp The timestamp of the table.
     * @private
     */
    private loadTable(timestamp: string): DbTable {
        let table: DbTable;
        if (!this.meta.entries.includes(timestamp)) {
            table = getDummyDbTable(timestamp);
        } else {
            const path = this.getFilePath(`${timestamp}.json`);
            const value = this.native.loadFile(path);
            if (value !== null) {
                table = JSON.parse(value);
            } else {
                table = getDummyDbTable(timestamp);
            }
        }

        this.tables.set(timestamp, table);

        return table;
    }

    private getFilePath(filename: string): string {
        return `${this.userId}/${filename}`;
    }

    /**
     * Save the metadata to disk.
     */
    private saveMeta(): void {
        const path = this.getFilePath('meta.json');
        this.native.saveFile(path, JSON.stringify(this.meta));
    }

    /**
     * Save the table to disk, and updates the meta if necessary.
     *
     * @param table The table to save.
     */
    private saveTable(table: DbTable): void {
        // If the table is empty, remove it from the meta and disk.
        if (table.entries.length === 0) {
            this.meta.entries = this.meta.entries.filter((e) => e !== table.timestamp);
            this.saveMeta();

            const path = this.getFilePath(`${table.timestamp}.json`);
            this.native.deleteFile(path);

            return;
        }

        // update the meta if the table is not in the meta.
        if (!this.meta.entries.includes(table.timestamp)) {
            this.meta.entries.push(table.timestamp);
            this.meta.entries.sort();
            this.saveMeta();
        }

        // save the table to disk.
        table.updated = formatTimeISO(new Date());
        const path = this.getFilePath(`${table.timestamp}.json`);
        this.native.saveFile(path, JSON.stringify(table));
    }
}
