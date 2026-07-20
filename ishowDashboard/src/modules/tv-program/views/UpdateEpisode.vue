<script setup lang="ts">
import { reactive, ref, computed, watch } from "vue";
import { createBooleanOptions } from "../../../utils/status-options";
import { rules } from "../../../composables/validations/rules";
import { accessTypes, badges } from "../../../types/metadata";
import type { ISeasonEpisodeRequest } from "../types/episode-request";
import type { ISeasonEpisodeResponse } from "../types/episode-response";
import { useTvProgramStore } from "../stores/tvProgram";
import tvProgramService from "../services/tvProgramService";
import streamService from "../../stream/services/streamService";

interface Props {
  modelValue: boolean;
  episodeItem: ISeasonEpisodeResponse | null;
}

const props = defineProps<Props>();
const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
  (e: "updated"): void;
}>();

const dialog = ref(false);
const isValid = ref(false);
const formRef = ref();
const tvStore = useTvProgramStore();

const cases = createBooleanOptions("Active", "Not Active");
const badgesOptions = badges;
const accessTypeOptions = accessTypes;

const selectedPosterFiles = ref<File[]>([]);
const selectedVideoFiles = ref<File[]>([]);
const selectedSubtitlesFiles = ref<File[] | undefined>(undefined);

const currentPosterUrl = ref<string | null>(null);
const isLoadingPoster = ref(false);

const normalizedSubtitles = computed<File[]>(() => {
  const val = selectedSubtitlesFiles.value;
  if (!val) return [];
  if (Array.isArray(val)) return val;
  return [val];
});

const form = reactive<ISeasonEpisodeRequest>({
  episodeId: "",
  tvProgramId: "",
  seasonId: "",
  episodeNumber: "",
  titleEn: "",
  titleAr: "",
  active: "1",
  accessType: "",
  badge: "",
  poster: null,
  file: null,
  subtitles: [],
});

watch(
  () => props.modelValue,
  (val) => {
    dialog.value = val;
    if (val && props.episodeItem) {
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
  if (props.episodeItem?.poster?.generatedPath) {
    isLoadingPoster.value = true;
    try {
      if (currentPosterUrl.value) {
        URL.revokeObjectURL(currentPosterUrl.value);
      }
      const imageUrl = await streamService.fetchImage(props.episodeItem.poster.generatedPath);
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
  () => props.episodeItem,
  (newVal) => {
    if (newVal) {
      form.episodeId = newVal.id || "";
      form.tvProgramId = newVal.tvProgramId || "";
      form.seasonId = newVal.seasonId || "";
      form.episodeNumber = newVal.episodeNumber?.toString() || "";
      form.titleEn = newVal.titleEn || "";
      form.titleAr = newVal.titleAr || "";
      form.active = newVal.active ? "1" : "0";
      form.accessType = newVal.accessType || "";
      form.badge = newVal.badge || "";
      
      selectedPosterFiles.value = [];
      selectedVideoFiles.value = [];
      selectedSubtitlesFiles.value = undefined;

      loadCurrentPoster();
    }
  },
  { immediate: true }
);

async function submit() {
  const { valid } = await formRef.value.validate();
  if (!valid) return;

  if (selectedVideoFiles.value.length) {
    form.file = selectedVideoFiles.value[0];
  } else {
    form.file = null;
  }
  
  if (selectedPosterFiles.value.length) {
    form.poster = selectedPosterFiles.value[0];
  } else {
    form.poster = null;
  }
  
  if (normalizedSubtitles.value.length) {
    form.subtitles = normalizedSubtitles.value;
  } else {
    form.subtitles = [];
  }

  await tvProgramService.updateEpisode(form);

  resetForm();
  emit("updated");
  dialog.value = false;
}

function resetForm() {
  form.episodeId = "";
  form.episodeNumber = "";
  form.titleEn = "";
  form.titleAr = "";
  form.active = "1";
  form.accessType = "";
  form.badge = "";
  form.poster = null;
  form.file = null;
  form.subtitles = [];

  selectedVideoFiles.value = [];
  selectedPosterFiles.value = [];
  selectedSubtitlesFiles.value = undefined;
  
  if (currentPosterUrl.value) {
    URL.revokeObjectURL(currentPosterUrl.value);
    currentPosterUrl.value = null;
  }
}
</script>

<template>
  <v-dialog v-model="dialog" max-width="800" persistent>
    <v-card prepend-icon="mdi-pencil" title="Update Episode">
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
              <v-text-field
                v-model="form.episodeNumber"
                label="Episode Number"
                :rules="[rules.required]"
                variant="outlined"
                type="number"
              />
            </v-col>

            <v-col cols="12" md="6">
              <v-select
                v-model="form.badge"
                label="Badge"
                :items="badgesOptions"
                item-value="value"
                item-title="title"
                variant="outlined"
                clearable
              ></v-select>
            </v-col>

            <v-col cols="12" md="6">
              <v-select
                v-model="form.accessType"
                label="Access Type"
                :items="accessTypeOptions"
                :rules="[rules.required]"
                item-value="value"
                item-title="title"
                variant="outlined"
                clearable
              ></v-select>
            </v-col>

            <v-col cols="12" md="6">
              <v-select
                v-model="form.active"
                label="Episode Status"
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
                    alt="Episode Poster"
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
                label="Change Poster (Leave empty to keep current)"
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
                v-model="selectedVideoFiles"
                label="Change Video File (Leave empty to keep current)"
                variant="outlined"
                show-size
                prepend-icon="mdi-video"
                accept="video/*"
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
                Leave empty to keep the current video file
              </div>
            </v-col>

            <v-col cols="12">
              <v-file-input
                v-model="selectedSubtitlesFiles"
                label="Change Subtitles (Leave empty to keep current)"
                variant="outlined"
                show-size
                prepend-icon="mdi-file-document"
                accept=".srt,.vtt"
                multiple
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
                Leave empty to keep existing subtitles
              </div>

              <div v-if="normalizedSubtitles.length" class="text-caption text-medium-emphasis mt-1">
                {{ normalizedSubtitles.length }} new subtitle file(s) selected
              </div>
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
              dialog = false;
            "
          >
            Cancel
          </v-btn>

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