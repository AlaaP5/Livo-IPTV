<script setup lang="ts">
import { computed } from "vue";
import type { ISectionClipResponse } from "../../clip/types/clip-response";
import { useSportStore } from "../stores/sport";
import sportService from "../services/sportService";


interface Props {
  modelValue: boolean;
  sectionClipItem: ISectionClipResponse | null;
}

const sportStore = useSportStore();

const props = defineProps<Props>();
const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
  (e: "updated", updatedItem: ISectionClipResponse): void;
}>();

const nextStatus = computed(() =>
  props.sectionClipItem?.isTop ? "0" : "1",
);

const actionLabel = computed(() =>
  props.sectionClipItem?.isTop ? "not top" : "top",
);

async function confirm() {
  if (!props.sectionClipItem) return;

  await sportService.changeStatusClip({
    isTop: nextStatus.value,
    clipId: props.sectionClipItem.clipId,
    sectionId: props.sectionClipItem.sectionId,
  });

  const updatedItem = {
      ...props.sectionClipItem,
      isTop: nextStatus.value == "1" ? true : false
    };

  emit("updated", updatedItem);
  emit("update:modelValue", false);
}
</script>

<template>
  <v-dialog :model-value="modelValue" max-width="600">
    <v-card prepend-icon="mdi-pencil" title="Update Clip Status">
      <v-card-text>
        Are you sure you want to change this clip's status to 
        <strong>{{ actionLabel.toLowerCase() }}</strong>?
      </v-card-text>

      <v-card-actions class="justify-end">
        <v-btn variant="text" @click="emit('update:modelValue', false)">
          Cancel
        </v-btn>
        <v-btn
          color="secondary"
          :loading="sportStore.loading"
          @click="confirm"
        >
          Change 
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
