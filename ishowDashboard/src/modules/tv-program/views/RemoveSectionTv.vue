<script setup lang="ts">
import { reactive, watch } from "vue";
import type { ISectionTvResponse } from "../types/tv-response";
import { useTvProgramStore } from "../stores/tvProgram";
import type { IRemoveTvFromSectionRequest } from "../types/tv-request";
import tvProgramService from "../services/tvProgramService";

interface Props {
  modelValue: boolean;
  sectionTvItem: ISectionTvResponse | null;
}

const tvStore = useTvProgramStore();

const removeRequest = reactive<IRemoveTvFromSectionRequest>({
  tvId: "",
  sectionId: 0
});

const props = defineProps<Props>();
const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
  (e: "removed"): void;
}>();

watch(
  () => props.sectionTvItem,
  (newItem) => {
    if (newItem) {
      removeRequest.tvId = newItem.tvProgramId;
      removeRequest.sectionId = newItem.sectionId;
    }
  },
  { immediate: true },
);

async function confirm() {
  if (!props.sectionTvItem) return;

  await tvProgramService.removeTvFromSection(removeRequest);

  emit("removed");
  emit("update:modelValue", false);
}
</script>

<template>
  <v-dialog :model-value="modelValue" max-width="600">
    <v-card prepend-icon="mdi-delete" title="Remove Tv Program from section">

      <v-card-text>
        Are you sure you want to
        <strong>Remove</strong>
        this tv program from section?
      </v-card-text>

      <v-card-actions class="justify-end">
        <v-btn variant="text" @click="emit('update:modelValue', false)">
          Cancel
        </v-btn>
        <v-btn color="secondary" :loading="tvStore.loading" @click="confirm">
          remove
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
