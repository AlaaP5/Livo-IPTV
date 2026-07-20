<script setup lang="ts">
import { reactive, ref, watch } from "vue";
import { rules } from "../../../composables/validations/rules";
import { useTagStore } from "../stores/tag";
import type { ITagRequest } from "../types/tag-request";
import tagService from "../services/tagService";
import { createBooleanOptions } from "../../../utils/status-options";


interface Props {
  modelValue: boolean;
}

const props = defineProps<Props>();
const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
  (e: "created"): void;
}>();

const isValid = ref(false);

const tagStore = useTagStore();

const formRef = ref();

const form = reactive<ITagRequest>({
  id: 0,
  titleAr: "",
  titleEn: "",
  active: "1",
});

const cases = createBooleanOptions("Active", "Not Active");


watch(
  () => props.modelValue,
  (val) => {
    if (!val) resetForm();
  },
);

function resetForm() {
  form.id = 0;
  form.titleEn = "";
  form.titleAr = "";
  form.active = "1";
}

async function submit() {
  const valid = await formRef.value?.validate();
  if (!valid) return;

  await tagService.createTag(form);
  emit("created");
  emit("update:modelValue", false);
}
</script>

<template>
  <v-dialog :model-value="modelValue" max-width="700" persistent>
    <v-card prepend-icon="mdi-plus" title="Create Tag">
      <v-divider />

      <v-form ref="formRef" v-model="isValid">
        <v-card-text>
          <v-row>
            <v-col cols="12" md="6">
              <v-text-field
                v-model="form.titleEn"
                label="English Title"                
                :rules="[rules.required]"
                variant="outlined"
              />
            </v-col>

            <v-col cols="12" md="6">
              <v-text-field
                v-model="form.titleAr"
                label="Arabic Title"
                :rules="[rules.required]"
                variant="outlined"
                dir="rtl"
              />
            </v-col>

            <v-col cols="12">
              <v-select
                v-model="form.active"
                label="Status"
                :items="cases"
                item-value="value"
                item-title="title"
                variant="outlined"
              />
            </v-col>
          </v-row>
        </v-card-text>

        <v-card-actions class="justify-end">
          <v-btn variant="text" @click="emit('update:modelValue', false)">
            Cancel
          </v-btn>

          <v-btn
            color="secondary"
            :loading="tagStore.loading"
            :disabled="!isValid"
            @click="submit"
          >
            Create
          </v-btn>
        </v-card-actions>
      </v-form>
    </v-card>
  </v-dialog>
</template>
