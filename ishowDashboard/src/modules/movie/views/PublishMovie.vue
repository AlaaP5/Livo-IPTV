<script setup lang="ts">
import type { IFilterMovieResponse } from "../types/movie-response";
import { useMovieStore } from "../stores/movie";
import movieService from "../services/movieService";


interface Props {
  modelValue: boolean;
  movieItem: IFilterMovieResponse | null;
}

const movieStore = useMovieStore();

const props = defineProps<Props>();
const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
  (e: "published"): void;
}>();

async function confirm() {
  if (!props.movieItem) return;

  await movieService.publishMovie(props.movieItem.movieId);

  emit("published");
  emit("update:modelValue", false);
}
</script>

<template>
  <v-dialog :model-value="modelValue" max-width="600">
    <v-card prepend-icon="mdi-publish" title="Publish Movie">

      <v-card-text>
        Are you sure you want to
        <strong>Publish</strong>
        this movie?
      </v-card-text>

      <v-card-actions class="justify-end">
        <v-btn variant="text" @click="emit('update:modelValue', false)">
          Cancel
        </v-btn>
        <v-btn color="secondary" :loading="movieStore.loading" @click="confirm">
          publish
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
