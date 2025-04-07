<template>
    <div class="EntryCardList" :key="key">
        <div v-if="entries.length > 0" class="EntryCardList__content scrollable">
            <div class="EntryCardList__timestamp text-body-1" v-if="showDate">
                {{ formatTimestampToSlash(date) }}
            </div>
            <EntryCard
                v-for="(item, i) in entries"
                :key="i"
                :entry="item"
                @click="onEditStart(item)"></EntryCard>
            <v-dialog class="EntryCardList__dialog" v-model="dialogOpen" persistent>
                <EditView
                    title="编辑消费项目"
                    :entry="editEntry"
                    :on-close="onEditEnd"></EditView>
            </v-dialog>
        </div>
        <div v-else-if="showEmpty" class="EntryCardList__empty">
            <EmptyCard class="centered" show-hint></EmptyCard>
        </div>
    </div>
</template>

<style scoped>
.EntryCardList__content {
    display: flex;
    flex-direction: column;
    align-items: center;
}

.EntryCardList__timestamp {
    margin: 8px 0;
}

.EntryCardList__empty {
    width: 100%;
    height: 100%;
}

.centered {
    width: 100%;
    height: 100%;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
}
</style>

<script setup lang="ts">
import EmptyCard from '~/components/EmptyCard.vue';
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
import alertify from '~/extensions/alertify';
import { formatTimestampToSlash } from '~/utils/format';

interface EntryCardListProps {
    date: string;
    showDate?: boolean;
    showEmpty?: boolean;
}

const { date, showDate = true, showEmpty = false } = defineProps<EntryCardListProps>();

const entries = ref<Entry[]>([]);
const key = ref<number>(0);

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
    engine
        .getTable(date)
        .then((table) => {
            entries.value = [...table.entries];
            key.value++;
        })
        .catch((error) => {
            alertify.error(error.message);
        });
});

onUnmounted(() => {
    bus.off(BusEventTypes.ENTRY_UPDATE, onEntryUpdateEvent);
});
</script>
