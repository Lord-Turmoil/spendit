<template>
    <div class="InvitationView">
        <ActionHeader
            title="Invitation Code"
            action-text="Generate Invitation Code"
            :action="generateCode"></ActionHeader>
        <v-card>
            <v-tabs v-model="tab">
                <v-tab value="available">Available</v-tab>
                <v-tab value="invoked">Invoked</v-tab>
                <v-tab value="accepted">Accepted</v-tab>
            </v-tabs>
            <v-tabs-window v-model="tab">
                <v-tabs-window-item value="available">
                    <InvitationList
                        v-model="availableList"
                        type="available"></InvitationList>
                </v-tabs-window-item>
                <v-tabs-window-item value="invoked">
                    <InvitationList v-model="invokedList" type="invoked"></InvitationList>
                </v-tabs-window-item>
                <v-tabs-window-item value="accepted">
                    <InvitationList
                        v-model="acceptedList"
                        type="accepted"></InvitationList>
                </v-tabs-window-item>
            </v-tabs-window>
        </v-card>
    </div>
</template>

<style scoped></style>

<script setup lang="ts">
import { onMounted, onUnmounted, ref } from 'vue';
import { api } from '~/extensions/api';
import alertify from '~/extensions/alertify';
import InvitationList from '~/components/InvitationList.vue';
import { Invitation } from '~/extensions/models';
import { bus } from '~/extensions/emitter';
import { stall } from '~/extensions/stall';
import ActionHeader from '~/components/ActionHeader.vue';

const tab = ref();

const availableList = ref<Invitation[]>([]);
const invokedList = ref<Invitation[]>([]);
const acceptedList = ref<Invitation[]>([]);

async function fetchData() {
    let response = await api.get('/invitation/list', { type: 1 });
    if (response.status !== 200) {
        alertify.error(response.message);
        return;
    }
    availableList.value = response.data.items;

    response = await api.get('/invitation/list', { type: 2 });
    if (response.status !== 200) {
        alertify.error(response.message);
        return;
    }
    invokedList.value = response.data.items;

    response = await api.get('/invitation/list', { type: 3 });
    if (response.status !== 200) {
        alertify.error(response.message);
        return;
    }
    acceptedList.value = response.data.items;
}

async function generateCode() {
    const response = await stall(api.post('/invitation/create', { admin: false }));

    if (response.status !== 200) {
        alertify.error(response.message);
        return;
    }
    alertify.success('Invitation code created');

    const invitation = response.data as Invitation;
    bus.emit('create-invitation', invitation);
}

function onInvitationCreated(invitation: Invitation) {
    availableList.value.push(invitation);
}

function onInvitationInvoked(invitation: Invitation) {
    availableList.value = availableList.value.filter((inv: Invitation) => {
        return inv.id !== invitation.id;
    });
    invokedList.value.push(invitation);
}

onMounted(() => {
    fetchData();
    bus.on('create-invitation', onInvitationCreated);
    bus.on('cancel-invitation', onInvitationInvoked);
});

onUnmounted(() => {
    bus.off('create-invitation', onInvitationCreated);
    bus.off('cancel-invitation', onInvitationInvoked);
});
</script>
