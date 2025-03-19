<template>
    <v-app>
        <RouterView />
    </v-app>
</template>

<style scoped></style>

<script setup lang="ts">
import { RouterView } from 'vue-router';
import { bus } from '~/extensions/emitter';
import { BusEventTypes, EntryUpdateEvent } from '~/engine/events';
import { engine } from '~/engine/engine';

// Ensure there is only one notify for each event.
bus.on(BusEventTypes.ENTRY_UPDATE, (event: EntryUpdateEvent) => {
    engine.notify(event);
});
</script>
