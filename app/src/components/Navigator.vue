<template>
    <v-bottom-navigation class="Navigator" v-model="activeTab" mode="shift" grow mandatory>
        <v-btn>
            <v-icon>mdi-calendar-today</v-icon>
            <span>Today</span>
        </v-btn>
        <v-btn>
            <v-icon>mdi-home</v-icon>
            <span>Home</span>
        </v-btn>
    </v-bottom-navigation>
</template>

<style>
.Navigator {
    position: fixed !important;
    width: 100%;
    bottom: 0;
    right: 0;
}
</style>

<script setup lang="ts">

const activeTab = defineModel();

</script>
