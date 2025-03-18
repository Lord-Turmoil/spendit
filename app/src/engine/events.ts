/**
 * @module events
 *
 * Defines the events for the components to communicate with each other.
 */
import { Entry } from '~/engine/models.js';

export enum EntryUpdateTypes {
    CREATE = 'CREATE_ENTRY',
    UPDATE = 'UPDATE_ENTRY',
    DELETE = 'DELETE_ENTRY'
}

export interface EntryUpdateEvent {
    type: EntryUpdateTypes;
    entry: Entry;
}

export type EntryEventHandler = (event: EntryUpdateEvent) => void;
export const DefaultEntryEventHandler = (event: EntryUpdateEvent) => {};
