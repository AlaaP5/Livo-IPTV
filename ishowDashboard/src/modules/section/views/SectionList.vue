<script setup lang="ts">
import { reactive, ref } from "vue";
import type { DataTableHeader } from "../../../types/data-table-header.types";
import { useSectionStore } from "../stores/section";
import type { ISectionResponse } from "../types/section-response";
import type { IFilterSectionRequest } from "../types/filter-request";
import sectionService from "../services/sectionService";
import { mobilePages } from "../../../enums/MobilePageEnum";
import { contentTypes } from "../../../enums/ContentTypeEnum";
import { createBooleanOptions } from "../../../utils/status-options";

const sectionStore = useSectionStore();

const isValid = ref(true);

const totalItems = ref(0);

const items = ref<ISectionResponse[]>([]);

const credentials = reactive<IFilterSectionRequest>({
  page: 1,
  size: 10,
  active: "",
  publish: "",
  title: "",
  contentType: null,
  sectionPage: null,
});


const headers: DataTableHeader[] = [
  { title: "Section ID", key: "id" },
  { title: "Title En", key: "titleEn", align: "center" },
  { title: "Title Ar", key: "titleAr", align: "center" },
  { title: "Order", key: "order", align: "center" },
  { title: "Publish", key: "publish", align: "center" },
  { title: "Mobile Page", key: "page", align: "center" },
  { title: "Content Type", key: "contentType", align: "center" },
  { title: "Status", key: "active", align: "center" },
];

const cases = createBooleanOptions("Active", "Not Active");

const publishCases = createBooleanOptions("Publish", "Not Publish");

const pages = mobilePages;

const types = contentTypes;

async function handleFilters() {
  const response = await sectionService.filterSections(credentials);

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
        <div class="title">Section Service</div>
      </v-col>
    </v-row>
  </v-container>

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

            <v-col cols="12" md="2">
              <v-select
                v-model="credentials.sectionPage"
                label="Section Page"
                :items="pages"
                item-value="value"
                item-title="title"
                variant="outlined"
                outlined
                clearable
              ></v-select>
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
                :loading="sectionStore.loading"
                height="500"
                @update:page="updatePage"
                @update:items-per-page="updatePageSize"
              >

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
