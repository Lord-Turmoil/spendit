<template>
    <div class="MenuBar">
        <v-btn
            id="menu-selector-activator"
            class="MenuBar__button"
            variant="outlined"
            color="primary">
            {{ selected.name }}
        </v-btn>

        <span>{{ selected.description }}</span>

        <v-menu activator="#menu-selector-activator">
            <v-list>
                <v-list-item
                    v-for="(item, index) in items"
                    :key="index"
                    :value="index"
                    @click="() => onSelectItem(index)">
                    <v-list-item-title>{{ item.name }}</v-list-item-title>
                </v-list-item>
            </v-list>
        </v-menu>
    </div>
</template>

<style scoped>
.MenuBar {
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: left;
}

.MenuBar__button {
    width: 150px;
    margin-right: 10px;
}
</style>

<script setup lang="ts">
export interface MenuItem {
    name: string;
    description: string;
}

interface MenuBarProps {
    items: MenuItem[];
}

const { items } = defineProps<MenuBarProps>();
const selected = defineModel<MenuItem>();

const onSelectItem = (index: number) => {
    selected.value = items[index];
};
</script>
