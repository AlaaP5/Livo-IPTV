<script setup lang="ts">
import { computed, reactive, ref, watch } from "vue";
import { rules } from "../../../composables/validations/rules";
import { useSectionStore } from "../stores/section";
import type { ISectionRequest } from "../types/section-request";
import sectionService from "../services/sectionService";
import { createBooleanOptions } from "../../../utils/status-options";
import { MobilePage } from "../../../enums/MobilePageEnum";
import { ContentType, contentTypes } from "../../../enums/ContentTypeEnum";

interface Props {
  modelValue: boolean;
  mobilePage: MobilePage;
  contentType?: ContentType;
}

const props = defineProps<Props>();
const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
  (e: "created"): void;
}>();

const isValid = ref(false);

const sectionStore = useSectionStore();

const types = contentTypes;

const formRef = ref();

const form = reactive<ISectionRequest>({
  id: 0,
  titleAr: "",
  titleEn: "",
  order: null,
  page: props.mobilePage,
  contentType: props.contentType ?? null,
  active: "1",
});

const pagesWithDynamicContentType: MobilePage[] = [
  MobilePage.KIDS,
  MobilePage.SPORT,
  MobilePage.HOME,
];

const showContentTypeSelect = computed(() => {
  return pagesWithDynamicContentType.includes(props.mobilePage);
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
  form.titleAr = "";
  form.titleEn = "";
  form.order = null;
  form.page = props.mobilePage;
  form.contentType = props.contentType ?? null;
  form.active = "1";
}

async function submit() {
  const valid = await formRef.value?.validate();
  if (!valid) return;

  await sectionService.createSection(form);
  emit("created");
  emit("update:modelValue", false);
}
</script>

<template>
  <v-dialog :model-value="modelValue" max-width="700" persistent>
    <v-card prepend-icon="mdi-plus" title="Create Section">
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
          <v-btn variant="text" @click="emit('update:modelValue', false)">
            Cancel
          </v-btn>

          <v-btn
            color="secondary"
            :loading="sectionStore.loading"
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
