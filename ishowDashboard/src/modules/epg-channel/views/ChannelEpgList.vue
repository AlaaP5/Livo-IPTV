<script setup lang="ts">
import { reactive, ref } from "vue";
import type { DataTableHeader } from "../../../types/data-table-header.types";
import { createBooleanOptions } from "../../../utils/status-options";
import type {
  ILookupRequest,
  ILookupResponse,
} from "../../../types/lookup.types";
import lookupService from "../../../services/lookup";
import { useChannelEpgStore } from "../stores/channelEpg";
import type { IChannelEpgResponse } from "../types/epg-response";
import type { IFilterChannelEpgRequest } from "../types/filter-request";
import channelEpgService from "../services/channelEpgService";
import { rules } from "../../../composables/validations/rules";
import CreateChannelEpg from "./CreateChannelEpg.vue";
import UpdateChannelEpg from "./UpdateChannelEpg.vue";
import ActivateChannelEpg from "./ActivateChannelEpg.vue";

const epgStore = useChannelEpgStore();

const isValid = ref(true);

const totalItems = ref(0);

const channels = ref<ILookupResponse[]>([]);

const items = ref<IChannelEpgResponse[]>([]);

const lookupRequest = reactive<ILookupRequest>({
  name: "",
});

const credentials = reactive<IFilterChannelEpgRequest>({
  page: 1,
  size: 10,
  title: null,
  startDate: null,
  endDate: null,
  channelId: null,
  active: "1",
});

const headers: DataTableHeader[] = [
  { title: "Channel Epg ID", key: "id" },
  { title: "Channel Name", key: "channel.name", align: "center" },
  { title: "Title En", key: "titleEn", align: "center" },
  { title: "Title Ar", key: "titleAr", align: "center" },
  { title: "Start Date", key: "startDate", align: "center" },
  { title: "End Date", key: "endDate", align: "center" },
  { title: "Status", key: "active", align: "center" },
  { title: "Actions", key: "actions", align: "center" },
];

const cases = createBooleanOptions("Active", "Not Active");

const createDialog = ref(false);
const activateDialog = ref(false);
const editDialog = ref(false);
const selectedChannel = ref<IChannelEpgResponse | null>(null);

const openActivateDialog = (item: IChannelEpgResponse) => {
  selectedChannel.value = null;
  setTimeout(() => {
    selectedChannel.value = item;
    activateDialog.value = true;
  });
};

const openEditDialog = (item: IChannelEpgResponse) => {
  selectedChannel.value = item;
  editDialog.value = true;
};

const onCreated = () => {
  handleFilters();
};

const onActivated = () => {
  handleFilters();
};

const onUpdated = () => {
  handleFilters();
};

async function getChannels() {
  channels.value = await lookupService.getAllChannels(lookupRequest);
}

async function handleFilters() {
  const response = await channelEpgService.filterChannelEpgs(credentials);

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
        <div class="title">Channel Epg Service</div>
      </v-col>
    </v-row>
  </v-container>

  <v-tooltip text="Create Channel Epg">
    <template #activator="{ props }">
      <v-btn
        v-bind="props"
        icon
        size="large"
        color="secondary"
        class="fab-btn"
        elevation="8"
        aria-label="Create Channel Epg"
        @click="createDialog = true"
      >
        <v-icon>mdi-plus</v-icon>
      </v-btn>
    </template>
  </v-tooltip>

  <CreateChannelEpg v-model="createDialog" />

  <UpdateChannelEpg
    v-model="editDialog"
    :epg-item="selectedChannel"
    @updated="onUpdated"
  />

  <ActivateChannelEpg
    :key="selectedChannel?.id"
    v-model="activateDialog"
    :epg-item="selectedChannel"
  />

  <v-row justify="center">
    <v-col cols="12" style="max-width: 1600px">
      <v-form ref="form" v-model="isValid">
        <v-card-text>
          <v-row>
            <v-col cols="14" md="3">
              <v-text-field
                style="height: 85px"
                v-model="credentials.title"
                label="Channel Epg Title"
                variant="outlined"
                clearable
              ></v-text-field>
            </v-col>
            <v-col cols="14" sm="3">
              <v-select
                v-model="credentials.channelId"
                :items="channels"
                item-title="name"
                item-value="id"
                label="Channel"
                variant="outlined"
                :rules="[rules.required]"
                clearable
                outlined
                dense
              >
                <template v-slot:prepend-item>
                  <v-text-field
                    v-model="lookupRequest.name"
                    @input="getChannels"
                    label="Search Channels"
                    dense
                    clearable
                    outlined
                    class="mx-2 mt-2"
                  ></v-text-field> </template
              ></v-select>
            </v-col>

            <v-col cols="14" md="2">
              <v-select
                v-model="credentials.active"
                label="Channel Epg Status"
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
                :loading="epgStore.loading"
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
