<template>
    <div class="ReleaseView">
        <ActionHeader
            title="App Release"
            actionText="Publish Release"
            :action="openDialog" />
        <v-card>
            <v-list>
                <v-list-item v-for="(item, i) in versions" :key="i">
                    <div class="ReleaseView__item">
                        <span class="title">
                            <span class="version">{{ item.version }}</span>
                            <span class="code">{{ item.code }}</span>
                        </span>
                        <span>
                            <span class="description">
                                {{ item.description }}
                            </span>
                            <span class="actions">
                                <v-btn
                                    variant="text"
                                    icon="mdi-clipboard-multiple-outline"
                                    @click="() => onClickCopyUrl(item)"></v-btn>
                                <v-btn
                                    variant="text"
                                    icon="mdi-cancel"
                                    color="red"
                                    :loading="isWithdrawing"
                                    @click="() => onClickWithDraw(item)"></v-btn>
                            </span>
                        </span>
                    </div>
                </v-list-item>
            </v-list>
        </v-card>

        <v-dialog v-model="dialogOpen">
            <v-card class="ReleaseView__edit" title="Draft Release">
                <v-container>
                    <v-form ref="form">
                        <v-text-field
                            v-model="request.version"
                            variant="outlined"
                            label="Version"
                            placeholder="Version"
                            :rules="rules.version"></v-text-field>
                        <v-text-field
                            v-model="request.code"
                            variant="outlined"
                            label="Code"
                            placeholder="Code"
                            :rules="rules.code"></v-text-field>
                        <v-text-field
                            v-model="request.downloadUrl"
                            variant="outlined"
                            label="Download URL"
                            placeholder="Download URL"
                            :rules="rules.url"></v-text-field>
                        <v-textarea
                            v-model="request.description"
                            variant="outlined"
                            label="Description"
                            placeholder="Description"
                            rows="2"
                            auto-grow
                            :rules="rules.description"></v-textarea>
                    </v-form>
                </v-container>
                <v-card-actions>
                    <v-container class="actions">
                        <v-btn
                            variant="text"
                            color="primary"
                            :loading="isSubmitting"
                            @click="closeDialog">
                            Cancel
                        </v-btn>
                        <v-btn
                            variant="text"
                            color="success"
                            :loading="isSubmitting"
                            @click="onClickSubmit">
                            Publish
                        </v-btn>
                    </v-container>
                </v-card-actions>
            </v-card>
        </v-dialog>
    </div>
</template>

<style scoped>
.ReleaseView__edit {
    width: 60%;
    margin: 0 auto;
}

.ReleaseView__edit .actions {
    display: flex;
    flex-direction: row;
    justify-content: right;
    align-items: center;
    gap: 10px;
}

.ReleaseView__item {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
}

.ReleaseView__item .title {
    flex: 1;
}

.ReleaseView__item .title .version {
    margin-right: 5px;
    font-weight: bold;
}

.ReleaseView__item .title .code {
    font-style: italic;
}

.ReleaseView__item .description {
    margin-right: 10px;
}
</style>

<script setup lang="ts">
import ActionHeader from '~/components/ActionHeader.vue';
import { onMounted, ref } from 'vue';
import { Version } from '~/extensions/models';
import { api } from '~/extensions/api';
import alertify from '~/extensions/alertify';
import { VForm } from 'vuetify/components';
import { LONG_STALL, stall } from '~/extensions/stall';

const versions = ref<Version[]>([]);

async function fetchData() {
    const response = await api.get('/version/list');
    if (response.status !== 200) {
        alertify.error(response.message);
        return;
    }

    versions.value = response.data;
}

// edit actions
const dialogOpen = ref(false);

const openDialog = () => {
    dialogOpen.value = true;
};

const closeDialog = () => {
    dialogOpen.value = false;
};

const form = ref<VForm>();

async function validateForm() {
    const { valid } = await form.value.validate();
    return valid;
}

interface CreateReleaseRequest {
    version: string;
    downloadUrl: string;
    code: string;
    description: string;
}

const rules = {
    version: [
        (v: string) => !!v || 'Version is required',
        (v: string) => /^\d+\.\d+\.\d+$/.test(v) || 'Invalid version format'
    ],
    url: [
        (v: string) => !!v || 'URL is required',
        (v: string) =>
            /^(https?|ftp):\/\/[^\s\/$.?#].\S*$/.test(v) || 'Invalid URL format'
    ],
    code: [(v: string) => !!v || 'Code is required'],
    description: [(v: string) => !!v || 'Description is required']
};

const request = ref<CreateReleaseRequest>({
    version: '',
    downloadUrl: '',
    code: '',
    description: ''
});

const isSubmitting = ref(false);
const onClickSubmit = async () => {
    isSubmitting.value = true;
    await onSubmitRelease();
    isSubmitting.value = false;
};

const onSubmitRelease = async () => {
    if (!(await validateForm())) {
        return;
    }

    const response = await stall(api.post('/version/publish', request.value), LONG_STALL);
    if (response.status !== 200) {
        alertify.error(response.message);
        return;
    }
    alertify.success('Release published');
    versions.value.push(response.data);
    closeDialog();
};

const isWithdrawing = ref(false);

const onClickWithDraw = async (version: Version) => {
    isWithdrawing.value = true;
    const response = await stall(
        api.post('version/withdraw', { id: version.id }),
        LONG_STALL
    );
    isWithdrawing.value = false;

    if (response.status !== 200) {
        alertify.error(response.message);
        return;
    }
    alertify.success('Release withdrawn');

    versions.value = versions.value.filter((v) => v.id != version.id);
};

const onClickCopyUrl = async (item: Version) => {
    await navigator.clipboard.writeText(item.downloadUrl);
    alertify.success('Download URL copied to clipboard');
};

onMounted(() => {
    fetchData();
});
</script>
