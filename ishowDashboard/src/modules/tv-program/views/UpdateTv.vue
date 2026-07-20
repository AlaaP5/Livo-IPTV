<script setup lang="ts">
import { ref, watch, reactive, onMounted, computed } from "vue";
import { createBooleanOptions } from "../../../utils/status-options";
import { badges, languages } from "../../../types/metadata";
import { rules } from "../../../composables/validations/rules";
import lookupService from "../../../services/lookup";
import type {
  ILookupRequest,
  ILookupResponse,
} from "../../../types/lookup.types";
import { useTvProgramStore } from "../stores/tvProgram";
import type { ITvResponse } from "../types/tv-response";
import type { ITvRequest } from "../types/tv-request";
import tvProgramService from "../services/tvProgramService";
import streamService from "../../stream/services/streamService";

const props = defineProps<{
  modelValue: boolean;
  tvId: string;
}>();

const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
  (e: "updated"): void;
}>();

const tvStore = useTvProgramStore();

const dialog = ref(false);
const formRef = ref();
const isValid = ref(true);
const isLoading = ref(false);

const selectedPosterFiles = ref<File[]>([]);

const currentPosterUrl = ref<string | null>(null);
const isLoadingPoster = ref(false);

const cases = createBooleanOptions("Active", "Not Active");
const rightCases = createBooleanOptions("Has Right", "Hasn't Right");
const kidsCases = createBooleanOptions("Kids", "Not Kids");
const sportCases = createBooleanOptions("Sport", "Not Sport");

const badgesOptions = badges;
const languageOptions = languages;

const tags = ref<ILookupResponse[]>([]);

const tvData = ref<ITvResponse | null>(null);

const lookupTagRequest = reactive<ILookupRequest>({ name: "" });

const form = reactive<ITvRequest>({
  tvProgramId: "",
  titleEn: "",
  titleAr: "",
  descriptionEn: "",
  descriptionAr: "",
  badge: "",
  tags: [] as number[],
  releaseYear: "",
  hasRight: "",
  isKids: "",
  active: "",
  isSports: "",
  language: "",
  poster: null,
  audioLanguages: [],
  subtitleLanguages: [],
});

watch(
  () => props.modelValue,
  async (val) => {
    dialog.value = val;
    if (val && props.tvId) {
      await getTv();
      populateForm();
      await loadCurrentPoster();
    }
  },
  { immediate: true },
);

watch(dialog, (val) => {
  if (!val) {
    resetForm();
    // Clean up poster URL when dialog closes
    if (currentPosterUrl.value) {
      URL.revokeObjectURL(currentPosterUrl.value);
      currentPosterUrl.value = null;
    }
  }
  emit("update:modelValue", val);
});

async function loadCurrentPoster() {
  if (tvData.value?.poster?.generatedPath) {
    isLoadingPoster.value = true;
    try {
      if (currentPosterUrl.value) {
        URL.revokeObjectURL(currentPosterUrl.value);
      }
      const imageUrl = await streamService.fetchImage(
        tvData.value.poster.generatedPath,
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

async function getTags() {
  tags.value = await lookupService.getAllTags(lookupTagRequest);
}

async function getTv() {
  if (!props.tvId) {
    console.error("No tvId provided");
    return;
  }

  isLoading.value = true;
  try {
    console.log("Fetching tv with ID:", props.tvId);
    const response = await tvProgramService.getTv(props.tvId);
    console.log("Tv Program API response:", response);

    if (response && response) {
      tvData.value = response;
      console.log("Tv Program data received:", tvData.value);
    } else {
      console.error("No data in response:", response);
      tvData.value = null;
    }
  } catch (error) {
    console.error("Error fetching tv program:", error);
    tvData.value = null;
  } finally {
    isLoading.value = false;
  }
}

const booleanToString = (value: boolean | undefined | null): string => {
  if (value === undefined || value === null) return "";
  return value ? "1" : "0";
};

function populateForm() {
  if (!tvData.value) {
    console.warn("No tv program data to populate form");
    return;
  }

  console.log("tvData", tvData.value);
  console.log("tvId", tvData.value?.tvProgramId);

  console.log("Populating form with data:", tvData.value);

  form.tvProgramId = tvData.value?.id ?? "";
  form.titleEn = tvData.value.titleEn || "";
  form.titleAr = tvData.value.titleAr || "";
  form.descriptionEn = tvData.value.descriptionEn || "";
  form.descriptionAr = tvData.value.descriptionAr || "";

  form.badge = tvData.value.badge;

  form.tags = tvData.value.tags?.map((tag: any) => tag.id) || [];

  form.releaseYear = tvData.value.releaseYear;

  form.hasRight = booleanToString(tvData.value.hasRight);
  form.isKids = booleanToString(tvData.value.isKids);
  form.active = booleanToString(tvData.value.active);
  form.isSports = booleanToString(tvData.value.isSports);

  form.language = tvData.value.language;

  form.subtitleLanguages = tvData.value.subtitleLanguages || [];

  form.audioLanguages = tvData.value.audioLanguages || [];

  console.log("Form populated:", form);

  selectedPosterFiles.value = [];
}

function resetForm() {
  form.tvProgramId = props.tvId;
  form.titleEn = "";
  form.titleAr = "";
  form.descriptionEn = "";
  form.descriptionAr = "";
  form.badge = "";
  form.tags = [];
  form.releaseYear = "";
  form.hasRight = "";
  form.isKids = "";
  form.active = "";
  form.isSports = "";
  form.language = "";
  form.poster = null;
  form.audioLanguages = [];
  form.subtitleLanguages = [];

  selectedPosterFiles.value = [];

  tvData.value = null;
}

async function submit() {
  const { valid } = await formRef.value.validate();
  if (!valid) return;

  if (!selectedPosterFiles.value.length) {
    form.poster = null;
  } else {
    form.poster = selectedPosterFiles.value[0];
  }

  await tvProgramService.updateTv(form);

  emit("updated");
  dialog.value = false;
}

watch(
  () => props.tvId,
  async (newId, oldId) => {
    console.log("tvId changed from", oldId, "to", newId);
    if (dialog.value && newId) {
      await getTv();
      populateForm();
      await loadCurrentPoster();
    }
  },
);

onMounted(() => {
  getTags();
});
</script>

<template>
  <v-dialog v-model="dialog" max-width="800" persistent>
    <v-card prepend-icon="mdi-pencil" title="Update Tv Program">
      <v-divider></v-divider>

      <v-overlay v-model="isLoading" class="align-center justify-center">
        <v-progress-circular
          indeterminate
          size="64"
          color="primary"
        ></v-progress-circular>
      </v-overlay>

      <v-form ref="formRef" v-model="isValid">
        <v-card-text>
          <v-alert
            v-if="!tvData && !isLoading && dialog"
            type="warning"
            class="mb-4"
          >
            No tv programs data loaded. Please check the console for errors.
          </v-alert>

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
              <v-select
                v-model="form.badge"
                label="Badge"
                :items="badgesOptions"
                :rules="[rules.required]"
                item-value="value"
                item-title="title"
                variant="outlined"
                clearable
              ></v-select>
            </v-col>

            <v-col cols="12" md="6">
              <v-select
                v-model="form.language"
                label="Language"
                :items="languageOptions"
                item-value="value"
                item-title="title"
                variant="outlined"
                clearable
              ></v-select>
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

            <v-col cols="12" sm="6">
              <v-select
                v-model="form.tags"
                :items="tags"
                item-title="name"
                item-value="id"
                label="Tags"
                variant="outlined"
                multiple
                chips
                clearable
              >
                <template v-slot:prepend-item>
                  <v-text-field
                    v-model="lookupTagRequest.name"
                    @input="getTags"
                    label="Search Tags"
                    dense
                    clearable
                    outlined
                    class="mx-2 mt-2"
                  ></v-text-field>
                </template>
              </v-select>
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
                    alt="TV Program Poster"
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

            <v-col cols="12" md="6">
              <v-select
                v-model="form.audioLanguages"
                label="Audio Languages"
                :items="languageOptions"
                item-value="value"
                item-title="title"
                variant="outlined"
                multiple
                chips
                clearable
              />
            </v-col>

            <v-col cols="12" md="6">
              <v-select
                v-model="form.subtitleLanguages"
                label="Subtitle Languages"
                :items="languageOptions"
                item-value="value"
                item-title="title"
                variant="outlined"
                multiple
                chips
                clearable
              />
            </v-col>

            <v-col cols="12" md="6">
              <v-select
                v-model="form.active"
                label="Tv Program Status"
                :items="cases"
                :rules="[rules.required]"
                item-value="value"
                item-title="title"
                variant="outlined"
                clearable
              ></v-select>
            </v-col>

            <v-col cols="12" md="6">
              <v-select
                v-model="form.hasRight"
                label="Right Status"
                :items="rightCases"
                item-value="value"
                item-title="title"
                variant="outlined"
                clearable
              ></v-select>
            </v-col>

            <v-col cols="12" md="6">
              <v-select
                v-model="form.isKids"
                label="Kids Status"
                :items="kidsCases"
                item-value="value"
                item-title="title"
                variant="outlined"
                clearable
              ></v-select>
            </v-col>

            <v-col cols="12" md="6">
              <v-select
                v-model="form.isSports"
                label="Sport Status"
                :items="sportCases"
                item-value="value"
                item-title="title"
                variant="outlined"
                clearable
              ></v-select>
            </v-col>
          </v-row>
        </v-card-text>

        <v-divider />

        <v-card-actions>
          <v-spacer />
          <v-btn variant="text" @click="dialog = false">Cancel</v-btn>
          <v-btn
            color="secondary"
            :loading="tvStore.loading"
            :disabled="!isValid || isLoading"
            @click="submit"
          >
            Update
          </v-btn>
        </v-card-actions>
      </v-form>
    </v-card>
  </v-dialog>
</template>
