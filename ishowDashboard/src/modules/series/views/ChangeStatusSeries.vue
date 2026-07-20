<script setup lang="ts">
import { computed } from "vue";
import type { ISectionSeriesResponse } from "../types/series-response";
import { useSeriesStore } from "../stores/series";
import seriesService from "../services/seriesService";

interface Props {
  modelValue: boolean;
  sectionSeriesItem: ISectionSeriesResponse | null;
}

const seriesStore = useSeriesStore();

const props = defineProps<Props>();
const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
  (e: "updated", updatedItem: ISectionSeriesResponse): void;
}>();

const nextStatus = computed(() =>
  props.sectionSeriesItem?.isTop ? "0" : "1",
);

const actionLabel = computed(() =>
  props.sectionSeriesItem?.isTop ? "not top" : "top",
);

async function confirm() {
  if (!props.sectionSeriesItem) return;

  await seriesService.changeStatusSeries({
    isTop: nextStatus.value,
    seriesId: props.sectionSeriesItem.seriesId,
    sectionId: props.sectionSeriesItem.sectionId,
  });

  const updatedItem = {
      ...props.sectionSeriesItem,
      isTop: nextStatus.value == "1" ? true : false
    };

  emit("updated", updatedItem);
  emit("update:modelValue", false);
}
</script>

<template>
  <v-dialog :model-value="modelValue" max-width="600">
    <v-card prepend-icon="mdi-pencil" title="Update Series Status">
      <v-card-text>
        Are you sure you want to change this series's status to 
        <strong>{{ actionLabel.toLowerCase() }}</strong>?
      </v-card-text>

      <v-card-actions class="justify-end">
        <v-btn variant="text" @click="emit('update:modelValue', false)">
          Cancel
        </v-btn>
        <v-btn
          color="secondary"
          :loading="seriesStore.loading"
          @click="confirm"
        >
          Change 
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
