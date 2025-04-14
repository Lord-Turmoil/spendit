<template>
    <v-dialog v-model="dialogOpen" class="ConfirmDialog" :persistent="persistent">
        <v-card class="ConfirmDialog__main" prepend-icon="mdi-alert" :title="title">
            <v-card-text>{{ message }}</v-card-text>
            <v-card-actions>
                <v-btn :color="cancelColor" size="large" @click="onCancel">
                    {{ cancelText }}
                </v-btn>
                <v-btn :color="confirmColor" size="large" @click="onConfirm">
                    {{ confirmText }}
                </v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<style scoped>
.ConfirmDialog__main {
    width: 90%;
    margin: 0 auto;
}
</style>

<script setup lang="ts">
export interface ConfirmDialogProps {
    title: string;
    message: string;

    confirmText?: string;
    confirmColor?: string;
    onConfirm: () => void;

    cancelText?: string;
    cancelColor?: string;
    onCancel: () => void;

    persistent?: boolean;
}

const dialogOpen = defineModel<boolean>();

const {
    title,
    message,
    confirmText = '确认',
    confirmColor = 'success',
    onConfirm = () => {},
    cancelText = '取消',
    cancelColor = 'primary',
    onCancel = () => {},
    persistent = false
} = defineProps<ConfirmDialogProps>();
</script>
