<script setup lang="ts">
import { reactive, watch } from "vue";
import type { ISectionMovieResponse } from "../../movie/types/movie-response";
import type { IRemoveMovieFromSectionRequest } from "../../movie/types/movie-request";
import { useKidsStore } from "../stores/kids";
import kidsService from "../services/kidsService";

interface Props {
  modelValue: boolean;
  sectionMovieItem: ISectionMovieResponse | null;
}

const kidsStore = useKidsStore();

const removeRequest = reactive<IRemoveMovieFromSectionRequest>({
  movieId: "",
  sectionId: 0
});

const props = defineProps<Props>();
const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
  (e: "removed"): void;
}>();

watch(
  () => props.sectionMovieItem,
  (newItem) => {
    if (newItem) {
      removeRequest.movieId = newItem.movieId;
      removeRequest.sectionId = newItem.sectionId;
    }
  },
  { immediate: true },
);

async function confirm() {
  if (!props.sectionMovieItem) return;

  await kidsService.removeMovieFromSection(removeRequest);

  emit("removed");
  emit("update:modelValue", false);
}
</script>

<template>
  <v-dialog :model-value="modelValue" max-width="600">
    <v-card prepend-icon="mdi-delete" title="Remove Movie from section">

      <v-card-text>
        Are you sure you want to
        <strong>Remove</strong>
        this movie from section?
      </v-card-text>

      <v-card-actions class="justify-end">
        <v-btn variant="text" @click="emit('update:modelValue', false)">
          Cancel
        </v-btn>
        <v-btn color="secondary" :loading="kidsStore.loading" @click="confirm">
          remove
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
