<template>
    <v-card class="EntryCard" variant="flat" link>
        <v-card-title class="EntryCard__title split-wrapper">
            <h3 class="text-h6 split-primary">{{ entry.title }}</h3>
            <h4 class="text-h5 text-red-darken-1">
                <v-icon v-if="entry.money > 0" class="text-h6" icon="mdi-minus"></v-icon>
                <span>{{ formatMoney(entry.money) }}</span>
            </h4>
        </v-card-title>
        <v-card-subtitle>{{ entry.note }}</v-card-subtitle>
        <v-card-text class="EntryCard__body">
            <div class="EntryCard__body_category text-body-1 text-green-darken-1">
                {{ displayCategory }}
            </div>
            <div class="EntryCard__body_tags tags">
                <span class="tag" v-for="(name, i) in entry.people" :key="i">
                    <PeopleChip :name="name"></PeopleChip>
                </span>
                <span class="tag" v-for="(tag, i) in entry.tags" :key="i">
                    <TagChip :tag="tag"></TagChip>
                </span>
            </div>
        </v-card-text>
    </v-card>
</template>

<style>
.EntryCard {
    width: 100%;
    margin: 8px auto;
}

.EntryCard .v-card-text {
    padding-top: 8px;
}

.EntryCard__title h4 {
    display: flex;
    flex-direction: row;
    align-items: center;
}

.EntryCard__body_category {
    margin-right: 8px;
}

.EntryCard__body_tags {
    max-height: 32px;
    margin-top: 4px;
    display: flex;
    flex-direction: row;
    justify-content: left;
    align-items: center;
    flex-wrap: wrap;
    overflow: hidden;
}

.EntryCard__body_tags .tag {
    height: 32px;
    margin-right: 4px;
    display: flex;
    flex-direction: column;
    justify-content: center;
}
</style>

<script setup lang="ts">
import PeopleChip from '~/components/PeopleChip.vue';
import TagChip from '~/components/TagChip.vue';

import { computed } from 'vue';

import { Entry } from '~/engine/models';
import { formatMoney } from '~/utils/format';

interface EntryCardProps {
    entry: Entry;
}

const { entry } = defineProps<EntryCardProps>();

const displayCategory = computed(() => {
    if (entry.categories.length === 0) {
        return '未分类';
    }
    return entry.categories.join(' / ');
});
</script>
