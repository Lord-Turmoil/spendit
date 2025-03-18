<template>
    <div class="DetailView">
        <EntryCardList
            v-for="(date, i) in dates"
            :key="i"
            :date="date"
            :show-date="showDate"
        ></EntryCardList>
    </div>
</template>

<style></style>

<script setup lang="ts">
import EntryCardList from '~/components/EntryCardList.vue';

interface DetailViewProps {
    dates: string[];
    showDate?: boolean;
}

const { dates, showDate = true } = defineProps<DetailViewProps>();
// ensure the dates are sorted in descending order
dates.sort().reverse();
</script>
