<script setup lang="ts">
import { reactive, ref } from "vue";
import type { DataTableHeader } from "../../../types/data-table-header.types";
import { rules } from "../../../composables/validations/rules";
import { useAccountStore } from "../stores/account";
import type { IAccountResponse } from "../types/account-response";
import type { IFilterAccountRequest } from "../types/filter-request";
import accountService from "../services/accountService";
import CreateAccount from "./CreateAccount.vue";
import ActivateAccount from "./ActivateAccount.vue";
import { accountRole } from "../../../enums/AccountRoleEnum";
import { accountStatus } from "../../../enums/AccountStatusEnum";

const accountStore = useAccountStore();

const isValid = ref(true);

const totalItems = ref(0);

const items = ref<IAccountResponse[]>([]);

const credentials = reactive<IFilterAccountRequest>({
  page: 1,
  size: 10,
  status: "",
  role: "",
  userName: "",
});

const headers: DataTableHeader[] = [
  { title: "Account ID", key: "id" },
  { title: "User ID", key: "userId", align: "center" },
  { title: "User Name", key: "userName", align: "center" },
  { title: "Status", key: "accountStatus", align: "center" },
  { title: "Actions", key: "actions", align: "center" },
];

const roles = accountRole;

const cases = accountStatus;


const createDialog = ref(false);
const activateDialog = ref(false);
const selectedAccount = ref<IAccountResponse | null>(null);

const openActivateDialog = (item: IAccountResponse) => {
  selectedAccount.value = null; 
  setTimeout(() => {
    selectedAccount.value = item;
    activateDialog.value = true;
  });
};

const onCreated = () => {
  handleFilters();
};

const onActivated = () => {
  handleFilters();
};

async function handleFilters() {
  const response = await accountService.filterAccounts(credentials);

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
        <div class="title">Account Service</div>
      </v-col>
    </v-row>
  </v-container>

  <v-tooltip text="Create Account">
    <template #activator="{ props }">
      <v-btn
        v-bind="props"
        icon
        size="large"
        color="secondary"
        class="fab-btn"
        elevation="8"
        aria-label="Create Account"
        @click="createDialog = true"
      >
        <v-icon>mdi-plus</v-icon>
      </v-btn>
    </template>
  </v-tooltip>

  <CreateAccount v-model="createDialog" @created="onCreated" />

  <ActivateAccount
    :key="selectedAccount?.id"
    v-model="activateDialog"
    :account-item="selectedAccount"
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
                v-model="credentials.userName"
                label="User Name"
                variant="outlined"
                clearable
              ></v-text-field>
            </v-col>
            <v-col cols="12" md="2">
              <v-select
                v-model="credentials.role"
                label="Account Role"
                :items="roles"
                :rules="[rules.required]"
                item-value="value"
                item-title="title"
                variant="outlined"
                outlined
                clearable
              ></v-select>
            </v-col>

            <v-col cols="12" md="2">
              <v-select
                v-model="credentials.status"
                label="Account Status"
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
                :loading="accountStore.loading"
                height="500"
                @update:page="updatePage"
                @update:items-per-page="updatePageSize"
              >
                <template #actions="{ item }">
                  <v-btn icon @click="openActivateDialog(item)">
                    <v-icon :color="item.accountStatus == 'ACTIVE' ? 'red' : 'green'">
                      {{ item.accountStatus == 'ACTIVE' ? "mdi-close" : "mdi-check" }}
                    </v-icon>
                  </v-btn>
                </template>

                <template v-slot:item.accountStatus="{ item }">
                  <v-chip
                    :color="item.accountStatus == 'ACTIVE' ? 'green' : 'red'"
                    small
                    class="ma-1"
                  >
                    {{ item.accountStatus }}
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
