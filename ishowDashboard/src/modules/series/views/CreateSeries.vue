<script setup lang="ts">
import { reactive, ref } from "vue";

import { createBooleanOptions } from "../../../utils/status-options";
import { rules } from "../../../composables/validations/rules";
import { badges, languages } from "../../../types/metadata";
import type {
  ILookupRequest,
  ILookupResponse,
} from "../../../types/lookup.types";
import lookupService from "../../../services/lookup";
import { useSeriesStore } from "../stores/series";
import type { ISeriesRequest } from "../types/series-request";
import seriesService from "../services/seriesService";

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
const seriesStore = useSeriesStore();

const cases = createBooleanOptions("Active", "Not Active");
const rightCases = createBooleanOptions("Has Right", "Hasn't Right");
const kidsCases = createBooleanOptions("Kids", "Not Kids");
const sportCases = createBooleanOptions("Sport", "Not Sport");

const badgesOptions = badges;
const languageOptions = languages;

const selectedPosterFiles = ref<File[]>([]);


const tags = ref<ILookupResponse[]>([]);
const actors = ref<ILookupResponse[]>([]);

const lookupTagRequest = reactive<ILookupRequest>({ name: "" });
const lookupActorRequest = reactive<ILookupRequest>({ name: "" });

const form = reactive<ISeriesRequest>({
  seriesId: "",
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
  subtitleLanguages: []
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
  if (!selectedPosterFiles.value.length) return;

  form.poster = selectedPosterFiles.value[0];

  await seriesService.createSeries(form);

  resetForm();
  emit("created");
  emit("update:modelValue", false);
}

function resetForm() {
  form.seriesId = "";
  form.poster = null;
  form.titleEn = "";
  form.titleAr = "";
  form.descriptionEn = "";
  form.descriptionAr = "";
  form.badge = "";
  form.actors = [];
  form.language = "";
  form.releaseYear = "";
  form.rate = "";
  form.tags = [];
  form.hasRight = "";
  form.isKids = "";
  form.isSports = "";
  form.active = "1";
  form.audioLanguages = [];
  form.subtitleLanguages = [];

  lookupTagRequest.name = "";
  lookupActorRequest.name = "";

  selectedPosterFiles.value = [];
}
</script>

<template>
  <v-dialog
    :model-value="modelValue"
    max-width="800"
    persistent
    @update:model-value="emit('update:modelValue', $event)"
  >
    <v-card prepend-icon="mdi-plus" title="Create Series">
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
                label="Series Poster"
                variant="outlined"
                show-size
                prepend-icon="mdi-image"
                accept="image/*"
              />
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
            :loading="seriesStore.loading"
            :disabled="!isValid || !selectedPosterFiles.length"
            @click="submit"
          >
            Create
          </v-btn>
        </v-card-actions>
      </v-form>
    </v-card>
  </v-dialog>
</template>