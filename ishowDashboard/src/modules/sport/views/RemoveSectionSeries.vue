<script setup lang="ts">
import { reactive, watch } from "vue";
import type { ISectionSeriesResponse } from "../../series/types/series-response";
import type { IRemoveSeriesFromSectionRequest } from "../../series/types/series-request";
import { useSportStore } from "../stores/sport";
import sportService from "../services/sportService";


interface Props {
  modelValue: boolean;
  sectionSeriesItem: ISectionSeriesResponse | null;
}

const sportStore = useSportStore();

const removeRequest = reactive<IRemoveSeriesFromSectionRequest>({
  seriesId: "",
  sectionId: 0
});

const props = defineProps<Props>();
const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
  (e: "removed"): void;
}>();

watch(
  () => props.sectionSeriesItem,
  (newItem) => {
    if (newItem) {
      removeRequest.seriesId = newItem.seriesId;
      removeRequest.sectionId = newItem.sectionId;
    }
  },
  { immediate: true },
);

async function confirm() {
  if (!props.sectionSeriesItem) return;

  await sportService.removeSeriesFromSection(removeRequest);

  emit("removed");
  emit("update:modelValue", false);
}
</script>

<template>
  <v-dialog :model-value="modelValue" max-width="600">
    <v-card prepend-icon="mdi-delete" title="Remove Series from section">

      <v-card-text>
        Are you sure you want to
        <strong>Remove</strong>
        this series from section?
      </v-card-text>

      <v-card-actions class="justify-end">
        <v-btn variant="text" @click="emit('update:modelValue', false)">
          Cancel
        </v-btn>
        <v-btn color="secondary" :loading="sportStore.loading" @click="confirm">
          remove
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
