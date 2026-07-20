<script setup lang="ts">
import { computed } from "vue";
import type { ISectionResponse } from "../types/section-response";
import { useSectionStore } from "../stores/section";
import sectionService from "../services/sectionService";



interface Props {
  modelValue: boolean;
  sectionItem: ISectionResponse | null;
}

const sectionStore = useSectionStore();

const props = defineProps<Props>();
const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
  (e: "activated"): void;
}>();

const nextStatus = computed(() =>
  (props.sectionItem?.active) ? "0" : "1"
);

const actionLabel = computed(() =>
  (props.sectionItem?.active) ? "Deactivate" : "Activate"
);

async function confirm() {
  if (!props.sectionItem) return;

  await sectionService.activateSection(props.sectionItem.id, {
    active: nextStatus.value,
  });

  emit("activated");
  emit("update:modelValue", false);
}
</script>

<template>
  <v-dialog :model-value="modelValue" max-width="600">
    <v-card prepend-icon="mdi-delete" :title="`${actionLabel} Section`">

      <v-card-text>
        Are you sure you want to
        <strong>{{ actionLabel.toLowerCase() }}</strong>
        this section?
      </v-card-text>

      <v-card-actions class="justify-end">
        <v-btn variant="text" @click="emit('update:modelValue', false)">
          Cancel
        </v-btn>
        <v-btn color="secondary" :loading="sectionStore.loading" @click="confirm">
          {{ actionLabel }}
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
