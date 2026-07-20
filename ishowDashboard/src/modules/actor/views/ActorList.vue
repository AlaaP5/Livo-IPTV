<script setup lang="ts">
import { reactive, ref } from "vue";
import type { DataTableHeader } from "../../../types/data-table-header.types";
import { useActorStore } from "../stores/actor";
import type { IActorResponse } from "../types/actor-response";
import type { IFilterActorRequest } from "../types/filter-request";
import actorService from "../services/actorService";
import CreateActor from "./CreateActor.vue";
import UpdateActor from "./UpdateActor.vue";
import ActivateActor from "./ActivateActor.vue";
import { createBooleanOptions } from "../../../utils/status-options";
import streamService from "../../stream/services/streamService";

const actorStore = useActorStore();

const isValid = ref(true);

const totalItems = ref(0);

const imageUrls = reactive<Record<string, string>>({});

const items = ref<IActorResponse[]>([]);

const credentials = reactive<IFilterActorRequest>({
  page: 1,
  size: 10,
  active: "",
  search: "",
});

const headers: DataTableHeader[] = [
  { title: "Actor ID", key: "id" },
  { title: "Name En", key: "nameEn", align: "center" },
  { title: "Name Ar", key: "nameAr", align: "center" },
  { title: "Image", key: "imagePath", align: "center" },
  { title: "Status", key: "active", align: "center" },
  { title: "Actions", key: "actions", align: "center" },
];

const cases = createBooleanOptions("Active", "Not Active");

const createDialog = ref(false);
const activateDialog = ref(false);
const editDialog = ref(false);
const selectedActor = ref<IActorResponse | null>(null);

const openEditDialog = (item: IActorResponse) => {
  selectedActor.value = item;
  editDialog.value = true;
};

const openActivateDialog = (item: IActorResponse) => {
  selectedActor.value = null;
  setTimeout(() => {
    selectedActor.value = item;
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
  const response = await actorService.filterActors(credentials);

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
          console.error("Failed to load image", error);
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
        <div class="title">Actor Service</div>
      </v-col>
    </v-row>
  </v-container>

  <v-tooltip text="Create Actor">
    <template #activator="{ props }">
      <v-btn
        v-bind="props"
        icon
        size="large"
        color="secondary"
        class="fab-btn"
        elevation="8"
        aria-label="Create Actor"
        @click="createDialog = true"
      >
        <v-icon>mdi-plus</v-icon>
      </v-btn>
    </template>
  </v-tooltip>

  <CreateActor v-model="createDialog" @created="onCreated" />

  <UpdateActor
    v-model="editDialog"
    :actor-item="selectedActor"
    @updated="onUpdated"
  />

  <ActivateActor
    :key="selectedActor?.id"
    v-model="activateDialog"
    :actor-item="selectedActor"
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
                label="Actor Name"
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
                :loading="actorStore.loading"
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

                  <span v-else>Loading...</span>
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
