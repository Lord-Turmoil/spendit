/**
 * @module tags
 *
 * Provide tag support.
 */
import { CategoryList, TagList } from '~/engine/models.js';
import { getNative, Native } from '~/utils/native.js';

interface TagData {
    categories: CategoryList;
    tags: TagList;
    people: TagList;
    updated: string; // ISO string
}

const DummyTagData: TagData = {
    categories: [],
    tags: [],
    people: [],
    updated: ''
};

const TAGS_FILE = 'tags.json';

export class TagsModule {
    private data: TagData;
    private native: Native = getNative();
    private readonly path: string;

    constructor(userId: string) {
        this.path = `${userId}/${TAGS_FILE}`;
        this.data = DummyTagData;
        // TODO: handle async result.
        this.loadTags();
    }

    // ========================================================================
    // Category
    // ========================================================================

    getCategories(): CategoryList {
        return this.data.categories;
    }

    getFirstCategories(): string[] {
        return this.data.categories.map((category) => category[0]);
    }

    getSecondCategories(first: string): string[] {
        for (const category of this.data.categories) {
            if (category[0] === first) {
                return category[1];
            }
        }
        return [];
    }

    /**
     * Call {@link saveTags} after updating the categories.
     */
    updateCategories(categories: string[]) {
        if (categories.length === 0) {
            return;
        }
        let first = categories[0];
        let second = undefined;
        if (categories.length > 1) {
            second = categories[1];
        }

        for (const category of this.data.categories) {
            if (category[0] === first) {
                if (second !== undefined && !category[1].includes(second)) {
                    category[1].push(second);
                }
                return;
            }
        }
        if (second === undefined) {
            this.data.categories.push([first, []]);
        } else {
            this.data.categories.push([first, [second]]);
        }
    }

    // ========================================================================
    // Tags
    // ========================================================================

    getTags(): TagList {
        return this.data.tags;
    }

    /**
     * Call {@link saveTags} after updating the tags.
     */
    updateTags(tags: TagList): void {
        for (const tag of tags) {
            if (!this.data.tags.includes(tag)) {
                this.data.tags.push(tag);
            }
        }
    }

    deleteTag(tag: string): void {
        if (this.data.tags.includes(tag)) {
            this.data.tags = this.data.tags.filter((t) => t !== tag);
        }
    }

    // ========================================================================
    // People
    // ========================================================================

    getPeople(): TagList {
        return this.data.people;
    }

    /**
     * Call {@link saveTags} after updating the people.
     */
    updatePeople(people: TagList): void {
        for (const person of people) {
            if (!this.data.people.includes(person)) {
                this.data.people.push(person);
            }
        }
    }

    deletePerson(person: string): void {
        if (this.data.people.includes(person)) {
            this.data.people = this.data.people.filter((p) => p !== person);
        }
    }

    // ========================================================================
    // Storage
    // ========================================================================

    private async loadTags(): Promise<void> {
        let value = await this.native.loadFile(this.path);
        if (value === null) {
            value = JSON.stringify(DummyTagData);
            this.data = JSON.parse(value);
            await this.saveTags();
        } else {
            this.data = JSON.parse(value);
        }
    }

    /**
     * This must be manually called to save the tags.
     */
    async saveTags(): Promise<void> {
        this.data.updated = new Date().toISOString();
        await this.native.saveFile(this.path, JSON.stringify(this.data));
    }
}
