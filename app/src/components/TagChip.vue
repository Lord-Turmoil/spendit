<template>
    <v-chip v-bind="props" variant="outlined" color="blue-darken-1" size="x-small">
        <v-icon icon="mdi-pound"></v-icon>
        &nbsp;{{ tag }}
    </v-chip>
</template>

<style scoped></style>

<script setup lang="ts">
const { tag, props } = defineProps<{ tag: string, props?: any }>();
</script>
