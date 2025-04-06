<template>
    <div class="FabBackButton">
        <v-fab icon="mdi-close" color="deep-orange-darken-1" @click="onClickBack"></v-fab>
    </div>
</template>

<style scoped>
.FabBackButton {
    position: fixed;
    bottom: 16px;
    right: 16px;
    z-index: 1000;
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
