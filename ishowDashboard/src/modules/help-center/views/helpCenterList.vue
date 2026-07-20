<script setup lang="ts">
import { onMounted, ref } from "vue";
import type { DataTableHeader } from "../../../types/data-table-header.types";
import { createBooleanOptions } from "../../../utils/status-options";
import { useHelpCenterStore } from "../stores/helpCenter";
import helpCenterService from "../services/helpCenterService";
import type { IHelpCenterMeta } from "../../../types/metadata";
import type { IHelpCenterRequest } from "../types/helpCenter-request";
import AddHelpCenterDialog from "./AddHelpCenterDialog.vue";

const helpCenterStore = useHelpCenterStore();

const isValid = ref(true);

const totalItems = ref(0);

const items = ref<IHelpCenterMeta[]>([]);

const formData = ref<IHelpCenterRequest>({
  helpCenterMetas: [],
});

const showAddDialog = ref(false);
const showDeleteDialog = ref(false);
const selectedItem = ref<IHelpCenterMeta | null>(null);

const headers: DataTableHeader[] = [
  { title: "Question Content", key: "question" },
  { title: "Answer Content", key: "answer", align: "center" },
  { title: "Order", key: "order", align: "center" },
  { title: "Actions", key: "actions", align: "center" },
];

async function addNewItem(newItem: IHelpCenterMeta) {
  formData.value.helpCenterMetas = [...items.value, newItem];

  const response = await helpCenterService.updateHelpCenters(formData.value);

  items.value =
    response.data?.helpCenterMetas ?? formData.value.helpCenterMetas;
  totalItems.value = items.value.length;
}

function openDeleteDialog(item: IHelpCenterMeta) {
  selectedItem.value = item;
  showDeleteDialog.value = true;
}

async function confirmDelete() {
  if (!selectedItem.value) return;

  const updatedItems = items.value.filter(
    (item) => item !== selectedItem.value,
  );

  const reorderedItems = updatedItems.map((item, index) => ({
    ...item,
    order: index + 1,
  }));

  formData.value.helpCenterMetas = reorderedItems;

  const response = await helpCenterService.updateHelpCenters(formData.value);

  if (response.code === "SUCCESS_200") {
    items.value = response.data?.helpCenterMetas ?? reorderedItems;
    totalItems.value = items.value.length;
    console.log("FAQ deleted successfully!");
  } else {
    console.error("Failed to delete FAQ");
  }

  showDeleteDialog.value = false;
  selectedItem.value = null;
}

function cancelDelete() {
  showDeleteDialog.value = false;
  selectedItem.value = null;
}

onMounted(() => {
  handleFilters();
});

async function handleFilters() {
  const response = await helpCenterService.filterHelpCenters();

  console.log(response);
  items.value = response.data?.helpCenterMetas ?? [];
  totalItems.value = response.totalCount;
}
</script>

<template>
  <v-container>
    <v-row justify="center">
      <v-col cols="12" style="max-width: 900px">
        <div class="title">Help Center Service</div>
      </v-col>
    </v-row>
  </v-container>

  <v-row justify="center">
    <v-col cols="12" style="max-width: 1600px">
      <v-form ref="form" v-model="isValid">
        <v-card-text>
          <v-row>
            <v-btn
              class="btn"
              :disabled="!isValid"
              color="primary"
              @click="handleFilters"
            >
              Search
            </v-btn>

            <v-btn
              class="btn ml-2"
              color="success"
              @click="showAddDialog = true"
            >
              <v-icon left>mdi-plus</v-icon>
              Add FAQ
            </v-btn>
          </v-row>

          <v-row>
            <v-col cols="12">
              <AppTable
                :headers="headers"
                :items="items"
                :total-items="totalItems"
                :loading="helpCenterStore.loading"
                height="500"
              >
                <template #actions="{ item }">
                  <v-btn
                    icon
                    color="error"
                    size="small"
                    @click="openDeleteDialog(item)"
                    title="Delete FAQ"
                  >
                    <v-icon>mdi-delete</v-icon>
                  </v-btn>
                </template>
              </AppTable>
            </v-col>
          </v-row>
        </v-card-text>
      </v-form>
    </v-col>
  </v-row>

  <AddHelpCenterDialog v-model="showAddDialog" @add="addNewItem" />

  <v-dialog v-model="showDeleteDialog" max-width="500px" persistent>
    <v-card prepend-icon="mdi-delete" title="Delete FAQ">

      <v-card-text class="pt-4">
        <div class="text-body-1 mb-4">
          Are you sure you want to delete this FAQ?
        </div>

        <div class="grey lighten-4 pa-3 rounded">
          <div class="font-weight-bold mb-2">Question:</div>
          <div class="mb-3">{{ selectedItem?.question }}</div>

          <div class="font-weight-bold mb-2">Answer:</div>
          <div class="mb-3">{{ selectedItem?.answer }}</div>

          <div class="font-weight-bold mb-2">Order:</div>
          <div>
            <v-chip small color="primary">{{ selectedItem?.order }}</v-chip>
          </div>
        </div>
      </v-card-text>

      <v-card-actions class="pa-4">
        <v-spacer></v-spacer>
        <v-btn color="grey darken-1" variant="text" @click="cancelDelete">
          Cancel
        </v-btn>
        <v-btn
          color="error"
          variant="flat"
          @click="confirmDelete"
          :loading="helpCenterStore.loading"
        >
          <v-icon left size="18">mdi-delete</v-icon>
          Delete
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
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
