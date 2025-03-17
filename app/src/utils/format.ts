/**
 * Format the given date to YYYY-MM-DD.
 *
 * @param date The date to format.
 * @returns The formatted date as string.
 */
export function formatTimestamp(date: Date): string {
    // Interestingly, it works.
    return date.toISOString().split('T')[0];
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
