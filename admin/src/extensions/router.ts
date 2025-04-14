import { createRouter, createWebHistory } from 'vue-router';
import DashBoardPage from '~/pages/DashBoardPage.vue';
import LoginPage from '~/pages/LoginPage.vue';
import { state } from '~/extensions/state';
import alertify from '~/extensions/alertify';

const router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: '/',
            redirect: '/dashboard'
        },
        {
            path: '/dashboard',
            component: DashBoardPage
        },
        {
            path: '/login',
            component: LoginPage
        },
        {
            path: '/:pathMatch(.*)*',
            redirect: '/'
        }
    ]
});

router.beforeEach((to, from, next) => {
    if (to.path !== '/login' && !state.isLoggedIn()) {
        alertify.error('Login required');
        next({ path: '/login' });
    } else {
        next();
    }
});

export default router;
