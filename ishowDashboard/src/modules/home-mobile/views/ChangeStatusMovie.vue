<script setup lang="ts">
import { computed } from "vue";
import type { ISectionMovieResponse } from "../../movie/types/movie-response";
import { useHomeStore } from "../stores/home";
import homeService from "../services/homeService";


interface Props {
  modelValue: boolean;
  sectionMovieItem: ISectionMovieResponse | null;
}

const homeStore = useHomeStore();

const props = defineProps<Props>();
const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
  (e: "updated", updatedItem: ISectionMovieResponse): void;
}>();

const nextStatus = computed(() =>
  props.sectionMovieItem?.isTop ? "0" : "1",
);

const actionLabel = computed(() =>
  props.sectionMovieItem?.isTop ? "not top" : "top",
);

async function confirm() {
  if (!props.sectionMovieItem) return;

  await homeService.changeStatusMovie({
    isTop: nextStatus.value,
    movieId: props.sectionMovieItem.movieId,
    sectionId: props.sectionMovieItem.sectionId,
  });

  const updatedItem = {
      ...props.sectionMovieItem,
      isTop: nextStatus.value == "1" ? true : false
    };

  emit("updated", updatedItem);
  emit("update:modelValue", false);
}
</script>

<template>
  <v-dialog :model-value="modelValue" max-width="600">
    <v-card prepend-icon="mdi-pencil" title="Update Movie Status">
      <v-card-text>
        Are you sure you want to change this movie's status to 
        <strong>{{ actionLabel.toLowerCase() }}</strong>?
      </v-card-text>

      <v-card-actions class="justify-end">
        <v-btn variant="text" @click="emit('update:modelValue', false)">
          Cancel
        </v-btn>
        <v-btn
          color="secondary"
          :loading="homeStore.loading"
          @click="confirm"
        >
          Change 
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
