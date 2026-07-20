<script setup lang="ts">
import { ref, watch, reactive, computed } from "vue";
import { rules } from "../../../composables/validations/rules";
import type { ISectionResponse } from "../types/section-response";
import { useSectionStore } from "../stores/section";
import type { ISectionRequest } from "../types/section-request";
import sectionService from "../services/sectionService";
import { contentTypes } from "../../../enums/ContentTypeEnum";
import { createBooleanOptions } from "../../../utils/status-options";
import { MobilePage } from "../../../enums/MobilePageEnum";

const props = defineProps<{
  modelValue: boolean;
  sectionItem: ISectionResponse | null;
}>();

const sectionStore = useSectionStore();

const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
  (e: "updated"): void;
}>();

const formRef = ref();

const dialog = ref(false);

const cases = createBooleanOptions("Active", "Not Active");

const types = contentTypes;

const pagesWithDynamicContentType: MobilePage[] = [
  MobilePage.KIDS,
  MobilePage.SPORT,
  MobilePage.HOME,
];

const showContentTypeSelect = computed(() => {
  if (!props.sectionItem) return false;
  
  const pageValue = props.sectionItem.page?.toLowerCase();
  const isDynamicPage = pagesWithDynamicContentType.some(
    page => page.toLowerCase() === pageValue
  );
  return isDynamicPage;
});

watch(
  () => props.modelValue,
  (val) => (dialog.value = val),
);

watch(dialog, (val) => emit("update:modelValue", val));

const isValid = ref(true);

const form = reactive<ISectionRequest>({
  id: 0,
  titleAr: "",
  titleEn: "",
  order: 0,
  page: null,
  contentType: null,
  active: "1",
});

watch(
  () => props.sectionItem,
  (item) => {
    if (!item) return;

    Object.assign(form, {
      id: item.id,
      titleEn: item.titleEn,
      titleAr: item.titleAr,
      order: item.order,
      page: item.page,
      contentType: item.contentType,
      active: item.active ? "1" : "0",
    });
  },
  { immediate: true },
);

async function submit() {
  const valid = await formRef.value?.validate();
  if (!valid) return;

  await sectionService.updateSection(form);

  emit("updated");
  dialog.value = false;
}
</script>

<template>
  <v-dialog v-model="dialog" max-width="700">
    <v-card prepend-icon="mdi-pencil" title="Update Section">
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

            <v-col cols="12" sm="6">
              <v-text-field
                v-model="form.order"
                label="Order"
                type="number"
                variant="outlined"
                :rules="[rules.required]"
              />
            </v-col>

            <v-col cols="12" md="6" v-if="showContentTypeSelect">
              <v-select
                v-model="form.contentType"
                label="Content Type"
                :items="types"
                item-title="title"
                item-value="value"
                variant="outlined"
                :rules="[rules.required]"
              />
            </v-col>

            <v-col cols="12" md="6">
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
          <v-btn variant="text" @click="dialog = false"> Cancel </v-btn>

          <v-btn
            color="secondary"
            :loading="sectionStore.loading"
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
