<template>
    <div class="BackButton">
        <v-btn @click="onClickBack" block variant="flat" color="deep-orange-darken-1">
            返回
        </v-btn>
    </div>
</template>

<style scoped>
.BackButton {
    width: 100%;
    padding: 0 8px;
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
