<template>
    <v-card class="OverviewView" variant="flat">
        <v-card-title class="OverviewView__title split-wrapper">
            <h3 class="text-h4 split-primary">Total</h3>
            <h4 class="text-h4 text-red-darken-1">
                <v-icon
                    v-if="statistics.total > 0"
                    class="text-h4"
                    icon="mdi-minus"></v-icon>
                <span>{{ formatMoney(statistics.total) }}</span>
            </h4>
        </v-card-title>
        <v-card-text v-if="entries.length > 0" class="OverviewView__body">
            <v-divider class="divider"></v-divider>
            <div class="tags">
                <span class="tag" v-for="(name, i) in statistics.people" :key="i">
                    <PeopleChip :name="name"></PeopleChip>
                </span>
            </div>
            <div class="tags">
                <span class="tag" v-for="(tag, i) in statistics.tags" :key="i">
                    <TagChip :tag="tag"></TagChip>
                </span>
            </div>
            <v-divider class="divider"></v-divider>
            <SpendList :spends="statistics.spends"></SpendList>
            <v-divider class="divider"></v-divider>
            <v-btn
                class="detail"
                @click="onClickDetail"
                variant="text"
                color="blue-darken-1"
                size="large"
                block>
                查看详细消费记录
            </v-btn>
        </v-card-text>
        <v-card-text v-else class="OverviewView__empty">
            <EmptyCard></EmptyCard>
        </v-card-text>
    </v-card>
</template>

<style scoped>
.OverviewView {
    width: 100%;
    padding-top: 16px;
}

.OverviewView__title h4 {
    display: flex;
    flex-direction: row;
    align-items: center;
}

.OverviewView__body .tags {
    flex: 1;
    margin: 8px;
}

.OverviewView__body .tags .tag {
    display: inline-block;
    margin: 4px;
}

.OverviewView__body .divider {
    margin: 8px auto;
}

.OverviewView__body .detail {
    margin-top: 16px;
}

.OverviewView__empty {
    padding: 16px 0;
}
</style>

<script setup lang="ts">
import EmptyCard from '~/components/EmptyCard.vue';
import PeopleChip from '~/components/PeopleChip.vue';
import SpendList from '~/components/SpendList.vue';
import TagChip from '~/components/TagChip.vue';

import { onMounted, ref } from 'vue';

import { engine } from '~/engine/engine';
import { DummyStatistics, Entry, Statistics } from '~/engine/models';
import router from '~/extensions/router';
import { formatMoney } from '~/utils/format';

interface OverviewViewProps {
    dates: string[];
}

const overviewReady = defineModel<boolean>();
const { dates } = defineProps<OverviewViewProps>();
const statistics = ref<Statistics>(DummyStatistics);
const entries = ref<Entry[]>([]);

const prepareStatistics = async () => {
    let list: Entry[] = [];
    for (const date of dates) {
        list.push(...engine.getDatabase().getTable(date).entries);
    }
    statistics.value = engine.getStatistics().sum(list);
    entries.value = list;
    overviewReady.value = true;
};

const onClickDetail = () => {
    engine.setSelectedDates(dates);
    router.push('/detail');
};

onMounted(() => {
    prepareStatistics();
});
</script>
