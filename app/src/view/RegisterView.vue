<template>
    <div class="RegisterView">
        <v-card class="RegisterView__main" title="注册" variant="flat">
            <v-container>
                <v-form ref="form">
                    <v-text-field
                        v-model="code"
                        variant="outlined"
                        label="邀请码"
                        placeholder="8 位字母和数字"
                        :rules="rules.code"></v-text-field>
                    <v-text-field
                        v-model="username"
                        variant="outlined"
                        label="用户名"
                        placeholder="4 - 16 位字母、数字、_ 和 -"
                        counter
                        :rules="rules.username"></v-text-field>
                    <v-text-field
                        v-model="password"
                        variant="outlined"
                        label="密码"
                        placeholder="6 - 16 位"
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
                    @click="onClickRegister">
                    注册
                </v-btn>
                <v-btn
                    variant="flat"
                    block
                    color="primary"
                    :loading="isLoading"
                    :disabled="isDisabled"
                    @click="isRegister = false">
                    已经有账号了？去登录
                </v-btn>
            </v-card-actions>
        </v-card>
    </div>
</template>

<style scoped>
.RegisterView {
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
}

.RegisterView__main {
    width: 100%;
    height: 85%;
}

.RegisterView__main .v-form .v-text-field {
    margin-bottom: 16px;
}

.RegisterView__main .actions {
    display: flex;
    flex-direction: column;
}
</style>

<script setup lang="ts">
import { ref } from 'vue';
import { VForm } from 'vuetify/components';
import { engine } from '~/engine/engine';
import alertify from '~/extensions/alertify';
import router from '~/extensions/router';
import { api, ApiResponse } from '~/extensions/api';
import { delay, LONG_STALL, NORMAL_STALL, stall } from '~/utils/stall';

const isRegister = defineModel<boolean>();

const showPassword = ref(false);

// Form rules
const rules = {
    code: [
        (v: string) => !!v || '请输入邀请码',
        (v: string) => {
            // 16 alphanumeric characters
            const regex = /^[a-zA-Z0-9]{8}$/;
            return regex.test(v) || '邀请码格式不正确';
        }
    ],
    username: [
        (v: string) => !!v || '请输入用户名',
        (v: string) => {
            // 4-16 characters, letters, numbers, underscores, or hyphens
            const regex = /^[a-zA-Z0-9_-]{4,16}$/;
            return (
                regex.test(v) || '用户名只能包含字母、数字、_ 和 -，长度为 4 - 16 个字符'
            );
        }
    ],
    password: [
        (v: string) => !!v || '请输入密码',
        (v: string) => {
            // 8-16 characters, letters, numbers, or special characters
            const regex = /^[a-zA-Z0-9!@#$%^&*()_+]{8,16}$/;
            return (
                regex.test(v) || '密码只能包含字母、数字和特殊字符，长度为 6 - 16 个字符'
            );
        }
    ]
};

// Form values
const code = ref('');
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

interface RegisterData {
    id: number;
    username: string;
    badge: string;
    admin: boolean;
}

const onClickRegister = async () => {
    isLoading.value = true;

    if (!(await validateForm())) {
        isLoading.value = false;
        return;
    }

    await api
        .post('/auth/register', {
            code: code.value,
            username: username.value,
            password: password.value
        })
        .then((response: ApiResponse) => {
            if (response.status === 200) {
                onRegisterSuccess(response.data as RegisterData);
            } else {
                onRegisterError(response);
            }
        })
        .finally(() => {
            isLoading.value = false;
        });
};

const onRegisterSuccess = async (data: RegisterData) => {
    isDisabled.value = true;

    alertify.success('欢迎来到 Spendit！');
    await tryLogin(data);
};

const onRegisterError = (response: ApiResponse) => {
    alertify.error(response.message);
};

const tryLogin = async (data: RegisterData) => {
    return await api
        .post('/auth/login', {
            username: username.value,
            password: password.value
        })
        .then((response: ApiResponse) => {
            if (response.status === 200) {
                onLoginSuccess(response.data as RegisterData);
            } else {
                onLoginError(response);
            }
        });
};

const onLoginSuccess = async (data: RegisterData) => {
    // Update user data.
    const profile = await engine.getUserProfile();
    profile.name = data.username;
    profile.badge = data.badge;
    profile.onlineId = data.id;
    await stall(engine.updateUserProfile(profile), LONG_STALL);

    router.back();
};

const onLoginError = (response: ApiResponse) => {
    alertify.error('自动登录失败，请手动登录');
    isRegister.value = false;
};
</script>
