<script setup lang="ts">
import { ref, watch, reactive, computed } from "vue";
import { createBooleanOptions } from "../../../utils/status-options";
import { rules } from "../../../composables/validations/rules";
import type { ITvSeasonResponse } from "../types/season-response";
import { useTvProgramStore } from "../stores/tvProgram";
import type { ITvSeasonRequest } from "../types/season-request";
import tvProgramService from "../services/tvProgramService";
import streamService from "../../stream/services/streamService";

const props = defineProps<{
  modelValue: boolean;
  seasonItem: ITvSeasonResponse | null;
}>();

const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
  (e: "updated"): void;
}>();

const tvStore = useTvProgramStore();

const dialog = ref(false);
const formRef = ref();
const isValid = ref(true);

const selectedTrailerFiles = ref<File[]>([]);
const selectedPosterFiles = ref<File[]>([]);

const currentPosterUrl = ref<string | null>(null);
const isLoadingPoster = ref(false);

const cases = createBooleanOptions("Active", "Not Active");

const form = reactive<ITvSeasonRequest>({
  seasonId: "",
  tvProgramId: "",
  seasonNumber: "",
  titleEn: "",
  titleAr: "",
  descriptionEn: "",
  descriptionAr: "",
  releaseYear: "",
  active: "",
  trailer: null,
  poster: null,
});

watch(
  () => props.modelValue,
  (val) => {
    dialog.value = val;
    if (val && props.seasonItem) {
      loadCurrentPoster();
    }
  },
  { immediate: true },
);

watch(dialog, (val) => {
  if (!val) {
    if (currentPosterUrl.value) {
      URL.revokeObjectURL(currentPosterUrl.value);
      currentPosterUrl.value = null;
    }
  }
  emit("update:modelValue", val);
});

async function loadCurrentPoster() {
  if (props.seasonItem?.poster?.generatedPath) {
    isLoadingPoster.value = true;
    try {
      if (currentPosterUrl.value) {
        URL.revokeObjectURL(currentPosterUrl.value);
      }
      const imageUrl = await streamService.fetchImage(
        props.seasonItem.poster.generatedPath,
      );
      currentPosterUrl.value = imageUrl;
    } catch (error) {
      console.error("Failed to load current poster", error);
      currentPosterUrl.value = null;
    } finally {
      isLoadingPoster.value = false;
    }
  } else {
    currentPosterUrl.value = null;
  }
}

const previewPosterUrl = computed(() => {
  if (selectedPosterFiles.value.length > 0) {
    return URL.createObjectURL(selectedPosterFiles.value[0]);
  }
  return null;
});

const displayPosterUrl = computed(() => {
  return previewPosterUrl.value || currentPosterUrl.value;
});

watch(
  () => props.seasonItem,
  async (item) => {
    if (!item) return;

    form.seasonId = item.id;
    form.tvProgramId = item.tvProgramId;
    form.seasonNumber = item.seasonNumber;
    form.titleEn = item.titleEn;
    form.titleAr = item.titleAr;
    form.descriptionEn = item.descriptionEn;
    form.descriptionAr = item.descriptionAr;
    form.releaseYear = item.releaseYear;
    form.active = item.active ? "1" : "0";
    form.poster = null;
    form.trailer = null;
    selectedPosterFiles.value = [];
    selectedTrailerFiles.value = [];

    await loadCurrentPoster();
  },
  { immediate: true },
);

async function submit() {
  const { valid } = await formRef.value.validate();
  if (!valid) return;

  form.poster = selectedPosterFiles.value[0] ?? null;
  form.trailer = selectedTrailerFiles.value[0] ?? null;

  await tvProgramService.updateSeason(form);

  emit("updated");
  dialog.value = false;
}
</script>

<template>
  <v-dialog v-model="dialog" max-width="800">
    <v-card prepend-icon="mdi-pencil" title="Update Season">
      <v-divider></v-divider>
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

            <v-col cols="12" md="6">
              <v-textarea
                v-model="form.descriptionEn"
                label="English Description"
                :rules="[rules.required]"
                variant="outlined"
              />
            </v-col>

            <v-col cols="12" md="6">
              <v-textarea
                v-model="form.descriptionAr"
                label="Arabic Description"
                :rules="[rules.required]"
                variant="outlined"
                dir="rtl"
              />
            </v-col>

            <v-col cols="12" md="6">
              <v-text-field
                v-model="form.releaseYear"
                label="Release Year"
                :rules="[rules.required]"
                variant="outlined"
                type="number"
              />
            </v-col>

            <v-col cols="12" md="6">
              <v-text-field
                v-model="form.seasonNumber"
                label="Season Number"
                :rules="[rules.required]"
                variant="outlined"
                type="number"
              />
            </v-col>

            <v-col cols="12" md="6">
              <v-select
                v-model="form.active"
                label="Season Status"
                :items="cases"
                :rules="[rules.required]"
                item-value="value"
                item-title="title"
                variant="outlined"
                clearable
              ></v-select>
            </v-col>

            <v-col v-if="displayPosterUrl" cols="12">
              <v-card class="pa-3">
                <div class="text-subtitle-2 mb-2">Current Poster</div>
                <div class="d-flex justify-center">
                  <v-progress-circular
                    v-if="isLoadingPoster"
                    indeterminate
                    color="primary"
                  />
                  <img
                    v-else
                    :src="displayPosterUrl"
                    alt="Season Poster"
                    width="200"
                    height="120"
                    style="object-fit: cover; border-radius: 8px"
                  />
                </div>
              </v-card>
            </v-col>

            <v-col cols="12">
              <v-file-input
                v-model="selectedPosterFiles"
                label="Change Poster (leave empty to keep existing)"
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
                Leave empty to keep the current poster
              </div>
            </v-col>

            <v-col cols="12">
              <v-file-input
                v-model="selectedTrailerFiles"
                label="Season Trailer"
                variant="outlined"
                show-size
                prepend-icon="mdi-video"
                accept="video/*"
              />
            </v-col>
          </v-row>
        </v-card-text>

        <v-card-actions class="justify-end">
          <v-btn variant="text" @click="dialog = false">Cancel</v-btn>
          <v-btn
            color="secondary"
            :loading="tvStore.loading"
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
