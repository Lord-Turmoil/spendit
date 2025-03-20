<template>
    <v-card class="EntryCard" variant="flat" link>
        <v-card-title class="EntryCard__title split-wrapper">
            <h3 class="text-h5 split-primary">{{ entry.title }}</h3>
            <h4 class="text-h4 text-red-darken-1">
                <v-icon v-if="entry.money > 0" class="text-h5" icon="mdi-minus"></v-icon>
                <span>{{ formatMoney(entry.money) }}</span>
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
                        <PeopleChip :name="name"></PeopleChip>
                    </span>
                </span>
            </span>
            <span class="EntryCard__body_tags tags">
                <span class="tag" v-for="(tag, i) in entry.tags" :key="i">
                    <TagChip :tag="tag"></TagChip>
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
import PeopleChip from '~/components/PeopleChip.vue';
import TagChip from '~/components/TagChip.vue';

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
