<script setup lang="ts">
import { ref, onMounted, reactive } from "vue";
import type { DataTableHeader } from "../../../types/data-table-header.types";
import router from "../../../router";
import { useTvProgramStore } from "../stores/tvProgram";
import type { ITvSeasonResponse } from "../types/season-response";
import tvProgramService from "../services/tvProgramService";
import CreateSeason from "./CreateSeason.vue";
import UpdateSeason from "./UpdateSeason.vue";
import streamService from "../../stream/services/streamService";

const props = defineProps<{
  tvId: string;
}>();

const isValid = ref(true);
const tvStore = useTvProgramStore();
const isLoading = ref(false);
const totalItems = ref(0);
const items = ref<ITvSeasonResponse[]>([]);

const posterUrls = reactive<Record<string, string>>({});

const headers: DataTableHeader[] = [
  { title: "Season ID", key: "id", align: "center" },
  { title: "Tv Program ID", key: "tvProgramId", align: "center" },
  { title: "Season Number", key: "seasonNumber", align: "center" },
  { title: "Title En", key: "titleEn", align: "center" },
  { title: "Title Ar", key: "titleAr", align: "center" },
  { title: "Description En", key: "descriptionEn", align: "center" },
  { title: "Description Ar", key: "descriptionAr", align: "center" },
  { title: "Release Year", key: "releaseYear", align: "center" },
  { title: "Episode Count", key: "episodeCount", align: "center" },
  { title: "Status", key: "active", align: "center" },
  { title: "Poster", key: "poster", align: "center" },
  { title: "Actions", key: "actions", align: "center" },
];

const createDialog = ref(false);
const editDialog = ref(false);

const selectedSeason = ref<ITvSeasonResponse | null>(null);

const openEditDialog = (item: ITvSeasonResponse) => {
  selectedSeason.value = item;
  editDialog.value = true;
};

const onCreated = () => {
  fetchSeriesSeasons();
};

const onUpdated = () => {
  // const index = items.value.findIndex((item) => item.id === updatedSeason.id);
  // if (index !== -1) {
  //   items.value[index] = {
  //     ...items.value[index],
  //     ...updatedSeason,
  //   };
  // }
  fetchSeriesSeasons();
};

const viewEpisodes = (season: ITvSeasonResponse) => {
  router.push({
    name: "View Episodes For Season",
    params: {
      tvId: season.tvProgramId,
      seasonId: season.id,
    }
  });
};

async function fetchSeriesSeasons() {
  isLoading.value = true;
  try {
    const response = await tvProgramService.getTvSeasons(props.tvId);
    items.value = response.data ?? [];
    totalItems.value = response.totalCount;
    await Promise.all(
      items.value.map(async (item) => {
        if (item.poster?.generatedPath) {
          try {
            const imageUrl = await streamService.fetchImage(item.poster.generatedPath);
            posterUrls[item.id] = imageUrl;
          } catch (error) {
            console.error("Failed to load poster for season", item.id, error);
          }
        }
      }),
    );
  } catch (error) {
    console.error("Error fetching tv program seasons:", error);
  } finally {
    isLoading.value = false;
  }
}

const goBack = () => {
  window.history.back();
};

onMounted(() => {
  fetchSeriesSeasons();
});
</script>

<template>
  <v-container>
    <v-row justify="center">
      <v-col cols="12" style="max-width: 900px">
        <div class="d-flex align-center mb-4">
          <v-btn icon variant="text" @click="goBack" class="mr-4">
            <v-icon>mdi-arrow-left</v-icon>
          </v-btn>
          <div class="title">Tv Seasons - ID: {{ props.tvId }}</div>
        </div>
      </v-col>
    </v-row>
  </v-container>

  <v-tooltip text="Create Season">
    <template #activator="{ props }">
      <v-btn
        v-bind="props"
        icon
        size="large"
        color="secondary"
        class="fab-btn"
        elevation="8"
        aria-label="Create Season"
        @click="createDialog = true"
      >
        <v-icon>mdi-plus</v-icon>
      </v-btn>
    </template>
  </v-tooltip>

  <CreateSeason
    :tv-id="props.tvId"
    v-model="createDialog"
    @created="onCreated"
  />

  <UpdateSeason
    v-model="editDialog"
    :season-item="selectedSeason"
    @updated="onUpdated"
  />

  <v-row justify="center">
    <v-col cols="12" style="max-width: 1600px">
      <v-form ref="form" v-model="isValid">
        <v-card-text>
          <v-row>
            <v-col cols="12">
              <AppTable
                :headers="headers"
                :items="items"
                :total-items="totalItems"
                :loading="tvStore.loading"
                height="500"
                hide-default-footer
              >
                <template #actions="{ item }">
                  <!-- <v-btn icon @click="openActivateDialog(item)">
                    <v-icon :color="item.active ? 'red' : 'green'">
                      {{ item.active ? "mdi-close" : "mdi-check" }}
                    </v-icon>
                  </v-btn> -->
                  <v-btn
                    icon
                    color="primary"
                    @click="viewEpisodes(item)"
                    class="mr-2"
                  >
                    <v-icon>mdi-playlist-play</v-icon>
                  </v-btn>

                  <v-btn icon @click="openEditDialog(item)">
                    <v-icon color="secondary">mdi-pencil</v-icon>
                  </v-btn>
                </template>

                <template v-slot:item.poster="{ item }">
                  <img
                    v-if="posterUrls[item.id]"
                    :src="posterUrls[item.id]"
                    width="100"
                    height="60"
                    style="object-fit: cover; border-radius: 8px"
                  />
                  <v-progress-circular
                    v-else-if="tvStore.loading"
                    indeterminate
                    size="30"
                    width="2"
                    color="primary"
                  />
                  <span v-else>No Poster</span>
                </template>

                <template v-slot:item.active="{ item }">
                  <v-chip
                    :color="item.active ? 'green' : 'red'"
                    small
                    class="ma-1"
                  >
                    {{ item.active ? "Active" : "Not Active" }}
                  </v-chip>
                </template>
              </AppTable>
            </v-col>
          </v-row>
        </v-card-text>
      </v-form>
    </v-col>
  </v-row>
</template>

<style scoped>
.title {
  font-size: 24px;
  font-weight: 500;
}
</style>
