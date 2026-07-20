<script setup lang="ts">
import { ref, watch, reactive, onMounted, computed } from "vue";
import type { IChannelResponse } from "../types/channel-response";
import { useChannelStore } from "../stores/channel";
import type { IChannelRequest } from "../types/channel-request";
import channelService from "../services/channelService";
import { createBooleanOptions } from "../../../utils/status-options";
import { badges } from "../../../types/metadata";
import { rules } from "../../../composables/validations/rules";
import lookupService from "../../../services/lookup";
import type {
  ILookupRequest,
  ILookupResponse,
} from "../../../types/lookup.types";
import streamService from "../../stream/services/streamService";

const props = defineProps<{
  modelValue: boolean;
  channelItem: IChannelResponse | null;
}>();

const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
  (e: "updated"): void;
}>();

const channelStore = useChannelStore();

const dialog = ref(false);
const formRef = ref();
const isValid = ref(true);

const selectedFiles = ref<File[]>([]);

const currentLogoUrl = ref<string | null>(null);
const isLoadingLogo = ref(false);

const cases = createBooleanOptions("Active", "Not Active");

const rightCases = createBooleanOptions("Has Right", "Hasn't Right");

const kidsCases = createBooleanOptions("Kids", "Not Kids");

const sportCases = createBooleanOptions("Sport", "Not Sport");

const badgesOptions = badges;

const tags = ref<ILookupResponse[]>([]);

const lookupRequest = reactive<ILookupRequest>({
  name: "",
});

const form = reactive<IChannelRequest>({
  id: undefined,
  titleEn: "",
  titleAr: "",
  descriptionEn: "",
  descriptionAr: "",
  badge: "",
  tags: [] as number[],
  hasRight: "",
  isKids: "",
  active: "",
  isSports: "",
  logo: null as unknown as File,
});

watch(
  () => props.modelValue,
  (val) => (dialog.value = val),
);
watch(dialog, (val) => emit("update:modelValue", val));

watch(
  () => props.channelItem,
  async (item) => {
    if (!item) return;

    form.id = item.id;
    form.titleEn = item.titleEn;
    form.titleAr = item.titleAr;
    form.descriptionEn = item.descriptionEn;
    form.descriptionAr = item.descriptionAr;
    form.badge = item.badge;
    form.tags = item.tags?.map((tag) => tag.id) || [];
    form.hasRight = item.hasRight ? "1" : "0";
    form.isKids = item.isKids ? "1" : "0";
    form.isSports = item.isSports ? "1" : "0";
    form.active = item.active ? "1" : "0";
    form.logo = null;
    selectedFiles.value = [];

    if (item.logo?.generatedPath) {
      isLoadingLogo.value = true;
      try {
        if (currentLogoUrl.value) {
          URL.revokeObjectURL(currentLogoUrl.value);
        }

        const imageUrl = await streamService.fetchImage(
          item.logo.generatedPath,
        );
        currentLogoUrl.value = imageUrl;
      } catch (error) {
        console.error("Failed to load current logo", error);
        currentLogoUrl.value = null;
      } finally {
        isLoadingLogo.value = false;
      }
    } else {
      currentLogoUrl.value = null;
    }
  },
  { immediate: true },
);

watch(dialog, (isOpen) => {
  if (!isOpen && currentLogoUrl.value) {
    URL.revokeObjectURL(currentLogoUrl.value);
    currentLogoUrl.value = null;
  }
});

const previewLogoUrl = computed(() => {
  if (selectedFiles.value.length > 0) {
    return URL.createObjectURL(selectedFiles.value[0]);
  }
  return null;
});

const displayLogoUrl = computed(() => {
  return previewLogoUrl.value || currentLogoUrl.value;
});

async function getTags() {
  tags.value = await lookupService.getAllTags(lookupRequest);
}

async function submit() {
  const { valid } = await formRef.value.validate();
  if (!valid) return;

  form.logo = selectedFiles.value[0] ?? null;

  await channelService.updateChannel(form);

  emit("updated");
  dialog.value = false;
}

onMounted(async () => {
  getTags();
});
</script>

<template>
  <v-dialog v-model="dialog" max-width="800">
    <v-card prepend-icon="mdi-pencil" title="Update Channel">
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
            <v-col v-if="displayLogoUrl" cols="12">
              <v-card class="pa-3">
                <div class="text-subtitle-2 mb-2">Current Logo</div>
                <div class="d-flex justify-center">
                  <v-progress-circular
                    v-if="isLoadingLogo"
                    indeterminate
                    color="primary"
                  />
                  <img
                    v-else
                    :src="displayLogoUrl"
                    alt="Channel Logo"
                    width="120"
                    height="120"
                    style="object-fit: cover; border-radius: 8px"
                  />
                </div>
              </v-card>
            </v-col>

            <v-col cols="12">
              <v-file-input
                v-model="selectedFiles"
                label="Change Logo (Optional)"
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
                Leave empty to keep the current logo
              </div>
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
                outlined
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
                    v-model="lookupRequest.name"
                    @input="getTags"
                    label="Search Tags"
                    dense
                    clearable
                    outlined
                    class="mx-2 mt-2"
                  ></v-text-field> </template
              ></v-select>
            </v-col>
            <v-col cols="12" md="6">
              <v-select
                v-model="form.active"
                label="Channel Status"
                :items="cases"
                :rules="[rules.required]"
                item-value="value"
                item-title="title"
                variant="outlined"
                outlined
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
                outlined
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
                outlined
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
                outlined
                clearable
              ></v-select>
            </v-col>
          </v-row>
        </v-card-text>

        <v-card-actions class="justify-end">
          <v-btn variant="text" @click="dialog = false">Cancel</v-btn>
          <v-btn
            color="secondary"
            :loading="channelStore.loading"
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
