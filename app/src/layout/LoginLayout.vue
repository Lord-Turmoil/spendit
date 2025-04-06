<template>
    <div class="LoginLayout">
        <RegisterView v-model="isRegister" v-if="isRegister"></RegisterView>
        <LoginView v-model="isRegister" :type="title" :can-logout="canLogout" v-else></LoginView>
    </div>
</template>

<style scoped>
.LoginLayout {
    width: 100%;
    height: 100%;
}
</style>

<script setup lang="ts">
import { computed, ref, watch } from 'vue';
import { useRoute } from 'vue-router';
import { engine } from '~/engine/engine';
import router from '~/extensions/router';
import LoginView from '~/view/LoginView.vue';
import RegisterView from '~/view/RegisterView.vue';

const isRegister = ref(useRoute().query.register === 'true');
watch(isRegister, () => {
    router.replace({ query: { register: isRegister.value.toString() } });
});

const title = computed(() => {
    return engine.isLoggedIn() ? '切换账号' : '登录';
});

const canLogout = computed(() => {
    return engine.isLoggedIn();
});

</script>
