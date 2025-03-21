<template>
    <div class="DaySelector">
        <v-btn
            class="DaySelector__date text-body-1"
            prepend-icon="mdi-calendar-range"
            size="large"
            variant="outlined"
            color="light-blue-darken-1"
            @click="onDaySelectStart">
            {{ displayDate }}
        </v-btn>
        <v-btn
            class="DaySelector__reset"
            icon="mdi-restore"
            size="default"
            variant="flat"
            color="orange-darken-1"
            @click="resetDay"></v-btn>

        <v-dialog class="DaySelector__dialog day-selector" v-model="dialogOpen">
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
import { ref, watch } from 'vue';
import { useDate } from 'vuetify';

const activeDate = defineModel<Date>();

const resetDay = () => {
    activeDate.value = new Date();
};

const adapter = useDate();
const displayDate = ref(adapter.format(activeDate.value, 'fullDate'));

watch(activeDate, () => {
    displayDate.value = adapter.format(activeDate.value, 'fullDate');
});

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
