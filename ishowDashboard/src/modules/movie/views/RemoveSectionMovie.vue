<script setup lang="ts">
import type { ISectionMovieResponse } from "../types/movie-response";
import { useMovieStore } from "../stores/movie";
import movieService from "../services/movieService";
import { reactive, watch } from "vue";
import type { IRemoveMovieFromSectionRequest } from "../types/movie-request";

interface Props {
  modelValue: boolean;
  sectionMovieItem: ISectionMovieResponse | null;
}

const movieStore = useMovieStore();

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

  await movieService.removeMovieFromSection(removeRequest);

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
        <v-btn color="secondary" :loading="movieStore.loading" @click="confirm">
          remove
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
