<script setup lang="ts">
import { reactive, ref } from "vue";

import { createBooleanOptions } from "../../../utils/status-options";
import { rules } from "../../../composables/validations/rules";
import type {
  ILookupRequest,
  ILookupResponse,
} from "../../../types/lookup.types";
import lookupService from "../../../services/lookup";
import { useTvProgramStore } from "../stores/tvProgram";
import type { ITvSeasonRequest } from "../types/season-request";
import tvProgramService from "../services/tvProgramService";

interface Props {
  modelValue: boolean;
  tvId: string;
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

const selectedTrailerFiles = ref<File[]>([]);
const selectedPosterFiles = ref<File[]>([]);

const actors = ref<ILookupResponse[]>([]);

const lookupActorRequest = reactive<ILookupRequest>({ name: "" });

const form = reactive<ITvSeasonRequest>({
  seasonId: "",
  tvProgramId: props.tvId,
  seasonNumber: "",
  titleEn: "",
  titleAr: "",
  descriptionEn: "",
  descriptionAr: "",
  releaseYear: "",
  active: "",
  trailer: null,
  poster: null
});

async function submit() {
  const { valid } = await formRef.value.validate();
  if (!valid) return;
  if (!selectedTrailerFiles.value.length) return;
  if (!selectedPosterFiles.value.length) return;

  form.trailer = selectedTrailerFiles.value[0];
  form.poster = selectedPosterFiles.value[0];

  await tvProgramService.createSeason(form);

  resetForm();
  emit("created");
  emit("update:modelValue", false);
}

function resetForm() {
  form.seasonId = "";
  form.trailer = null;
  form.poster = null;
  form.titleEn = "";
  form.titleAr = "";
  form.descriptionEn = "";
  form.descriptionAr = "";
  form.seasonNumber = "";
  form.releaseYear = "";
  form.active = "1";

  selectedTrailerFiles.value = [];
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
    <v-card prepend-icon="mdi-plus" title="Create Season">
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

            <v-col cols="12" md="12">
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

            <v-col cols="12">
              <v-file-input
                v-model="selectedPosterFiles"
                label="Season Poster"
                variant="outlined"
                show-size
                prepend-icon="mdi-image"
                accept="image/*"
              />
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
