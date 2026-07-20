<script setup lang="ts">
import { computed } from "vue";
import type { IAccountResponse } from "../types/account-response";
import { useAccountStore } from "../stores/account";
import accountService from "../services/accountService";



interface Props {
  modelValue: boolean;
  accountItem: IAccountResponse | null;
}

const accountStore = useAccountStore();

const props = defineProps<Props>();
const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
  (e: "activated"): void;
}>();

const nextStatus = computed(() =>
  (props.accountItem?.accountStatus == "ACTIVE") ? "DELETED" : "ACTIVE"
);

const actionLabel = computed(() =>
  (props.accountItem?.accountStatus == "ACTIVE") ? "DELETED" : "ACTIVE"
);

async function confirm() {
  if (!props.accountItem) return;

  await accountService.activateAccount(props.accountItem.id, {
    status: nextStatus.value,
  });

  emit("activated");
  emit("update:modelValue", false);
}
</script>

<template>
  <v-dialog :model-value="modelValue" max-width="600">
    <v-card prepend-icon="mdi-delete" :title="`${actionLabel} Account`">

      <v-card-text>
        Are you sure you want to
        <strong>{{ actionLabel.toLowerCase() }}</strong>
        this account?
      </v-card-text>

      <v-card-actions class="justify-end">
        <v-btn variant="text" @click="emit('update:modelValue', false)">
          Cancel
        </v-btn>
        <v-btn color="secondary" :loading="accountStore.loading" @click="confirm">
          {{ actionLabel }}
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
