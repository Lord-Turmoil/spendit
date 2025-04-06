<template>
    <v-container class="DetailScreen scrollable">
        <DetailView :dates="dates" show-date style="margin-bottom: 36px"></DetailView>
        <FabBackButton></FabBackButton>
    </v-container>
</template>

<style scoped>
.DetailScreen {
    height: 100vh;
    flex-direction: column;
}
</style>

<script setup lang="ts">
import FabBackButton from '~/components/FabBackButton.vue';
import DetailView from '~/view/DetailView.vue';

import { engine } from '~/engine/engine';

const dates = engine.getSelectedDates();
</script>
