<script setup lang="ts">
import { reactive, ref } from "vue";
import type { DataTableHeader } from "../../../types/data-table-header.types";
import { createBooleanOptions } from "../../../utils/status-options";
import { useChampionStore } from "../stores/champion";
import type { IChampionResponse } from "../types/champion-response";
import type { IFilterChampionRequest } from "../types/filter-request";
import championService from "../services/championService";
import CreateChampion from "./CreateChampion.vue";
import UpdateChampion from "./UpdateChampion.vue";
import ActivateChampion from "./ActivateChampion.vue";
import streamService from "../../stream/services/streamService";

const championStore = useChampionStore();

const isValid = ref(true);

const totalItems = ref(0);

const items = ref<IChampionResponse[]>([]);

const logoUrls = reactive<Record<number, string>>({});

const credentials = reactive<IFilterChampionRequest>({
  page: 1,
  size: 10,
  active: "",
  search: "",
});

const headers: DataTableHeader[] = [
  { title: "Champion ID", key: "id" },
  { title: "Name En", key: "nameEn", align: "center" },
  { title: "Name Ar", key: "nameAr", align: "center" },
  { title: "Logo", key: "logoPath", align: "center" },
  { title: "Status", key: "active", align: "center" },
  { title: "Actions", key: "actions", align: "center" },
];

const cases = createBooleanOptions("Active", "Not Active");

const createDialog = ref(false);
const activateDialog = ref(false);
const editDialog = ref(false);
const selectedChampion = ref<IChampionResponse | null>(null);

const openEditDialog = (item: IChampionResponse) => {
  selectedChampion.value = item;
  editDialog.value = true;
};

const openActivateDialog = (item: IChampionResponse) => {
  selectedChampion.value = null;
  setTimeout(() => {
    selectedChampion.value = item;
    activateDialog.value = true;
  });
};

const onCreated = () => {
  handleFilters();
};

const onUpdated = () => {
  handleFilters();
};

const onActivated = () => {
  handleFilters();
};

async function handleFilters() {
  const response = await championService.filterChampions(credentials);

  console.log(response);
  items.value = response.data ?? [];
  totalItems.value = response.totalCount;

  await Promise.all(
    items.value.map(async (item) => {
      if (item.logoPath) {
        try {
          const imageUrl = await streamService.fetchImage(item.logoPath);
          logoUrls[item.id] = imageUrl;
        } catch (error) {
          console.error("Failed to load logo for champion", item.id, error);
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
        <div class="title">Champion Service</div>
      </v-col>
    </v-row>
  </v-container>

  <v-tooltip text="Create Champion">
    <template #activator="{ props }">
      <v-btn
        v-bind="props"
        icon
        size="large"
        color="secondary"
        class="fab-btn"
        elevation="8"
        aria-label="Create Champion"
        @click="createDialog = true"
      >
        <v-icon>mdi-plus</v-icon>
      </v-btn>
    </template>
  </v-tooltip>

  <CreateChampion v-model="createDialog" @created="onCreated" />

  <UpdateChampion
    v-model="editDialog"
    :champion-item="selectedChampion"
    @updated="onUpdated"
  />

  <ActivateChampion
    :key="selectedChampion?.id"
    v-model="activateDialog"
    :champion-item="selectedChampion"
    @activated="onActivated"
  />

  <v-row justify="center">
    <v-col cols="12" style="max-width: 1600px">
      <v-form ref="form" v-model="isValid">
        <v-card-text>
          <v-row>
            <v-col cols="12" md="2">
              <v-text-field
                style="height: 85px"
                v-model="credentials.search"
                label="Champion Name"
                variant="outlined"
                clearable
              ></v-text-field>
            </v-col>

            <v-col cols="12" md="2">
              <v-select
                v-model="credentials.active"
                label="Status"
                :items="cases"
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
                :loading="championStore.loading"
                height="500"
                @update:page="updatePage"
                @update:items-per-page="updatePageSize"
              >
                <template #actions="{ item }">
                  <v-btn icon @click="openActivateDialog(item)">
                    <v-icon :color="item.active ? 'red' : 'green'">
                      {{ item.active ? "mdi-close" : "mdi-check" }}
                    </v-icon>
                  </v-btn>

                  <v-btn icon @click="openEditDialog(item)">
                    <v-icon color="secondary">mdi-pencil</v-icon>
                  </v-btn>
                </template>

                <template v-slot:item.logoPath="{ item }">
                  <img
                    v-if="logoUrls[item.id]"
                    :src="logoUrls[item.id]"
                    width="80"
                    height="80"
                    style="object-fit: cover; border-radius: 50%"
                  />
                  <v-progress-circular
                    v-else-if="championStore.loading"
                    indeterminate
                    size="30"
                    width="2"
                    color="primary"
                  />
                  <span v-else>No Logo</span>
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
.fab-btn {
  position: fixed;
  bottom: 40px;
  right: 40px;
  z-index: 1000;
  border-radius: 50%;
}
</style>
