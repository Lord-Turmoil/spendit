/**
 * Provide APIs for compatibility issues.
 */

export function hasProperty(obj: object, field: string) {
    return Object.prototype.hasOwnProperty.call(obj, field);
}
