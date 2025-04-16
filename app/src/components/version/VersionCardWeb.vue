<template>
    <div class="VersionCardWeb">
        <div class="split-wrapper current">
            <span class="text-h6 split-primary">{{ displayCurrentVersion }}</span>
        </div>
        <div v-if="hasApp" class="latest">
            <div class="title text-body-1 text-green-darken-2">
                <span>安卓 App 版本：</span>
                <span>{{ latestVersion.version }}</span>
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
                    下载并安装安卓 App
                </v-btn>
            </div>
        </div>
    </div>
</template>

<style scoped>
.VersionCardWeb {
    width: 100%;
    padding: 8px;
}

.VersionCardWeb .latest .title {
    margin-top: 8px;
}

.VersionCardWeb .latest .changelog {
    margin: 8px 0;
}

.VersionCardWeb .latest .changelog li {
    list-style-position: inside;
}
</style>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue';
import { engine } from '~/engine/engine';
import { DummyVersion, Version, VersionMeta } from '~/engine/models';

const systemProfile = engine.getSystemProfile();

const displayCurrentVersion = computed(() => {
    return `网页版本 ${systemProfile.version} - ${systemProfile.code}`;
});

// Update
const latestVersion = ref<Version>(DummyVersion);
const changeLog = ref<string[]>([]);
const hasApp = ref(false);

function getChangeLog(description: string): string[] {
    return description
        .split(/\n/)
        .map((str: string) => str.trim())
        .filter((str: string) => str.length > 0);
}

onMounted(() => {
    engine.checkForUpdate(false).then((meta: VersionMeta) => {
        if (meta.version) {
            latestVersion.value = meta.version;
            changeLog.value = getChangeLog(latestVersion.value.description);
            hasApp.value = true;
        }
    });
});
</script>
