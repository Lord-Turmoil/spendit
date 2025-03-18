<template>
    <div class="DetailView">
        <EntryCard v-for="(item, i) in entries" :key="i" :entry="item"></EntryCard>
    </div>
</template>

<style>
.DetailView {
    display: flex;
    flex-direction: column;
    align-items: center;
    overflow: auto;
}
</style>

<script setup lang="ts">
import { Entry } from '~/engine/models.js';
import EntryCard from '~/components/EntryCard.vue';

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
for (let i = 0; i < 50; i++) {
    entries.push(entry);
}
</script>
