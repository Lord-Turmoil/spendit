<template>
    <div class="AboutLayout split-wrapper">
        <div class="AboutLayout__logo">
            <img src="/favicon.png" alt="logo" class="icon" @click="triggerEasterEgg" />
            <h2 class="title text-h6 rainbow rainbow_text_animated">{{ displayName }}</h2>
        </div>
        <v-divider class="divider" />
        <div class="AboutLayout__description section">
            <div class="paragraph">
                <p>{{ displayDescription }}</p>
            </div>
        </div>
        <v-divider class="divider" />
        <div class="AboutLayout__version">
            <VersionCardWeb v-if="isWeb" />
            <VersionCard v-else />
        </div>
        <v-divider class="divider" />
        <div class="AboutLayout__disclaimer">
            <DisclaimerCard />
        </div>
        <div class="AboutLayout__copyright split-primary">
            <v-divider class="divider" />
            <p class="text-caption font-italic text-center">
                <CopyrightText />
            </p>
        </div>
    </div>
</template>

<style scoped>
.AboutLayout {
    flex-direction: column;
}

.AboutLayout .divider {
    margin: 4px auto;
}

.AboutLayout .section .paragraph {
    padding: 8px;
    text-align: justify;
}

.AboutLayout__logo {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
}

.AboutLayout__logo .icon {
    width: 64px;
}

.AboutLayout__version {
    width: 100%;
}

.AboutLayout__copyright {
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: end;
    width: 100%;
}

/* Rainbow Effect */
#rainbow {
    background-color: rgb(0, 0, 0);
    /* Fallback color */
    background-color: rgba(0, 0, 0, 0.2);
    /* Black w/opacity/see-through */
    border: 3px solid;
}

.rainbow {
    text-align: center;
    text-decoration: underline;
    font-size: 32px;
    font-family: monospace;
    letter-spacing: 5px;
}

.rainbow_text_animated {
    background: linear-gradient(
        to right,
        #6666ff,
        #0099ff,
        #00ff00,
        #ff3399,
        #6666ff,
        #0099ff,
        #00ff00,
        #ff3399
    );
    -webkit-background-clip: text;
    background-clip: text;
    color: transparent;
    animation: rainbow_animation 3s linear infinite;
    background-size: 400% 100%;
}

@keyframes rainbow_animation {
    0% {
        background-position: 75% 0;
    }

    100% {
        background-position: 0 0;
    }
}
</style>

<script setup lang="ts">
import { computed } from 'vue';
import CopyrightText from '~/components/CopyrightText.vue';
import DisclaimerCard from '~/components/version/DisclaimerCard.vue';
import VersionCard from '~/components/version/VersionCard.vue';
import VersionCardWeb from '~/components/version/VersionCardWeb.vue';
import { engine } from '~/engine/engine';
import alertify from '~/extensions/alertify';
import { hurray } from '~/extensions/confetti';
import { getNative } from '~/utils/native';

const displayName = computed(() => {
    return `Spendit - ${engine.getSystemProfile().code}`;
});

const displayDescription = computed(() => {
    return 'Spendit 是一款轻量级的记账软件，支持个性化的消费管理，并为注册用户提供可选的云端数据同步。';
});

const isWeb = getNative().isWeb();

const triggerEasterEgg = () => {
    hurray();

    const old = alertify.get('notifier', 'delay');
    alertify.set('notifier', 'delay', 100);

    let delay = 0;
    let dismissDelay = 1500;
    let lastMessage = undefined;

    delay += 700;
    setTimeout(() => {
        lastMessage = alertify.success('You have come here');
    }, delay);

    delay += 3300;
    setTimeout(() => {
        const toDismiss = lastMessage;
        setTimeout(() => {
            toDismiss.dismiss();
        }, dismissDelay);
        lastMessage = alertify.warning('In pursuit of your deepest urge');
    }, delay);

    delay += 3500;
    setTimeout(() => {
        const toDismiss = lastMessage;
        setTimeout(() => {
            toDismiss.dismiss();
        }, dismissDelay);
        lastMessage = alertify.error('In pursuit of that wish');
    }, delay);

    delay += 2100;
    setTimeout(() => {
        const toDismiss = lastMessage;
        setTimeout(() => {
            toDismiss.dismiss();
        }, dismissDelay);
        lastMessage = alertify.success('which till now has been silent...');
    }, delay);

    delay += 4450;
    setTimeout(() => {
        const toDismiss = lastMessage;
        setTimeout(() => {
            toDismiss.dismiss();
        }, dismissDelay);
        lastMessage = alertify.warning('silent...');
    }, delay);

    delay += 3500;
    setTimeout(() => {
        alertify.dismissAll();
        alertify.set('notifier', 'delay', old);
    }, delay);
};
</script>
