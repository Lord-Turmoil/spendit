<template>
    <v-container class="SyncScreen split-wrapper">
        <SyncLayout class="split-primary"></SyncLayout>
        <BackButton></BackButton>
    </v-container>
</template>

<style scoped>
.SyncScreen {
    width: 100%;
    height: 100%;
    flex-direction: column;
}
</style>

<script setup lang="ts">
import BackButton from '~/components/BackButton.vue';
import SyncLayout from '~/layout/SyncLayout.vue';
</script>
