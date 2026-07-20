<script setup lang="ts">
import { reactive, ref, computed, watch } from "vue";

import { createBooleanOptions } from "../../../utils/status-options";
import { rules } from "../../../composables/validations/rules";
import { accessTypes, badges, languages } from "../../../types/metadata";
import type {
  ILookupRequest,
  ILookupResponse,
} from "../../../types/lookup.types";
import lookupService from "../../../services/lookup";
import { useMovieStore } from "../stores/movie";
import type { IMovieRequest } from "../types/movie-request";
import movieService from "../services/movieService";
import { useVideoDuration } from "../../../composables/useVideoDuration";

interface Props {
  modelValue: boolean;
}

const props = defineProps<Props>();
const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
  (e: "created"): void;
}>();

const isValid = ref(false);
const formRef = ref();
const movieStore = useMovieStore();

const cases = createBooleanOptions("Active", "Not Active");
const rightCases = createBooleanOptions("Has Right", "Hasn't Right");
const kidsCases = createBooleanOptions("Kids", "Not Kids");
const sportCases = createBooleanOptions("Sport", "Not Sport");

const badgesOptions = badges;
const languageOptions = languages;
const accessTypeOptions = accessTypes;

const selectedFiles = ref<File[]>([]);
const selectedTrailerFiles = ref<File[]>([]);
const selectedPosterFiles = ref<File[]>([]);
const selectedSubtitlesFiles = ref<File[] | undefined>(undefined);

const { duration, getDuration } = useVideoDuration();

watch(selectedFiles, async (files) => {
  if (!files.length) return;

  await getDuration(files[0]);

  console.log(duration.value);
});

const normalizedSubtitles = computed<File[]>(() => {
  const val = selectedSubtitlesFiles.value;
  if (!val) return [];
  if (Array.isArray(val)) return val;
  return [val];
});

const tags = ref<ILookupResponse[]>([]);
const actors = ref<ILookupResponse[]>([]);

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
  duration: undefined,
  trailer: null,
  poster: null,
  file: null,
  subtitles: [],
  audioLanguages: [],
  subtitleLanguages: [],
});

async function getTags() {
  tags.value = await lookupService.getAllTags(lookupTagRequest);
}

async function getActors() {
  actors.value = await lookupService.getAllActors(lookupActorRequest);
}

async function submit() {
  const { valid } = await formRef.value.validate();
  if (!valid) return;
  if (!selectedFiles.value.length) return;
  if (!selectedTrailerFiles.value.length) return;
  if (!selectedPosterFiles.value.length) return;
  if (!normalizedSubtitles.value.length) return;

  form.file = selectedFiles.value[0];
  form.trailer = selectedTrailerFiles.value[0];
  form.poster = selectedPosterFiles.value[0];
  form.subtitles = normalizedSubtitles.value;

  form.duration = duration.value;

  await movieService.createMovie(form);

  resetForm();
  emit("created");
  emit("update:modelValue", false);
}

function resetForm() {
  form.movieId = undefined;
  form.trailer = null;
  form.file = null;
  form.poster = null;
  form.subtitles = [];
  form.titleEn = "";
  form.titleAr = "";
  form.descriptionEn = "";
  form.descriptionAr = "";
  form.badge = "";
  form.actors = [];
  form.accessType = "";
  form.language = "";
  form.releaseYear = "";
  form.rating = "";
  form.duration = undefined;
  form.tags = [];
  form.hasRight = "";
  form.isKids = "";
  form.isSports = "";
  form.active = "1";
  form.audioLanguages = [];
  form.subtitleLanguages = [];

  lookupTagRequest.name = "";
  lookupActorRequest.name = "";

  selectedFiles.value = [];
  selectedTrailerFiles.value = [];
  selectedPosterFiles.value = [];
  selectedSubtitlesFiles.value = undefined;
}
</script>

<template>
  <v-dialog
    :model-value="modelValue"
    max-width="800"
    persistent
    @update:model-value="emit('update:modelValue', $event)"
  >
    <v-card prepend-icon="mdi-plus" title="Create Movie">
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

            <v-col cols="12">
              <v-file-input
                v-model="selectedPosterFiles"
                label="Movie Poster"
                variant="outlined"
                show-size
                prepend-icon="mdi-image"
                accept="image/*"
              />
            </v-col>

            <v-col cols="12">
              <v-file-input
                v-model="selectedTrailerFiles"
                label="Movie Trailer"
                variant="outlined"
                show-size
                prepend-icon="mdi-video"
                accept="video/*"
              />
            </v-col>

            <v-col cols="12">
              <v-file-input
                v-model="selectedFiles"
                label="Movie File"
                variant="outlined"
                show-size
                prepend-icon="mdi-movie"
                accept="video/*"
              />
            </v-col>

            <v-col cols="12">
              <v-file-input
                v-model="selectedSubtitlesFiles"
                label="Movie Subtitles"
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
                {{ normalizedSubtitles.length }} subtitle file(s) selected
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

          <v-btn
            variant="text"
            @click="
              resetForm();
              emit('update:modelValue', false);
            "
          >
            Cancel
          </v-btn>

          <v-btn
            color="secondary"
            :loading="movieStore.loading"
            :disabled="!isValid || !selectedFiles.length"
            @click="submit"
          >
            Create
          </v-btn>
        </v-card-actions>
      </v-form>
    </v-card>
  </v-dialog>
</template>
