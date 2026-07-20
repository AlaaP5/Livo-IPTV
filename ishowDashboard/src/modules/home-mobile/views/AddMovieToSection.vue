<script setup lang="ts">
import { reactive, ref, watch } from "vue";
import type {
  ILookupRequest,
  ILookupResponse,
} from "../../../types/lookup.types";
import lookupService from "../../../services/lookup";
import type { IAddMovieToSectionRequest } from "../../movie/types/movie-request";
import { useHomeStore } from "../stores/home";
import homeService from "../services/homeService";


interface Props {
  modelValue: boolean;
  sectionId: number | null;
}

const homeStore = useHomeStore();

const isValid = ref(false);

const formRef = ref();

const topOptions = [
  { value: "1", title: "Top" },
  { value: "0", title: "Not Top" },
];

const lookupMovieRequest = reactive<ILookupRequest>({
  name: "",
});

const movies = ref<ILookupResponse[]>([]);

const props = defineProps<Props>();
const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
  (e: "added"): void;
}>();

const credentials = reactive<IAddMovieToSectionRequest>({
  sectionId: null,
  isTop: null,
  movieId: "",
});

const resetCredentials = () => {
  credentials.sectionId = props.sectionId;
  credentials.isTop = null;
  credentials.movieId = "";
  lookupMovieRequest.name = "";
};

watch(
  () => props.modelValue,
  (isOpen) => {
    if (isOpen) {
      resetCredentials();
      getMovies();
    }
  }
);

watch(
  () => props.sectionId,
  (newSectionId) => {
    credentials.sectionId = newSectionId;
  },
  { immediate: true }
);

async function getMovies() {
  movies.value = await lookupService.getAllKidsMovies(lookupMovieRequest);
}

async function confirm() {

  await homeService.addMovieToSection(credentials);

  emit("added");
  emit("update:modelValue", false);
  resetCredentials();
}
</script>

<template>
  <v-dialog :model-value="modelValue" max-width="800">
    <v-card prepend-icon="mdi-publish" title="Add Movie To Section">
      <v-divider></v-divider>
      <v-form ref="formRef" v-model="isValid">
        <v-card-text>
          <v-row>
            <v-col cols="14" sm="4">
              <v-select
                v-model="credentials.movieId"
                :items="movies"
                item-title="name"
                item-value="id"
                label="Movie"
                variant="outlined"
                clearable
                outlined
                dense
              >
                <template v-slot:prepend-item>
                  <v-text-field
                    v-model="lookupMovieRequest.name"
                    @input="getMovies"
                    label="Search Movies"
                    dense
                    clearable
                    outlined
                    class="mx-2 mt-2"
                  ></v-text-field> </template
              ></v-select>
            </v-col>

            <v-col cols="12" md="4">
              <v-select
                v-model="credentials.isTop"
                label="Is Top"
                :items="topOptions"
                item-value="value"
                item-title="title"
                variant="outlined"
                outlined
                clearable
              ></v-select>
            </v-col>
          </v-row>
        </v-card-text>
      </v-form>
      <v-divider />

      <v-card-actions class="justify-end">
        <v-btn variant="text" @click="emit('update:modelValue', false)">
          Cancel
        </v-btn>
        <v-btn color="secondary" :loading="homeStore.loading" @click="confirm">
          add
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
