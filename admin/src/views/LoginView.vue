<template>
    <div class="LoginView">
        <v-card class="LoginView__main" title="Login" variant="flat">
            <v-container>
                <v-form ref="form">
                    <v-text-field
                        v-model="username"
                        variant="outlined"
                        label="Username"
                        placeholder="Username"
                        :rules="rules.username"></v-text-field>
                    <v-text-field
                        v-model="password"
                        variant="outlined"
                        label="Password"
                        placeholder="Password"
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
                    Login
                </v-btn>
            </v-card-actions>
        </v-card>
    </div>
</template>

<style scoped></style>

<script setup lang="ts">
// Form rules
import { ref } from 'vue';
import { VForm } from 'vuetify/components';
import { api, ApiResponse } from '~/extensions/api';
import router from '~/extensions/router';
import alertify from '~/extensions/alertify';
import { state } from '~/extensions/state';

const showPassword = ref(false);

const rules = {
    username: [(v: string) => !!v || 'Username is required'],
    password: [(v: string) => !!v || 'Password is required']
};

// Form values
const username = ref('');
const password = ref('');

// Form actions
const isLoading = ref(false);
const isDisabled = ref(false);

const form = ref<VForm>();

async function validateForm() {
    const { valid } = await form.value.validate();
    return valid;
}

// Login action
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
    api.post('/auth/login', {
        username: username.value,
        password: password.value
    })
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
    if (!data.admin) {
        alertify.error('Insolent one, you are not allowed to access this page');
        return;
    }

    isDisabled.value = true;
    alertify.success(`Welcome back, ${data.username}!`);
    state.setLoggedIn(true);
    await router.push({ path: '/' });
};

const onLoginError = (response: ApiResponse) => {
    alertify.error(response.message);
};
</script>
