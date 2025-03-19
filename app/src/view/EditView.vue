<template>
    <v-card class="EditView">
        <v-card-title>{{ title }}</v-card-title>
        <v-container>
            <v-form ref="form">
                <div class="EditView__form">
                    <!-- title -->
                    <v-text-field
                        label="Spend Item"
                        variant="outlined"
                        placeholder="Spend Item"
                        :rules="rules.title"
                        v-model="data.title"></v-text-field>
                    <!-- date and money -->
                    <div class="date-and-money">
                        <span class="date text-h6">
                            {{ formatTimestampToSlash(data.date) }}
                        </span>
                        <span class="money">
                            <v-icon
                                class="icon text-h4 text-red-lighten-1"
                                icon="mdi-minus"></v-icon>
                            <span class="input text-red-lighten-1">
                                <v-text-field
                                    label="Money"
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
                        label="Categories"
                        placeholder="Categories"
                        chips
                        clearable
                        closable-chips
                        multiple>
                        <template v-slot:chip="{ props, item }">
                            <v-chip
                                prepend-icon="mdi-slash-forward"
                                v-bind="props"
                                color="green-darken-1"
                                variant="outlined">
                                <strong>{{ item.raw }}</strong>
                            </v-chip>
                        </template>
                    </v-combobox>
                    <!-- people list -->
                    <v-combobox
                        v-model="peopleChips"
                        :items="peopleList"
                        variant="outlined"
                        label="People"
                        placeholder="People"
                        chips
                        clearable
                        closable-chips
                        multiple>
                        <template v-slot:chip="{ props, item }">
                            <v-chip
                                prepend-icon="mdi-at"
                                v-bind="props"
                                color="pink"
                                variant="outlined">
                                <strong>{{ item.raw }}</strong>
                            </v-chip>
                        </template>
                    </v-combobox>
                    <!-- tag list -->
                    <v-combobox
                        v-model="tagChips"
                        :items="tagList"
                        variant="outlined"
                        label="Tags"
                        placeholder="Tags"
                        chips
                        clearable
                        closable-chips
                        multiple>
                        <template v-slot:chip="{ props, item }">
                            <v-chip
                                prepend-icon="mdi-pound"
                                v-bind="props"
                                color="blue-darken-2"
                                variant="outlined">
                                <strong>{{ item.raw }}</strong>
                            </v-chip>
                        </template>
                    </v-combobox>
                    <!-- note -->
                    <v-textarea
                        class="note"
                        v-model="data.note"
                        variant="outlined"
                        label="Note"
                        rows="2"
                        auto-grow
                        clearable></v-textarea>
                </div>
            </v-form>
        </v-container>
        <v-card-actions>
            <v-btn color="error" :loading="isLoading" @click="onClickConfirm(true)">
                Delete
            </v-btn>
            <v-btn color="primary" :loading="isLoading" @click="onClickCancel">
                Cancel
            </v-btn>
            <v-btn color="success" :loading="isLoading" @click="onClickConfirm(false)">
                Confirm
            </v-btn>
        </v-card-actions>
    </v-card>
</template>

<style>
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
    max-width: 60%;
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
    transform: translateY(-30%);
}

.EditView__form .date-and-money .money .input input {
    text-align: right;
    font-size: x-large;
}
</style>

<script setup lang="ts">
import { nextTick, ref, watch } from 'vue';
import { Entry } from '~/engine/models.js';
import { engine } from '~/engine/engine.js';
import {
    BusEventTypes,
    EntryEventHandler,
    EntryUpdateEvent,
    EntryUpdateTypes
} from '~/engine/events.js';
import { formatMoney, formatTimestampToSlash, parseMoney } from '../utils/format.js';
import { VForm } from 'vuetify/components';
import { stall } from '~/utils/stall.js';
import { bus } from '~/extensions/emitter';

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
        (v: string) => !!v || 'You must enter a title.',
        (v: string) => (v && v.length > 0) || 'You must enter a title.'
    ],
    money: [
        (v: string) => !!v || 'You must enter a money amount.',
        (v: string) => {
            // money must be in dd.dd format
            if (v && v.length > 0 && /^\d+(\.\d{0,2})?$/.test(v)) {
                return true;
            }
            return 'You must enter a valid money amount.';
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

const onClickCancel = () => {
    invokeOnClose();
};

const onClickConfirm = (isDelete: boolean) => {
    if (isDelete) {
        invokeEmitEvent({
            type: EntryUpdateTypes.DELETE,
            entry: data.value
        });
        invokeOnClose();
        return;
    }

    isLoading.value = true;
    validateForm()
        .then((valid: boolean) => {
            if (!valid) {
                alert('Please correct the form errors.');
            } else {
                const event: EntryUpdateEvent = {
                    type: create ? EntryUpdateTypes.CREATE : EntryUpdateTypes.UPDATE,
                    entry: getEditedEntry()
                };
                invokeEmitEvent(event);
            }
        })
        .finally(() => {
            isLoading.value = false;
            invokeOnClose();
        });
};
</script>
