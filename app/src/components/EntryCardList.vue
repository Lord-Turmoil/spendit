<template>
    <div class="EntryCardList">
        <div class="EntryCardList__timestamp text-h6" v-if="showDate">
            {{ formatTimestampToSlash(date) }}
        </div>
        <EntryCard v-for="(item, i) in entries" :key="i" :entry="item"></EntryCard>
    </div>
</template>

<style scoped>
.EntryCardList {
    display: flex;
    flex-direction: column;
    align-items: center;
    overflow: auto;
}

.EntryCardList__timestamp {
    margin: 8px 0;
}
</style>

<script setup lang="ts">
import EntryCard from '~/components/EntryCard.vue';
import { Entry } from '~/engine/models.js';
import { formatTimestampToSlash } from '../utils/format.js';

interface EntryCardListProps {
    date: string;
    showDate?: boolean;
}

const { date, showDate = true } = defineProps<EntryCardListProps>();

const entry: Entry = {
    title: 'Bread',
    date: '2021-10-01',
    timestamp: 123456,
    money: 1990,
    category: ['Life', 'Food'],
    people: ['mascota', 'wuswada'],
    tags: ['food', 'bread', 'extra'],
    note: '3 caterpillars from sandwiches, and a chocolate cake. 3 caterpillars from sandwiches, and a chocolate cake.'
};

const entries: Entry[] = [];
for (let i = 0; i < 5; i++) {
    entries.push(entry);
}
</script>
