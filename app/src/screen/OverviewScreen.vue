<template>
    <v-container class="OverviewScreen scrollable split-wrapper">
        <DaySelectLayout class="split-primary"></DaySelectLayout>
        <BackButton></BackButton>
    </v-container>
</template>

<style scoped>
.OverviewScreen {
    min-height: 100vh;
    flex-direction: column;
}
</style>

<script setup lang="ts">
import BackButton from '~/components/BackButton.vue';
import DaySelectLayout from '~/layout/DaySelectLayout.vue';
</script>
