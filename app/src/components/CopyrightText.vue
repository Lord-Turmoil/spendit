<template>
    <span class="CopyrightText">
        Copyright &copy; {{ displayAuthor }} {{ displayYear }}
    </span>
</template>

<style scoped></style>

<script setup lang="ts">
import { computed } from 'vue';
import { engine } from '~/engine/engine';

const displayAuthor = computed(() => {
    const systemProfile = engine.getSystemProfile();
    return systemProfile.author;
});

const displayYear = computed(() => {
    const currentYear = new Date().getFullYear();
    if (currentYear === 2025) {
        return `2025`;
    }
    return `2025 - ${currentYear}`;
});
</script>
