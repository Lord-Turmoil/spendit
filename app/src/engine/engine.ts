import { UserProfile } from '~/engine/models.js';
import { ProfileModule } from '~/engine/modules/profile.js';
import { DatabaseModule } from '~/engine/modules/database.js';

export class SpendEngine {
    private profile: ProfileModule;
    private database: DatabaseModule;

    constructor() {
        this.profile = new ProfileModule();
        this.database = new DatabaseModule(this.profile.getUserProfile().id);
        console.log('Powered by SpendEngine');
    }

    // ========================================================================
    // User profile operations
    // ========================================================================

    getAllUserProfiles(): UserProfile[] {
        return this.profile.getUserProfiles();
    }

    getUserProfile(): UserProfile {
        return this.profile.getUserProfile();
    }

    selectUserProfile(userId: string) {
        if (this.profile.selectUserProfile(userId)) {
            this.database = new DatabaseModule(this.profile.getUserProfile().id);
        }
    }

    updateUserProfile(userProfile: UserProfile) {
        this.profile.updateUserProfile(userProfile);
    }

    getSystemProfile() {
        return this.profile.getSystemProfile();
    }

    // ========================================================================
    // Database operations
    // ========================================================================
    getDatabase(): DatabaseModule {
        return this.database;
    }
}

export const engine = new SpendEngine();
