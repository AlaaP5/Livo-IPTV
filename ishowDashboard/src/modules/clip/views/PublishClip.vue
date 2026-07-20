<script setup lang="ts">
import clipService from '../services/clipService';
import { useClipStore } from '../stores/clip';
import type { IFilterClipResponse } from '../types/clip-response';




interface Props {
  modelValue: boolean;
  clipItem: IFilterClipResponse | null;
}

const clipStore = useClipStore();

const props = defineProps<Props>();
const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
  (e: "published"): void;
}>();

async function confirm() {
  if (!props.clipItem) return;

  await clipService.publishClip(props.clipItem.clipId);

  emit("published");
  emit("update:modelValue", false);
}
</script>

<template>
  <v-dialog :model-value="modelValue" max-width="600">
    <v-card prepend-icon="mdi-publish" title="Publish Clip">

      <v-card-text>
        Are you sure you want to
        <strong>Publish</strong>
        this clip?
      </v-card-text>

      <v-card-actions class="justify-end">
        <v-btn variant="text" @click="emit('update:modelValue', false)">
          Cancel
        </v-btn>
        <v-btn color="secondary" :loading="clipStore.loading" @click="confirm">
          publish
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
