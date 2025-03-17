<template>
    <div class="DayLayout">
        <DaySelector v-model="activeDate" />
    </div>
</template>

<style scoped></style>

<script setup lang="ts">
import { ref } from 'vue';

import DaySelector from '~/components/DaySelector.vue';

const activeDate = ref(new Date());

</script>
