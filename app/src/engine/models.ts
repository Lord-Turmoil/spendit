/**
 * @module models
 *
 * This module provides all models for the application.
 */

// ========================================================================
// Data models
// ========================================================================

/**
 * The detailed information for a spend item.
 */
export interface Entry {
    title: string;

    /**
     * The date which this entry belongs to, it is in the format of "YYYY-MM-DD".
     * Parse or format required when use it.
     */
    date: string;

    /**
     * The time when this entry is created, in milliseconds since epoch.
     * This is used to sort the entries.
     */
    timestamp: number;

    /**
     * Money is stored as integer, actual value is money / 100.
     */
    money: number;

    /**
     * Category is a ordered list, each is a sub-category of the previous one.
     * Currently, only a maximum of 2 levels is supported.
     */
    category: string[];

    /**
     * The involved people.
     */
    people: string[];

    /**
     * Well, the fancy tags.
     */
    tags: string[];

    /**
     * Extra note for this one.
     */
    note: string;
}

/**
 * The list of entries.
 * It can be used for a day's entries, or any other list of entries.
 */
export interface EntryList {
    entries: Entry[];
}

export interface DbMeta {
    /**
     * Entry is YYYY-MM-DD, representing a YYYY-MM-DD.json file.
     */
    entries: string[];
}

export const DummyMeta: DbMeta = {
    entries: []
};

/**
 * Represents data in a day.
 */
export interface DbTable {
    timestamp: string; // YYYY-MM-DD
    updated: string; // ISO string
    entries: Entry[];
}

export const DummyTable: DbTable = {
    timestamp: '',
    updated: '',
    entries: []
};

// ========================================================================
// User models
// ========================================================================

export interface UserProfile {
    id: string;
    name: string;
    title: string;
}

export const DummyUserProfile: UserProfile = {
    id: '',
    name: 'mascota',
    title: 'Internal User'
};

export interface UserProfileMeta {
    profiles: UserProfile[];
}

export const DummyUserProfileMeta: UserProfileMeta = {
    profiles: []
};

export interface SystemProfile {
    product: string;
    author: string;
    version: string;
    code: string;
}

export const CurrentSystemProfile: SystemProfile = {
    product: 'Spendit',
    author: "Tony's Studio",
    version: '0.1.0',
    code: 'Genesis'
};
