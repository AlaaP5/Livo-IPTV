<script setup lang="ts">
import { reactive, ref } from "vue";
import type { DataTableHeader } from "../../../types/data-table-header.types";
import { useAdStore } from "../stores/ad";
import type { IAdResponse } from "../types/ad-response";
import type { IFilterAdRequest } from "../types/filter-request";
import adService from "../services/adService";
import CreateAd from "./CreateAd.vue";
import UpdateAd from "./UpdateAd.vue";
import ActivateAd from "./ActivateAd.vue";
import { createBooleanOptions } from "../../../utils/status-options";
import streamService from "../../stream/services/streamService";

const adStore = useAdStore();

const isValid = ref(true);

const totalItems = ref(0);

const imageUrls = reactive<Record<string, string>>({});

const items = ref<IAdResponse[]>([]);

const credentials = reactive<IFilterAdRequest>({
  page: 1,
  size: 10,
  active: "",
  startDate: "",
  endDate: "",
});

const headers: DataTableHeader[] = [
  { title: "Ad ID", key: "id" },
  { title: "Deep Link", key: "deepLink", align: "center" },
  { title: "Image", key: "imagePath", align: "center" },
  { title: "Start Date", key: "startDate", align: "center" },
  { title: "End Date", key: "endDate", align: "center" },
  { title: "Status", key: "active", align: "center" },
  { title: "Actions", key: "actions", align: "center" },
];

const cases = createBooleanOptions("Active", "Not Active");

const createDialog = ref(false);
const activateDialog = ref(false);
const editDialog = ref(false);
const selectedAd = ref<IAdResponse | null>(null);

const openEditDialog = (item: IAdResponse) => {
  selectedAd.value = item;
  editDialog.value = true;
};

const openActivateDialog = (item: IAdResponse) => {
  selectedAd.value = null;
  setTimeout(() => {
    selectedAd.value = item;
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
  const response = await adService.filterAds(credentials);

  console.log(response);
  items.value = response.data ?? [];
  totalItems.value = response.totalCount;

  await Promise.all(
    items.value.map(async (item) => {
      if (item.imagePath) {
        try {
          const imageUrl = await streamService.fetchImage(item.imagePath);
          imageUrls[item.id] = imageUrl;
        } catch (error) {
          console.error("Failed to load image for ad", item.id, error);
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
        <div class="title">Ad Service</div>
      </v-col>
    </v-row>
  </v-container>

  <v-tooltip text="Create Ad">
    <template #activator="{ props }">
      <v-btn
        v-bind="props"
        icon
        size="large"
        color="secondary"
        class="fab-btn"
        elevation="8"
        aria-label="Create Ad"
        @click="createDialog = true"
      >
        <v-icon>mdi-plus</v-icon>
      </v-btn>
    </template>
  </v-tooltip>

  <CreateAd v-model="createDialog" @created="onCreated" />

  <UpdateAd v-model="editDialog" :ad-item="selectedAd" @updated="onUpdated" />

  <ActivateAd
    :key="selectedAd?.id"
    v-model="activateDialog"
    :ad-item="selectedAd"
    @activated="onActivated"
  />

  <v-row justify="center">
    <v-col cols="12" style="max-width: 1600px">
      <v-form ref="form" v-model="isValid">
        <v-card-text>
          <v-row>
            <v-col cols="12" md="3">
              <v-text-field
                v-model="credentials.startDate"
                label="Start Date"
                type="date"
                variant="outlined"
              />
            </v-col>

            <v-col cols="12" md="3">
              <v-text-field
                v-model="credentials.endDate"
                label="End Date"
                type="date"
                variant="outlined"
              />
            </v-col>

            <v-col cols="12" md="3">
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
                :loading="adStore.loading"
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

                <template v-slot:item.imagePath="{ item }">
                  <img
                    v-if="imageUrls[item.id]"
                    :src="imageUrls[item.id]"
                    width="120"
                    height="80"
                    style="object-fit: cover; border-radius: 8px"
                  />
                  <v-progress-circular
                    v-else-if="adStore.loading"
                    indeterminate
                    size="30"
                    width="2"
                    color="primary"
                  />
                  <span v-else>No Image</span>
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
