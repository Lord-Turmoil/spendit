<template>
    <div class="DaySelectLayout">
        <RangeSelector v-model="model"></RangeSelector>
        <v-btn
            class="confirm"
            @click="onConfirmSelection"
            :loading="!overviewReady"
            variant="flat"
            color="orange-darken-1"
            block>
            更新统计数据
        </v-btn>
        <OverviewView
            v-model="overviewReady"
            :dates="selectedDates"
            :key="key"></OverviewView>
    </div>
</template>

<style scoped>
.DaySelectLayout {
    width: 100%;
}

.DaySelectLayout .confirm {
    margin: 16px auto;
}
</style>

<script setup lang="ts">
import RangeSelector from '~/components/RangeSelector.vue';
import OverviewView from '~/view/OverviewView.vue';

import { ref, watch } from 'vue';

import { getNative } from '~/utils/native';
import { formatTimestamp } from '~/utils/format';

const currentTimestamp = formatTimestamp(new Date());
const firstDayTimestamp = currentTimestamp.substring(0, 8) + '01';
const native = getNative();

const parseQuery = (): { startDate: string; endDate: string } => {
    const pattern = /^\d{4}-\d{2}-\d{2}$/;
    let startDate = native.getCookies('startDate') || firstDayTimestamp;
    let endDate = native.getCookies('endDate') || currentTimestamp;

    if (!pattern.test(startDate) || !pattern.test(endDate)) {
        return { startDate: currentTimestamp, endDate: currentTimestamp };
    }

    if (startDate.localeCompare(currentTimestamp) > 0) {
        startDate = currentTimestamp;
    }
    if (endDate.localeCompare(currentTimestamp) > 0) {
        endDate = currentTimestamp;
    }
    if (startDate.localeCompare(endDate) > 0) {
        startDate = endDate;
    }

    return { startDate, endDate };
};

const model = ref(parseQuery());

const selectedDates = ref<string[]>([currentTimestamp]);
const key = ref<number>(0);
const displayOverview = ref<boolean>(false);
const overviewReady = ref<boolean>(true);

const onConfirmSelection = async () => {
    overviewReady.value = false;
    // get all dates between the start and end date
    const start = new Date(model.value.startDate);
    const end = new Date(model.value.endDate);
    const dates = [];
    for (let date = start; date <= end; date.setDate(date.getDate() + 1)) {
        dates.push(formatTimestamp(date));
    }
    selectedDates.value = dates;
    key.value++; // force re-render
    native.setCookies('startDate', model.value.startDate, 6);
    native.setCookies('endDate', model.value.endDate, 6);
};

onConfirmSelection();

watch(overviewReady, () => {
    displayOverview.value = overviewReady.value;
});
</script>
