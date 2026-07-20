<script setup lang="ts">
import { ref, onMounted, reactive } from "vue";
import type { DataTableHeader } from "../../../types/data-table-header.types";
import { useKidsStore } from "../stores/kids";
import kidsService from "../services/kidsService";
import type { IKidsBannerResponse } from "../types/kids-response";
import AddBanner from "./AddBanner.vue";
import streamService from "../../stream/services/streamService";
import RemoveBanner from "./RemoveBanner.vue";


const kidsStore = useKidsStore();

const isValid = ref(true);

const items = ref<IKidsBannerResponse[]>([]);
const totalItems = ref(0);

const addBannerDialog = ref(false);
const removeDialog = ref(false);

const selectedBanner = ref<IKidsBannerResponse | null>(null);

const posterUrls = reactive<Record<string, string>>({});

const headers: DataTableHeader[] = [
  {
    title: "Content ID",
    key: "contentId",
    align: "center",
  },
  {
    title: "English Title",
    key: "contentEnglishTitle",
    align: "center",
  },
  {
    title: "Arabic Title",
    key: "contentArabicTitle",
    align: "center",
  },
  {
    title: "Content Type",
    key: "contentType",
    align: "center",
  },
  {
    title: "Poster",
    key: "poster",
    align: "center",
  },
  {
    title: "Kids",
    key: "isKids",
    align: "center",
  },
  {
    title: "Create Date",
    key: "createAt",
    align: "center",
  },
  {
    title: "Actions",
    key: "actions",
    align: "center",
  },
];

onMounted(() => {
  handleFilters();
});

const openRemoveDialog = (item: IKidsBannerResponse) => {
  selectedBanner.value = null;

  setTimeout(() => {
    selectedBanner.value = item;
    removeDialog.value = true;
  });
};

const onRemoved = () => {
  handleFilters();
};

const onAdded = () => {
  handleFilters();
};

async function handleFilters() {
  const response = await kidsService.getBannerList();

  items.value = response.data ?? [];
  totalItems.value = response.totalCount ?? 0;

  await Promise.all(
    items.value.map(async (item) => {
      if (item.poster) {
        try {
          const imageUrl = await streamService.fetchImage(
            item.poster,
          );
          posterUrls[item.contentId] = imageUrl;
        } catch (error) {
          console.error("Failed to load poster for clip", item.contentId, error);
        }
      }
    }),
  );
}
</script>

<template>
  <v-container>
    <v-row justify="center">
      <v-col cols="12" style="max-width: 900px">
        <div class="title">Kids Banner List</div>
      </v-col>
    </v-row>
  </v-container>

  <!-- Add Banner -->

  <v-tooltip text="Add Banner">
    <template #activator="{ props }">
      <v-btn
        v-bind="props"
        icon
        size="large"
        color="secondary"
        class="fab-btn"
        elevation="8"
        @click="addBannerDialog = true"
      >
        <v-icon> mdi-plus </v-icon>
      </v-btn>
    </template>
  </v-tooltip>

  <AddBanner v-model="addBannerDialog" @added="onAdded" />

  <RemoveBanner
    :key="selectedBanner?.contentId"
    v-model="removeDialog"
    :banner-item="selectedBanner"
    @removed="onRemoved"
  />

  <v-row justify="center">
    <v-col cols="12" style="max-width: 1600px">
      <v-form v-model="isValid">
        <v-card-text>
          <!-- Filters -->

          <v-row>
            <v-btn
              class="btn"
              color="primary"
              :disabled="!isValid"
              @click="handleFilters"
            >
              Search
            </v-btn>
          </v-row>

          <!-- Table -->

          <v-row>
            <v-col cols="12">
              <AppTable
                :headers="headers"
                :items="items"
                :total-items="totalItems"
                :loading="kidsStore.loading"
              >
                <template v-slot:item.poster="{ item }">
                  <img
                    v-if="posterUrls[item.contentId]"
                    :src="posterUrls[item.contentId]"
                    width="100"
                    height="60"
                    style="object-fit: cover; border-radius: 8px"
                  />
                  <v-progress-circular
                    v-else-if="kidsStore.loading"
                    indeterminate
                    size="30"
                    width="2"
                    color="primary"
                  />
                  <span v-else>No Poster</span>
                </template>

                <template #item.contentType="{ item }">
                  <v-chip color="primary" small>
                    {{ item.contentType }}
                  </v-chip>
                </template>

                <template #item.isKids="{ item }">
                  <v-chip :color="item.isKids ? 'green' : 'red'" small>
                    {{ item.isKids ? "Yes" : "No" }}
                  </v-chip>
                </template>

                <template #actions="{ item }">
                  <v-btn icon @click="openRemoveDialog(item)">
                    <v-icon color="red"> mdi-delete </v-icon>
                  </v-btn>
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
