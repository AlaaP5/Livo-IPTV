<script setup lang="ts">
import { reactive, watch } from "vue";
import type { ISectionClipResponse } from "../types/clip-response";
import { useClipStore } from "../stores/clip";
import type { IRemoveClipFromSectionRequest } from "../types/clip-request";
import clipService from "../services/clipService";

interface Props {
  modelValue: boolean;
  sectionClipItem: ISectionClipResponse | null;
}

const clipStore = useClipStore();

const removeRequest = reactive<IRemoveClipFromSectionRequest>({
  clipId: "",
  sectionId: 0
});

const props = defineProps<Props>();
const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
  (e: "removed", item: ISectionClipResponse): void;
}>();

watch(
  () => props.sectionClipItem,
  (newItem) => {
    if (newItem) {
      removeRequest.clipId = newItem.clipId;
      removeRequest.sectionId = newItem.sectionId;
    }
  },
  { immediate: true },
);

async function confirm() {
  if (!props.sectionClipItem) return;

  await clipService.removeClipFromSection(removeRequest);

  emit("removed", props.sectionClipItem);
  emit("update:modelValue", false);
}
</script>

<template>
  <v-dialog :model-value="modelValue" max-width="600">
    <v-card prepend-icon="mdi-delete" title="Remove Clip from section">

      <v-card-text>
        Are you sure you want to
        <strong>Remove</strong>
        this clip from section?
      </v-card-text>

      <v-card-actions class="justify-end">
        <v-btn variant="text" @click="emit('update:modelValue', false)">
          Cancel
        </v-btn>
        <v-btn color="secondary" :loading="clipStore.loading" @click="confirm">
          remove
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
