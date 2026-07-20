<script setup lang="ts">
import { onMounted, reactive, ref } from "vue";
import type { DataTableHeader } from "../../../types/data-table-header.types";
import { useKidsStore } from "../stores/kids";
import type { ISectionTvResponse } from "../../tv-program/types/tv-response";
import type { ITvSectionRequest } from "../../tv-program/types/tv-request";
import kidsService from "../services/kidsService";
import AddTvToSection from "./AddTvToSection.vue";
import RemoveSectionTv from "./RemoveSectionTv.vue";
import ChangeStatusTv from "./ChangeStatusTv.vue";
import streamService from "../../stream/services/streamService";


const removeDialog = ref(false);
const addTvDialog = ref(false);
const updateDialog = ref(false);

const kidsStore = useKidsStore();
const sectionName = ref("");

const props = defineProps<{
  sectionId: number;
  sectionName: string;
}>();

const logoUrls = reactive<Record<string, string>>({});

const isValid = ref(true);
const totalItems = ref(0);
const items = ref<ISectionTvResponse[]>([]);
const selectedTv = ref<ISectionTvResponse | null>(null);

const topOptions = [
  { value: "1", title: "Top" },
  { value: "0", title: "Not Top" },
];

const credentials = reactive<ITvSectionRequest>({
  sectionId: null,
  isTop: null,
  page: 1,
  size: 10,
});

onMounted(() => {
  credentials.sectionId = props.sectionId;

  sectionName.value = props.sectionName;

  handleFilters();
});

const headers: DataTableHeader[] = [
  { title: "Tv Program ID", key: "tvProgramId", align: "center" },
  { title: "Title En", key: "titleEn", align: "center" },
  { title: "Title Ar", key: "titleAr", align: "center" },
  { title: "Description En", key: "descriptionEn", align: "center" },
  { title: "Description Ar", key: "descriptionAr", align: "center" },
  { title: "Poster", key: "poster", align: "center" },
  { title: "Badge", key: "badge", align: "center" },
  { title: "Tags", key: "tags", align: "center" },
  { title: "Release Year", key: "releaseYear", align: "center" },
  { title: "Create Date", key: "createDate", align: "center" },
  { title: "Language", key: "language", align: "center" },
  { title: "Section Id", key: "sectionId", align: "center" },
  { title: "Section Order", key: "sectionOrder", align: "center" },
  { title: "Section Title Ar", key: "sectionTitleAr", align: "center" },
  { title: "Section Title En", key: "sectionTitleEn", align: "center" },

  { title: "Season Count", key: "seasonCount", align: "center" },

  { title: "Is Top", key: "isTop", align: "center" },
  { title: "Is Kids", key: "isKids", align: "center" },
  { title: "Is Sport", key: "isSports", align: "center" },
  { title: "Has Right", key: "hasRight", align: "center" },
  { title: "Tv Program Status", key: "active", align: "center" },
  { title: "Is Publish", key: "isPublish", align: "center" },
  { title: "Section Is Publish", key: "sectionPublish", align: "center" },
  { title: "Section Status", key: "sectionActive", align: "center" },
  { title: "Actions", key: "actions", align: "center" },
];

/* ------------------------------- DIALOGS --------------------------------- */

const openRemoveDialog = (item: ISectionTvResponse) => {
  selectedTv.value = null;
  setTimeout(() => {
    selectedTv.value = item;
    removeDialog.value = true;
  });
};

const openChangeDialog = (item: ISectionTvResponse) => {
  selectedTv.value = null;
  setTimeout(() => {
    selectedTv.value = item;
    updateDialog.value = true;
  });
};

const onRemoved = () => handleFilters();
const onAdded = () => handleFilters();
const onChanged = (updatedItem: ISectionTvResponse) => {
  const index = items.value.findIndex(
    (item) =>
      item.tvProgramId === updatedItem.tvProgramId &&
      item.sectionId === updatedItem.sectionId,
  );

  if (index !== -1) {
    items.value[index] = updatedItem;
  }
};

async function handleFilters() {
  const response = await kidsService.viewSectionTvs(credentials);

  items.value = response.data ?? [];
  totalItems.value = response.totalCount;

  await Promise.all(
    items.value.map(async (item) => {
      if (item.poster?.generatedPath) {
        try {
          const imageUrl = await streamService.fetchImage(
            item.poster.generatedPath,
          );
          logoUrls[item.tvProgramId] = imageUrl;
        } catch (error) {
          console.error("Failed to load logo for channel", item.tvProgramId, error);
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
        <div class="title">Tv Programs For Section ({{ sectionName }})</div>
      </v-col>
    </v-row>
  </v-container>

  <v-tooltip text="add Tv Program To Section">
    <template #activator="{ props }">
      <v-btn
        v-bind="props"
        icon
        size="large"
        color="secondary"
        class="fab-btn"
        elevation="8"
        aria-label="add Tv Program To Section"
        @click="addTvDialog = true"
      >
        <v-icon>mdi-plus</v-icon>
      </v-btn>
    </template>
  </v-tooltip>

  <AddTvToSection
    :section-id="credentials.sectionId"
    v-model="addTvDialog"
    @added="onAdded"
  />

  <RemoveSectionTv
    :key="selectedTv?.tvProgramId"
    v-model="removeDialog"
    :section-tv-item="selectedTv"
    @removed="onRemoved"
  />

  <ChangeStatusTv
    :key="selectedTv?.tvProgramId"
    v-model="updateDialog"
    :section-tv-item="selectedTv"
    @updated="onChanged"
  />

  <!-- table -->
  <v-row justify="center">
    <v-col cols="12" style="max-width: 1600px">
      <v-form v-model="isValid">
        <v-card-text>
          <!-- filters -->
          <v-row>
            <v-col cols="12" md="2">
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

            <v-btn
              class="btn"
              :disabled="!isValid"
              color="primary"
              @click="handleFilters"
            >
              Search
            </v-btn>
          </v-row>

          <!-- table -->
          <v-row>
            <v-col cols="12">
              <AppTable
                :headers="headers"
                :items="items"
                :total-items="totalItems"
                :page="credentials.page"
                :items-per-page="credentials.size"
                :loading="kidsStore.loading"
                height="500"
                @update:page="updatePage"
                @update:items-per-page="updatePageSize"
              >
                <!-- actions -->
                <template #actions="{ item }">
                  <v-btn icon @click="openRemoveDialog(item)">
                    <v-icon color="red">mdi-delete</v-icon>
                  </v-btn>

                  <v-btn icon @click="openChangeDialog(item)">
                    <v-icon :color="item.isTop ? 'red' : 'green'">
                      {{ item.isTop ? "mdi-close" : "mdi-check" }}
                    </v-icon>
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
                    v-if="logoUrls[item.tvProgramId]"
                    :src="logoUrls[item.tvProgramId]"
                    width="80"
                    height="80"
                    style="object-fit: cover; border-radius: 8px"
                  />
                  <v-progress-circular
                    v-else-if="kidsStore.loading"
                    indeterminate
                    size="30"
                    width="2"
                    color="primary"
                  />
                  <span v-else>No Logo</span>
                </template>

                <template #item.active="{ item }">
                  <v-chip :color="item.active ? 'green' : 'red'" small>
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
                    :color="item.isTop ? 'green' : 'red'"
                    small
                    class="ma-1"
                  >
                    {{ item.isTop ? "Yes" : "No" }}
                  </v-chip>
                </template>

                <template v-slot:item.sectionActive="{ item }">
                  <v-chip
                    :color="item.sectionActive ? 'green' : 'red'"
                    small
                    class="ma-1"
                  >
                    {{ item.sectionActive ? "Yes" : "No" }}
                  </v-chip>
                </template>

                <template v-slot:item.sectionPublish="{ item }">
                  <v-chip
                    :color="item.sectionPublish ? 'green' : 'red'"
                    small
                    class="ma-1"
                  >
                    {{ item.sectionPublish ? "Yes" : "No" }}
                  </v-chip>
                </template>

                <template v-slot:item.isTop="{ item }">
                  <v-chip
                    :color="item.isTop ? 'green' : 'red'"
                    small
                    class="ma-1"
                  >
                    {{ item.isTop ? "Yes" : "No" }}
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
