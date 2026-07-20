<script setup lang="ts">
import { reactive, ref } from "vue";
import type { DataTableHeader } from "../../../types/data-table-header.types";
import { createBooleanOptions } from "../../../utils/status-options";
import type {
  ILookupRequest,
  ILookupResponse,
} from "../../../types/lookup.types";
import lookupService from "../../../services/lookup";
import { badges, languages } from "../../../types/metadata";
import router from "../../../router";
import { useTvProgramStore } from "../stores/tvProgram";
import type { ITvResponse } from "../types/tv-response";
import type { IFilterTvRequest } from "../types/filter-request";
import tvProgramService from "../services/tvProgramService";
import CreateTv from "./CreateTv.vue";
import UpdateTv from "./UpdateTv.vue";
import PublishTv from "./PublishTv.vue";
import streamService from "../../stream/services/streamService";

const tvStore = useTvProgramStore();

const isValid = ref(true);

const totalItems = ref(0);

const accounts = ref<ILookupResponse[]>([]);

const badgesOptions = badges;

const languageOptions = languages;

const items = ref<ITvResponse[]>([]);

const posterUrls = reactive<Record<string, string>>({});

const lookupAccountRequest = reactive<ILookupRequest>({
  name: "",
});

const credentials = reactive<IFilterTvRequest>({
  page: 1,
  size: 10,
  name: null,
  accountId: null,
  isActive: "1",
  isPublish: null,
  isKids: null,
  isSport: null,
  hasRight: null,
  badge: null,
  language: null,
});

const headers: DataTableHeader[] = [
  { title: "Tv Program ID", key: "tvProgramId" },
  { title: "Title En", key: "titleEn", align: "center" },
  { title: "Title Ar", key: "titleAr", align: "center" },
  { title: "Description En", key: "descriptionEn", align: "center" },
  { title: "Description Ar", key: "descriptionAr", align: "center" },
  { title: "Badge", key: "badge", align: "center" },
  { title: "Tags", key: "tags", align: "center" },
  { title: "Release Year", key: "releaseYear", align: "center" },
  { title: "Language", key: "language", align: "center" },
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

const rightCases = createBooleanOptions("Has Right", "Hasn't Right");

const createDialog = ref(false);
const editDialog = ref(false);
const publishDialog = ref(false);
const selectedTv = ref<ITvResponse | null>(null);
const selectedTvId = ref<string>("");

const openEditDialog = (item: ITvResponse) => {
  selectedTvId.value = item.tvProgramId;
  editDialog.value = true;
};

const openPublishDialog = (item: ITvResponse) => {
  selectedTv.value = null;
  setTimeout(() => {
    selectedTv.value = item;
    publishDialog.value = true;
  });
};

const goToTvSeasons = (tvId: string) => {
  router.push({
    name: "View Seasons For Tv Programs",
    params: { tvId: tvId },
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
  accounts.value = await lookupService.getAllAccounts(lookupAccountRequest);
}

async function handleFilters() {
  const response = await tvProgramService.filterTvs(credentials);

  items.value = response.data ?? [];
  totalItems.value = response.totalCount;

  await Promise.all(
    items.value.map(async (item) => {
      if (item.poster?.generatedPath) {
        try {
          const imageUrl = await streamService.fetchImage(
            item.poster.generatedPath,
          );
          posterUrls[item.tvProgramId] = imageUrl;
        } catch (error) {
          console.error("Failed to load poster for TV program", item.tvProgramId, error);
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
        <div class="title">Tv Program Service</div>
      </v-col>
    </v-row>
  </v-container>

  <v-tooltip text="Create Tv Program">
    <template #activator="{ props }">
      <v-btn
        v-bind="props"
        icon
        size="large"
        color="secondary"
        class="fab-btn"
        elevation="8"
        aria-label="Create Tv Program"
        @click="createDialog = true"
      >
        <v-icon>mdi-plus</v-icon>
      </v-btn>
    </template>
  </v-tooltip>

  <CreateTv v-model="createDialog" @created="onCreated" />

  <PublishTv
    :key="selectedTv?.tvProgramId"
    v-model="publishDialog"
    :tv-item="selectedTv"
    @published="onPublished"
  />

  <UpdateTv v-model="editDialog" :tv-id="selectedTvId" @updated="onUpdated" />

  <v-row justify="center">
    <v-col cols="12" style="max-width: 1600px">
      <v-form ref="form" v-model="isValid">
        <v-card-text>
          <v-row>
            <v-col cols="14" md="3">
              <v-text-field
                style="height: 85px"
                v-model="credentials.name"
                label="Tv Program Title"
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
                    v-model="lookupAccountRequest.name"
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
                label="Tv Program Status"
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

            <v-col cols="12" md="6">
              <v-select
                v-model="credentials.hasRight"
                label="Right Status"
                :items="rightCases"
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
                :loading="tvStore.loading"
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

                  <v-btn icon @click="goToTvSeasons(item.tvProgramId)">
                    <v-icon color="primary">mdi-television-classic</v-icon>
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

                <template v-slot:item.poster="{ item }">
                  <img
                    v-if="posterUrls[item.tvProgramId]"
                    :src="posterUrls[item.tvProgramId]"
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
