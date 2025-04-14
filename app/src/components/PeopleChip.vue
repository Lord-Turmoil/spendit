<template>
    <v-chip v-bind="props" variant="outlined" color="pink-darken-1" size="x-small">
        <v-icon icon="mdi-at"></v-icon>
        &nbsp;{{ name }}
    </v-chip>
</template>

<style scoped></style>

<script setup lang="ts">
const { name, props } = defineProps<{ name: string, props?: any }>();
</script>
