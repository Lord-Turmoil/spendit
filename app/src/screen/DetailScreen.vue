<template>
    <v-container class="DetailScreen scrollable split-wrapper">
        <DetailView class="split-primary" :dates="dates" :show-date="true"></DetailView>
        <BackButton></BackButton>
    </v-container>
</template>

<style scoped>
.DetailScreen {
    min-height: 100vh;
    flex-direction: column;
}
</style>

<script setup lang="ts">
import BackButton from '~/components/BackButton.vue';
import DetailView from '~/view/DetailView.vue';

import { engine } from '~/engine/engine';

const dates = engine.getSelectedDates();
</script>
