<template>
    <div class="EditView scrollable">
        <v-card class="EditView__card" :title="title">
            <v-container>
                <v-form ref="form">
                    <div class="EditView__form">
                        <!-- title -->
                        <v-text-field
                            :label="`消费项目 ${formatTimestampToSlash(data.date)}`"
                            variant="outlined"
                            :placeholder="`消费项目 ${formatTimestampToSlash(data.date)}`"
                            :rules="rules.title"
                            v-model="data.title"></v-text-field>
                        <!-- date and money -->
                        <div class="date-and-money">
                            <span class="money">
                                <v-icon
                                    class="icon text-h6 text-red-lighten-1"
                                    icon="mdi-minus"></v-icon>
                                <span class="input text-red-lighten-1">
                                    <v-text-field
                                        label="消费金额"
                                        variant="outlined"
                                        placeholder="Money"
                                        :rules="rules.money"
                                        v-model="moneyData"></v-text-field>
                                </span>
                            </span>
                        </div>
                        <!-- category -->
                        <v-combobox
                            v-model="categoryChips"
                            :items="categoryList"
                            variant="outlined"
                            label="分类"
                            placeholder="分类"
                            chips
                            clearable
                            closable-chips
                            multiple>
                            <template v-slot:chip="{ props, item }">
                                <v-chip
                                    prepend-icon="mdi-slash-forward"
                                    v-bind="props"
                                    size="x-small"
                                    color="green-darken-1"
                                    variant="outlined">
                                    {{ item.raw }}
                                </v-chip>
                            </template>
                        </v-combobox>
                        <!-- people list -->
                        <v-combobox
                            v-model="peopleChips"
                            :items="peopleList"
                            variant="outlined"
                            label="谁的消费"
                            placeholder="谁的消费"
                            chips
                            clearable
                            closable-chips
                            multiple>
                            <template v-slot:chip="{ props, item }">
                                <PeopleChip :name="item.raw"></PeopleChip>
                            </template>
                        </v-combobox>
                        <!-- tag list -->
                        <v-combobox
                            v-model="tagChips"
                            :items="tagList"
                            variant="outlined"
                            label="消费标签"
                            placeholder="消费标签"
                            chips
                            clearable
                            closable-chips
                            multiple>
                            <template v-slot:chip="{ props, item }">
                                <TagChip :tag="item.raw"></TagChip>
                            </template>
                        </v-combobox>
                        <!-- note -->
                        <v-textarea
                            class="note"
                            v-model="data.note"
                            variant="outlined"
                            label="消费备注"
                            placeholder="消费备注"
                            rows="2"
                            auto-grow
                            clearable></v-textarea>
                    </div>
                </v-form>
            </v-container>
            <v-card-actions class="actions">
                <v-btn
                    v-if="!create"
                    color="error"
                    size="large"
                    :loading="isLoading"
                    @click="onClickConfirm(true)">
                    删除
                </v-btn>
                <v-btn
                    color="primary"
                    size="large"
                    :loading="isLoading"
                    @click="onClickCancel">
                    取消
                </v-btn>
                <v-btn
                    color="success"
                    size="large"
                    :loading="isLoading"
                    @click="onClickConfirm(false)">
                    确认
                </v-btn>
            </v-card-actions>
        </v-card>
        <v-dialog v-model="confirmDialog">
            <v-card class="EditView__confirm" prepend-icon="mdi-alert" title="确认删除">
                <v-card-text>删除后将无法恢复，确定要删除这条记录吗？</v-card-text>
                <v-card-actions>
                    <v-btn color="error" size="large" @click="onConfirmDelete(true)">
                        删除
                    </v-btn>
                    <v-btn color="primary" size="large" @click="onConfirmDelete(false)">
                        取消
                    </v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>
    </div>
</template>

<style>
.EditView__card {
    padding-top: 8px;
}

.EditView__card .actions {
    justify-content: right;
}

.EditView__form .date-and-money {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
}

.EditView__form .date-and-money .date {
    transform: translateY(-30%);
}

.EditView__form .date-and-money .money {
    flex: 1;
    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;
}

.EditView__form .date-and-money .money .input {
    flex: 1;
    margin-left: 8px;
}

.EditView__form .date-and-money .money .icon {
    transform: translateY(-40%);
}

.EditView__form .date-and-money .money .input input {
    text-align: right;
}

.EditView__confirm {
    width: 90%;
    margin: 0 auto;
}
</style>

<script setup lang="ts">
import PeopleChip from '~/components/PeopleChip.vue';
import TagChip from '~/components/TagChip.vue';

import { nextTick, ref, watch } from 'vue';
import { VForm } from 'vuetify/components';

import { Entry } from '~/engine/models';
import { BusEventTypes, EntryUpdateEvent, EntryUpdateTypes } from '~/engine/events';
import { engine } from '~/engine/engine';
import { bus } from '~/extensions/emitter';
import { formatMoney, formatTimestampToSlash, parseMoney } from '~/utils/format';
import { stall } from '~/utils/stall';

// ============================================================================
// Component properties.
// ============================================================================

interface EditViewProps {
    title: string;
    entry?: Entry;
    onClose?: () => void;
}

const { title, entry, onClose } = defineProps<EditViewProps>();
const create = entry === undefined;
// Here, make a copy or the original entry will be modified unintentionally.
const data = ref<Entry>(entry ? { ...entry } : engine.createEntry());

// ============================================================================
// Form validation.
// ============================================================================

const form = ref<VForm>();
const rules = {
    title: [
        (v: string) => !!v || '消费项目不能为空',
        (v: string) => (v && v.length > 0) || '消费项目不能为空'
    ],
    money: [
        (v: string) => !!v || '消费金额不能为空',
        (v: string) => {
            // money must be in dd.dd format
            if (v && v.length > 0 && /^\d+(\.\d{0,2})?$/.test(v)) {
                return true;
            }
            return '消费金额格式不正确';
        }
    ]
};

async function validateForm() {
    const { valid } = await stall(form.value.validate());
    return valid;
}

// ============================================================================
// Form fields.
// ============================================================================

// money
const moneyData = ref<string>(formatMoney(data.value.money));

// categories
const categoryList = ref(engine.getTags().getFirstCategories());
const categoryChips = ref(data.value.categories);
watch(categoryChips, (value) => {
    if (value.length === 0) {
        categoryList.value = engine.getTags().getFirstCategories();
    } else if (value.length === 1) {
        categoryList.value = engine.getTags().getSecondCategories(value[0]);
    } else if (value.length === 2) {
        categoryList.value = [];
    } else {
        // TODO: prompt a warning message
        nextTick(() => categoryChips.value.pop());
    }
});

// tags
const tagList = engine.getTags().getTags();
const tagChips = ref(data.value.tags);

// people
const peopleList = engine.getTags().getPeople();
const peopleChips = ref(data.value.people);

// ============================================================================
// Action handlers.
// ============================================================================

const getEditedEntry = () => {
    return {
        ...data.value,
        money: parseMoney(moneyData.value),
        categories: categoryChips.value,
        people: peopleChips.value,
        tags: tagChips.value,
        note: data.value.note.trim()
    };
};

const isLoading = ref(false);

const invokeOnClose = () => {
    if (onClose) {
        onClose();
    }
};

const invokeEmitEvent = (event: EntryUpdateEvent) => {
    bus.emit(BusEventTypes.ENTRY_UPDATE, event);
};

const confirmDialog = ref(false);

const showConfirmDialog = () => {
    confirmDialog.value = true;
};

const closeConfirmDialog = () => {
    confirmDialog.value = false;
};

const onConfirmDelete = (confirm: boolean) => {
    closeConfirmDialog();
    if (confirm) {
        invokeEmitEvent({
            type: EntryUpdateTypes.DELETE,
            handled: false,
            entry: data.value
        });
        invokeOnClose();
    }
};

const onClickCancel = () => {
    invokeOnClose();
};

const onClickConfirm = (isDelete: boolean) => {
    if (isDelete) {
        showConfirmDialog();
        return;
    }

    isLoading.value = true;
    validateForm()
        .then((valid: boolean) => {
            if (valid) {
                const event: EntryUpdateEvent = {
                    type: create ? EntryUpdateTypes.CREATE : EntryUpdateTypes.UPDATE,
                    handled: false,
                    entry: getEditedEntry()
                };
                invokeEmitEvent(event);
                invokeOnClose();
            }
        })
        .finally(() => {
            isLoading.value = false;
        });
};
</script>
