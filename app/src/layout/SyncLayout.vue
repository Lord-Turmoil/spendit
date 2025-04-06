<template>
    <div class="SyncLayout">
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
                <p>合并本地与云端数据，并更新本地数据。</p>
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
import { ref } from 'vue';
import { engine } from '~/engine/engine';
import alertify from '~/extensions/alertify';
import { delay, LONG_STALL, stall } from '~/utils/stall';

// dialog control
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

const onClickPush = async () => {
    onSyncBegin();
    await stall(engine.push(), LONG_STALL)
        .then(() => {
            alertify.success('数据推送成功');
        })
        .catch((err) => {
            alertify.error(err.message);
        });
    onSyncEnd();
};

const onClickPull = async () => {
    onSyncBegin();
    await stall(engine.pull(), LONG_STALL)
        .then(() => {
            alertify.success('数据拉取成功');
        })
        .catch((err) => {
            alertify.error(err.message);
        });
    onSyncEnd();
};

const onCLickMerge = async () => {
    onSyncBegin();
    await stall(engine.merge(), LONG_STALL)
        .then(() => {
            alertify.success('数据同步成功');
        })
        .catch((err) => {
            alertify.error(err.message);
        });
    onSyncEnd();
};
</script>
