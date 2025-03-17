import { Capacitor } from '@capacitor/core';

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
    public getLocalStorage(key: string): string {
        return localStorage.getItem(key);
    }

    /**
     * Save the given file.
     *
     * @param filename The filename to save.
     * @param content The content to save.
     */
    abstract saveFile(filename: string, content: string): void;

    /**
     * Load the given file.
     *
     * @param filename The filename to load.
     * @returns The content of the file, or null if not found.
     */
    abstract loadFile(filename: string): string | null;
}

export class WebNative extends Native {
    override saveFile(filename: string, content: string): void {
        localStorage.setItem(filename, content);
    }

    override loadFile(filename: string): string | null {
        return localStorage.getItem(filename);
    }
}

export class MobileNative extends Native {
    override saveFile(filename: string, content: string): void {
        throw new Error('Method not implemented.');
    }

    override loadFile(filename: string): string | null {
        throw new Error('Method not implemented.');
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
