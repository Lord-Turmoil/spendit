<template>
    <div class="LoginView">
        <v-card class="LoginView__main" :title="type" variant="flat">
            <v-container>
                <v-form ref="form">
                    <v-text-field
                        v-model="username"
                        variant="outlined"
                        label="用户名"
                        placeholder="用户名"
                        :rules="rules.username"></v-text-field>
                    <v-text-field
                        v-model="password"
                        variant="outlined"
                        label="密码"
                        placeholder="密码"
                        :append-icon="showPassword ? 'mdi-eye' : 'mdi-eye-off'"
                        :rules="rules.password"
                        :type="showPassword ? 'text' : 'password'"
                        @click:append="showPassword = !showPassword"></v-text-field>
                </v-form>
            </v-container>
            <v-card-actions class="actions" style="margin: 0 8px">
                <v-btn
                    variant="flat"
                    block
                    color="success"
                    :loading="isLoading"
                    :disabled="isDisabled"
                    @click="onClickLogin">
                    {{ type }}
                </v-btn>
                <v-btn
                    variant="flat"
                    block
                    color="primary"
                    :loading="isLoading"
                    :disabled="isDisabled"
                    @click="isRegister = true">
                    还没有账号？去注册
                </v-btn>
                <v-btn
                    v-if="canLogout"
                    variant="flat"
                    block
                    color="error"
                    :loading="isLoading"
                    :disabled="isDisabled"
                    @click="onClickLogout">
                    退出登录（将无法与云端数据同步）
                </v-btn>
            </v-card-actions>
        </v-card>
    </div>
</template>

<style scoped>
.LoginView {
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
}

.LoginView__main {
    width: 100%;
    height: 85%;
}

.LoginView__main .v-form .v-text-field {
    margin-bottom: 16px;
}

.LoginView__main .actions {
    display: flex;
    flex-direction: column;
}
</style>

<script setup lang="ts">
import { ref } from 'vue';
import { VForm } from 'vuetify/components';

import { engine } from '~/engine/engine';
import { DummyUserProfile } from '~/engine/models';
import alertify from '~/extensions/alertify';
import router from '~/extensions/router';
import { api, ApiResponse } from '~/extensions/api';
import { LONG_STALL, NORMAL_STALL, stall } from '~/utils/stall';

const isRegister = defineModel<boolean>();

interface LoginViewProps {
    type: '登录' | '切换账号';
    canLogout: boolean;
}

const { type, canLogout } = defineProps<LoginViewProps>();

const showPassword = ref(false);

// Form rules
const rules = {
    username: [(v: string) => !!v || '请输入用户名'],
    password: [(v: string) => !!v || '请输入密码']
};

// Form values
const username = ref('');
const password = ref('');

// Form actions
const isLoading = ref(false);
const isDisabled = ref(false);

const form = ref<VForm>();

async function validateForm() {
    const { valid } = await stall(form.value.validate());
    return valid;
}

interface LoginData {
    id: number;
    username: string;
    badge: string;
    admin: boolean;
}

const onClickLogin = async () => {
    isLoading.value = true;

    if (!(await validateForm())) {
        isLoading.value = false;
        return;
    }

    await stall(
        api.post('/auth/login', {
            username: username.value,
            password: password.value
        }),
        NORMAL_STALL
    )
        .then((response: ApiResponse) => {
            if (response.status === 200) {
                onLoginSuccess(response.data as LoginData);
            } else {
                onLoginError(response);
            }
        })
        .finally(() => {
            isLoading.value = false;
        });
};

const onLoginSuccess = async (data: LoginData) => {
    isDisabled.value = true;

    alertify.success('欢迎回来！');

    // Update user data.
    const profile = await engine.getUserProfile();
    profile.name = data.username;
    profile.badge = data.badge;
    profile.onlineId = data.id;
    await stall(engine.updateUserProfile(profile), LONG_STALL);

    router.back();
};

const onLoginError = (response: ApiResponse) => {
    alertify.error(response.message);
};

const onClickLogout = async () => {
    isLoading.value = true;

    await stall(api.post('/auth/logout'), NORMAL_STALL)
        .then((response: ApiResponse) => {
            if (response.status === 200) {
                onLogoutSuccess();
            } else {
                onLogoutError(response);
            }
        })
        .finally(() => {
            isLoading.value = false;
        });
};

const onLogoutSuccess = async () => {
    isDisabled.value = true;

    alertify.success('下次再见！');

    // Update user data.
    const profile = await engine.getUserProfile();
    await stall(
        engine.updateUserProfile({ ...DummyUserProfile, id: profile.id }),
        LONG_STALL
    );

    router.back();
};

const onLogoutError = (response: ApiResponse) => {
    alertify.error(response.message);
};
</script>
