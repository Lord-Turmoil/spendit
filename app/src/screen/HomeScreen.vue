<template>
    <v-container class="HomeScreen">
        <HomeLayout />
    </v-container>
</template>

<style scoped>
.HomeScreen {
    width: 100%;
    height: 100%;
}
</style>

<script setup lang="ts">
import router from '~/extensions/router';
import HomeLayout from '~/layout/HomeLayout.vue';

const route = router.currentRoute.value;
if (!route.path.endsWith('/home')) {
    router.replace({ path: '/home' });
}
</script>
