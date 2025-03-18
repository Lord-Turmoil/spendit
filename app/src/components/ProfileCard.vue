<template>
    <div class="ProfileCard">
        <v-card>
            <v-container>
                <div class="ProfileCard__user">
                    <h2 class="text-h5">{{ userProfile.name }}</h2>
                    <!-- TODO: make it a chip badge -->
                    <span>{{ userProfile.title }}</span>
                </div>
                <v-divider class="ProfileCard__divider"></v-divider>
                <div class="ProfileCard__system">
                    <span>{{ displayVersion }}</span>
                    <span>{{ displayCopyright }}</span>
                </div>
            </v-container>
        </v-card>
    </div>
</template>

<style scoped>
.ProfileCard__user {
    display: flex;
    flex-direction: row;
    align-items: center;
    width: 100%;
}

.ProfileCard__user h2 {
    flex: 1;
}

.ProfileCard__divider {
    margin: 10px auto;
}

.ProfileCard__system span {
    display: block;
}
</style>

<script setup lang="ts">
import { computed, ref } from 'vue';
import { engine } from '~/engine/engine.js';

// User information.
const userProfile = ref(engine.getUserProfile());

// System information.
const systemProfile = ref(engine.getSystemProfile());

const displayVersion = computed(() => {
    return `${systemProfile.value.product} ${systemProfile.value.version} - ${systemProfile.value.code}`;
});

const displayCopyright = computed(() => {
    const currentYear = new Date().getFullYear();
    if (currentYear === 2025) {
        return `Copyright © 2025 ${systemProfile.value.author}`;
    }
    return `Copyright © 2025 - ${currentYear} ${systemProfile.value.author}`;
});
</script>
