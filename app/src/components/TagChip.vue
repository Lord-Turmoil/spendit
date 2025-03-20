<template>
    <v-chip variant="outlined" color="blue-darken-1" size="x-small">
        <v-icon icon="mdi-pound"></v-icon>
        {{ tag }}
    </v-chip>
</template>

<style scoped></style>

<script setup lang="ts">
const { tag } = defineProps<{ tag: string }>();
</script>
