<script setup lang="ts">
import { reactive, ref } from "vue";
import type { DataTableHeader } from "../../../types/data-table-header.types";
import { useChannelStore } from "../stores/channel";
import type { IChannelResponse } from "../types/channel-response";
import type { IFilterChannelRequest } from "../types/filter-request";
import channelService from "../services/channelService";
import { createBooleanOptions } from "../../../utils/status-options";
import type {
  ILookupRequest,
  ILookupResponse,
} from "../../../types/lookup.types";
import lookupService from "../../../services/lookup";
import CreateChannel from "./CreateChannel.vue";
import { badges } from "../../../types/metadata";
import ActivateChannel from "./ActivateChannel.vue";
import UpdateChannel from "./UpdateChannel.vue";
import PublishChannel from "./PublishChannel.vue";
import streamService from "../../stream/services/streamService";
// import router from "../../../router";

const channelStore = useChannelStore();

const isValid = ref(true);

const totalItems = ref(0);

const accounts = ref<ILookupResponse[]>([]);

const tags = ref<ILookupResponse[]>([]);

const badgesOptions = badges;

const items = ref<IChannelResponse[]>([]);

const logoUrls = reactive<Record<string, string>>({});

const lookupRequest = reactive<ILookupRequest>({
  name: "",
});

const lookupRequest2 = reactive<ILookupRequest>({
  name: "",
});

const credentials = reactive<IFilterChannelRequest>({
  page: 1,
  size: 10,
  name: null,
  accountId: null,
  isActive: "1",
  isPublish: null,
  isKids: null,
  isSport: null,
  badge: null,
  tagId: null,
});

const headers: DataTableHeader[] = [
  { title: "Channel ID", key: "id" },
  { title: "Title En", key: "titleEn", align: "center" },
  { title: "Title Ar", key: "titleAr", align: "center" },
  { title: "Description En", key: "descriptionEn", align: "center" },
  { title: "Description Ar", key: "descriptionAr", align: "center" },
  { title: "Tags", key: "tags", align: "center" },
  { title: "Badge", key: "badge", align: "center" },
  { title: "Logo", key: "logo", align: "center" },
  { title: "Has Right", key: "hasRight", align: "center" },
  { title: "Is Kids", key: "isKids", align: "center" },
  { title: "Status", key: "active", align: "center" },
  { title: "Is Sports", key: "isSports", align: "center" },
  { title: "Is Publish", key: "isPublish", align: "center" },
  { title: "Actions", key: "actions", align: "center" },
];

const cases = createBooleanOptions("Active", "Not Active");

const publishCases = createBooleanOptions("Publish", "Not Publish");

const kidsCases = createBooleanOptions("Kids", "Not Kids");

const sportCases = createBooleanOptions("Sport", "Not Sport");

const createDialog = ref(false);
const activateDialog = ref(false);
const editDialog = ref(false);
const publishDialog = ref(false);
const selectedChannel = ref<IChannelResponse | null>(null);

const openActivateDialog = (item: IChannelResponse) => {
  selectedChannel.value = null;
  setTimeout(() => {
    selectedChannel.value = item;
    activateDialog.value = true;
  });
};

const openEditDialog = (item: IChannelResponse) => {
  selectedChannel.value = item;
  editDialog.value = true;
};

const openPublishDialog = (item: IChannelResponse) => {
  selectedChannel.value = null;
  setTimeout(() => {
    selectedChannel.value = item;
    publishDialog.value = true;
  });
};

// function goToViewSectionChannels() {
//   router.push({
//     name: "View Channels For Section",
//   });
// }

const onCreated = () => {
  handleFilters();
};

const onActivated = () => {
  handleFilters();
};

const onUpdated = () => {
  handleFilters();
};

const onPublished = () => {
  handleFilters();
};

async function getAccounts() {
  accounts.value = await lookupService.getAllAccounts(lookupRequest);
}

async function getTags() {
  tags.value = await lookupService.getAllTags(lookupRequest2);
}

async function handleFilters() {
  const response = await channelService.filterChannels(credentials);

  items.value = response.data ?? [];
  totalItems.value = response.totalCount;

  await Promise.all(
    items.value.map(async (item) => {
      if (item.logo?.generatedPath) {
        try {
          const imageUrl = await streamService.fetchImage(
            item.logo.generatedPath,
          );
          logoUrls[item.id] = imageUrl;
        } catch (error) {
          console.error("Failed to load logo for channel", item.id, error);
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
        <div class="title">Channel Service</div>
      </v-col>
    </v-row>
  </v-container>

  <v-tooltip text="Create Channel">
    <template #activator="{ props }">
      <v-btn
        v-bind="props"
        icon
        size="large"
        color="secondary"
        class="fab-btn"
        elevation="8"
        aria-label="Create Channel"
        @click="createDialog = true"
      >
        <v-icon>mdi-plus</v-icon>
      </v-btn>
    </template>
  </v-tooltip>

  <CreateChannel v-model="createDialog" @created="onCreated" />

  <UpdateChannel
    v-model="editDialog"
    :channel-item="selectedChannel"
    @updated="onUpdated"
  />

  <ActivateChannel
    :key="selectedChannel?.id"
    v-model="activateDialog"
    :channel-item="selectedChannel"
    @activated="onActivated"
  />

  <PublishChannel
    :key="selectedChannel?.id"
    v-model="publishDialog"
    :channel-item="selectedChannel"
    @published="onPublished"
  />

  <v-row justify="center">
    <v-col cols="12" style="max-width: 1600px">
      <v-form ref="form" v-model="isValid">
        <v-card-text>
          <v-row>
            <v-col cols="14" md="3">
              <v-text-field
                style="height: 85px"
                v-model="credentials.name"
                label="Channel Title"
                variant="outlined"
                clearable
              ></v-text-field>
            </v-col>
            <v-col cols="14" sm="3">
              <v-select
                v-model="credentials.accountId"
                :items="accounts"
                item-title="name"
                item-value="id"
                label="Account"
                variant="outlined"
                clearable
                outlined
                dense
              >
                <template v-slot:prepend-item>
                  <v-text-field
                    v-model="lookupRequest.name"
                    @input="getAccounts"
                    label="Search Accounts"
                    dense
                    clearable
                    outlined
                    class="mx-2 mt-2"
                    dir="rtl"
                  ></v-text-field> </template
              ></v-select>
            </v-col>

            <v-col cols="14" sm="3">
              <v-select
                v-model="credentials.tagId"
                :items="tags"
                item-title="name"
                item-value="id"
                label="Tag"
                variant="outlined"
                clearable
                outlined
                dense
              >
                <template v-slot:prepend-item>
                  <v-text-field
                    v-model="lookupRequest2.name"
                    @input="getTags"
                    label="Search Tags"
                    dense
                    clearable
                    outlined
                    class="mx-2 mt-2"
                  ></v-text-field> </template
              ></v-select>
            </v-col>

            <v-col cols="12" md="2">
              <v-select
                v-model="credentials.badge"
                label="Badge"
                :items="badgesOptions"
                item-value="value"
                item-title="title"
                variant="outlined"
                outlined
                clearable
              ></v-select>
            </v-col>
            <v-col cols="14" md="2">
              <v-select
                v-model="credentials.isActive"
                label="Channel Status"
                :items="cases"
                item-value="value"
                item-title="title"
                variant="outlined"
                outlined
                clearable
              ></v-select>
            </v-col>

            <v-col cols="14" md="2">
              <v-select
                v-model="credentials.isPublish"
                label="Publish Status"
                :items="publishCases"
                item-value="value"
                item-title="title"
                variant="outlined"
                outlined
                clearable
              ></v-select>
            </v-col>

            <v-col cols="14" md="2">
              <v-select
                v-model="credentials.isKids"
                label="Kids Status"
                :items="kidsCases"
                item-value="value"
                item-title="title"
                variant="outlined"
                outlined
                clearable
              ></v-select>
            </v-col>

            <v-col cols="14" md="2">
              <v-select
                v-model="credentials.isSport"
                label="Sport Status"
                :items="sportCases"
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

            <!-- <v-btn class="btn" color="primary" @click="goToViewSectionChannels()">
              View Section Channels
            </v-btn> -->
          </v-row>

          <v-row>
            <v-col cols="12">
              <AppTable
                :headers="headers"
                :items="items"
                :total-items="totalItems"
                :page="credentials.page"
                :items-per-page="credentials.size"
                :loading="channelStore.loading"
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

                  <v-btn
                    :disabled="item.isPublish"
                    icon
                    @click="openPublishDialog(item)"
                  >
                    <v-icon>mdi-publish</v-icon>
                  </v-btn>

                  <v-btn icon @click="openEditDialog(item)">
                    <v-icon color="secondary">mdi-pencil</v-icon>
                  </v-btn>
                </template>

                <template v-slot:item.tags="{ item }">
                  <div>
                    <v-chip
                      v-for="tag in item.tags"
                      :key="tag.id"
                      class="ma-1"
                      small
                    >
                      {{ tag.titleEn }}
                    </v-chip>
                  </div>
                </template>

                <template v-slot:item.logo="{ item }">
                  <img
                    v-if="logoUrls[item.id]"
                    :src="logoUrls[item.id]"
                    width="80"
                    height="80"
                    style="object-fit: cover; border-radius: 8px"
                  />
                  <v-progress-circular
                    v-else-if="channelStore.loading"
                    indeterminate
                    size="30"
                    width="2"
                    color="primary"
                  />
                  <span v-else>No Logo</span>
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

                <template v-slot:item.isKids="{ item }">
                  <v-chip
                    :color="item.isKids ? 'green' : 'red'"
                    small
                    class="ma-1"
                  >
                    {{ item.isKids ? "Yes" : "No" }}
                  </v-chip>
                </template>

                <template v-slot:item.isSports="{ item }">
                  <v-chip
                    :color="item.isSports ? 'green' : 'red'"
                    small
                    class="ma-1"
                  >
                    {{ item.isSports ? "Yes" : "No" }}
                  </v-chip>
                </template>

                <template v-slot:item.isPublish="{ item }">
                  <v-chip
                    :color="item.isPublish ? 'green' : 'red'"
                    small
                    class="ma-1"
                  >
                    {{ item.isPublish ? "Yes" : "No" }}
                  </v-chip>
                </template>

                <template v-slot:item.hasRight="{ item }">
                  <v-chip
                    :color="item.hasRight ? 'green' : 'red'"
                    small
                    class="ma-1"
                  >
                    {{ item.hasRight ? "Yes" : "No" }}
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
