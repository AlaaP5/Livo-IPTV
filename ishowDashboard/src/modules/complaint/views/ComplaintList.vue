<script setup lang="ts">
import { reactive, ref } from "vue";
import type { DataTableHeader } from "../../../types/data-table-header.types";
import { createBooleanOptions } from "../../../utils/status-options";
import { useComplaintStore } from "../stores/complaint";
import type { IComplaintResponse } from "../types/complaint-response";
import type { IFilterComplaintRequest } from "../types/filter-request";
import complaintService from "../services/complaintService";
import ShowComplaint from "./ShowComplaint.vue";


const complaintStore = useComplaintStore();

const isValid = ref(true);

const totalItems = ref(0);

const items = ref<IComplaintResponse[]>([]);

const credentials = reactive<IFilterComplaintRequest>({
  page: 1,
  size: 10,
  status: "",
  title: "",
  phoneNumber: ""
});

const headers: DataTableHeader[] = [
  { title: "Complaint ID", key: "id" },
  { title: "Title", key: "title", align: "center" },
  { title: "Description", key: "description", align: "center" },
  { title: "Alternative Phone", key: "alternativePhone", align: "center" },
  { title: "User Name", key: "account.username", align: "center" },
  { title: "Is Read", key: "isRead", align: "center" },
  { title: "Actions", key: "actions", align: "center" },
];

const cases = createBooleanOptions("Show", "Not Show");

const activateDialog = ref(false);
const selectedComplaint = ref<IComplaintResponse | null>(null);


const openShowDialog = (item: IComplaintResponse) => {
  selectedComplaint.value = null;
  setTimeout(() => {
    selectedComplaint.value = item;
    activateDialog.value = true;
  });
};

const onActivated = () => {
  handleFilters();
};

async function handleFilters() {
  const response = await complaintService.filterComplaints(credentials);

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
        <div class="title">Complaint Service</div>
      </v-col>
    </v-row>
  </v-container>

  <ShowComplaint
    :key="selectedComplaint?.id"
    v-model="activateDialog"
    :complaint-item="selectedComplaint"
    @showed="onActivated"
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
                label="Complaint Title"
                variant="outlined"
                clearable
              ></v-text-field>
            </v-col>

            <v-col cols="12" md="2">
              <v-text-field
                style="height: 85px"
                v-model="credentials.phoneNumber"
                label="Phone Number"
                variant="outlined"
                clearable
              ></v-text-field>
            </v-col>

            <v-col cols="12" md="2">
              <v-select
                v-model="credentials.status"
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
                :loading="complaintStore.loading"
                height="500"
                @update:page="updatePage"
                @update:items-per-page="updatePageSize"
              >
                <template #actions="{ item }">
                  <v-btn :disabled="item.isRead" icon @click="openShowDialog(item)">
                    <v-icon :color="item.isRead ? 'red' : 'green'">
                      {{ item.isRead ? "mdi-close" : "mdi-check" }}
                    </v-icon>
                  </v-btn>
                </template>

                <template v-slot:item.isRead="{ item }">
                  <v-chip
                    :color="item.isRead ? 'green' : 'red'"
                    small
                    class="ma-1"
                  >
                    {{ item.isRead ? "Show" : "Not Show" }}
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
