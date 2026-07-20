<script setup lang="ts">
import { reactive, ref } from "vue";
import type { DataTableHeader } from "../../../types/data-table-header.types";
import { createBooleanOptions } from "../../../utils/status-options";
import type {
  ILookupRequest,
  ILookupResponse,
} from "../../../types/lookup.types";
import lookupService from "../../../services/lookup";
import {
  accessTypes,
  badges,
  languages,
  transcodes,
} from "../../../types/metadata";
import { useMovieStore } from "../stores/movie";
import type {
  IFilterMovieResponse,
  IMovieResponse,
} from "../types/movie-response";
import type { IFilterMovieRequest } from "../types/filter-request";
import { getTranscodeStatusColor } from "../../../utils/transcodeStatusUtils";
import movieService from "../services/movieService";
import CreateMovie from "./CreateMovie.vue";
import PublishMovie from "./PublishMovie.vue";
import UpdateMovie from "./UpdateMovie.vue";
import streamService from "../../stream/services/streamService";

const movieStore = useMovieStore();

const isValid = ref(true);

const totalItems = ref(0);

const accounts = ref<ILookupResponse[]>([]);

const actors = ref<ILookupResponse[]>([]);

const badgesOptions = badges;

const transCodeOptions = transcodes;

const languageOptions = languages;

const accessTypeOptions = accessTypes;

const items = ref<IFilterMovieResponse[]>([]);

const posterUrls = reactive<Record<string, string>>({});

const lookupRequest = reactive<ILookupRequest>({
  name: "",
});

const lookupRequest2 = reactive<ILookupRequest>({
  name: "",
});

const credentials = reactive<IFilterMovieRequest>({
  page: 1,
  size: 10,
  name: null,
  status: null,
  accountId: null,
  isActive: "1",
  isPublish: null,
  isKids: null,
  isSport: null,
  accessType: null,
  badge: null,
  language: null,
  actorId: null,
});

const headers: DataTableHeader[] = [
  { title: "Movie ID", key: "movieId" },
  { title: "Title En", key: "titleEn", align: "center" },
  { title: "Title Ar", key: "titleAr", align: "center" },
  { title: "Description En", key: "descriptionEn", align: "center" },
  { title: "Description Ar", key: "descriptionAr", align: "center" },
  { title: "Badge", key: "badge", align: "center" },
  { title: "Tags", key: "tags", align: "center" },
  // { title: "Actors", key: "actors", align: "center" },
  { title: "Access Type", key: "accessType", align: "center" },
  { title: "Release Year", key: "releaseYear", align: "center" },

  { title: "Transcode Status", key: "transcodeStatus", align: "center" },
  { title: "Language", key: "language", align: "center" },
  { title: "Rating", key: "rating", align: "center" },
  { title: "Duration", key: "duration", align: "center" },
  { title: "Subtitle Languages", key: "subtitleLanguages", align: "center" },
  { title: "Audio Languages", key: "audioLanguages", align: "center" },
  { title: "Poster", key: "poster", align: "center" },
  { title: "Has Right", key: "hasRight", align: "center" },
  { title: "Is Kids", key: "isKids", align: "center" },
  { title: "Status", key: "active", align: "center" },
  { title: "Is Sports", key: "isSports", align: "center" },
  { title: "Is Publish", key: "isPublish", align: "center" },
  { title: "Actions", key: "actions", align: "center" },
];

const cases = createBooleanOptions("Active", "Not Active");

const publishCases = createBooleanOptions("Publish", "Not Publish");

const kidsCases = createBooleanOptions("Kids", "Not Kids");

const sportCases = createBooleanOptions("Sport", "Not Sport");

const createDialog = ref(false);
const publishDialog = ref(false);
const editDialog = ref(false);
const selectedMovie = ref<IFilterMovieResponse | null>(null);
const selectedMovieId = ref<string>("");

const openEditDialog = (item: IFilterMovieResponse) => {
  selectedMovieId.value = item.movieId;
  editDialog.value = true;
};

const openPublishDialog = (item: IFilterMovieResponse) => {
  selectedMovie.value = null;
  setTimeout(() => {
    selectedMovie.value = item;
    publishDialog.value = true;
  });
};

const onCreated = () => {
  handleFilters();
};

const onPublished = () => {
  handleFilters();
};

const onUpdated = () => {
  handleFilters();
};

async function getAccounts() {
  accounts.value = await lookupService.getAllAccounts(lookupRequest);
}

async function getActors() {
  actors.value = await lookupService.getAllActors(lookupRequest2);
}

async function handleFilters() {
  const response = await movieService.filterMovies(credentials);

  items.value = response.data ?? [];
  totalItems.value = response.totalCount;

  await Promise.all(
    items.value.map(async (item) => {
      if (item.poster?.generatedPath) {
        try {
          const imageUrl = await streamService.fetchImage(
            item.poster.generatedPath,
          );
          posterUrls[item.movieId] = imageUrl;
        } catch (error) {
          console.error("Failed to load poster for movie", item.movieId, error);
        }
      }
    }),
  );
}

const updatePage = (page: number) => {
  credentials.page = page;
  handleFilters();
};

const updatePageSize = (itemsPerPage: number) => {
  credentials.size = itemsPerPage;
  handleFilters();
};
</script>

<template>
  <v-container>
    <v-row justify="center">
      <v-col cols="12" style="max-width: 900px">
        <div class="title">Movie Service</div>
      </v-col>
    </v-row>
  </v-container>

  <v-tooltip text="Create Movie">
    <template #activator="{ props }">
      <v-btn
        v-bind="props"
        icon
        size="large"
        color="secondary"
        class="fab-btn"
        elevation="8"
        aria-label="Create Movie"
        @click="createDialog = true"
      >
        <v-icon>mdi-plus</v-icon>
      </v-btn>
    </template>
  </v-tooltip>

  <CreateMovie v-model="createDialog" @created="onCreated" />

  <UpdateMovie
    v-model="editDialog"
    :movie-id="selectedMovieId"
    @updated="onUpdated"
  />

  <PublishMovie
    :key="selectedMovie?.movieId"
    v-model="publishDialog"
    :movie-item="selectedMovie"
    @published="onPublished"
  />

  <v-row justify="center">
    <v-col cols="12" style="max-width: 1600px">
      <v-form ref="form" v-model="isValid">
        <v-card-text>
          <v-row>
            <v-col cols="14" md="3">
              <v-text-field
                style="height: 85px"
                v-model="credentials.name"
                label="Movie Title"
                variant="outlined"
                clearable
              ></v-text-field>
            </v-col>
            <v-col cols="14" sm="3">
              <v-select
                v-model="credentials.accountId"
                :items="accounts"
                item-title="name"
                item-value="id"
                label="Account"
                variant="outlined"
                clearable
                outlined
                dense
              >
                <template v-slot:prepend-item>
                  <v-text-field
                    v-model="lookupRequest.name"
                    @input="getAccounts"
                    label="Search Accounts"
                    dense
                    clearable
                    outlined
                    class="mx-2 mt-2"
                    dir="rtl"
                  ></v-text-field> </template
              ></v-select>
            </v-col>

            <v-col cols="14" sm="3">
              <v-select
                v-model="credentials.actorId"
                :items="actors"
                item-title="name"
                item-value="id"
                label="Actor"
                variant="outlined"
                clearable
                outlined
                dense
              >
                <template v-slot:prepend-item>
                  <v-text-field
                    v-model="lookupRequest2.name"
                    @input="getActors"
                    label="Search Actors"
                    dense
                    clearable
                    outlined
                    class="mx-2 mt-2"
                  ></v-text-field> </template
              ></v-select>
            </v-col>

            <v-col cols="12" md="2">
              <v-select
                v-model="credentials.status"
                label="Trans Code Status"
                :items="transCodeOptions"
                item-value="value"
                item-title="title"
                variant="outlined"
                outlined
                clearable
              ></v-select>
            </v-col>

            <v-col cols="12" md="2">
              <v-select
                v-model="credentials.badge"
                label="Badge"
                :items="badgesOptions"
                item-value="value"
                item-title="title"
                variant="outlined"
                outlined
                clearable
              ></v-select>
            </v-col>

            <v-col cols="12" md="2">
              <v-select
                v-model="credentials.accessType"
                label="Access Type"
                :items="accessTypeOptions"
                item-value="value"
                item-title="title"
                variant="outlined"
                outlined
                clearable
              ></v-select>
            </v-col>

            <v-col cols="12" md="2">
              <v-select
                v-model="credentials.language"
                label="Language"
                :items="languageOptions"
                item-value="value"
                item-title="title"
                variant="outlined"
                outlined
                clearable
              ></v-select>
            </v-col>
            <v-col cols="14" md="2">
              <v-select
                v-model="credentials.isActive"
                label="Movie Status"
                :items="cases"
                item-value="value"
                item-title="title"
                variant="outlined"
                outlined
                clearable
              ></v-select>
            </v-col>

            <v-col cols="14" md="2">
              <v-select
                v-model="credentials.isPublish"
                label="Publish Status"
                :items="publishCases"
                item-value="value"
                item-title="title"
                variant="outlined"
                outlined
                clearable
              ></v-select>
            </v-col>

            <v-col cols="14" md="2">
              <v-select
                v-model="credentials.isKids"
                label="Kids Status"
                :items="kidsCases"
                item-value="value"
                item-title="title"
                variant="outlined"
                outlined
                clearable
              ></v-select>
            </v-col>

            <v-col cols="14" md="2">
              <v-select
                v-model="credentials.isSport"
                label="Sport Status"
                :items="sportCases"
                item-value="value"
                item-title="title"
                variant="outlined"
                outlined
                clearable
              ></v-select>
            </v-col>

            <v-btn
              class="btn"
              :disabled="!isValid"
              color="primary"
              @click="onCreated"
            >
              Search
            </v-btn>
          </v-row>

          <v-row>
            <v-col cols="12">
              <AppTable
                :headers="headers"
                :items="items"
                :total-items="totalItems"
                :page="credentials.page"
                :items-per-page="credentials.size"
                :loading="movieStore.loading"
                height="500"
                @update:page="updatePage"
                @update:items-per-page="updatePageSize"
              >
                <template #actions="{ item }">
                  <v-btn icon @click="openEditDialog(item)">
                    <v-icon color="secondary">mdi-pencil</v-icon>
                  </v-btn>
                  <v-btn
                    :disabled="item.isPublish"
                    icon
                    @click="openPublishDialog(item)"
                  >
                    <v-icon>mdi-publish</v-icon>
                  </v-btn>
                </template>

                <template v-slot:item.tags="{ item }">
                  <div>
                    <v-chip
                      v-for="tag in item.tags"
                      :key="tag.id"
                      class="ma-1"
                      small
                    >
                      {{ tag.titleEn }}
                    </v-chip>
                  </div>
                </template>

                <template v-slot:item.actors="{ item }">
                  <div>
                    <v-chip
                      v-for="actor in item.actors"
                      :key="actor.id"
                      class="ma-1"
                      small
                    >
                      {{ actor.titleEn }}
                    </v-chip>
                  </div>
                </template>

                <template v-slot:item.poster="{ item }">
                  <img
                    v-if="posterUrls[item.movieId]"
                    :src="posterUrls[item.movieId]"
                    width="100"
                    height="60"
                    style="object-fit: cover; border-radius: 8px"
                  />
                  <v-progress-circular
                    v-else-if="movieStore.loading"
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

                <template v-slot:item.isKids="{ item }">
                  <v-chip
                    :color="item.isKids ? 'green' : 'red'"
                    small
                    class="ma-1"
                  >
                    {{ item.isKids ? "Yes" : "No" }}
                  </v-chip>
                </template>

                <template v-slot:item.isSports="{ item }">
                  <v-chip
                    :color="item.isSports ? 'green' : 'red'"
                    small
                    class="ma-1"
                  >
                    {{ item.isSports ? "Yes" : "No" }}
                  </v-chip>
                </template>

                <template v-slot:item.isPublish="{ item }">
                  <v-chip
                    :color="item.isPublish ? 'green' : 'red'"
                    small
                    class="ma-1"
                  >
                    {{ item.isPublish ? "Yes" : "No" }}
                  </v-chip>
                </template>

                <template v-slot:item.hasRight="{ item }">
                  <v-chip
                    :color="item.hasRight ? 'green' : 'red'"
                    small
                    class="ma-1"
                  >
                    {{ item.hasRight ? "Yes" : "No" }}
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
              </AppTable>
            </v-col>
          </v-row>
        </v-card-text>
      </v-form>
    </v-col>
  </v-row>
</template>

<style scoped>
.fab-btn {
  position: fixed;
  bottom: 40px;
  right: 40px;
  z-index: 1000;
  border-radius: 50%;
}
</style>
