<template>
    <v-container class="DashBoardPage">
        <v-card class="DashBoardPage__nav">
            <MenuBar v-model="selected" :items="MENU_ITEMS"></MenuBar>
        </v-card>
        <v-divider></v-divider>
        <div class="DashBoardPage__content">
            <component :is="activeComponent"></component>
        </div>
    </v-container>
</template>

<style scoped>
.DashBoardPage {
    min-height: 100vh;
}

.DashBoardPage__nav {
    padding: 10px;
}
</style>

<script setup lang="ts">
import MenuBar, { MenuItem } from '~/components/MenuBar.vue';
import { computed, ref, watch } from 'vue';
import ServerStatusView from '~/views/ServerStatusView.vue';
import InvitationView from '~/views/InvitationView.vue';
import ReleaseView from '~/views/ReleaseView.vue';

interface MenuItemWithCallback extends MenuItem {
    id: number;
    component: () => any;
}

const MENU_ITEMS = [
    {
        id: 0,
        name: 'Server Status',
        description: 'Monitoring server status',
        component: () => ServerStatusView
    },
    {
        id: 1,
        name: 'Invitation',
        description: 'Manage invitation codes',
        component: () => InvitationView
    },
    {
        id: 2,
        name: 'Release',
        description: 'Publish new version',
        component: () => ReleaseView
    }
];

const lastTabId = parseInt(localStorage.getItem('lastTabId') || '0');
const selected = ref<MenuItemWithCallback>(MENU_ITEMS[lastTabId]);

const activeComponent = computed(() => selected.value.component());

watch(selected, (value) => {
    localStorage.setItem('lastTabId', value.id.toString());
});
</script>
