<template>
    <div class="ActionHeader">
        <h2 class="title">{{ title }}</h2>
        <v-btn
            v-if="actionText"
            variant="outlined"
            :color="actionColor"
            :loading="isLoading"
            @click="invokeAction">
            {{ actionText }}
        </v-btn>
    </div>
</template>

<style scoped>
.ActionHeader {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
}

.ActionHeader .title {
    margin: 20px 0;
    flex: 1;
}
</style>

<script setup lang="ts">
import { ref } from 'vue';

interface ActionHeaderProps {
    title: string;
    actionText?: string;
    actionColor?: string;
    action?: () => Promise<void> | void;
}

const {
    title,
    actionText,
    actionColor = 'primary',
    action
} = defineProps<ActionHeaderProps>();

const isLoading = ref(false);

const invokeAction = async () => {
    if (action) {
        isLoading.value = true;
        await action();
        isLoading.value = false;
    }
};
</script>
