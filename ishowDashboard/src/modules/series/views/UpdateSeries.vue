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
import { useSeriesStore } from "../stores/series";
import type { ISeriesResponse } from "../types/series-response";
import type { ISeriesRequest } from "../types/series-request";
import seriesService from "../services/seriesService";
import streamService from "../../stream/services/streamService";

const props = defineProps<{
  modelValue: boolean;
  seriesId: string;
}>();

const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
  (e: "updated"): void;
}>();

const seriesStore = useSeriesStore();

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
const actors = ref<ILookupResponse[]>([]);

const seriesData = ref<ISeriesResponse | null>(null);

const lookupTagRequest = reactive<ILookupRequest>({ name: "" });
const lookupActorRequest = reactive<ILookupRequest>({ name: "" });

const form = reactive<ISeriesRequest>({
  seriesId: undefined,
  titleEn: "",
  titleAr: "",
  descriptionEn: "",
  descriptionAr: "",
  badge: "",
  tags: [] as number[],
  actors: [] as number[],
  releaseYear: "",
  hasRight: "",
  isKids: "",
  active: "",
  isSports: "",
  language: "",
  rate: "",
  poster: null,
  audioLanguages: [],
  subtitleLanguages: [],
});

watch(
  () => props.modelValue,
  async (val) => {
    dialog.value = val;
    if (val && props.seriesId) {
      await getSeries();
      populateForm();
      await loadCurrentPoster();
    }
  },
  { immediate: true },
);

watch(dialog, (val) => {
  if (!val) {
    resetForm();
    if (currentPosterUrl.value) {
      URL.revokeObjectURL(currentPosterUrl.value);
      currentPosterUrl.value = null;
    }
  }
  emit("update:modelValue", val);
});

async function loadCurrentPoster() {
  if (seriesData.value?.poster?.generatedPath) {
    isLoadingPoster.value = true;
    try {
      if (currentPosterUrl.value) {
        URL.revokeObjectURL(currentPosterUrl.value);
      }
      const imageUrl = await streamService.fetchImage(
        seriesData.value.poster.generatedPath,
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

async function getSeries() {
  if (!props.seriesId) {
    console.error("No seriesId provided");
    return;
  }

  isLoading.value = true;
  try {
    console.log("Fetching series with ID:", props.seriesId);
    const response = await seriesService.getSeries(props.seriesId);
    console.log("Series API response:", response);

    if (response && response) {
      seriesData.value = response;
      console.log("Series data received:", seriesData.value);
    } else {
      console.error("No data in response:", response);
      seriesData.value = null;
    }
  } catch (error) {
    console.error("Error fetching series:", error);
    seriesData.value = null;
  } finally {
    isLoading.value = false;
  }
}

async function getActors() {
  actors.value = await lookupService.getAllActors(lookupActorRequest);
}

const booleanToString = (value: boolean | undefined | null): string => {
  if (value === undefined || value === null) return "";
  return value ? "1" : "0";
};

function populateForm() {
  if (!seriesData.value) {
    console.warn("No series data to populate form");
    return;
  }

  console.log("seriesData", seriesData.value);
  console.log("seriesId", seriesData.value?.seriesId);

  console.log("Populating form with data:", seriesData.value);

  form.seriesId = seriesData.value?.id ?? "";
  form.titleEn = seriesData.value.titleEn || "";
  form.titleAr = seriesData.value.titleAr || "";
  form.descriptionEn = seriesData.value.descriptionEn || "";
  form.descriptionAr = seriesData.value.descriptionAr || "";

  form.badge = seriesData.value.badge;

  form.tags = seriesData.value.tags?.map((tag: any) => tag.id) || [];

  form.actors = seriesData.value.actors?.map((actor: any) => actor.id) || [];

  form.releaseYear = seriesData.value.releaseYear;

  form.hasRight = booleanToString(seriesData.value.hasRight);
  form.isKids = booleanToString(seriesData.value.isKids);
  form.active = booleanToString(seriesData.value.active);
  form.isSports = booleanToString(seriesData.value.isSports);

  form.subtitleLanguages = seriesData.value.subtitleLanguages || [];

  form.audioLanguages = seriesData.value.audioLanguages || [];

  form.language = seriesData.value.language;

  form.rate = seriesData.value.rating;

  console.log("Form populated:", form);

  selectedPosterFiles.value = [];
}

function resetForm() {
  form.seriesId = props.seriesId;
  form.titleEn = "";
  form.titleAr = "";
  form.descriptionEn = "";
  form.descriptionAr = "";
  form.badge = "";
  form.tags = [];
  form.actors = [];
  form.releaseYear = "";
  form.hasRight = "";
  form.isKids = "";
  form.active = "";
  form.isSports = "";
  form.language = "";
  form.rate = "";
  form.poster = null;
  form.audioLanguages = [];
  form.subtitleLanguages = [];

  selectedPosterFiles.value = [];

  seriesData.value = null;
}

async function submit() {
  const { valid } = await formRef.value.validate();
  if (!valid) return;

  if (!selectedPosterFiles.value.length) {
    form.poster = null;
  } else {
    form.poster = selectedPosterFiles.value[0];
  }

  await seriesService.updateSeries(form);

  emit("updated");
  dialog.value = false;
}

watch(
  () => props.seriesId,
  async (newId, oldId) => {
    console.log("seriesId changed from", oldId, "to", newId);
    if (dialog.value && newId) {
      await getSeries();
      populateForm();
      await loadCurrentPoster();
    }
  },
);

onMounted(() => {
  getTags();
  getActors();
});
</script>

<template>
  <v-dialog v-model="dialog" max-width="800" persistent>
    <v-card prepend-icon="mdi-pencil" title="Update Series">
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
            v-if="!seriesData && !isLoading && dialog"
            type="warning"
            class="mb-4"
          >
            No series data loaded. Please check the console for errors.
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

            <v-col cols="12" md="6">
              <v-text-field
                v-model="form.rate"
                label="Rating"
                :rules="[rules.required]"
                variant="outlined"
                type="number"
                step="0.1"
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

            <v-col cols="12" sm="6">
              <v-select
                v-model="form.actors"
                :items="actors"
                item-title="name"
                item-value="id"
                label="Actors"
                variant="outlined"
                multiple
                chips
                clearable
              >
                <template v-slot:prepend-item>
                  <v-text-field
                    v-model="lookupActorRequest.name"
                    @input="getActors"
                    label="Search Actors"
                    dense
                    clearable
                    outlined
                    class="mx-2 mt-2"
                  ></v-text-field>
                </template>
              </v-select>
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
                    alt="Series Poster"
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
                v-model="form.active"
                label="Series Status"
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
            :loading="seriesStore.loading"
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
