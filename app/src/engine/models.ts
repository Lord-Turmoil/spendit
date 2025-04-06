/**
 * @module models
 *
 * This module provides all models for the application.
 */

// ========================================================================
// Data models
// ========================================================================

import { formatTimestamp } from '~/utils/format';

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
     *
     * Note that timestamp doesn't mean to be in the same day as the date.
     * Because this is only an edit time.
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
    categories: string[];

    /**
     * The involved people.
     */
    people: TagList;

    /**
     * Well, the fancy tags.
     */
    tags: TagList;

    /**
     * Extra note for this one.
     */
    note: string;
}

export function getDummyEntry(): Entry {
    const timestamp = new Date();
    return {
        title: '',
        date: formatTimestamp(timestamp),
        timestamp: timestamp.getTime(),
        money: 0,
        categories: [],
        people: [],
        tags: [],
        note: ''
    };
}

export interface DbMeta {
    /**
     * Entry is YYYY-MM-DD, representing a YYYY-MM-DD.json file.
     */
    entries: string[];
}

export function getDummyDbMeta(): DbMeta {
    return { entries: [] };
}

/**
 * Represents data in a day.
 */
export interface DbTable {
    timestamp: string; // YYYY-MM-DD
    updated: string; // ISO string
    entries: Entry[];
}

export function getDummyDbTable(timestamp?: string): DbTable {
    return {
        timestamp: timestamp || '',
        updated: '',
        entries: []
    };
}

// ========================================================================
// User models
// ========================================================================

export interface UserProfile {
    id: string;
    name: string;
    badge: string;
    onlineId: number;
}

export const DummyUserProfile: UserProfile = {
    id: '',
    name: '未登录',
    badge: '本地用户',
    onlineId: 0
};

export interface UserProfileMeta {
    profiles: UserProfile[];
}

export function getDummyUserProfileMeta(): UserProfileMeta {
    return { profiles: [] };
}

export interface SystemProfile {
    product: string;
    author: string;
    version: string;
    code: string;
}

export const CurrentSystemProfile: SystemProfile = {
    product: 'Spendit',
    author: 'Tony\'s Studio',
    version: '0.1.3',
    code: 'Genesis'
};

// ========================================================================
// Tag models
// ========================================================================

export interface CategoryEntry {
    primary: string,
    secondaries: string[],
}

export type CategoryList = CategoryEntry[];
export type TagList = string[];

// ========================================================================
// Statistics models
// ========================================================================
export interface Statistics {
    total: number;
    people: string[];
    tags: string[];
    spends: CategorySpend[];
}

export type CategorySpend = [string, number, SubCategorySpend[]];
export type SubCategorySpend = [string, number];

export const DummyStatistics: Statistics = {
    total: 0,
    people: [],
    tags: [],
    spends: []
};
