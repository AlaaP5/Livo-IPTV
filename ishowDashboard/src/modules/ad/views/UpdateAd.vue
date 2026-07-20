<script setup lang="ts">
import { ref, watch, reactive, computed } from "vue";
import { rules } from "../../../composables/validations/rules";
import { useAdStore } from "../stores/ad";
import type { IAdResponse } from "../types/ad-response";
import type { IAdRequest } from "../types/ad-request";
import adService from "../services/adService";
import streamService from "../../stream/services/streamService";

const props = defineProps<{
  modelValue: boolean;
  adItem: IAdResponse | null;
}>();

const adStore = useAdStore();

const selectedFiles = ref<File[]>([]);

const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
  (e: "updated"): void;
}>();

const formRef = ref();

const dialog = ref(false);

const currentImageUrl = ref<string | null>(null);
const isLoadingImage = ref(false);

watch(
  () => props.modelValue,
  (val) => (dialog.value = val),
);

watch(dialog, (val) => emit("update:modelValue", val));

const isValid = ref(true);

const form = reactive<IAdRequest>({
  id: 0,
  deepLink: "",
  imagePath: null as unknown as File,
  startDate: "",
  endDate: "",
  active: "1",
});

watch(
  () => props.adItem,
  async (item) => {
    if (!item) return;

    Object.assign(form, {
      id: item.id,
      deepLink: item.deepLink,
      imagePath: null,
      startDate: item.startDate,
      endDate: item.endDate,
      active: item.active ? "1" : "0",
    });

    selectedFiles.value = [];

    if (item.imagePath) {
      isLoadingImage.value = true;
      try {
        if (currentImageUrl.value) {
          URL.revokeObjectURL(currentImageUrl.value);
        }
        
        const imageUrl = await streamService.fetchImage(item.imagePath);
        currentImageUrl.value = imageUrl;
      } catch (error) {
        console.error("Failed to load current image", error);
        currentImageUrl.value = null;
      } finally {
        isLoadingImage.value = false;
      }
    } else {
      currentImageUrl.value = null;
    }
  },
  { immediate: true },
);

watch(dialog, (isOpen) => {
  if (!isOpen && currentImageUrl.value) {
    URL.revokeObjectURL(currentImageUrl.value);
    currentImageUrl.value = null;
  }
});

// Preview new image when selected
const previewImageUrl = computed(() => {
  if (selectedFiles.value.length > 0) {
    return URL.createObjectURL(selectedFiles.value[0]);
  }
  return null;
});

const displayImageUrl = computed(() => {
  return previewImageUrl.value || currentImageUrl.value;
});

async function submit() {
  const valid = await formRef.value?.validate();
  if (!valid) return;

  form.imagePath = selectedFiles.value[0] ?? null;

  await adService.updateAd(form);

  emit("updated");
  dialog.value = false;
}
</script>

<template>
  <v-dialog v-model="dialog" max-width="700">
    <v-card prepend-icon="mdi-pencil" title="Update Ad">
      <v-form ref="formRef" v-model="isValid">
        <v-card-text>
          <v-divider />

          <v-row>
            <v-col cols="12">
              <v-text-field
                v-model="form.deepLink"
                label="Deep Link"
                :rules="[rules.required]"
                variant="outlined"
              />
            </v-col>

            <v-col cols="12" md="6">
              <v-text-field
                v-model="form.startDate"
                label="Start Date"
                type="date"
                variant="outlined"
                :rules="[rules.required]"
              />
            </v-col>

            <v-col cols="12" md="6">
              <v-text-field
                v-model="form.endDate"
                label="End Date"
                type="date"
                variant="outlined"
                :rules="[rules.required]"
              />
            </v-col>

            <v-col v-if="displayImageUrl" cols="12">
              <v-card class="pa-3">
                <div class="text-subtitle-2 mb-2">Current Image</div>
                <div class="d-flex justify-center">
                  <v-progress-circular
                    v-if="isLoadingImage"
                    indeterminate
                    color="primary"
                  />
                  <img
                    v-else
                    :src="displayImageUrl"
                    alt="Ad Image"
                    width="200"
                    height="150"
                    style="object-fit: cover; border-radius: 8px"
                  />
                </div>
              </v-card>
            </v-col>

            <v-col cols="12">
              <v-file-input
                v-model="selectedFiles"
                label="Change Image (Optional)"
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
                Leave empty to keep the current image
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
            :loading="adStore.loading"
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
