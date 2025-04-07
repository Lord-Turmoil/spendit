import { Capacitor } from '@capacitor/core';
import { Directory, Encoding, Filesystem } from '@capacitor/filesystem';

export abstract class Native {
    /**
     * Set the given key in local storage.
     * @param key The key to set in local storage.
     * @param value The value to set in local storage.
     */
    public setLocalStorage(key: string, value: string) {
        localStorage.setItem(key, value);
    }

    /**
     * Get the value of the given key in local storage.
     * @param key The key to get from local storage.
     * @returns The value of the key in local storage, or null if not found.
     */
    public getLocalStorage(key: string): string | null {
        return localStorage.getItem(key);
    }

    /**
     * Get the value of the given key in local storage.
     *
     * @param key Key of the cookie.
     * @param value Value of the cookie.
     * @param hours Expiration time of the cookie in hours.
     */
    public setCookies(key: string, value: string, hours: number) {
        const expireDate = new Date();
        expireDate.setTime(expireDate.getTime() + hours * 60 * 60 * 1000);
        let expires = 'expires=' + expireDate.toUTCString();
        document.cookie = key + '=' + value + ';' + expires + ';path=/';
    }

    /**
     * Get the value of the given key in local storage.
     *
     * @param key Key of the cookie.
     */
    public getCookies(key: string): string | undefined {
        key = key + '=';
        let decodedCookie = decodeURIComponent(document.cookie);
        let ca = decodedCookie.split(';');
        for (let i = 0; i < ca.length; i++) {
            let c = ca[i];
            while (c.charAt(0) == ' ') {
                c = c.substring(1);
            }
            if (c.indexOf(key) == 0) {
                return c.substring(key.length, c.length);
            }
        }
        return undefined;
    }

    /**
     * Save the given file.
     *
     * @param filename The filename to save.
     * @param content The content to save.
     */
    abstract saveFile(filename: string, content: string): Promise<void>;

    /**
     * Load the given file.
     *
     * @param filename The filename to load.
     * @returns The content of the file, or null if not found.
     */
    abstract loadFile(filename: string): Promise<string | null>;

    /**
     * Delete the given file.
     * @param filename The filename to delete.
     */
    abstract deleteFile(filename: string): Promise<void>;
}

export class WebNative extends Native {
    override async saveFile(filename: string, content: string): Promise<void> {
        localStorage.setItem(filename, content);
    }

    override async loadFile(filename: string): Promise<string | null> {
        return Promise.resolve(localStorage.getItem(filename));
    }

    override async deleteFile(filename: string): Promise<void> {
        localStorage.removeItem(filename);
    }
}

export class MobileNative extends Native {
    private dirname(filename: string): string {
        const index = filename.lastIndexOf('/');
        if (index === -1) {
            return '';
        }
        return filename.substring(0, index);
    }

    private async exists(dirname: string): Promise<boolean> {
        return await Filesystem.stat({
            path: dirname,
            directory: Directory.External
        })
            .then(() => {
                return true;
            })
            .catch(() => {
                return false;
            });
    }

    private async ensureParentDir(filename: string): Promise<void> {
        const dir = this.dirname(filename);
        if (dir === '') {
            return;
        }

        if (await this.exists(dir)) {
            return;
        }

        await Filesystem.mkdir({
            path: dir,
            directory: Directory.External,
            recursive: true
        }).catch((error) => {
            console.error('Error creating directory', error);
        });
    }

    override async saveFile(filename: string, content: string): Promise<void> {
        await this.ensureParentDir(filename);

        await Filesystem.writeFile({
            path: filename,
            data: content,
            directory: Directory.External,
            encoding: Encoding.UTF8
        }).catch((error) => {
            console.error('Error saving file', error);
        });
    }

    override async loadFile(filename: string): Promise<string | null> {
        return await Filesystem.readFile({
            path: filename,
            directory: Directory.External,
            encoding: Encoding.UTF8
        })
            .then((result) => {
                return result.data;
            })
            .catch((error) => {
                console.error('Error loading file', error);
                return null;
            });
    }

    override async deleteFile(filename: string): Promise<void> {
        await Filesystem.deleteFile({
            path: filename,
            directory: Directory.External
        }).catch((error) => {
            console.error('Error deleting file', error);
        });
    }
}

// get native implementation
export function getNative(): Native {
    const platform = Capacitor.getPlatform();
    if (platform === 'web') {
        return new WebNative();
    }
    return new MobileNative();
}
