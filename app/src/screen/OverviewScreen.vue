<template>
    <v-container class="OverviewScreen scrollable split-wrapper">
        <DaySelectLayout class="split-primary"></DaySelectLayout>
        <FabBackButton></FabBackButton>
    </v-container>
</template>

<style scoped>
.OverviewScreen {
    min-height: 100vh;
    flex-direction: column;
}
</style>

<script setup lang="ts">
import FabBackButton from '~/components/FabBackButton.vue';
import DaySelectLayout from '~/layout/DaySelectLayout.vue';
</script>
