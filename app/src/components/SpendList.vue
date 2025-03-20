<template>
    <div class="SpendList">
        <div v-for="(category, i) in spends" :key="i" class="SpendList__category">
            <div class="title split-wrapper">
                <h4 class="text-h6 split-primary">{{ category[0] }}</h4>
                <h5 class="text-h6 text-red-lighten-1">
                    <v-icon
                        v-if="category[1] > 0"
                        class="text-h6"
                        icon="mdi-minus"></v-icon>
                    <span>{{ formatMoney(category[1]) }}</span>
                </h5>
            </div>
            <div class="list">
                <div
                    v-for="(subCategory, j) in category[2]"
                    :key="j"
                    class="SpendList__sub">
                    <div class="split-wrapper">
                        <h5 class="text-body-1 split-primary text-grey-darken-4">
                            {{ subCategory[0] }}
                        </h5>
                        <h6 class="text-body-1 text-red-lighten-2">
                            <v-icon
                                v-if="subCategory[1] > 0"
                                class="text-body-1"
                                icon="mdi-minus"></v-icon>
                            <span>{{ formatMoney(subCategory[1]) }}</span>
                        </h6>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
.SpendList__category {
    margin: 8px auto;
}

.SpendList__sub {
    margin: 4px auto 4px 16px;
}
</style>

<script setup lang="ts">
import { CategorySpend } from '~/engine/models';
import { formatMoney } from '~/utils/format';

const { spends } = defineProps<{ spends: CategorySpend[] }>();
</script>
