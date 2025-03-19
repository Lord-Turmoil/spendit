<template>
    <div class="DaySelector">
        <v-btn
            class="DaySelector__date"
            prepend-icon="mdi-calendar-range"
            size="large"
            @click="onDaySelectStart">
            {{ displayDay }}
        </v-btn>
        <v-btn
            class="DaySelector__reset"
            icon="mdi-restore"
            size="large"
            @click="resetDay"></v-btn>

        <v-dialog class="DaySelector__selector" v-model="dialogOpen">
            <v-card class="card" prepend-icon="mdi-calendar-range" title="Select a Day">
                <v-date-picker
                    class="selector"
                    v-model="selectedDate"
                    :max="new Date()"
                    show-adjacent-months></v-date-picker>
                <v-card-actions>
                    <v-btn text @click="onDaySelectEnd(false)">Cancel</v-btn>
                    <v-btn text @click="onDaySelectEnd(true)">OK</v-btn>
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

.DaySelector__selector .card {
    margin: 0 auto;
}

.DaySelector__selector .selector {
    margin: 0 auto;
}

.DaySelector__selector .selector .v-picker-title {
    display: none;
}
</style>

<script setup lang="ts">
import { computed, ref, watch } from 'vue';
import { useDate } from 'vuetify';

const activeDate = defineModel<Date>();
const adapter = useDate();

const resetDay = () => {
    activeDate.value = new Date();
};

const displayDay = computed(() => {
    return adapter.format(activeDate.value, 'keyboardDate');
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
