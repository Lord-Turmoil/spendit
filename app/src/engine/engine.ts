import { DbTable, Entry, getDummyEntry, UserProfile } from '~/engine/models';
import { ProfileModule } from '~/engine/modules/profile';
import { DatabaseModule } from '~/engine/modules/database';
import { EntryUpdateEvent, EntryUpdateTypes } from '~/engine/events';
import { StatisticsModule } from '~/engine/modules/statistics';
import { TagsModule } from '~/engine/modules/tags';

export class SpendEngine {
    private readonly statistics: StatisticsModule;

    private profile: ProfileModule;
    private database: DatabaseModule;
    private tags: TagsModule;

    /**
     * The focused day in the home screen, in the format of 'YYYY-MM-DD'.
     *
     * It is used to create new entry with the current date.
     *
     * @private
     */
    private focusDate: string = '';
    private selectedDates: string[] = [];

    constructor() {
        this.statistics = new StatisticsModule();
    }

    async init(): Promise<void> {
        this.profile = new ProfileModule();

        await this.profile.init().then(() => {
            this.database = new DatabaseModule(this.profile.getUserProfile().id);
            this.tags = new TagsModule(this.profile.getUserProfile().id);
            console.log('Powered by SpendEngine');
        });
    }

    // ========================================================================
    // User profile operations
    // ------------------------------------------------------------------------
    // Since the change in user will trigger updates of other modules, we can
    // not expose the profile module directly.
    // ========================================================================

    getUserProfile(): UserProfile {
        return this.profile.getUserProfile();
    }

    async updateUserProfile(userProfile: UserProfile): Promise<void> {
        await this.profile.updateUserProfile(userProfile);
    }

    getSystemProfile() {
        return this.profile.getSystemProfile();
    }

    isLoggedIn(): boolean {
        return this.getUserProfile().onlineId !== 0;
    }

    // ========================================================================
    // Database operations
    // ========================================================================

    getDatabase(): DatabaseModule {
        return this.database;
    }

    async getTable(timestamp: string): Promise<DbTable> {
        return this.getDatabase().getTable(timestamp);
    }

    setFocusDate(date: string): void {
        this.focusDate = date;
    }

    /**
     * Create a new entry.
     *
     * @returns The new entry with empty fields.
     */
    createEntry(): Entry {
        const entry = getDummyEntry();
        if (this.focusDate !== '') {
            entry.date = this.focusDate;
        }
        return entry;
    }

    /**
     * Notify the engine so that it can update the entry.
     * @param event
     */
    async notify(event: EntryUpdateEvent): Promise<void> {
        await this.database.update(event);
        if (event.type == EntryUpdateTypes.DELETE) {
            return;
        }

        // update category, people and tags
        this.tags.updateCategories(event.entry.categories);
        this.tags.updatePeople(event.entry.people);
        this.tags.updateTags(event.entry.tags);
        await this.tags.saveTags();
    }

    // ========================================================================
    // Tags operations
    // ========================================================================

    getTags(): TagsModule {
        return this.tags;
    }

    // ========================================================================
    // Statistics operations
    // ========================================================================

    getStatistics(): StatisticsModule {
        return this.statistics;
    }

    // ========================================================================
    // Extra states
    // ========================================================================

    setSelectedDates(date: string[]): void {
        this.selectedDates = date;
    }

    getSelectedDates(): string[] {
        return this.selectedDates;
    }

    // ========================================================================
    // Synchronization operations
    // ========================================================================

    async push(): Promise<void> {
        await this.tags.push();
        await this.database.push();
    }

    async pull(): Promise<void> {
        await this.tags.pull();
        await this.database.pull();
    }

    async merge(): Promise<void> {
        await this.tags.merge();
        await this.database.merge();
    }
}

export const engine = new SpendEngine();
