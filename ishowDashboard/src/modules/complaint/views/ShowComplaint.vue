<script setup lang="ts">
import { computed } from "vue";
import type { IComplaintResponse } from "../types/complaint-response";
import { useComplaintStore } from "../stores/complaint";
import complaintService from "../services/complaintService";


interface Props {
  modelValue: boolean;
  complaintItem: IComplaintResponse | null;
}

const complaintStore = useComplaintStore();

const props = defineProps<Props>();
const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
  (e: "showed"): void;
}>();

const nextStatus = computed(() => (props.complaintItem?.isRead ? "0" : "1"));

const actionLabel = computed(() =>
  props.complaintItem?.isRead ? "Not Read" : "Read",
);

async function confirm() {
  if (!props.complaintItem) return;

  await complaintService.showComplaint(props.complaintItem.id, {
    status: nextStatus.value,
  });

  emit("showed");
  emit("update:modelValue", false);
}
</script>

<template>
  <v-dialog :model-value="modelValue" max-width="600">
    <v-card prepend-icon="mdi-read" :title="`${actionLabel} Complaint`">
      <v-card-text>
        Are you sure you want to make as
        <strong>{{ actionLabel.toLowerCase() }}</strong>
        this complaint?
      </v-card-text>

      <v-card-actions class="justify-end">
        <v-btn variant="text" @click="emit('update:modelValue', false)">
          Cancel
        </v-btn>
        <v-btn
          color="secondary"
          :loading="complaintStore.loading"
          @click="confirm"
        >
          make as {{ actionLabel }}
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
