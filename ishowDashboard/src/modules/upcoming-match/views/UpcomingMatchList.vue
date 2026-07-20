<script setup lang="ts">
import { reactive, ref } from "vue";
import type { DataTableHeader } from "../../../types/data-table-header.types";
import { createBooleanOptions } from "../../../utils/status-options";
import { useUpcomingMatchStore } from "../stores/upcoming-match";
import type { IUpcomingMatchResponse } from "../types/upcomingMatch-response";
import type { IFilterUpcomingMatchRequest } from "../types/filter-request";
import upcomingMatchService from "../services/upcomingMatchService";
import CreateUpcomingMatch from "./CreateUpcomingMatch.vue";
import UpdateUpcomingMatch from "./UpdateUpcomingMatch.vue";
import ActivateUpcomingMatch from "./ActivateUpcomingMatch.vue";
import { rules } from "../../../composables/validations/rules";

const upcomingStore = useUpcomingMatchStore();

const isValid = ref(true);

const totalItems = ref(0);

const items = ref<IUpcomingMatchResponse[]>([]);

const credentials = reactive<IFilterUpcomingMatchRequest>({
  page: 1,
  size: 10,
  active: "",
  search: "",
});

const headers: DataTableHeader[] = [
  { title: "Upcoming Match ID", key: "id" },
  { title: "Home team name", key: "homeTeam.nameEn", align: "center" },
  { title: "Away team name", key: "awayTeam.nameEn", align: "center" },
  { title: "Champion name", key: "champion.nameEn", align: "center" },
  { title: "Channel name", key: "channel.titleEn", align: "center" },
  { title: "Match date", key: "matchDate", align: "center" },
  { title: "Status", key: "active", align: "center" },
  { title: "Actions", key: "actions", align: "center" },
];

const cases = createBooleanOptions("Active", "Not Active");

const createDialog = ref(false);
const activateDialog = ref(false);
const editDialog = ref(false);
const selectedUpcomingMatch = ref<IUpcomingMatchResponse | null>(null);

const openEditDialog = (item: IUpcomingMatchResponse) => {
  selectedUpcomingMatch.value = item;
  editDialog.value = true;
};

const openActivateDialog = (item: IUpcomingMatchResponse) => {
  selectedUpcomingMatch.value = null;
  setTimeout(() => {
    selectedUpcomingMatch.value = item;
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
  const response = await upcomingMatchService.filterUpcomingMatch(credentials);

  console.log(response);
  items.value = response.data ?? [];
  totalItems.value = response.totalCount;
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
        <div class="title">Upcoming Match Service</div>
      </v-col>
    </v-row>
  </v-container>

  <v-tooltip text="Create Upcoming Match">
    <template #activator="{ props }">
      <v-btn
        v-bind="props"
        icon
        size="large"
        color="secondary"
        class="fab-btn"
        elevation="8"
        aria-label="Create Upcoming Match"
        @click="createDialog = true"
      >
        <v-icon>mdi-plus</v-icon>
      </v-btn>
    </template>
  </v-tooltip>

  <CreateUpcomingMatch v-model="createDialog" @created="onCreated" />

  <UpdateUpcomingMatch
    v-model="editDialog"
    :upcoming-item="selectedUpcomingMatch"
    @updated="onUpdated"
  />

  <ActivateUpcomingMatch
    :key="selectedUpcomingMatch?.id"
    v-model="activateDialog"
    :upcoming-item="selectedUpcomingMatch"
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
                label="Upcoming Match Name"
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
                :rules="[rules.required]"
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
                :loading="upcomingStore.loading"
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
