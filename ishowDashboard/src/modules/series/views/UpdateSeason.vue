<script setup lang="ts">
import { ref, watch, reactive, onMounted } from "vue";
import { createBooleanOptions } from "../../../utils/status-options";
import { rules } from "../../../composables/validations/rules";
import lookupService from "../../../services/lookup";
import type {
  ILookupRequest,
  ILookupResponse,
} from "../../../types/lookup.types";
import type { ISeriesSeasonResponse } from "../types/season-response";
import { useSeriesStore } from "../stores/series";
import type { ISeriesSeasonRequest } from "../types/season-request";
import seriesService from "../services/seriesService";
import { computed } from "vue";
import streamService from "../../stream/services/streamService";

const props = defineProps<{
  modelValue: boolean;
  seasonItem: ISeriesSeasonResponse | null;
}>();

const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
  (e: "updated"): void;
}>();

const seriesStore = useSeriesStore();

const dialog = ref(false);
const formRef = ref();
const isValid = ref(true);

const selectedTrailerFiles = ref<File[]>([]);
const selectedPosterFiles = ref<File[]>([]);

const cases = createBooleanOptions("Active", "Not Active");

const currentPosterUrl = ref<string | null>(null);
const isLoadingPoster = ref(false);

const actors = ref<ILookupResponse[]>([]);

const lookupActorRequest = reactive<ILookupRequest>({ name: "" });

const form = reactive<ISeriesSeasonRequest>({
  seasonId: "",
  seriesId: "",
  seasonNumber: "",
  titleEn: "",
  titleAr: "",
  descriptionEn: "",
  descriptionAr: "",
  actors: [] as number[],
  releaseYear: "",
  active: "",
  rating: "",
  trailer: null,
  poster: null,
});

watch(
  () => props.modelValue,
  (val) => (dialog.value = val),
);
watch(dialog, (val) => emit("update:modelValue", val));

watch(
  () => props.seasonItem,
  async (item) => {
    if (!item) return;

    form.seasonId = item.id;
    form.seriesId = item.seriesId;
    form.seasonNumber = item.seasonNumber;
    form.titleEn = item.titleEn;
    form.titleAr = item.titleAr;
    form.descriptionEn = item.descriptionEn;
    form.descriptionAr = item.descriptionAr;
    form.releaseYear = item.releaseYear;
    form.rating = item.rating;
    form.actors = item.actors?.map((actor: any) => actor.id) || [];
    form.active = item.active ? "1" : "0";
    form.poster = null;
    form.trailer = null;
    selectedPosterFiles.value = [];
    selectedTrailerFiles.value = [];

    // Load current poster using generatedPath
    if (item.poster?.generatedPath) {
      isLoadingPoster.value = true;
      try {
        if (currentPosterUrl.value) {
          URL.revokeObjectURL(currentPosterUrl.value);
        }
        const imageUrl = await streamService.fetchImage(
          item.poster.generatedPath,
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
  },
  { immediate: true },
);

watch(dialog, (isOpen) => {
  if (!isOpen && currentPosterUrl.value) {
    URL.revokeObjectURL(currentPosterUrl.value);
    currentPosterUrl.value = null;
  }
});

const previewPosterUrl = computed(() => {
  if (selectedPosterFiles.value.length > 0) {
    return URL.createObjectURL(selectedPosterFiles.value[0]);
  }
  return null;
});

const displayPosterUrl = computed(() => {
  return previewPosterUrl.value || currentPosterUrl.value;
});

async function getActors() {
  actors.value = await lookupService.getAllActors(lookupActorRequest);
}

async function submit() {
  const { valid } = await formRef.value.validate();
  if (!valid) return;

  form.poster = selectedPosterFiles.value[0] ?? null;
  form.trailer = selectedTrailerFiles.value[0] ?? null;

  await seriesService.updateSeason(form);

  emit("updated");
  dialog.value = false;
}

onMounted(async () => {
  getActors();
});
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
            :loading="seriesStore.loading"
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
