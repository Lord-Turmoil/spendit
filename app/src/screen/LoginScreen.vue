<template>
    <v-container class="LoginScreen scrollable split-wrapper">
        <LoginLayout class="split-primary"></LoginLayout>
        <BackButton></BackButton>
    </v-container>
</template>

<style scoped>
.LoginScreen {
    width: 100%;
    height: 100%;
    flex-direction: column;
}
</style>

<script setup lang="ts">
import BackButton from '~/components/BackButton.vue';
import LoginLayout from '~/layout/LoginLayout.vue';
</script>
