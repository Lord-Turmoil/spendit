<template>
    <v-container class="AboutScreen scrollable split-wrapper">
        <AboutLayout class="split-primary" />
        <div style="width: 100%; padding: 0 8px">
            <BackButton />
        </div>
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
