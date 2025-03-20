<template>
    <div class="DetailView">
        <EntryCardList
            v-for="(date, i) in sortedDates"
            :key="i"
            :date="date"
            :show-date="showDate"
            :show-empty="showEmpty"></EntryCardList>
    </div>
</template>

<style scoped></style>

<script setup lang="ts">
import EntryCardList from '~/components/EntryCardList.vue';

import { ref } from 'vue';

interface DetailViewProps {
    dates: string[];
    showDate?: boolean;
    showEmpty?: boolean;
}

const { dates, showDate = true, showEmpty = false } = defineProps<DetailViewProps>();

// ensure the dates are sorted in descending order
const sortedDates = ref<string[]>(dates.sort().reverse());
</script>
