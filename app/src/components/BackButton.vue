<template>
    <v-btn
        class="BackButton"
        size="large"
        @click="onClickBack"
        variant="flat"
        color="deep-orange-darken-1"
        block>
        返回
    </v-btn>
</template>

<style scoped>
.BackButton {
    max-height: 44px;
    margin-top: 16px;
}
</style>

<script setup lang="ts">
import router from '~/extensions/router';

const onClickBack = () => {
    router.back();
};
</script>
