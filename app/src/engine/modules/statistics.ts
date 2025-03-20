/**
 * @module statistics
 *
 * This module provide the statistics operations for the application.
 */
import { Entry, Statistics } from '~/engine/models';

const UnknownCategory = 'N/A';

export class StatisticsModule {
    sum(entries: Entry[]): Statistics {
        const data: Statistics = {
            total: 0,
            people: [],
            tags: [],
            spends: []
        };

        for (const entry of entries) {
            data.total += entry.money;
            this.addPeople(data, entry.people);
            this.addTags(data, entry.tags);
            this.addSpends(data, entry);
        }
        this.formatData(data);

        return data;
    }

    private addPeople(data: Statistics, people: string[]) {
        for (const person of people) {
            if (!data.people.includes(person)) {
                data.people.push(person);
            }
        }
    }

    private addTags(data: Statistics, tags: string[]) {
        for (const tag of tags) {
            if (!data.tags.includes(tag)) {
                data.tags.push(tag);
            }
        }
    }

    private addSpends(data: Statistics, entry: Entry) {
        if (entry.categories.length === 0) {
            this.addSpendsImpl(data, UnknownCategory, UnknownCategory, entry.money);
        } else if (entry.categories.length === 1) {
            this.addSpendsImpl(data, entry.categories[0], UnknownCategory, entry.money);
        } else {
            this.addSpendsImpl(
                data,
                entry.categories[0],
                entry.categories[1],
                entry.money
            );
        }
    }

    private addSpendsImpl(
        data: Statistics,
        first: string,
        second: string,
        money: number
    ) {
        for (const spend of data.spends) {
            if (spend[0] === first) {
                spend[1] += money;
                for (const sub of spend[2]) {
                    if (sub[0] === second) {
                        // both first and second found
                        sub[1] += money;
                        return;
                    }
                }
                // no sub found
                spend[2].push([second, money]);
                return;
            }
        }
        // no first found
        data.spends.push([first, money, [[second, money]]]);
    }

    private formatData(data: Statistics) {
        data.people.sort();
        data.tags.sort();
        for (const spend of data.spends) {
            spend[2].sort((a, b) => b[1] - a[1]);
        }
        data.spends.sort((a, b) => b[1] - a[1]);

        // if the category only have unknown category, remove it
        for (const spend of data.spends) {
            if (spend[2].length === 1 && spend[2][0][0] === UnknownCategory) {
                spend[2] = [];
            }
        }
    }
}
