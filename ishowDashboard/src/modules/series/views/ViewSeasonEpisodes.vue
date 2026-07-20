<script setup lang="ts">
import { ref, onMounted, reactive } from "vue";
import type { DataTableHeader } from "../../../types/data-table-header.types";
import type { ISeasonEpisodesResponse } from "../types/episode-response";
import seriesService from "../services/seriesService";
import { useSeriesStore } from "../stores/series";
import CreateEpisode from "./CreateEpisode.vue";
import UpdateEpisode from "./UpdateEpisode.vue";
import type { IFilterSeasonEpisodes } from "../types/filter-request";
import { getTranscodeStatusColor } from "../../../utils/transcodeStatusUtils";
import streamService from "../../stream/services/streamService";

const props = defineProps<{
  seriesId: string;
  seasonId: string;
}>();

const isValid = ref(true);
const seriesStore = useSeriesStore();
const isLoading = ref(false);
const totalItems = ref(0);
const items = ref<ISeasonEpisodesResponse[]>([]);

const posterUrls = reactive<Record<string, string>>({});

const credentials = reactive<IFilterSeasonEpisodes>({
  page: 1,
  size: 10,
  seriesId: props.seriesId,
  seasonId: props.seasonId,
});

const headers: DataTableHeader[] = [
  { title: "Episode ID", key: "id", align: "center" },
  { title: "Episode Number", key: "episodeNumber", align: "center" },
  { title: "Title En", key: "titleEn", align: "center" },
  { title: "Title Ar", key: "titleAr", align: "center" },
  { title: "Poster", key: "poster", align: "center" },
  { title: "Status", key: "active", align: "center" },
  { title: "Published", key: "isPublish", align: "center" },
  { title: "Transcode Status", key: "transcodeStatus", align: "center" },
  { title: "Access Type", key: "accessType", align: "center" },
  { title: "Badge", key: "badge", align: "center" },
  { title: "Rate", key: "rate", align: "center" },
  { title: "Transcode MetaData", key: "transcodeMetaData", align: "center" },
  {
    title: "Original Upload Metadata",
    key: "originalUploadMetadata",
    align: "center",
  },
  { title: "Subtitles", key: "subtitles", align: "center" },
  { title: "Actions", key: "actions", align: "center" },
];

const createDialog = ref(false);
const editDialog = ref(false);

const selectedEpisode = ref<ISeasonEpisodesResponse | null>(null);

const openEditDialog = (item: ISeasonEpisodesResponse) => {
  selectedEpisode.value = item;
  editDialog.value = true;
};

const onCreated = () => {
  fetchSeasonEpisodes();
};

const onUpdated = () => {
  fetchSeasonEpisodes();
};

async function fetchSeasonEpisodes() {
  isLoading.value = true;
  try {
    const response = await seriesService.getSeasonEpisodes(credentials);
    items.value = response.data ?? [];
    totalItems.value = response.totalCount;

    await Promise.all(
      items.value.map(async (item) => {
        if (item.poster?.generatedPath) {
          try {
            const imageUrl = await streamService.fetchImage(item.poster.generatedPath);
            posterUrls[item.id] = imageUrl;
          } catch (error) {
            console.error("Failed to load poster for episode", item.id, error);
          }
        }
      }),
    );
  } catch (error) {
    console.error("Error fetching season episodes:", error);
  } finally {
    isLoading.value = false;
  }
}

const goBack = () => {
  window.history.back();
};

onMounted(() => {
  fetchSeasonEpisodes();
});

const updatePage = (page: number) => {
  credentials.page = page;
  fetchSeasonEpisodes();
};

const updatePageSize = (itemsPerPage: number) => {
  credentials.size = itemsPerPage;
  fetchSeasonEpisodes();
};
</script>

<template>
  <v-container>
    <v-row justify="center">
      <v-col cols="12" style="max-width: 900px">
        <div class="d-flex align-center mb-4">
          <v-btn icon variant="text" @click="goBack" class="mr-4">
            <v-icon>mdi-arrow-left</v-icon>
          </v-btn>
          <div class="title">Season Episodes</div>
        </div>
      </v-col>
    </v-row>
  </v-container>

  <v-tooltip text="Create Episode">
    <template #activator="{ props }">
      <v-btn
        v-bind="props"
        icon
        size="large"
        color="secondary"
        class="fab-btn"
        elevation="8"
        aria-label="Create Episode"
        @click="createDialog = true"
      >
        <v-icon>mdi-plus</v-icon>
      </v-btn>
    </template>
  </v-tooltip>

  <CreateEpisode
    :series-id="props.seriesId"
    :season-id="props.seasonId"
    v-model="createDialog"
    @created="onCreated"
  />

  <UpdateEpisode
    v-model="editDialog"
    :episode-item="selectedEpisode"
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
                :page="credentials.page"
                :items-per-page="credentials.size"
                :loading="seriesStore.loading"
                height="500"
                @update:page="updatePage"
                @update:items-per-page="updatePageSize"
              >
                <template #actions="{ item }">
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
                    v-else-if="seriesStore.loading"
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

                <template v-slot:item.isPublish="{ item }">
                  <v-chip
                    :color="item.isPublish ? 'green' : 'red'"
                    small
                    class="ma-1"
                  >
                    {{ item.isPublish ? "Published" : "Not Published" }}
                  </v-chip>
                </template>

                <template v-slot:item.transcodeStatus="{ item }">
                  <v-chip
                    :color="getTranscodeStatusColor(item.transcodeStatus)"
                    small
                    class="ma-1"
                  >
                    {{ item.transcodeStatus }}
                  </v-chip>
                </template>

                <template v-slot:item.accessType="{ item }">
                  <v-chip small class="ma-1">
                    {{ item.accessType }}
                  </v-chip>
                </template>

                <template v-slot:item.badge="{ item }">
                  <v-chip small class="ma-1">
                    {{ item.badge }}
                  </v-chip>
                </template>

                <!-- <template v-slot:item.rate="{ item }">
                  <v-rating
                    :model-value="item.rate"
                    readonly
                    half-increments
                    size="small"
                    color="amber"
                  />
                </template> -->
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
