<template>
    <v-card
        class="ProfileCard"
        variant="flat"
        color="purple-darken-1"
        link
        href="https://spendit.tonys-studio.top/release/"
        target="_blank">
        <v-container>
            <div class="ProfileCard__user">
                <h2 class="text-h5">{{ userProfile.name }}</h2>
                <!-- TODO: make it a chip badge -->
                <span>{{ userProfile.badge }}</span>
            </div>
            <v-divider class="ProfileCard__divider"></v-divider>
            <div class="ProfileCard__system text-caption">
                <span>{{ displayVersion }}</span>
                <span>{{ displayCopyright }}</span>
                <v-icon class="icon" icon="mdi-open-in-new"></v-icon>
            </div>
        </v-container>
    </v-card>
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

.ProfileCard__system {
    position: relative;
}

.ProfileCard__system span {
    display: block;
}

.ProfileCard__system .icon {
    position: absolute;
    top: 50%;
    right: 8px;
    transform: translateY(-50%);
}
</style>

<script setup lang="ts">
import { computed, onMounted, ref } from 'vue';

import { engine } from '~/engine/engine';
import { DummyUserProfile } from '~/engine/models';

// User information.
const userProfile = ref(DummyUserProfile);

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

onMounted(() => {
    // Fetch user profile from the engine.
    engine.getUserProfile().then((profile) => {
        userProfile.value = profile;
    });
});
</script>
