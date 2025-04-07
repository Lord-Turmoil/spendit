<template>
    <div class="HomeLayout">
        <DayLayout v-show="activeTab === 0" />
        <MenuLayout v-show="activeTab === 1" />

        <Navigator v-model="activeTab" />
    </div>
</template>

<style>
.HomeLayout {
    width: 100%;
    height: 100%;
}
</style>

<script setup lang="ts">
import DayLayout from '~/layout/DayLayout.vue';
import MenuLayout from '~/layout/MenuLayout.vue';
import Navigator from '~/components/Navigator.vue';

import { ref, watch } from 'vue';
import { useRoute } from 'vue-router';

import router from '~/extensions/router';

const activeTab = ref(useRoute().query.tab === '1' ? 1 : 0);

watch(activeTab, () => {
    router.replace({ query: { tab: activeTab.value.toString() } });
});
</script>
