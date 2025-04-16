import { Version, VersionMeta } from '~/engine/models';
import { api } from '~/extensions/api';

export class VersionModule {
    private readonly currentVersion: string;
    private promise: Promise<VersionMeta>;

    constructor(currentVersion: string) {
        this.currentVersion = currentVersion;
        this.promise = this.preparePromise();
    }

    /**
     * Check for update.
     *
     * @param force Force check for update.
     *
     * @throws Error if the request fails.
     */
    async checkForUpdate(force: boolean): Promise<VersionMeta> {
        if (force) {
            this.promise = this.preparePromise();
        }
        return this.promise;
    }

    private needUpdate(oldVersion: string, newVersion: string): boolean {
        const oldVersionParts = oldVersion.split('.').map(Number);
        const newVersionParts = newVersion.split('.').map(Number);
        for (
            let i = 0;
            i < Math.max(oldVersionParts.length, newVersionParts.length);
            i++
        ) {
            const oldPart = oldVersionParts[i] || 0;
            const newPart = newVersionParts[i] || 0;
            if (oldPart < newPart) {
                return true;
            } else if (oldPart > newPart) {
                return false;
            }
        }
    }

    private async fetchLatestVersion(): Promise<Version> {
        const response = await api.get('/version/latest');
        if (response.status === 200) {
            return response.data;
        }
        throw new Error(response.message);
    }

    private async preparePromise(): Promise<VersionMeta> {
        return await this.fetchLatestVersion()
            .then((version) => {
                return {
                    version: version,
                    updateAvailable: this.needUpdate(this.currentVersion, version.version)
                };
            })
            .catch((error) => {
                throw new Error(error.message);
            });
    }
}
