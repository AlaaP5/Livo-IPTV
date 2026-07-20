<script setup lang="ts">
import { reactive, ref } from "vue";
import type { DataTableHeader } from "../../../types/data-table-header.types";
import { MobilePage } from "../../../enums/MobilePageEnum";
import type { ISectionResponse } from "../../section/types/section-response";
import type { IFilterSectionRequest } from "../../section/types/filter-request";
import sectionService from "../../section/services/sectionService";
import router from "../../../router";
import CreateSection from "../../section/views/CreateSection.vue";
import { createBooleanOptions } from "../../../utils/status-options";
import ActivateSection from "../../section/views/ActivateSection.vue";
import UpdateSection from "../../section/views/UpdateSection.vue";
import PublishSection from "../../section/views/PublishSection.vue";
import { contentTypes } from "../../../enums/ContentTypeEnum";
import { useHomeStore } from "../stores/home";

const homeStore = useHomeStore();

const isValid = ref(true);

const totalItems = ref(0);

const items = ref<ISectionResponse[]>([]);

const types = contentTypes;

const createDialog = ref(false);
const activateDialog = ref(false);
const editDialog = ref(false);
const publishDialog = ref(false);
const selectedSection = ref<ISectionResponse | null>(null);

const credentials = reactive<IFilterSectionRequest>({
  page: 1,
  size: 10,
  active: "",
  publish: "",
  title: "",
  contentType: null,
  sectionPage: MobilePage.HOME,
});

const headers: DataTableHeader[] = [
  { title: "Section ID", key: "id" },
  { title: "Title En", key: "titleEn", align: "center" },
  { title: "Title Ar", key: "titleAr", align: "center" },
  { title: "Content Type", key: "contentType", align: "center" },
  { title: "Order", key: "order", align: "center" },
  { title: "Is Publish", key: "publish", align: "center" },
  { title: "Section Status", key: "active", align: "center" },
  { title: "Actions", key: "actions", align: "center" },
];

const cases = createBooleanOptions("Active", "Not Active");

const publishCases = createBooleanOptions("Publish", "Not Publish");

function goToViewSectionData(item: ISectionResponse) {
  const contentType = item.contentType?.toLowerCase();

  if (contentType === "movies") {
    router.push({
      name: "View Movies For Home Section",
      params: {
        sectionId: item.id,
        sectionName: item.titleEn,
      },
    });
  } else if (contentType === "series") {
    router.push({
      name: "View Series For Home Section",
      params: {
        sectionId: item.id,
        sectionName: item.titleEn,
      },
    });
  } else if (contentType === "clips") {
    router.push({
      name: "View Clips For Home Section",
      params: {
        sectionId: item.id,
        sectionName: item.titleEn,
      },
    });
  } else if (contentType === "channels") {
    router.push({
      name: "View Channels For Home Section",
      params: {
        sectionId: item.id,
        sectionName: item.titleEn,
      },
    });
  } else if (contentType === "tv_programs") {
    router.push({
      name: "View Tv Programs For Home Section",
      params: {
        sectionId: item.id,
        sectionName: item.titleEn,
      },
    });
  }
}

function getViewButtonText(contentType: string): string {
  const type = contentType?.toLowerCase();
  if (type === "movies") return "view movies";
  if (type === "series") return "view series";
  if (type === "clips") return "view clips";
  if (type === "channels") return "view channels";
  if (type === "tv_programs") return "view programs";
  return "view content";
}

const openEditDialog = (item: ISectionResponse) => {
  selectedSection.value = item;
  editDialog.value = true;
};

const openActivateDialog = (item: ISectionResponse) => {
  selectedSection.value = null;
  setTimeout(() => {
    selectedSection.value = item;
    activateDialog.value = true;
  });
};

const openPublishDialog = (item: ISectionResponse) => {
  selectedSection.value = null;
  setTimeout(() => {
    selectedSection.value = item;
    publishDialog.value = true;
  });
};

const onCreated = () => {
  handleFilters();
};

const onUpdated = () => {
  handleFilters();
};

const onPublished = () => {
  handleFilters();
};

const onActivated = () => {
  handleFilters();
};

async function handleFilters() {
  const response = await sectionService.filterSections(credentials);

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
        <div class="title">Home Sections</div>
      </v-col>
    </v-row>
  </v-container>

  <v-tooltip text="Create Section">
    <template #activator="{ props }">
      <v-btn
        v-bind="props"
        icon
        size="large"
        color="secondary"
        class="fab-btn"
        elevation="8"
        aria-label="Create Section"
        @click="createDialog = true"
      >
        <v-icon>mdi-plus</v-icon>
      </v-btn>
    </template>
  </v-tooltip>

  <CreateSection
    v-model="createDialog"
    :mobile-page="MobilePage.HOME"
    @created="onCreated"
  />

  <ActivateSection
    :key="selectedSection?.id"
    v-model="activateDialog"
    :section-item="selectedSection"
    @activated="onActivated"
  />

  <UpdateSection
    v-model="editDialog"
    :section-item="selectedSection"
    @updated="onUpdated"
  />

  <PublishSection
    :key="selectedSection?.id"
    v-model="publishDialog"
    :section-item="selectedSection"
    @published="onPublished"
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
                label="Section Title"
                variant="outlined"
                clearable
              ></v-text-field>
            </v-col>

            <v-col cols="12" md="2">
              <v-select
                v-model="credentials.contentType"
                label="Content Type"
                :items="types"
                item-value="value"
                item-title="title"
                variant="outlined"
                outlined
                clearable
              ></v-select>
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

            <v-col cols="12" md="2">
              <v-select
                v-model="credentials.publish"
                label="Publish Status"
                :items="publishCases"
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

          <v-row>
            <v-col cols="12">
              <AppTable
                :headers="headers"
                :items="items"
                :total-items="totalItems"
                :page="credentials.page"
                :items-per-page="credentials.size"
                :loading="homeStore.loading"
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

                  <v-btn
                    :disabled="item.publish"
                    icon
                    @click="openPublishDialog(item)"
                  >
                    <v-icon>mdi-publish</v-icon>
                  </v-btn>

                  <v-btn
                    :disabled="!item.publish || !item.active"
                    color="primary"
                    class="ml-2"
                    style="width: 50%"
                    @click="goToViewSectionData(item)"
                  >
                    {{ getViewButtonText(item.contentType) }}
                  </v-btn>
                </template>

                <template v-slot:item.publish="{ item }">
                  <v-chip
                    :color="item.publish ? 'green' : 'red'"
                    small
                    class="ma-1"
                  >
                    {{ item.publish ? "Publish" : "Not Publish" }}
                  </v-chip>
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
