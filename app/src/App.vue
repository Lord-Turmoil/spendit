<template>
    <v-app>
        <RouterView />
    </v-app>
</template>

<style scoped></style>

<script setup lang="ts">
import { RouterView } from 'vue-router';

import { BusEventTypes, EntryUpdateEvent } from '~/engine/events';
import { engine } from '~/engine/engine';
import { bus } from '~/extensions/emitter';

// Ensure there is only one notify for each event.
bus.on(BusEventTypes.ENTRY_UPDATE, (event: EntryUpdateEvent) => {
    if (!event.handled) {
        event.handled = true;
        engine.notify(event);
    }
});
</script>
