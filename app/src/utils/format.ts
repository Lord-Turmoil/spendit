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

/**
 * Format raw money to string representation, e.g. 1234 -> 12.34.
 * @param money
 */
export function formatMoney(money: number): string {
    const raw = money.toString(10);
    if (raw.length <= 2) {
        return `0.${raw.padStart(2, '0')}`;
    }
    return `${raw.slice(0, -2)}.${raw.slice(-2)}`;
}

export function parseMoney(money: string): number {
    const [integer, decimal] = money.split('.');
    if (decimal === undefined) {
        return parseInt(integer, 10) * 100;
    }
    return parseInt(integer, 10) * 100 + parseInt(decimal.padEnd(2, '0'), 10);
}
