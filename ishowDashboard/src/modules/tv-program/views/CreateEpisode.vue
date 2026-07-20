<script setup lang="ts">
import { reactive, ref, computed } from "vue";

import { createBooleanOptions } from "../../../utils/status-options";
import { rules } from "../../../composables/validations/rules";
import { accessTypes, badges } from "../../../types/metadata";
import type { ISeasonEpisodeRequest } from "../types/episode-request";
import { useTvProgramStore } from "../stores/tvProgram";
import tvProgramService from "../services/tvProgramService";

interface Props {
  modelValue: boolean;
  tvId: string;
  seasonId: string;
}

const props = defineProps<Props>();
const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
  (e: "created"): void;
}>();

const isValid = ref(false);
const formRef = ref();
const tvStore = useTvProgramStore();

const cases = createBooleanOptions("Active", "Not Active");
const badgesOptions = badges;
const accessTypeOptions = accessTypes;

const selectedPosterFiles = ref<File[]>([]);
const selectedVideoFiles = ref<File[]>([]);
const selectedSubtitlesFiles = ref<File[] | undefined>(undefined);

const normalizedSubtitles = computed<File[]>(() => {
  const val = selectedSubtitlesFiles.value;
  if (!val) return [];
  if (Array.isArray(val)) return val;
  return [val];
});

const form = reactive<ISeasonEpisodeRequest>({
  episodeId: "",
  tvProgramId: props.tvId,
  seasonId: props.seasonId,
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

async function submit() {
  const { valid } = await formRef.value.validate();
  if (!valid) return;
  if (!selectedVideoFiles.value.length) return;
  if (!selectedPosterFiles.value.length) return;
  if (!normalizedSubtitles.value.length) return;

  form.file = selectedVideoFiles.value[0];
  form.poster = selectedPosterFiles.value[0];
  form.subtitles = normalizedSubtitles.value;

  await tvProgramService.createEpisode(form);

  resetForm();
  emit("created");
  emit("update:modelValue", false);
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
  form.subtitles = null;

  selectedVideoFiles.value = [];
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
    <v-card prepend-icon="mdi-plus" title="Create Episode">
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

            <v-col cols="12">
              <v-file-input
                v-model="selectedPosterFiles"
                label="Episode Poster"
                variant="outlined"
                show-size
                prepend-icon="mdi-image"
                accept="image/*"
              />
            </v-col>

            <v-col cols="12">
              <v-file-input
                v-model="selectedVideoFiles"
                label="Episode Video File"
                variant="outlined"
                show-size
                prepend-icon="mdi-video"
                accept="video/*"
                :rules="[rules.required]"
              />
            </v-col>

            <v-col cols="12">
              <v-file-input
                v-model="selectedSubtitlesFiles"
                label="Episode Subtitles"
                variant="outlined"
                show-size
                prepend-icon="mdi-file-document"
                accept=".srt,.vtt"
                multiple
                clearable
              />

              <div v-if="normalizedSubtitles.length" class="text-caption text-medium-emphasis mt-1">
                {{ normalizedSubtitles.length }} subtitle file(s) selected
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
              emit('update:modelValue', false);
            "
          >
            Cancel
          </v-btn>

          <v-btn
            color="secondary"
            :loading="tvStore.loading"
            :disabled="!isValid || !selectedVideoFiles.length"
            @click="submit"
          >
            Create
          </v-btn>
        </v-card-actions>
      </v-form>
    </v-card>
  </v-dialog>
</template>