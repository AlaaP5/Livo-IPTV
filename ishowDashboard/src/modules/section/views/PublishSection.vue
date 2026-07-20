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
  (e: "published"): void;
}>();

const nextStatus = computed(() =>
  (props.sectionItem?.publish) ? "0" : "1"
);

const actionLabel = computed(() =>
  (props.sectionItem?.publish) ? "Depublish" : "Publish"
);

async function confirm() {
  if (!props.sectionItem) return;

  await sectionService.publishSection(props.sectionItem.id, {
    publish: nextStatus.value,
  });

  emit("published");
  emit("update:modelValue", false);
}
</script>

<template>
  <v-dialog :model-value="modelValue" max-width="600">
    <v-card prepend-icon="mdi-publish" :title="`${actionLabel} Section`">

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
