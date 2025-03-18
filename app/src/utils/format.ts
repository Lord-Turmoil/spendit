/**
 * Format the given date to YYYY-MM-DD.
 *
 * @param date The date to format.
 * @returns The formatted date as string.
 */
export function formatTimestamp(date: Date): string {
    const year = date.getFullYear();
    const month = date.getMonth() + 1;
    const day = date.getDate();
    return `${year}-${month < 10 ? '0' : ''}${month}-${day < 10 ? '0' : ''}${day}`;
}

/**
 * Convert YYYY-MM-DD to YYYY/MM/DD.
 *
 * @param date The date to format.
 * @returns The formatted date as string.
 */
export function formatTimestampToSlash(date: string): string {
    return date.replace(/-/g, '/');
}

/**
 * Format the given date to ISO string.
 *
 * @param date The date to format.
 * @returns The formatted date as string.
 */
export function formatTimeISO(date: Date): string {
    return date.toISOString();
}
