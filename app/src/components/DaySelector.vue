<template>
    <div class="DaySelector">
        <v-btn class="DaySelector__date" prepend-icon="mdi-calendar-range" size="large" @click="onDaySelectStart">
            {{ displayDay }}
        </v-btn>
        <v-btn class="DaySelector__reset" icon="mdi-restore" size="large" @click="resetDay"></v-btn>

        <v-dialog v-model="dialogOpen">
            <v-card max-width="400" prepend-icon="mdi-calendar-range" title="Pick a day">
                <v-date-picker v-model="selectedDate" :max="new Date()" show-adjacent-months></v-date-picker>
                <v-card-actions>
                    <v-btn text @click="onDaySelectEnd(false)">Cancel</v-btn>
                    <v-btn text @click="onDaySelectEnd(true)">OK</v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>
    </div>
</template>

<style scoped>
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
</style>

<script setup>

import { computed, ref } from 'vue';
import { useDate } from 'vuetify';

const activeDate = defineModel();
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

const onDaySelectEnd = (confirm) => {
    if (confirm) {
        activeDate.value = selectedDate.value;
    }
    closeDialog();
};

</script>
