<template>
    <div class="DaySelector">
        <v-btn
            class="DaySelector__date text-h6"
            prepend-icon="mdi-calendar-range"
            size="x-large"
            @click="onDaySelectStart">
            {{ formatTimestampToSlash(formatTimestamp(activeDate)) }}
        </v-btn>
        <v-btn
            class="DaySelector__reset"
            icon="mdi-restore"
            size="large"
            @click="resetDay"></v-btn>

        <v-dialog class="DaySelector__dialog" v-model="dialogOpen">
            <v-card class="card" prepend-icon="mdi-calendar-range" title="选择日期">
                <v-date-picker
                    class="selector"
                    v-model="selectedDate"
                    :max="new Date()"
                    show-adjacent-months></v-date-picker>
                <v-card-actions>
                    <v-btn text color="primary" @click="onDaySelectEnd(false)">
                        取消
                    </v-btn>
                    <v-btn text color="success" @click="onDaySelectEnd(true)">确定</v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>
    </div>
</template>

<style>
.DaySelector {
    display: flex;
    justify-content: space-between;
    align-items: center;
    height: min-content;
}

.DaySelector__date {
    /* take up all available space */
    flex: 1;
}

.DaySelector__reset {
    margin-left: 20px;
}

.DaySelector__dialog .card {
    margin: 0 auto;
}

.DaySelector__dialog .selector {
    margin: 0 auto;
}

.DaySelector__dialog .selector .v-picker-title {
    display: none;
}
</style>

<script setup lang="ts">
import { ref } from 'vue';
import { formatTimestamp, formatTimestampToSlash } from '~/utils/format';

const activeDate = defineModel<Date>();

const resetDay = () => {
    activeDate.value = new Date();
};

// dialog status control
const dialogOpen = ref(false);

const openDialog = () => {
    dialogOpen.value = true;
};

const closeDialog = () => {
    dialogOpen.value = false;
};

// day selection control
const selectedDate = ref(new Date());

const onDaySelectStart = () => {
    selectedDate.value = activeDate.value;
    openDialog();
};

const onDaySelectEnd = (confirm: boolean) => {
    if (confirm) {
        activeDate.value = selectedDate.value;
    }
    closeDialog();
};
</script>
