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
import { ref } from 'vue';
import { engine } from '~/engine/engine';
import router from '~/extensions/router';

const OFFLINE_LIST = [
    {
        title: '消费统计',
        icon: 'mdi-chart-bar',
        action: () => {
            router.push('/overview');
        }
    },
    {
        title: '登录/注册',
        icon: 'mdi-account',
        action: () => {
            router.push('/login');
        }
    }
];

const ONLINE_LIST = [
    {
        title: '消费统计',
        icon: 'mdi-chart-bar',
        action: () => {
            router.push('/overview');
        }
    },
    {
        title: '切换账号/登出',
        icon: 'mdi-account',
        action: () => {
            router.push('/login');
        }
    },
    {
        title: '数据同步',
        icon: 'mdi-cloud-upload',
        action: () => {
            router.push('/sync');
        }
    }
];

const menuList = ref(engine.isLoggedIn() ? ONLINE_LIST : OFFLINE_LIST);
</script>
