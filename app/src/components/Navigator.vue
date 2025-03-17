<template>
    <ul>
        <li><a @click="toOverview()">Overview</a></li>
        <li><a @click="toDetail()">Detail</a></li>
    </ul>
</template>

<style scoped></style>

<script setup lang="ts">
import router from '~/router/router';

// jump to /overview
const toOverview = () => {
    router.push('/overview');
};

// jump to /detail
const toDetail = () => {
    router.push('/detail');
};
</script>
