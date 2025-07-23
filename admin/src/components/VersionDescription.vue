<template>
    <ul class="VersionDescription">
        <li v-for="(item, index) in items" :key="index">
            {{ item }}
        </li>
    </ul>
</template>

<style scoped></style>

<script setup lang="ts">
interface VersionDescriptionProps {
    description: string;
}

const { description } = defineProps<VersionDescriptionProps>();

const items = description.split('\n').filter((line) => line.trim() !== '');
</script>
