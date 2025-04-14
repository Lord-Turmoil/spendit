<template>
    <v-container class="LoginPage">
        <div class="LoginPage__title">
            <img src="/favicon.png" />
            <h1>Spendit Admin Dashboard</h1>
        </div>
        <div class="LoginPage__form">
            <LoginView></LoginView>
        </div>
    </v-container>
</template>

<style scoped>
.LoginPage {
    width: 100vw;
    height: 100vh;
    display: flex;
    flex-direction: row;
    align-items: center;
    gap: 20px;
}

.LoginPage__title {
    display: flex;
    flex-direction: column;
    align-items: end;
}

.LoginPage__title img {
    width: 150px;
}

.LoginPage__form {
    flex: 1;
}
</style>

<script lang="ts" setup>
import LoginView from '~/views/LoginView.vue';
</script>
