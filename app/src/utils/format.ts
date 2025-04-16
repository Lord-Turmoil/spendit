function getLocalDate(date: Date): Date {
    // Convert to local time zone
    return new Date(date.getTime() - date.getTimezoneOffset() * 60000);
}

/**
 * Format the given date to YYYY-MM-DD.
 *
 * @param date The date to format.
 * @returns The formatted date as string.
 */
export function formatTimestamp(date: Date): string {
    const localDate = getLocalDate(date);

    const year = localDate.getFullYear();
    const month = localDate.getMonth() + 1;
    const day = localDate.getDate();
    return `${year}-${month < 10 ? '0' : ''}${month}-${day < 10 ? '0' : ''}${day}`;
}

/**
 * Parse the given timestamp to date. From YYYY-MM-DD to Date.
 * @param timestamp The timestamp to parse.
 */
export function parseTimestamp(timestamp: string): Date {
    return new Date(timestamp);
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
    // Convert to local time zone
    return getLocalDate(date).toISOString();
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

const VALUE_REGEX = /^\d+(\.\d{0,2})?$/;
const EXPR_REGEX = /^\s*\d+(\.\d{0,2})?\s*([+\-*/]\s*\d+(\.\d{0,2})?\s*)*$/;

export function isValidMoney(money: string): boolean {
    return money !== '' && (VALUE_REGEX.test(money) || EXPR_REGEX.test(money));
}

function parseFixedMoney(money: string): number {
    const [integer, decimal] = money.split('.');
    if (decimal === undefined) {
        return parseInt(integer, 10) * 100;
    }
    return parseInt(integer, 10) * 100 + parseInt(decimal.padEnd(2, '0'), 10);
}

export function parseMoney(money: string): number {
    if (VALUE_REGEX.test(money)) {
        return parseFixedMoney(money);
    } else if (EXPR_REGEX.test(money)) {
        // evaluate the expression
        return parseFixedMoney((eval(money) as number).toFixed(2).toString());
    } else {
        throw new Error('金额格式不正确');
    }
}
