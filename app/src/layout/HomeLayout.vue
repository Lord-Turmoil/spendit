<!-- Top-level layout -->
<template>
    <v-container class="HomeLayout">
        <DayLayout v-show="activeTab === 0" />
        <MenuLayout v-show="activeTab === 1" />

        <Navigator v-model="activeTab" />
    </v-container>
</template>

<style>
.HomeLayout {
    width: 100%;
    height: 100%;
}
</style>

<script setup lang="ts">
import { ref } from 'vue';

import DayLayout from '~/layout/DayLayout.vue';
import MenuLayout from '~/layout/MenuLayout.vue';
import Navigator from '~/components/Navigator.vue';

const activeTab = ref(0);
</script>
