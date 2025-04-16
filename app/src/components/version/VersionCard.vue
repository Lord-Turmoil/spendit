<template>
    <div class="VersionCard">
        <div class="split-wrapper current">
            <span class="text-h6 split-primary">{{ displayCurrentVersion }}</span>
            <v-btn
                variant="flat"
                color="yellow-darken-1"
                :loading="isCheckingUpdate"
                @click="onClickCheckUpdate">
                检查更新
            </v-btn>
        </div>
        <div v-if="isUpdateAvailable" class="latest">
            <div class="title text-body-1 text-green-darken-2">
                <span>有新版本可用：</span>
                <span>{{ displayNewVersion }}</span>
            </div>
            <div class="changelog">
                <ul>
                    <li v-for="(item, index) in changeLog" :key="index">
                        {{ item }}
                    </li>
                </ul>
            </div>
            <div>
                <v-btn
                    variant="flat"
                    color="yellow-darken-1"
                    :href="latestVersion.downloadUrl"
                    target="_blank"
                    block>
                    （从浏览器）下载并安装
                </v-btn>
            </div>
        </div>
        <div v-if="isLatestVersion">
            <div class="title text-body-1">
                <span>当前已是最新版本。</span>
            </div>
        </div>
    </div>
</template>

<style scoped>
.VersionCard {
    width: 100%;
    padding: 8px;
}

.VersionCard .current {
    margin-bottom: 16px;
}

.VersionCard .latest .changelog {
    margin: 8px 0;
}

.VersionCard .latest .changelog li {
    list-style-position: inside;
}
</style>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue';
import { engine } from '~/engine/engine';
import { DummyVersion, Version, VersionMeta } from '~/engine/models';
import alertify from '~/extensions/alertify';
import { hurray } from '~/extensions/confetti';
import { LONG_STALL, stall } from '~/utils/stall';

const systemProfile = engine.getSystemProfile();

const displayCurrentVersion = computed(() => {
    return `当前版本 ${systemProfile.version}`;
});

// Update
const isUpdateAvailable = ref(false);
const latestVersion = ref<Version>(DummyVersion);
const changeLog = ref<string[]>([]);

const isCheckingUpdate = ref(true);
const isLatestVersion = ref(false);

const displayNewVersion = computed(() => {
    return `${latestVersion.value.version} - ${latestVersion.value.code}`;
});

const onClickCheckUpdate = async () => {
    isCheckingUpdate.value = true;
    await stall(engine.checkForUpdate(true), LONG_STALL)
        .then((meta: VersionMeta) => {
            isUpdateAvailable.value = meta.updateAvailable;
            if (isUpdateAvailable.value) {
                latestVersion.value = meta.version!;
                changeLog.value = getChangeLog(latestVersion.value.description);
                isLatestVersion.value = false;
                alertify.success(`发现新版本 ${latestVersion.value.version}`);
            } else {
                alertify.success('当前已是最新版本');
                isLatestVersion.value = true;
                hurray();
            }
        })
        .catch((error) => {
            alertify.error(error.message);
        })
        .finally(() => {
            isCheckingUpdate.value = false;
        });
};

function getChangeLog(description: string): string[] {
    return description
        .split(/\n/)
        .map((str: string) => str.trim())
        .filter((str: string) => str.length > 0);
}

onMounted(() => {
    engine
        .checkForUpdate(false)
        .then((meta: VersionMeta) => {
            isUpdateAvailable.value = meta.updateAvailable;
            if (isUpdateAvailable.value) {
                latestVersion.value = meta.version!;
                changeLog.value = getChangeLog(latestVersion.value.description);
            } else {
                isLatestVersion.value = true;
            }
        })
        .finally(() => {
            isCheckingUpdate.value = false;
        });
});
</script>
