<script setup lang="ts">
import { computed } from "vue";
import type { ISectionTvResponse } from "../../tv-program/types/tv-response";
import kidsService from "../services/kidsService";
import { useKidsStore } from "../stores/kids";


interface Props {
  modelValue: boolean;
  sectionTvItem: ISectionTvResponse | null;
}

const kidsStore = useKidsStore();

const props = defineProps<Props>();
const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
  (e: "updated", updatedItem: ISectionTvResponse): void;
}>();

const nextStatus = computed(() =>
  props.sectionTvItem?.isTop ? "0" : "1",
);

const actionLabel = computed(() =>
  props.sectionTvItem?.isTop ? "not top" : "top",
);

async function confirm() {
  if (!props.sectionTvItem) return;

  await kidsService.changeStatusTv({
    isTop: nextStatus.value,
    tvProgramId: props.sectionTvItem.tvProgramId,
    sectionId: props.sectionTvItem.sectionId,
  });

  const updatedItem = {
      ...props.sectionTvItem,
      isTop: nextStatus.value == "1" ? true : false
    };

  emit("updated", updatedItem);
  emit("update:modelValue", false);
}
</script>

<template>
  <v-dialog :model-value="modelValue" max-width="600">
    <v-card prepend-icon="mdi-pencil" title="Update Tv Program Status">
      <v-card-text>
        Are you sure you want to change this tv program's status to 
        <strong>{{ actionLabel.toLowerCase() }}</strong>?
      </v-card-text>

      <v-card-actions class="justify-end">
        <v-btn variant="text" @click="emit('update:modelValue', false)">
          Cancel
        </v-btn>
        <v-btn
          color="secondary"
          :loading="kidsStore.loading"
          @click="confirm"
        >
          Change 
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
