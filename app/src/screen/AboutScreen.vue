<template>
    <v-container class="AboutScreen scrollable split-wrapper">
        <AboutLayout class="split-primary" />
        <BackButton />
    </v-container>
</template>

<style scoped>
.AboutScreen {
    min-height: 100%;
    flex-direction: column;
}
</style>

<script setup lang="ts">
import BackButton from '~/components/BackButton.vue';
import AboutLayout from '~/layout/AboutLayout.vue';
</script>
