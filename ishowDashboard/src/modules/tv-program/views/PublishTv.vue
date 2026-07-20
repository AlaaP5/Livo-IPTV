<script setup lang="ts">
import tvProgramService from '../services/tvProgramService';
import { useTvProgramStore } from '../stores/tvProgram';
import type { ITvResponse } from '../types/tv-response';


interface Props {
  modelValue: boolean;
  tvItem: ITvResponse | null;
}

const tvStore = useTvProgramStore();

const props = defineProps<Props>();
const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
  (e: "published"): void;
}>();

async function confirm() {
  if (!props.tvItem) return;

  await tvProgramService.publishTv(props.tvItem.tvProgramId);

  emit("published");
  emit("update:modelValue", false);
}
</script>

<template>
  <v-dialog :model-value="modelValue" max-width="600">
    <v-card prepend-icon="mdi-publish" title="Publish Tv Program">

      <v-card-text>
        Are you sure you want to
        <strong>Publish</strong>
        this tv program?
      </v-card-text>

      <v-card-actions class="justify-end">
        <v-btn variant="text" @click="emit('update:modelValue', false)">
          Cancel
        </v-btn>
        <v-btn color="secondary" :loading="tvStore.loading" @click="confirm">
          publish
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
