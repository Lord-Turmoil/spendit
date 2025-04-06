<template>
    <v-btn
        class="BackButton"
        @click="onClickBack"
        variant="flat"
        color="deep-orange-darken-1">
        返回
    </v-btn>
</template>

<style scoped>
.BackButton {
    max-height: 44px;
    margin-top: 16px;
    width: 100%;
}
</style>

<script setup lang="ts">
import router from '~/extensions/router';

const onClickBack = () => {
    // check if there is a previous route
    if (window.history.state.back === null) {
        router.push('/home');
    } else {
        router.back();
    }
};
</script>
