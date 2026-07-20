<script setup lang="ts">
import { ref, watch, reactive, onMounted } from "vue";
import { createBooleanOptions } from "../../../utils/status-options";
import { accessTypes, badges } from "../../../types/metadata";
import { rules } from "../../../composables/validations/rules";
import lookupService from "../../../services/lookup";
import type {
  ILookupRequest,
  ILookupResponse,
} from "../../../types/lookup.types";
import { useClipStore } from "../stores/clip";
import type { IClipResponse } from "../types/clip-response";
import type { IClipRequest } from "../types/clip-request";
import clipService from "../services/clipService";
import { computed } from "vue";
import streamService from "../../stream/services/streamService";
import { useVideoDuration } from "../../../composables/useVideoDuration";


const props = defineProps<{
  modelValue: boolean;
  clipId: string;
}>();

const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
  (e: "updated"): void;
}>();

const clipStore = useClipStore();

const dialog = ref(false);
const formRef = ref();
const isValid = ref(true);
const isLoading = ref(false);

const selectedFiles = ref<File[]>([]);
const selectedPosterFiles = ref<File[]>([]);

const cases = createBooleanOptions("Active", "Not Active");
const rightCases = createBooleanOptions("Has Right", "Hasn't Right");
const kidsCases = createBooleanOptions("Kids", "Not Kids");
const sportCases = createBooleanOptions("Sport", "Not Sport");

const badgesOptions = badges;
const accessTypeOptions = accessTypes;

const { duration, getDuration } = useVideoDuration();

const tags = ref<ILookupResponse[]>([]);

const currentPosterUrl = ref<string | null>(null);
const isLoadingPoster = ref(false);

const clipData = ref<IClipResponse | null>(null);

const lookupTagRequest = reactive<ILookupRequest>({ name: "" });

const form = reactive<IClipRequest>({
  clipId: undefined,
  titleEn: "",
  titleAr: "",
  descriptionEn: "",
  descriptionAr: "",
  badge: "",
  tags: [] as number[],
  accessType: "",
  hasRight: "",
  isKids: "",
  active: "",
  isSports: "",
  poster: null,
  file: null,
});

watch(
  () => props.modelValue,
  async (val) => {
    dialog.value = val;
    if (val && props.clipId) {
      await getClip();
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
  if (clipData.value?.poster?.generatedPath) {
    isLoadingPoster.value = true;
    try {
      if (currentPosterUrl.value) {
        URL.revokeObjectURL(currentPosterUrl.value);
      }
      const imageUrl = await streamService.fetchImage(
        clipData.value.poster.generatedPath,
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

async function getClip() {
  if (!props.clipId) {
    console.error("No clipId provided");
    return;
  }

  isLoading.value = true;
  try {
    console.log("Fetching clip with ID:", props.clipId);
    const response = await clipService.getClip(props.clipId);
    console.log("Clip API response:", response);

    if (response && response) {
      clipData.value = response;
      console.log("Clip data received:", clipData.value);
    } else {
      console.error("No data in response:", response);
      clipData.value = null;
    }
  } catch (error) {
    console.error("Error fetching clip:", error);
    clipData.value = null;
  } finally {
    isLoading.value = false;
  }
}

const booleanToString = (value: boolean | undefined | null): string => {
  if (value === undefined || value === null) return "";
  return value ? "1" : "0";
};

function populateForm() {
  if (!clipData.value) {
    console.warn("No clip data to populate form");
    return;
  }

  console.log("clipData", clipData.value);
  console.log("clipId", clipData.value?.id);

  console.log("Populating form with data:", clipData.value);

  form.clipId = clipData.value?.id ?? "";
  form.titleEn = clipData.value.titleEn || "";
  form.titleAr = clipData.value.titleAr || "";
  form.descriptionEn = clipData.value.descriptionEn || "";
  form.descriptionAr = clipData.value.descriptionAr || "";

  form.badge = clipData.value.badge;

  form.tags = clipData.value.tags?.map((tag: any) => tag.id) || [];

  form.accessType = clipData.value.accessType;

  form.duration = clipData.value.duration;

  form.hasRight = booleanToString(clipData.value.hasRight);
  form.isKids = booleanToString(clipData.value.isKids);
  form.active = booleanToString(clipData.value.active);
  form.isSports = booleanToString(clipData.value.isSports);

  console.log("Form populated:", form);

  selectedFiles.value = [];
  selectedPosterFiles.value = [];
}

watch(selectedFiles, async (files) => {
  if (!files.length) return;

  const seconds = await getDuration(files[0]);

  console.log(seconds);

  form.duration = seconds;
});

function resetForm() {
  form.clipId = props.clipId;
  form.titleEn = "";
  form.titleAr = "";
  form.descriptionEn = "";
  form.descriptionAr = "";
  form.badge = "";
  form.tags = [];
  form.accessType = "";
  form.duration = undefined;
  form.hasRight = "";
  form.isKids = "";
  form.active = "";
  form.isSports = "";
  form.poster = null;
  form.file = null;

  selectedFiles.value = [];
  selectedPosterFiles.value = [];

  clipData.value = null;
}

async function submit() {
  const { valid } = await formRef.value.validate();
  if (!valid) return;

  if (!selectedFiles.value.length) {
    form.file = null;
  } else {
    form.file = selectedFiles.value[0];
  }

  if (!selectedPosterFiles.value.length) {
    form.poster = null;
  } else {
    form.poster = selectedPosterFiles.value[0];
  }

  await clipService.updateClip(form);

  emit("updated");
  dialog.value = false;
}

watch(
  () => props.clipId,
  async (newId, oldId) => {
    console.log("clipId changed from", oldId, "to", newId);
    if (dialog.value && newId) {
      await getClip();
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
    <v-card prepend-icon="mdi-pencil" title="Update Clip">
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
            v-if="!clipData && !isLoading && dialog"
            type="warning"
            class="mb-4"
          >
            No clip data loaded. Please check the console for errors.
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
                v-model="form.accessType"
                label="Access Type"
                :items="accessTypeOptions"
                item-value="value"
                item-title="title"
                variant="outlined"
                clearable
              ></v-select>
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
                    alt="Clip Poster"
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
                v-model="selectedFiles"
                label="Clip File (leave empty to keep existing)"
                variant="outlined"
                show-size
                prepend-icon="mdi-movie"
                accept="video/*"
                clearable
              />
            </v-col>

            <v-col cols="12" md="6">
              <v-select
                v-model="form.active"
                label="Clip Status"
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
            :loading="clipStore.loading"
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
