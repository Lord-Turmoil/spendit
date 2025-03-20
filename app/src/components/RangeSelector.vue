<template>
    <div class="RangeSelector">
        <div class="RangeSelector__main">
            <v-btn
                class="date"
                prepend-icon="mdi-calendar-range"
                size="large"
                @click="onSelectStartDate">
                {{ formatTimestampToSlash(model.startDate) }}
            </v-btn>
            <span>To</span>
            <v-btn
                class="date"
                prepend-icon="mdi-calendar-range"
                size="large"
                @click="onSelectEndDate">
                {{ formatTimestampToSlash(model.endDate) }}
            </v-btn>
        </div>
        <v-dialog class="RangeSelector__dialog" v-model="startDialogOpen">
            <v-card class="card" prepend-icon="mdi-calendar-range" title="Select a Day">
                <v-date-picker
                    class="selector"
                    v-model="selectedDate"
                    :max="model.endDate"
                    show-adjacent-months></v-date-picker>
                <v-card-actions>
                    <v-btn text @click="onSelectStartEnd(false)">Cancel</v-btn>
                    <v-btn text @click="onSelectStartEnd(true)">OK</v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>
        <v-dialog class="RangeSelector__dialog" v-model="endDialogOpen">
            <v-card class="card" prepend-icon="mdi-calendar-range" title="Select a Day">
                <v-date-picker
                    class="selector"
                    v-model="selectedDate"
                    :min="model.startDate"
                    :max="new Date()"
                    show-adjacent-months></v-date-picker>
                <v-card-actions>
                    <v-btn text @click="onSelectEndEnd(false)">Cancel</v-btn>
                    <v-btn text @click="onSelectEndEnd(true)">OK</v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>
    </div>
</template>

<style scoped>
.RangeSelector__main {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
}

.RangeSelector__main .date {
    flex: 1;
}

.RangeSelector__main > span {
    margin: 0 10px;
}

.RangeSelector__dialog .card {
    margin: 0 auto;
}

.RangeSelector__dialog .selector {
    margin: 0 auto;
}

.RangeSelector__dialog .selector .v-picker-title {
    display: none;
}
</style>

<script setup lang="ts">
import { ref } from 'vue';
import { formatTimestamp, formatTimestampToSlash, parseTimestamp } from '~/utils/format';

interface RangeSelectorModel {
    startDate: string;
    endDate: string;
}

const model = defineModel<RangeSelectorModel>();

// edit data
const selectedDate = ref<Date>(new Date());

// dialog control
const startDialogOpen = ref<boolean>(false);
const endDialogOpen = ref<boolean>(false);

const onSelectStartDate = () => {
    selectedDate.value = parseTimestamp(model.value.startDate);
    startDialogOpen.value = true;
};

const onSelectStartEnd = (confirm: boolean) => {
    if (confirm) {
        model.value.startDate = formatTimestamp(selectedDate.value);
    }
    startDialogOpen.value = false;
};

const onSelectEndDate = () => {
    selectedDate.value = parseTimestamp(model.value.endDate);
    endDialogOpen.value = true;
};

const onSelectEndEnd = (confirm: boolean) => {
    if (confirm) {
        model.value.endDate = formatTimestamp(selectedDate.value);
    }
    endDialogOpen.value = false;
};
</script>
