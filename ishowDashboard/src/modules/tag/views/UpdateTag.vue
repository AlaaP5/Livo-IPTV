<script setup lang="ts">
import { ref, watch, reactive } from "vue";
import { rules } from "../../../composables/validations/rules";
import type { ITagResponse } from "../types/tag-response";
import { useTagStore } from "../stores/tag";
import type { ITagRequest } from "../types/tag-request";
import tagService from "../services/tagService";


const props = defineProps<{
  modelValue: boolean;
  tagItem: ITagResponse | null;
}>();

const tagStore = useTagStore();

const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
  (e: "updated"): void;
}>();

const formRef = ref();

const dialog = ref(false);

watch(
  () => props.modelValue,
  (val) => (dialog.value = val)
);

watch(dialog, (val) => emit("update:modelValue", val));

const isValid = ref(true);

const form = reactive<ITagRequest>({
  id: 0,
  titleEn: "",
  titleAr: "",
  active: "1",
});

watch(
  () => props.tagItem,
  (item) => {
    if (!item) return;

    Object.assign(form, {
      id: item.id,
      titleEn: item.titleEn,
      titleAr: item.titleAr,
      active: (item.active) ? "1" : "0",
    });
  },
  { immediate: true }
);

async function submit() {
  const valid = await formRef.value?.validate();
  if (!valid) return;

  await tagService.updateTag(form);

  emit("updated");
  dialog.value = false;
}
</script>

<template>
  <v-dialog v-model="dialog" max-width="700">
    <v-card prepend-icon="mdi-pencil" title="Update Tag">
      <v-form ref="formRef" v-model="isValid">
        <v-card-text>
          <v-divider />

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
                variant="outlined"
                :items="[
                  { title: 'Active', value: '1' },
                  { title: 'Not Active', value: '0' }
                ]"
              />
            </v-col>
          </v-row>
        </v-card-text>

        <v-card-actions class="justify-end">
          <v-btn variant="text" @click="dialog = false">
            Cancel
          </v-btn>

          <v-btn
            color="secondary"
            :loading="tagStore.loading"
            :disabled="!isValid"
            @click="submit"
          >
            Update
          </v-btn>
        </v-card-actions>
      </v-form>
    </v-card>
  </v-dialog>
</template>