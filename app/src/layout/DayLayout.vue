<template>
    <div class="DayLayout">
        <DaySelector v-model="activeDate" />
        <div class="DayLayout__list no-scrollbar">
            <DetailView :dates="dates" :show-date="false" :key="key"></DetailView>
        </div>
    </div>
</template>

<style scoped>
.DayLayout {
    width: 100%;
    height: 100%;
}

.DayLayout__list {
    width: 100%;
    height: calc(100% - 56px);
    overflow: auto;
    /* make some room for the top bar */
    margin-top: 16px;
    /* compensate for the navbar */
    padding-bottom: 70px;
}
</style>

<script setup lang="ts">
import { ref, watch } from 'vue';

import DaySelector from '~/components/DaySelector.vue';
import DetailView from '~/view/DetailView.vue';
import { formatTimestamp } from '~/utils/format.js';
import { engine } from '~/engine/engine';

const activeDate = ref(new Date());
const dates = ref<string[]>([formatTimestamp(activeDate.value)]);
const key = ref<number>(0);

watch(activeDate, () => {
    engine.setFocusDate(formatTimestamp(activeDate.value));
    dates.value = [formatTimestamp(activeDate.value)];
    key.value++; // force re-render
});
</script>
