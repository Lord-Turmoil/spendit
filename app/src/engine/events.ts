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
    handled: boolean;
    entry: Entry;
}

export type EntryCallback = (entry: Entry) => void;
