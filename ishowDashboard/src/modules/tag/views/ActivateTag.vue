<script setup lang="ts">
import { computed } from "vue";
import type { ITagResponse } from "../types/tag-response";
import { useTagStore } from "../stores/tag";
import tagService from "../services/tagService";



interface Props {
  modelValue: boolean;
  tagItem: ITagResponse | null;
}

const tagStore = useTagStore();

const props = defineProps<Props>();
const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
  (e: "activated"): void;
}>();

const nextStatus = computed(() =>
  (props.tagItem?.active) ? "0" : "1"
);

const actionLabel = computed(() =>
  (props.tagItem?.active) ? "Deactivate" : "Activate"
);

async function confirm() {
  if (!props.tagItem) return;

  await tagService.activateTag(props.tagItem.id, {
    active: nextStatus.value,
  });

  emit("activated");
  emit("update:modelValue", false);
}
</script>

<template>
  <v-dialog :model-value="modelValue" max-width="600">
    <v-card prepend-icon="mdi-delete" :title="`${actionLabel} Tag`">

      <v-card-text>
        Are you sure you want to
        <strong>{{ actionLabel.toLowerCase() }}</strong>
        this tag?
      </v-card-text>

      <v-card-actions class="justify-end">
        <v-btn variant="text" @click="emit('update:modelValue', false)">
          Cancel
        </v-btn>
        <v-btn color="secondary" :loading="tagStore.loading" @click="confirm">
          {{ actionLabel }}
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
