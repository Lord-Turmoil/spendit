<template>
    <v-card class="EntryCard" link>
        <v-card-title class="EntryCard__title">
            <h3 class="text-h5">{{ entry.title }}</h3>
            <h4 class="text-h4 text-red-lighten-1">
                <v-icon class="text-h5" icon="mdi-minus"></v-icon>
                <span>{{ displayMoney }}</span>
            </h4>
        </v-card-title>
        <v-card-subtitle>{{ entry.note }}</v-card-subtitle>
        <v-card-text class="EntryCard__body">
            <span class="EntryCard__meta">
                <span class="EntryCard__body_category text-h6 text-green-darken-1">
                    {{ displayCategory }}
                </span>
                <span class="EntryCard__body_people tags">
                    <span class="tag" v-for="(name, i) in entry.people" :key="i">
                        <v-chip variant="outlined" color="pink" size="small">
                            <v-icon icon="mdi-at"></v-icon>
                            {{ name }}
                        </v-chip>
                    </span>
                </span>
            </span>
            <span class="EntryCard__body_tags tags">
                <span class="tag" v-for="(tag, i) in entry.tags" :key="i">
                    <v-chip variant="outlined" color="blue-darken-2" size="small">
                        <v-icon icon="mdi-pound"></v-icon>
                        {{ tag }}
                    </v-chip>
                </span>
            </span>
        </v-card-text>
    </v-card>
</template>

<style scoped>
.EntryCard {
    width: 100%;
    margin: 8px auto;
}

.EntryCard__title {
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.EntryCard__title h3 {
    flex: 1;
}

.EntryCard__title h4 {
    display: flex;
    flex-direction: row;
    align-items: center;
}

.EntryCard__body {
    display: flex;
    justify-content: space-between;
}

.tags {
    max-height: 32px;
    display: flex;
    flex-direction: row;
    justify-content: right;
    align-items: center;
    flex-wrap: wrap;
    overflow: hidden;
}

.tags .tag {
    height: 32px;
    margin: 0 4px;
    display: flex;
    flex-direction: column;
    justify-content: center;
}

.EntryCard__meta {
    flex: 1;
    max-width: 65%;
    display: flex;
    flex-direction: row;
    justify-content: left;
    align-items: center;
}

.EntryCard__body_category {
    margin-right: 8px;
}

/*********** people style ***********/

.EntryCard__body_people {
    flex: 1;
    max-width: 60%;
    justify-content: left;
}

/*********** tags style ***********/

.EntryCard__body_tags {
    max-width: 40%;
}
</style>

<script setup lang="ts">
import { computed } from 'vue';
import { Entry } from '~/engine/models.js';

interface EntryCardProps {
    entry: Entry;
}

const { entry } = defineProps<EntryCardProps>();

const displayMoney = computed(() => {
    return (entry.money * 0.01).toFixed(2);
});

const displayCategory = computed(() => {
    if (entry.categories.length === 0) {
        return 'N/A';
    }
    return entry.categories.join(' / ');
});
</script>
