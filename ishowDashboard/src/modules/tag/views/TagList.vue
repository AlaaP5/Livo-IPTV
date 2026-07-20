<script setup lang="ts">
import { reactive, ref } from "vue";
import type { DataTableHeader } from "../../../types/data-table-header.types";
import { useTagStore } from "../stores/tag";
import type { ITagResponse } from "../types/tag-response";
import type { IFilterTagRequest } from "../types/filter-request";
import tagService from "../services/tagService";
import CreateTag from "./CreateTag.vue";
import UpdateTag from "./UpdateTag.vue";
import ActivateTag from "./ActivateTag.vue";
import { createBooleanOptions } from "../../../utils/status-options";


const tagStore = useTagStore();

const isValid = ref(true);

const totalItems = ref(0);

const items = ref<ITagResponse[]>([]);

const credentials = reactive<IFilterTagRequest>({
  page: 1,
  size: 10,
  active: "",
  title: "",
});

const headers: DataTableHeader[] = [
  { title: "Tag ID", key: "id" },
  { title: "Title En", key: "titleEn", align: "center" },
  { title: "Title Ar", key: "titleAr", align: "center" },
  { title: "Status", key: "active", align: "center" },
  { title: "Actions", key: "actions", align: "center" },
];

const cases = createBooleanOptions("Active", "Not Active");


const createDialog = ref(false);
const activateDialog = ref(false);
const editDialog = ref(false);
const selectedTag = ref<ITagResponse | null>(null);

const openEditDialog = (item: ITagResponse) => {
  selectedTag.value = item;
  editDialog.value = true;
};

const openActivateDialog = (item: ITagResponse) => {
  selectedTag.value = null; 
  setTimeout(() => {
    selectedTag.value = item;
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
  const response = await tagService.filterTags(credentials);

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
        <div class="title">Tag Service</div>
      </v-col>
    </v-row>
  </v-container>

  <v-tooltip text="Create Tag">
    <template #activator="{ props }">
      <v-btn
        v-bind="props"
        icon
        size="large"
        color="secondary"
        class="fab-btn"
        elevation="8"
        aria-label="Create Tag"
        @click="createDialog = true"
      >
        <v-icon>mdi-plus</v-icon>
      </v-btn>
    </template>
  </v-tooltip>

  <CreateTag v-model="createDialog" @created="onCreated" />

  <UpdateTag
    v-model="editDialog"
    :tag-item="selectedTag"
    @updated="onUpdated"
  />

  <ActivateTag
    :key="selectedTag?.id"
    v-model="activateDialog"
    :tag-item="selectedTag"
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
                v-model="credentials.title"
                label="Tag Title"
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
                :loading="tagStore.loading"
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
