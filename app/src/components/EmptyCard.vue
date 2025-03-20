<template>
    <div class="EmptyCard">
        <div class="EmptyCard__body">
            <v-icon class="text-h2" icon="mdi-currency-usd-off" size="x-large"></v-icon>
            <h2 class="text-h5">还没有消费记录</h2>
        </div>
        <div v-if="showHint" class="EmptyCard__hint">
            <h3 class="text-h6">
                点击下方
                <v-icon icon="mdi-plus"></v-icon>
                新建
            </h3>
            <v-icon class="text-h3" icon="mdi-arrow-down" size="x-large"></v-icon>
        </div>
    </div>
</template>

<style scoped>
.EmptyCard {
    width: 100%;
    height: 100%;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
}

.EmptyCard__body {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
}

.EmptyCard__body h2 {
    margin: 16px auto;
}

.EmptyCard__hint {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
}

.EmptyCard__hint h3 {
    margin: 8px auto;
    display: flex;
    align-items: center;
}
</style>

<script setup lang="ts">
interface EmptyCardProps {
    showHint?: boolean;
}

const { showHint = false } = defineProps<EmptyCardProps>();
</script>
