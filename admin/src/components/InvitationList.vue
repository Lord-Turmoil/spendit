<template>
    <div class="InvitationList">
        <v-list>
            <InvitationItem
                v-for="(item, i) in items"
                :type="type"
                :key="i"
                :item="item"></InvitationItem>
        </v-list>
    </div>
</template>

<style scoped></style>

<script setup lang="ts">
import { Invitation } from '~/extensions/models';
import InvitationItem, { InvitationType } from '~/components/InvitationItem.vue';

const { type } = defineProps<{ type: InvitationType }>();
const items = defineModel<Invitation[]>();
</script>
