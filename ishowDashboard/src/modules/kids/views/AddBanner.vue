<script setup lang="ts">
import { reactive, ref, watch } from "vue";
import kidsService from "../services/kidsService";
import type {
  ILookupRequest,
  ILookupResponse,
} from "../../../types/lookup.types";
import lookupService from "../../../services/lookup";

const dialog = defineModel<boolean>({
  required: true,
});

const emit = defineEmits<{
  added: [];
}>();

const tab = ref("movie");

const movieId = ref("");
const seriesId = ref("");
const channelId = ref("");
const tvProgramId = ref("");
const clipId = ref("");

const movies = ref<ILookupResponse[]>([]);
const series = ref<ILookupResponse[]>([]);
const channels = ref<ILookupResponse[]>([]);
const tvPrograms = ref<ILookupResponse[]>([]);
const clips = ref<ILookupResponse[]>([]);

const lookupMovieRequest = reactive<ILookupRequest>({
  name: "",
});

const lookupSeriesRequest = reactive<ILookupRequest>({
  name: "",
});

const lookupChannelRequest = reactive<ILookupRequest>({
  name: "",
});

const lookupTvRequest = reactive<ILookupRequest>({
  name: "",
});

const lookupClipRequest = reactive<ILookupRequest>({
  name: "",
});

const getMovies = async () => {
  movies.value = await lookupService.getAllKidsMovies(lookupMovieRequest);
};

const getSeries = async () => {
  series.value = await lookupService.getAllKidsSeries(lookupSeriesRequest);
};

const getChannels = async () => {
  channels.value = await lookupService.getAllKidsChannels(lookupChannelRequest);
};

const getTvPrograms = async () => {
  tvPrograms.value = await lookupService.getAllKidsTvPrograms(lookupTvRequest);
};

const getClips = async () => {
  clips.value = await lookupService.getAllKidsClips(lookupClipRequest);
};

const loading = ref(false);

const save = async () => {
  loading.value = true;

  try {
    switch (tab.value) {
      case "movie":
        console.log(await kidsService.addMovieBanner(movieId.value));
        break;

      case "series":
        console.log(await kidsService.addSeriesBanner(seriesId.value));
        break;

      case "channel":
        await kidsService.addChannelBanner(channelId.value);
        break;

      case "tv":
        await kidsService.addTvProgramBanner(tvProgramId.value);
        break;

      case "clip":
        await kidsService.addClipBanner(clipId.value);
        break;
    }

    emit("added");

    dialog.value = false;

    reset();
  } finally {
    loading.value = false;
  }
};

watch(
  tab,
  async (value) => {
    switch (value) {
      case "movie":
        await getMovies();
        break;

      case "series":
        await getSeries();
        break;

      case "channel":
        await getChannels();
        break;

      case "tv":
        await getTvPrograms();
        break;

      case "clip":
        await getClips();
        break;
    }
  },
  { immediate: true },
);

const reset = () => {
  movieId.value = "";
  seriesId.value = "";
  channelId.value = "";
  tvProgramId.value = "";
  clipId.value = "";
};
</script>

<template>
  <v-dialog v-model="dialog" width="700">
    <v-card prepend-icon="mdi-plus" title="Add Banner">
      <v-divider></v-divider>

      <v-tabs v-model="tab">
        <v-tab value="movie">Movie</v-tab>
        <v-tab value="series">Series</v-tab>
        <v-tab value="channel">Channel</v-tab>
        <v-tab value="tv">TV Program</v-tab>
        <v-tab value="clip">Clip</v-tab>
      </v-tabs>

      <v-window v-model="tab">
        <v-window-item value="movie">
          <v-card-text>
            <v-select
              v-model="movieId"
              :items="movies"
              item-title="name"
              item-value="id"
              label="Movie"
              variant="outlined"
              clearable
            >
              <template #prepend-item>
                <v-text-field
                  v-model="lookupMovieRequest.name"
                  label="Search Movie"
                  clearable
                  class="mx-2 mt-2"
                  @input="getMovies"
                />
              </template>
            </v-select>
          </v-card-text>
        </v-window-item>

        <v-window-item value="series">
          <v-card-text>
            <v-select
              v-model="seriesId"
              :items="series"
              item-title="name"
              item-value="id"
              label="Series"
              variant="outlined"
              clearable
            >
              <template #prepend-item>
                <v-text-field
                  v-model="lookupSeriesRequest.name"
                  label="Search Series"
                  clearable
                  class="mx-2 mt-2"
                  @input="getSeries"
                />
              </template>
            </v-select>
          </v-card-text>
        </v-window-item>

        <v-window-item value="channel">
          <v-card-text>
            <v-select
              v-model="channelId"
              :items="channels"
              item-title="name"
              item-value="id"
              label="Channel"
              variant="outlined"
              clearable
            >
              <template #prepend-item>
                <v-text-field
                  v-model="lookupChannelRequest.name"
                  label="Search Channels"
                  clearable
                  class="mx-2 mt-2"
                  @input="getChannels"
                />
              </template>
            </v-select>
          </v-card-text>
        </v-window-item>

        <v-window-item value="tv">
          <v-card-text>
            <v-select
              v-model="tvProgramId"
              :items="tvPrograms"
              item-title="name"
              item-value="id"
              label="TV Program"
              variant="outlined"
              clearable
            >
              <template #prepend-item>
                <v-text-field
                  v-model="lookupTvRequest.name"
                  label="Search TV Program"
                  clearable
                  class="mx-2 mt-2"
                  @input="getTvPrograms"
                />
              </template>
            </v-select>
          </v-card-text>
        </v-window-item>

        <v-window-item value="clip">
          <v-card-text>
            <v-select
              v-model="clipId"
              :items="clips"
              item-title="name"
              item-value="id"
              label="Clip"
              variant="outlined"
              clearable
            >
              <template #prepend-item>
                <v-text-field
                  v-model="lookupClipRequest.name"
                  label="Search Clip"
                  clearable
                  class="mx-2 mt-2"
                  @input="getClips"
                />
              </template>
            </v-select>
          </v-card-text>
        </v-window-item>
      </v-window>

      <v-card-actions>
        <v-spacer />

        <v-btn color="grey" @click="dialog = false"> Cancel </v-btn>

        <v-btn color="primary" :loading="loading" @click="save"> Add </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
