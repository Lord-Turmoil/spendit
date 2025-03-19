<template>
    <div class="PanelView">
        <v-card class="mx-auto">
            <v-card-title>Functions</v-card-title>
            <v-list class="PanelView__list">
                <v-list-item
                    v-for="(item, i) in menuList"
                    :key="i"
                    link
                    @click="item.action">
                    <template v-slot:prepend>
                        <v-icon :icon="item.icon"></v-icon>
                    </template>
                    <v-list-item-title v-text="item.title"></v-list-item-title>
                </v-list-item>
            </v-list>
        </v-card>
    </div>
</template>

<style>
.PanelView__list {
    width: 98%;
    margin: 0 auto;
}

.PanelView__list .v-list-item {
    margin: 5px auto;
}
</style>

<script setup lang="ts">
import router from '~/extensions/router';

const menuList = [
    {
        title: 'View days',
        icon: 'mdi-calendar-range',
        action: () => {
            router.push('/overview');
        }
    },
    {
        title: 'Settings',
        icon: 'mdi-cog',
        action: () => {
            console.log('Settings clicked');
        }
    }
];
</script>
