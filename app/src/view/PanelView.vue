<template>
    <v-card class="PanelView" title="实用功能" variant="flat">
        <v-list class="PanelView__list">
            <v-list-item
                v-for="(item, i) in menuList"
                :key="i"
                link
                @click="item.action"
                :prepend-icon="item.icon"
                append-icon="mdi-chevron-right">
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
import { engine } from '~/engine/engine';
import router from '~/extensions/router';

const menuList = [
    {
        title: '消费统计',
        icon: 'mdi-chart-bar',
        action: () => {
            router.push('/overview');
        }
    },
    {
        title: engine.isLoggedIn() ? '切换账号/登出' : '登录/注册',
        icon: 'mdi-account',
        action: () => {
            router.push('/login');
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

if (engine.isLoggedIn()) {
    menuList.push({
        title: '同步数据',
        icon: 'mdi-sync',
        action: () => {
            router.push('/sync');
        }
    });
}
</script>
