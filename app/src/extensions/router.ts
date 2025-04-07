import HomeScreen from '~/screen/HomeScreen.vue';
import DetailScreen from '~/screen/DetailScreen.vue';
import LoginScreen from '~/screen/LoginScreen.vue';
import OverviewScreen from '~/screen/OverviewScreen.vue';
import SyncScreen from '~/screen/SyncScreen.vue';

import { createRouter, createWebHistory } from 'vue-router';

import { engine } from '~/engine/engine';
import alertify from '~/extensions/alertify';

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
        },
        {
            path: '/login',
            component: LoginScreen
        },
        {
            path: '/sync',
            component: SyncScreen
        },
        {
            path: '/:pathMatch(.*)*',
            redirect: '/home'
        }
    ]
});

const AUTH_ROUTES = ['/sync'];
router.beforeEach((to, from, next) => {
    if (AUTH_ROUTES.includes(to.path) && !engine.isLoggedIn()) {
        alertify.error('请先登录');
        next({ path: '/login' });
    } else {
        next();
    }
});

export default router;
