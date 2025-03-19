import {
    CurrentSystemProfile,
    DummyUserProfile,
    getDummyUserProfileMeta,
    SystemProfile,
    UserProfile,
    UserProfileMeta
} from '~/engine/models.js';
import { getNative, Native } from '~/utils/native.js';

const PROFILE_FILE = 'profiles.json';

export class ProfileModule {
    private userProfileMeta: UserProfileMeta = getDummyUserProfileMeta();
    private userProfile: UserProfile = DummyUserProfile;
    private systemProfile: SystemProfile = CurrentSystemProfile;

    private native: Native = getNative();

    constructor() {
        this.loadProfileMeta();
        this.loadProfile();
    }

    getUserProfiles(): UserProfile[] {
        return this.userProfileMeta.profiles;
    }

    getUserProfile(): UserProfile {
        return this.userProfile;
    }

    /**
     * Select the current user profile.
     *
     * @param userId User ID to select.
     * @returns True if the user is found, false otherwise.
     */
    selectUserProfile(userId: string): boolean {
        if (this.userProfile.id === userId) {
            return false;
        }
        this.userProfileMeta.profiles.forEach((user) => {
            if (user.id === userId) {
                this.userProfile = user;
                this.updateState();
                return true;
            }
        });
        return false;
    }

    updateUserProfile(userProfile: UserProfile) {
        this.userProfile = { ...userProfile, id: this.userProfile.id };
        this.userProfileMeta.profiles = this.userProfileMeta.profiles.map((user) => {
            if (user.id === this.userProfile.id) {
                return this.userProfile;
            }
            return user;
        });
        this.saveProfiles();
        this.updateState();
    }

    getSystemProfile(): SystemProfile {
        return this.systemProfile;
    }

    private generateUserId(): string {
        return Math.random().toString(36).substring(2, 15);
    }

    private loadProfileMeta(): void {
        let value = this.native.loadFile(PROFILE_FILE);
        if (value === null) {
            value = JSON.stringify(getDummyUserProfileMeta());
            this.native.saveFile(PROFILE_FILE, value);
        }
        this.userProfileMeta = JSON.parse(value);
    }

    private loadProfile(): void {
        // ensure at least one profile exists
        if (this.userProfileMeta.profiles.length === 0) {
            this.initProfile();
        }

        // load the last user
        let lastUserId = this.native.getLocalStorage('user');
        if (lastUserId === null) {
            lastUserId = this.userProfileMeta.profiles[0].id;
        }

        // find the user
        let user = this.userProfileMeta.profiles.find((user) => user.id === lastUserId);
        if (user === undefined) {
            user = this.userProfileMeta.profiles[0];
        }

        this.userProfile = user;
        this.updateState();
    }

    /**
     * Create the first user and add it to the profile.
     */
    private initProfile(): void {
        const user = DummyUserProfile;
        user.id = this.generateUserId();

        this.userProfileMeta.profiles.push(user);
        this.userProfile = user;

        this.saveProfiles();
    }

    private saveProfiles(): void {
        this.native.saveFile(PROFILE_FILE, JSON.stringify(this.userProfileMeta));
    }

    private updateState(): void {
        this.native.setLocalStorage('user', this.userProfile.id);
    }
}
