<template>
    <v-chip variant="outlined" color="pink-darken-1" size="x-small">
        <v-icon icon="mdi-at"></v-icon>
        {{ name }}
    </v-chip>
</template>

<style scoped></style>

<script setup lang="ts">
const { name } = defineProps<{ name: string }>();
</script>
