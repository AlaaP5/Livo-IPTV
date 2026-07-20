<script setup lang="ts">
import { ref, watch, reactive, onMounted, computed } from "vue";
import { createBooleanOptions } from "../../../utils/status-options";
import { accessTypes, badges, languages } from "../../../types/metadata";
import { rules } from "../../../composables/validations/rules";
import lookupService from "../../../services/lookup";
import type {
  ILookupRequest,
  ILookupResponse,
} from "../../../types/lookup.types";
import { useMovieStore } from "../stores/movie";
import type { IMovieRequest } from "../types/movie-request";
import type { IMovieResponse } from "../types/movie-response";
import movieService from "../services/movieService";
import { useVideoDuration } from "../../../composables/useVideoDuration";
import streamService from "../../stream/services/streamService";

const props = defineProps<{
  modelValue: boolean;
  movieId: string;
}>();

const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
  (e: "updated"): void;
}>();

const movieStore = useMovieStore();

const dialog = ref(false);
const formRef = ref();
const isValid = ref(true);
const isLoading = ref(false);

const selectedFiles = ref<File[]>([]);
const selectedTrailerFiles = ref<File[]>([]);
const selectedPosterFiles = ref<File[]>([]);
const selectedSubtitlesFiles = ref<File[] | undefined>(undefined);

const currentPosterUrl = ref<string | null>(null);
const isLoadingPoster = ref(false);

const { duration, getDuration } = useVideoDuration();

const normalizedSubtitles = computed<File[]>(() => {
  const val = selectedSubtitlesFiles.value;
  if (!val) return [];
  if (Array.isArray(val)) return val;
  return [val];
});

const cases = createBooleanOptions("Active", "Not Active");
const rightCases = createBooleanOptions("Has Right", "Hasn't Right");
const kidsCases = createBooleanOptions("Kids", "Not Kids");
const sportCases = createBooleanOptions("Sport", "Not Sport");

const badgesOptions = badges;
const languageOptions = languages;
const accessTypeOptions = accessTypes;

const tags = ref<ILookupResponse[]>([]);
const actors = ref<ILookupResponse[]>([]);

const movieData = ref<IMovieResponse | null>(null);

const lookupTagRequest = reactive<ILookupRequest>({ name: "" });
const lookupActorRequest = reactive<ILookupRequest>({ name: "" });

const form = reactive<IMovieRequest>({
  movieId: undefined,
  titleEn: "",
  titleAr: "",
  descriptionEn: "",
  descriptionAr: "",
  badge: "",
  tags: [] as number[],
  actors: [] as number[],
  accessType: "",
  releaseYear: undefined,
  hasRight: "",
  isKids: "",
  active: "",
  isSports: "",
  language: "",
  rating: undefined,
  trailer: null,
  poster: null,
  file: null,
  subtitles: [],
  audioLanguages: [],
  subtitleLanguages: [],
});

watch(selectedFiles, async (files) => {
  if (!files.length) return;

  const seconds = await getDuration(files[0]);

  console.log(seconds);

  form.duration = seconds;
});

watch(
  () => props.modelValue,
  async (val) => {
    dialog.value = val;
    if (val && props.movieId) {
      await getMovie();
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
  if (movieData.value?.poster?.generatedPath) {
    isLoadingPoster.value = true;
    try {
      if (currentPosterUrl.value) {
        URL.revokeObjectURL(currentPosterUrl.value);
      }
      const imageUrl = await streamService.fetchImage(
        movieData.value.poster.generatedPath,
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

async function getMovie() {
  if (!props.movieId) {
    console.error("No movieId provided");
    return;
  }

  isLoading.value = true;
  try {
    console.log("Fetching movie with ID:", props.movieId);
    const response = await movieService.getMovie(props.movieId);
    console.log("Movie API response:", response);

    if (response && response) {
      movieData.value = response;
      console.log("Movie data received:", movieData.value);
    } else {
      console.error("No data in response:", response);
      movieData.value = null;
    }
  } catch (error) {
    console.error("Error fetching movie:", error);
    movieData.value = null;
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
  if (!movieData.value) {
    console.warn("No movie data to populate form");
    return;
  }

  console.log("movieData", movieData.value);
  console.log("movieId", movieData.value?.id);

  console.log("Populating form with data:", movieData.value);

  form.movieId = movieData.value?.id ?? "";
  form.titleEn = movieData.value.titleEn || "";
  form.titleAr = movieData.value.titleAr || "";
  form.descriptionEn = movieData.value.descriptionEn || "";
  form.descriptionAr = movieData.value.descriptionAr || "";

  form.badge = movieData.value.badge;

  form.tags = movieData.value.tags?.map((tag: any) => tag.id) || [];

  form.actors = movieData.value.actors?.map((actor: any) => actor.id) || [];

  form.accessType = movieData.value.accessType;

  form.releaseYear = movieData.value.releaseYear;

  form.hasRight = booleanToString(movieData.value.hasRight);
  form.isKids = booleanToString(movieData.value.isKids);
  form.active = booleanToString(movieData.value.active);
  form.isSports = booleanToString(movieData.value.isSports);

  form.language = movieData.value.language;

  form.rating = movieData.value.rating;

  form.duration = movieData.value.duration;

  form.subtitleLanguages = movieData.value.subtitleLanguages || [];

  form.audioLanguages = movieData.value.audioLanguages || [];

  console.log("Form populated:", form);

  selectedFiles.value = [];
  selectedTrailerFiles.value = [];
  selectedPosterFiles.value = [];
  selectedSubtitlesFiles.value = [];
}

function resetForm() {
  form.movieId = props.movieId;
  form.titleEn = "";
  form.titleAr = "";
  form.descriptionEn = "";
  form.descriptionAr = "";
  form.badge = "";
  form.tags = [];
  form.actors = [];
  form.accessType = "";
  form.releaseYear = undefined;
  form.duration = undefined;
  form.hasRight = "";
  form.isKids = "";
  form.active = "";
  form.isSports = "";
  form.language = "";
  form.rating = undefined;
  form.trailer = null;
  form.poster = null;
  form.file = null;
  form.subtitles = [];
  form.audioLanguages = [];
  form.subtitleLanguages = [];

  selectedFiles.value = [];
  selectedTrailerFiles.value = [];
  selectedPosterFiles.value = [];
  selectedSubtitlesFiles.value = [];

  movieData.value = null;
}

async function submit() {
  const { valid } = await formRef.value.validate();
  if (!valid) return;

  if (!selectedFiles.value.length) {
    form.file = null;
  } else {
    form.file = selectedFiles.value[0];
  }

  if (!selectedTrailerFiles.value.length) {
    form.trailer = null;
  } else {
    form.trailer = selectedTrailerFiles.value[0];
  }

  if (!selectedPosterFiles.value.length) {
    form.poster = null;
  } else {
    form.poster = selectedPosterFiles.value[0];
  }

  if (!normalizedSubtitles.value.length) {
    form.subtitles = [];
  } else {
    form.subtitles = normalizedSubtitles.value;
  }

  await movieService.updateMovie(form);

  emit("updated");
  dialog.value = false;
}

watch(
  () => props.movieId,
  async (newId, oldId) => {
    console.log("movieId changed from", oldId, "to", newId);
    if (dialog.value && newId) {
      await getMovie();
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
    <v-card prepend-icon="mdi-pencil" title="Update Movie">
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
            v-if="!movieData && !isLoading && dialog"
            type="warning"
            class="mb-4"
          >
            No movie data loaded. Please check the console for errors.
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

            <v-col cols="12" md="3">
              <v-select
                v-model="form.accessType"
                label="Access Type"
                :items="accessTypeOptions"
                item-value="value"
                item-title="title"
                variant="outlined"
                clearable
              ></v-select>
            </v-col>

            <v-col cols="12" md="3">
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
                v-model="form.rating"
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
                    alt="Movie Poster"
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
                label="Movie Trailer (leave empty to keep existing)"
                variant="outlined"
                show-size
                prepend-icon="mdi-video"
                accept="video/*"
                clearable
              />
            </v-col>

            <v-col cols="12">
              <v-file-input
                v-model="selectedFiles"
                label="Movie File (leave empty to keep existing)"
                variant="outlined"
                show-size
                prepend-icon="mdi-movie"
                accept="video/*"
                clearable
              />
            </v-col>

            <v-col cols="12">
              <v-file-input
                v-model="selectedSubtitlesFiles"
                label="Movie Subtitles (leave empty to keep existing)"
                variant="outlined"
                show-size
                prepend-icon="mdi-file-document"
                accept=".srt,.vtt"
                multiple
                clearable
              />
              <div
                v-if="normalizedSubtitles.length"
                class="text-caption text-medium-emphasis mt-1"
              >
                {{ normalizedSubtitles.length }} new subtitle file(s) selected
              </div>
            </v-col>

            <v-col cols="12" md="6">
              <v-select
                v-model="form.active"
                label="Movie Status"
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
            :loading="movieStore.loading"
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
