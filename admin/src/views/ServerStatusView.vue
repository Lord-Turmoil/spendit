<template>
    <div class="ServerStatusView">
        <ActionHeader
            title="Endpoint Availability"
            action-text="Check All"
            :action="checkAll"></ActionHeader>
        <v-row>
            <v-col v-for="(api, index) in apiList" :key="index" cols="12" md="4">
                <v-card class="api-card" :title="api.name">
                    <template v-slot:prepend>
                        <v-progress-circular
                            v-if="api.pending"
                            indeterminate
                            color="success"
                            width="4"></v-progress-circular>
                        <v-badge
                            v-else
                            :color="api.status === '200' ? 'green' : 'red'"
                            inline></v-badge>
                    </template>
                    <v-card-subtitle v-if="api.pending">Checking...</v-card-subtitle>
                    <v-card-subtitle v-else>{{ api.status }}</v-card-subtitle>
                    <v-card-actions class="actions">
                        <v-btn @click="() => checkApiStatus(api)" :disabled="api.pending">
                            Check Status
                        </v-btn>
                    </v-card-actions>
                </v-card>
            </v-col>
        </v-row>
    </div>
</template>

<style scoped>
.ServerStatusView .title {
    margin: 20px 0;
}

.ServerStatusView .api-card .actions {
    justify-content: right;
}
</style>

<script setup lang="ts">
import { api } from '~/extensions/api';
import { onMounted, ref } from 'vue';
import { LONG_STALL, stall } from '~/extensions/stall';
import ActionHeader from '~/components/ActionHeader.vue';

interface ApiStatus {
    name: string;
    status: string; // status code
    pending: boolean;
    action: (status: ApiStatus) => Promise<void>;
}

function initApiStatus(
    name: string,
    action: (status: ApiStatus) => Promise<void>
): ApiStatus {
    return {
        name,
        status: '',
        pending: true,
        action
    };
}

async function checkApiStatus(status: ApiStatus) {
    status.pending = true;
    await status.action(status).finally(() => {
        status.pending = false;
    });
}

const apiList = ref<ApiStatus[]>([
    initApiStatus('Server', checkServerStatus),
    initApiStatus('Web App', checkWebAppStatus),
    initApiStatus('Release Page', checkReleasePageStatus)
]);

async function checkAll() {
    const promises = [];
    for (const api of apiList.value) {
        promises.push(checkApiStatus(api));
    }
    await Promise.all(promises);
}

async function checkServerStatus(status: ApiStatus): Promise<void> {
    const response = await stall(api.get('/health/ping'), LONG_STALL);
    status.status = response.status.toString();
    return Promise.resolve();
}

async function checkWebAppStatus(status: ApiStatus): Promise<void> {
    await stall(
        fetch('https://spendit.tonys-studio.top')
            .then((response) => {
                status.status = response.status.toString();
            })
            .catch((error) => {
                status.status = error.message;
            }),
        LONG_STALL
    );
    return Promise.resolve();
}

async function checkReleasePageStatus(status: ApiStatus): Promise<void> {
    await stall(
        fetch('https://lord-turmoil.github.io/spendit/')
            .then((response) => {
                status.status = response.status.toString();
            })
            .catch((error) => {
                status.status = error.message;
            }),
        LONG_STALL
    );
    return Promise.resolve();
}

onMounted(() => {
    checkAll();
});
</script>
