<template>
  <v-dialog v-model="dialogVisible" max-width="800" persistent>
    <v-card prepend-icon="mdi-plus" title="Add Question">
      <v-card-text>
        <v-form ref="form" v-model="isValid">
          <v-container>
            <v-row>
              <v-col cols="12">
                <v-text-field
                  v-model="formData.question"
                  label="Question"
                  required
                  :rules="[rules.required]"
                  outlined
                ></v-text-field>
              </v-col>

              <v-col cols="12">
                <v-textarea
                  v-model="formData.answer"
                  label="Answer"
                  required
                  :rules="[rules.required]"
                  outlined
                  rows="4"
                ></v-textarea>
              </v-col>

              <v-col cols="12">
                <v-text-field
                  v-model.number="formData.order"
                  label="Order"
                  type="number"
                  required
                  :rules="[rules.required]"
                  outlined
                ></v-text-field>
              </v-col>
            </v-row>
          </v-container>
        </v-form>
      </v-card-text>

      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="grey darken-1" text @click="closeDialog">Cancel</v-btn>
        <v-btn color="primary" :disabled="!isValid" @click="addItem">Add</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue';
import type { IHelpCenterMeta } from '../../../types/metadata';
import { rules } from '../../../composables/validations/rules';

const props = defineProps<{
  modelValue: boolean;
}>();

const emit = defineEmits<{
  (e: 'update:modelValue', value: boolean): void;
  (e: 'add', item: IHelpCenterMeta): void;
}>();

const dialogVisible = ref(props.modelValue);
const isValid = ref(false);
const form = ref<any>(null);

const formData = ref<IHelpCenterMeta>({
  question: '',
  answer: '',
  order: 1
});

watch(() => props.modelValue, (newVal) => {
  dialogVisible.value = newVal;
});

watch(dialogVisible, (newVal) => {
  emit('update:modelValue', newVal);
});

function closeDialog() {
  dialogVisible.value = false;
  resetForm();
}

function resetForm() {
  formData.value = {
    question: '',
    answer: '',
    order: 1
  };
  form.value?.resetValidation();
}

function addItem() {
  if (form.value?.validate()) {
    emit('add', { ...formData.value });
    closeDialog();
  }
}
</script>