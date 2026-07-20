<script setup lang="ts">
import { ref, watch, reactive } from "vue";
import { rules } from "../../../composables/validations/rules";
import type { IChampionResponse } from "../types/champion-response";
import { useChampionStore } from "../stores/champion";
import type { IChampionRequest } from "../types/champion-request";
import championService from "../services/championService";
import { computed } from "vue";
import streamService from "../../stream/services/streamService";

const props = defineProps<{
  modelValue: boolean;
  championItem: IChampionResponse | null;
}>();

const championStore = useChampionStore();

const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
  (e: "updated"): void;
}>();

const formRef = ref();

const selectedFiles = ref<File[]>([]);

const dialog = ref(false);

const currentLogoUrl = ref<string | null>(null);
const isLoadingLogo = ref(false);

watch(
  () => props.modelValue,
  (val) => {
    dialog.value = val;
    if (val && props.championItem) {
      loadCurrentLogo();
    }
  },
  { immediate: true },
);

watch(dialog, (val) => {
  if (!val) {
    if (currentLogoUrl.value) {
      URL.revokeObjectURL(currentLogoUrl.value);
      currentLogoUrl.value = null;
    }
  }
  emit("update:modelValue", val);
});

async function loadCurrentLogo() {
  if (props.championItem?.logoPath) {
    isLoadingLogo.value = true;
    try {
      if (currentLogoUrl.value) {
        URL.revokeObjectURL(currentLogoUrl.value);
      }
      const imageUrl = await streamService.fetchImage(
        props.championItem.logoPath,
      );
      currentLogoUrl.value = imageUrl;
    } catch (error) {
      console.error("Failed to load current logo", error);
      currentLogoUrl.value = null;
    } finally {
      isLoadingLogo.value = false;
    }
  } else {
    currentLogoUrl.value = null;
  }
}

const previewLogoUrl = computed(() => {
  if (selectedFiles.value.length > 0) {
    return URL.createObjectURL(selectedFiles.value[0]);
  }
  return null;
});

const displayLogoUrl = computed(() => {
  return previewLogoUrl.value || currentLogoUrl.value;
});

const isValid = ref(true);

const form = reactive<IChampionRequest>({
  id: 0,
  nameEn: "",
  nameAr: "",
  imagePath: null as unknown as File,
  active: "1",
});

watch(
  () => props.championItem,
  (item) => {
    if (!item) return;

    Object.assign(form, {
      id: item.id,
      nameEn: item.nameEn,
      nameAr: item.nameAr,
      imagePath: null,
      active: item.active ? "1" : "0",
    });

    selectedFiles.value = [];

    loadCurrentLogo();
  },
  { immediate: true },
);

async function submit() {
  const valid = await formRef.value?.validate();
  if (!valid) return;

  form.imagePath = selectedFiles.value[0] ?? null;

  await championService.updateChampion(form);

  emit("updated");
  dialog.value = false;
}
</script>

<template>
  <v-dialog v-model="dialog" max-width="700">
    <v-card prepend-icon="mdi-pencil" title="Update Champion">
      <v-form ref="formRef" v-model="isValid">
        <v-card-text>
          <v-divider />

          <v-row>
            <v-col cols="12" md="6">
              <v-text-field
                v-model="form.nameEn"
                label="English Name"
                :rules="[rules.required]"
                variant="outlined"
              />
            </v-col>

            <v-col cols="12" md="6">
              <v-text-field
                v-model="form.nameAr"
                label="Arabic Name"
                :rules="[rules.required]"
                variant="outlined"
                dir="rtl"
              />
            </v-col>

            <v-col cols="12">
              <v-file-input
                v-model="selectedFiles"
                label="Change Logo (Optional)"
                variant="outlined"
                show-size
                prepend-icon="mdi-image"
                accept="image/*"
                clearable
              >
                <template #selection="{ fileNames }">
                  <template v-for="fileName in fileNames" :key="fileName">
                    <v-chip size="small" class="me-2">
                      {{ fileName }}
                    </v-chip>
                  </template>
                </template>
              </v-file-input>
              <div class="text-caption text-grey">
                Leave empty to keep the current logo
              </div>
            </v-col>

            <v-col cols="12">
              <v-select
                v-model="form.active"
                label="Status"
                variant="outlined"
                :items="[
                  { title: 'Active', value: '1' },
                  { title: 'Not Active', value: '0' },
                ]"
              />
            </v-col>
          </v-row>
        </v-card-text>

        <v-card-actions class="justify-end">
          <v-btn variant="text" @click="dialog = false"> Cancel </v-btn>

          <v-btn
            color="secondary"
            :loading="championStore.loading"
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
