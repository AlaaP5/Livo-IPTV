<script setup lang="ts">
import { reactive, ref, watch } from "vue";

import { createBooleanOptions } from "../../../utils/status-options";
import { rules } from "../../../composables/validations/rules";
import { accessTypes, badges } from "../../../types/metadata";
import type {
  ILookupRequest,
  ILookupResponse,
} from "../../../types/lookup.types";
import lookupService from "../../../services/lookup";
import { useClipStore } from "../stores/clip";
import type { IClipRequest } from "../types/clip-request";
import clipService from "../services/clipService";
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
const clipStore = useClipStore();

const cases = createBooleanOptions("Active", "Not Active");
const rightCases = createBooleanOptions("Has Right", "Hasn't Right");
const kidsCases = createBooleanOptions("Kids", "Not Kids");
const sportCases = createBooleanOptions("Sport", "Not Sport");

const badgesOptions = badges;
const accessTypeOptions = accessTypes;

const selectedFiles = ref<File[]>([]);
const selectedPosterFiles = ref<File[]>([]);

const { duration, getDuration } = useVideoDuration();

watch(selectedFiles, async (files) => {
  if (!files.length) return;

  await getDuration(files[0]);

  console.log(duration.value);
});


const tags = ref<ILookupResponse[]>([]);

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
  duration: undefined,
  hasRight: "",
  isKids: "",
  active: "",
  isSports: "",
  poster: null,
  file: null
});

async function getTags() {
  tags.value = await lookupService.getAllTags(lookupTagRequest);
}

async function submit() {
  const { valid } = await formRef.value.validate();
  if (!valid) return;
  if (!selectedFiles.value.length) return;
  if (!selectedPosterFiles.value.length) return;

  form.file = selectedFiles.value[0];
  form.poster = selectedPosterFiles.value[0];
  form.duration = duration.value;

  await clipService.createClip(form);

  resetForm();
  emit("created");
  emit("update:modelValue", false);
}

function resetForm() {
  form.clipId = undefined;
  form.file = null;
  form.poster = null;
  form.titleEn = "";
  form.titleAr = "";
  form.descriptionEn = "";
  form.descriptionAr = "";
  form.badge = "";
  form.accessType = "";
  form.duration = undefined;
  form.tags = [];
  form.hasRight = "";
  form.isKids = "";
  form.isSports = "";
  form.active = "1";

  lookupTagRequest.name = "";

  selectedFiles.value = [];
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
    <v-card prepend-icon="mdi-plus" title="Create Clip">
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

            <v-col cols="12">
              <v-file-input
                v-model="selectedPosterFiles"
                label="Clip Poster"
                variant="outlined"
                show-size
                prepend-icon="mdi-image"
                accept="image/*"
              />
            </v-col>

            <v-col cols="12">
              <v-file-input
                v-model="selectedFiles"
                label="Clip File"
                variant="outlined"
                show-size
                prepend-icon="mdi-movie"
                accept="video/*"
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
            :loading="clipStore.loading"
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