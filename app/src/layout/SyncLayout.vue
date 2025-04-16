<template>
    <div class="SyncLayout scrollable">
        <v-card variant="flat" title="推送数据" prepend-icon="mdi-cloud-upload">
            <v-card-text>
                <p>将本地数据上传至云端，推荐用于首次备份。</p>
                <p class="danger">云端数据将会被覆盖！</p>
            </v-card-text>
            <v-card-actions>
                <v-btn
                    variant="flat"
                    color="success"
                    block
                    :disabled="isLoading"
                    @click="onClickPush">
                    推送数据
                </v-btn>
            </v-card-actions>
        </v-card>

        <v-card variant="flat" title="拉取数据" prepend-icon="mdi-cloud-download">
            <v-card-text>
                <p>从云端拉取数据，推荐用于首次获取云端数据。</p>
                <p class="danger">本地数据将会被覆盖！</p>
            </v-card-text>
            <v-card-actions>
                <v-btn
                    variant="flat"
                    color="success"
                    block
                    :disabled="isLoading"
                    @click="onClickPull">
                    拉取数据
                </v-btn>
            </v-card-actions>
        </v-card>

        <v-card variant="flat" title="同步数据" prepend-icon="mdi-cloud-sync">
            <v-card-text>
                <p>合并本地与云端数据，首次同步请推送数据。</p>
            </v-card-text>
            <v-card-actions>
                <v-btn
                    variant="flat"
                    color="success"
                    block
                    :disabled="isLoading"
                    @click="onCLickMerge">
                    同步数据
                </v-btn>
            </v-card-actions>
        </v-card>

        <div style="text-align: center; margin-top: 16px">
            <span class="text-grey-darken-3 text-body-2">
                上次同步时间：{{ lastSyncTime }}
            </span>
        </div>

        <v-dialog class="SyncLayout__popup" v-model="dialogOpen" persistent>
            <v-card variant="flat" title="同步中">
                <template v-slot:prepend>
                    <v-progress-circular
                        indeterminate
                        color="warning"
                        width="4"></v-progress-circular>
                </template>
                <v-card-text>
                    <p style="color: red">正在同步数据，请勿关闭该页面...</p>
                </v-card-text>
            </v-card>
        </v-dialog>

        <ConfirmDialog
            v-model="confirmDialog"
            :title="confirmDialogProps.title"
            :message="confirmDialogProps.message"
            :confirmText="confirmDialogProps.confirmText"
            :cancelText="confirmDialogProps.cancelText"
            :on-confirm="confirmDialogProps.onConfirm"
            :on-cancel="confirmDialogProps.onCancel" />
    </div>
</template>

<style scoped>
.SyncLayout {
    width: 100%;
    height: 100%;
}

.SyncLayout p.danger {
    color: red;
}
</style>

<script setup lang="ts">
import ConfirmDialog, { ConfirmDialogProps } from '~/components/ConfirmDialog.vue';

import { onMounted, ref } from 'vue';
import { useDate } from 'vuetify';

import { engine } from '~/engine/engine';
import alertify from '~/extensions/alertify';
import { getNative } from '~/utils/native';
import { LONG_STALL, stall } from '~/utils/stall';

const native = getNative();

// sync time
const loadSyncTime = () => {
    const value = native.getLocalStorage('lastSyncTime');
    if (value === null) {
        lastSyncTime.value = '没有同步记录';
    } else {
        lastSyncTime.value = adapter.format(new Date(value), 'fullDateTime24h');
    }
};

const saveSyncTime = () => {
    const now = new Date();
    native.setLocalStorage('lastSyncTime', now.toISOString());
    lastSyncTime.value = adapter.format(now, 'fullDateTime24h');
};

onMounted(() => {
    loadSyncTime();
});

// confirm dialog control
const PUSH_CONFIRM = {
    title: '确认推送数据',
    message: '推送数据将会覆盖云端数据，未拉取到本地的数据将会丢失，是否继续？',
    confirmText: '继续',
    cancelText: '取消',
    onConfirm: () => onConfirmPush(true),
    onCancel: () => onConfirmPush(false)
};

const PULL_CONFIRM = {
    title: '确认拉取数据',
    message: '拉取数据将会覆盖本地数据，本地未推送的数据将会丢失，是否继续？',
    confirmText: '继续',
    cancelText: '取消',
    onConfirm: () => onConfirmPull(true),
    onCancel: () => onConfirmPull(false)
};

const confirmDialog = ref(false);
const confirmDialogProps = ref<ConfirmDialogProps>(PUSH_CONFIRM);

const openConfirmDialog = () => {
    confirmDialog.value = true;
};

const closeConfirmDialog = () => {
    confirmDialog.value = false;
};

// sync dialog control
const dialogOpen = ref(false);
const isLoading = ref(false);

const onSyncBegin = () => {
    isLoading.value = true;
    dialogOpen.value = true;
};

const onSyncEnd = () => {
    isLoading.value = false;
    dialogOpen.value = false;
};

const lastSyncTime = ref('');
const adapter = useDate();

const onClickPush = async () => {
    confirmDialogProps.value = PUSH_CONFIRM;
    openConfirmDialog();
};

const onConfirmPush = (confirm: boolean) => {
    closeConfirmDialog();
    if (confirm) {
        onClickPushConfirm();
    }
};

const onClickPushConfirm = async () => {
    onSyncBegin();
    await stall(engine.push(), LONG_STALL)
        .then(() => {
            alertify.success('数据推送成功');
            saveSyncTime();
        })
        .catch((error) => {
            alertify.error(error.message);
        });
    onSyncEnd();
};

const onClickPull = async () => {
    confirmDialogProps.value = PULL_CONFIRM;
    openConfirmDialog();
};

const onConfirmPull = (confirm: boolean) => {
    closeConfirmDialog();
    if (confirm) {
        onClickPullConfirm();
    }
};

const onClickPullConfirm = async () => {
    onSyncBegin();
    await stall(engine.pull(), LONG_STALL)
        .then(() => {
            alertify.success('数据拉取成功');
            saveSyncTime();
        })
        .catch((error) => {
            alertify.error(error.message);
        });
    onSyncEnd();
};

const onCLickMerge = async () => {
    onSyncBegin();
    await stall(engine.merge(), LONG_STALL)
        .then(() => {
            alertify.success('数据同步成功');
            saveSyncTime();
        })
        .catch((error) => {
            alertify.error(error.message);
        });
    onSyncEnd();
};
</script>
