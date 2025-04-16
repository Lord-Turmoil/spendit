<template>
    <v-chip v-bind="props" size="x-small" color="green-darken-1" variant="outlined">
        {{ name }}
    </v-chip>
</template>

<style scoped></style>

<script setup lang="ts">
const { name, props } = defineProps<{ name: string; props?: any }>();
</script>
