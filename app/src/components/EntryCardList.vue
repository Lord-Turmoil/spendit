<template>
    <div v-if="entries.length > 0" class="EntryCardList scrollable">
        <div class="EntryCardList__timestamp text-h6" v-if="showDate">
            {{ formatTimestampToSlash(date) }}
        </div>
        <EntryCard
            v-for="(item, i) in entries"
            :key="i"
            :entry="item"
            @click="onEditStart(item)"></EntryCard>
        <v-dialog class="EntryCardList__dialog" v-model="dialogOpen" persistent>
            <EditView
                title="Edit Entry"
                :entry="editEntry"
                :on-close="onEditEnd"></EditView>
        </v-dialog>
    </div>
</template>

<style scoped>
.EntryCardList {
    display: flex;
    flex-direction: column;
    align-items: center;
}

.EntryCardList__timestamp {
    margin: 8px 0;
}
</style>

<script setup lang="ts">
import EntryCard from '~/components/EntryCard.vue';
import EditView from '~/view/EditView.vue';

import { onMounted, onUnmounted, ref } from 'vue';

import { Entry } from '~/engine/models';
import { engine } from '~/engine/engine';
import {
    BusEventTypes,
    EntryCallback,
    EntryUpdateEvent,
    EntryUpdateTypes
} from '~/engine/events';
import { bus } from '~/extensions/emitter';
import { formatTimestampToSlash } from '~/utils/format';

interface EntryCardListProps {
    date: string;
    showDate?: boolean;
}

const { date, showDate = true } = defineProps<EntryCardListProps>();

const entries = ref<Entry[]>([...engine.getDatabase().getTable(date).entries]);

// dialog control
const dialogOpen = ref(false);
const openDialog = () => {
    dialogOpen.value = true;
};

const closeDialog = () => {
    dialogOpen.value = false;
};

// event callback
const onEntryUpdateEvent = (event: EntryUpdateEvent) => {
    if (event.type === EntryUpdateTypes.CREATE) {
        entries.value.push(event.entry);
    } else if (event.type === EntryUpdateTypes.UPDATE) {
        entries.value = entries.value.map((item) => {
            if (item.timestamp === event.entry.timestamp) {
                return event.entry;
            }
            return item;
        });
    } else {
        // delete
        entries.value = entries.value.filter(
            (item) => item.timestamp !== event.entry.timestamp
        );
    }
};

// edit control
const editEntry = ref(engine.createEntry());

const onEditStart: EntryCallback = (entry: Entry) => {
    editEntry.value = entry;
    openDialog();
};

const onEditEnd = () => {
    closeDialog();
};

// event listener
onMounted(() => {
    bus.on(BusEventTypes.ENTRY_UPDATE, onEntryUpdateEvent);
});

onUnmounted(() => {
    bus.off(BusEventTypes.ENTRY_UPDATE, onEntryUpdateEvent);
});
</script>
