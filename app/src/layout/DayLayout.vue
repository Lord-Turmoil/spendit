<template>
    <div class="DayLayout">
        <DaySelector v-model="activeDate" />
        <div class="DayLayout__list">
            <DetailView></DetailView>
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
import { ref } from 'vue';

import DaySelector from '~/components/DaySelector.vue';
import DetailView from '~/view/DetailView.vue';

const activeDate = ref(new Date());
</script>
