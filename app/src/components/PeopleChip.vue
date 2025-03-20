<template>
    <v-chip variant="outlined" color="pink" size="small">
        <v-icon icon="mdi-at"></v-icon>
        {{ name }}
    </v-chip>
</template>

<style scoped></style>

<script setup lang="ts">
const { name } = defineProps<{ name: string }>();
</script>
