import { createRouter, createWebHistory } from 'vue-router';

import HomeScreen from '~/screen/HomeScreen.vue';
import DetailScreen from '~/screen/DetailScreen.vue';
import OverviewScreen from '~/screen/OverviewScreen.vue';

const router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: '/',
            redirect: '/home'
        },
        {
            path: '/home',
            component: HomeScreen
        },
        {
            path: '/overview',
            component: OverviewScreen
        },
        {
            path: '/detail',
            component: DetailScreen
        }
    ]
});

export default router;
