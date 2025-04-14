<template>
    <v-list-item class="InvitationItem">
        <div class="item">
            <div class="text">
                {{ item.code }}
            </div>

            <div v-if="item.admin">(Admin)</div>

            <div class="date" v-if="type === 'available'">
                {{ item.createdAt }}
            </div>
            <div class="date" v-else-if="type === 'invoked'">
                {{ item.invokedAt }}
            </div>
            <div class="date" v-else>
                {{ item.acceptedAt }}
            </div>

            <v-btn
                v-if="type === 'available'"
                variant="text"
                icon="mdi-clipboard-multiple-outline"
                @click="() => copyCode(item.code)"></v-btn>
            <v-btn
                v-if="type === 'available'"
                variant="text"
                icon="mdi-cancel"
                color="red"
                :loading="isLoading"
                @click="() => cancelCode()"></v-btn>
        </div>
    </v-list-item>
</template>

<style scoped>
.InvitationItem .item {
    width: 100%;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
}

.InvitationItem .item .text {
    flex: 1;
}

.InvitationItem .item .date {
    color: gray;
    margin-right: 5px;
}
</style>

<script setup lang="ts">
import { Invitation } from '~/extensions/models';
import alertify from '~/extensions/alertify';
import { bus } from '~/extensions/emitter';
import { api } from '~/extensions/api';
import { ref } from 'vue';
import { stall } from '~/extensions/stall';

export type InvitationType = 'available' | 'invoked' | 'accepted';
const { item, type } = defineProps<{ item: Invitation; type: InvitationType }>();

const copyCode = async (code: string) => {
    await navigator.clipboard.writeText(code);
    alertify.success('Invitation code copied to clipboard');
};

const isLoading = ref(false);

const cancelCode = async () => {
    isLoading.value = true;
    const response = await stall(api.post('/invitation/invoke', { id: item.id }));
    isLoading.value = false;
    if (response.status !== 200) {
        alertify.error(response.message);
        return;
    }
    alertify.success('Invitation code invoked');

    bus.emit('cancel-invitation', response.data as Invitation);
};
</script>
