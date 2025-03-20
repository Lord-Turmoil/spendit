<template>
    <v-card class="PanelView" title="实用功能">
        <v-list class="PanelView__list">
            <v-list-item v-for="(item, i) in menuList" :key="i" link @click="item.action">
                <template v-slot:prepend>
                    <v-icon :icon="item.icon" size="x-large"></v-icon>
                </template>
                <v-list-item-title
                    class="text-h6"
                    v-text="item.title"></v-list-item-title>
            </v-list-item>
        </v-list>
    </v-card>
</template>

<style>
.PanelView {
    padding-top: 8px;
}

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
        title: '消费统计',
        icon: 'mdi-calendar-range',
        action: () => {
            router.push('/overview');
        }
    }
    // {
    //     title: 'Settings',
    //     icon: 'mdi-cog',
    //     action: () => {
    //         console.log('Settings clicked');
    //     }
    // }
];
</script>
