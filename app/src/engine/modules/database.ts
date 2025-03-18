/**
 * @module database
 *
 * This module provide the database operations for the application.
 */

import { DbMeta, DbTable, DummyMeta, DummyTable } from '~/engine/models.js';
import { formatTimeISO } from '~/utils/format.js';
import { getNative, Native } from '~/utils/native.js';

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
    private meta: DbMeta = DummyMeta;
    private tables: Map<string, DbTable> = new Map();
    private native: Native = getNative();
    private readonly userId: string;

    constructor(userId: string) {
        this.userId = userId;
        this.loadMeta();
    }

    getTable(timestamp: string): DbTable {
        if (this.tables.has(timestamp)) {
            return this.tables.get(timestamp);
        }
        return this.loadTable(timestamp);
    }

    setTable(table: DbTable): void {
        this.tables.set(table.timestamp, table);
        this.saveTable(table);
    }

    private loadMeta() {
        let value = this.native.loadFile('meta.json');
        if (value === null) {
            value = JSON.stringify(DummyMeta);
            this.saveMeta();
        }
        this.meta = JSON.parse(value);
    }

    private loadTable(timestamp: string): DbTable {
        if (this.tables.has(timestamp)) {
            return this.tables.get(timestamp);
        }

        let table: DbTable;
        if (!this.meta.entries.includes(timestamp)) {
            this.meta.entries.push(timestamp);
            table = { ...DummyTable, timestamp };
        } else {
            const path = this.getFilePath(`${timestamp}.json`);
            const value = this.native.loadFile(path);
            if (value !== null) {
                table = JSON.parse(value);
            } else {
                table = { ...DummyTable, timestamp };
            }
        }

        this.tables.set(timestamp, table);

        return table;
    }

    private getFilePath(filename: string): string {
        return `${this.userId}/${filename}`;
    }

    /**
     * Save the meta data to disk.
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
        if (table.entries.length === 0) {
            return;
        }
        if (!this.meta.entries.includes(table.timestamp)) {
            this.meta.entries.push(table.timestamp);
            this.meta.entries.sort();
            this.saveMeta();
        }
        table.updated = formatTimeISO(new Date());
        const path = this.getFilePath(`${table.timestamp}.json`);
        this.native.saveFile(path, JSON.stringify(table));
    }
}
