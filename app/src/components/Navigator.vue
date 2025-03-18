<template>
    <div class="Navigator">
        <v-bottom-navigation
            class="Navigator_navigator"
            v-model="activeTab"
            mode="shift"
            grow
            mandatory
        >
            <v-btn>
                <v-icon>mdi-calendar-today</v-icon>
                <span>Today</span>
            </v-btn>
            <v-btn>
                <v-icon>mdi-home</v-icon>
                <span>Home</span>
            </v-btn>
        </v-bottom-navigation>
        <v-btn
            class="Navigator_plus"
            icon="mdi-plus"
            size="x-large"
            @click="onCreate"
        ></v-btn>
    </div>
</template>

<style>
.Navigator_navigator {
    position: fixed !important;
    width: 100%;
    bottom: 0;
    right: 0;
}

.Navigator_plus {
    position: fixed !important;
    left: 50%;
    bottom: 20px;
    transform: translateX(-50%);
    font-size: xx-large;
    z-index: 2000;
}
</style>

<script setup lang="ts">
interface NavigatorProps {
    onCreate?: () => void;
}

const activeTab = defineModel();
const { onCreate = () => {} } = defineProps<NavigatorProps>();
</script>
