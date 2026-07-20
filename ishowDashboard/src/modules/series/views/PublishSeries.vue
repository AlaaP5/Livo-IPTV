<script setup lang="ts">
import seriesService from '../services/seriesService';
import { useSeriesStore } from '../stores/series';
import type { ISeriesResponse } from '../types/series-response';



interface Props {
  modelValue: boolean;
  seriesItem: ISeriesResponse | null;
}

const seriesStore = useSeriesStore();

const props = defineProps<Props>();
const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
  (e: "published"): void;
}>();

async function confirm() {
  if (!props.seriesItem) return;

  await seriesService.publishSeries(props.seriesItem.seriesId);

  emit("published");
  emit("update:modelValue", false);
}
</script>

<template>
  <v-dialog :model-value="modelValue" max-width="600">
    <v-card prepend-icon="mdi-publish" title="Publish Series">

      <v-card-text>
        Are you sure you want to
        <strong>Publish</strong>
        this series?
      </v-card-text>

      <v-card-actions class="justify-end">
        <v-btn variant="text" @click="emit('update:modelValue', false)">
          Cancel
        </v-btn>
        <v-btn color="secondary" :loading="seriesStore.loading" @click="confirm">
          publish
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
