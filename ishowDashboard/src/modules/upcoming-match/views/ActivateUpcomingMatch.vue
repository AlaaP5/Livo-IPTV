<script setup lang="ts">
import { computed } from "vue";
import type { IUpcomingMatchResponse } from "../types/upcomingMatch-response";
import { useUpcomingMatchStore } from "../stores/upcoming-match";
import upcomingMatchService from "../services/upcomingMatchService";

interface Props {
  modelValue: boolean;
  upcomingItem: IUpcomingMatchResponse | null;
}

const upcomingStore = useUpcomingMatchStore();

const props = defineProps<Props>();
const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
  (e: "activated"): void;
}>();

const nextStatus = computed(() => (props.upcomingItem?.active ? "0" : "1"));

const actionLabel = computed(() =>
  props.upcomingItem?.active ? "Deactivate" : "Activate",
);

async function confirm() {
  if (!props.upcomingItem) return;

  await upcomingMatchService.activateUpcomingMatch(props.upcomingItem.id, {
    active: nextStatus.value,
  });

  emit("activated");
  emit("update:modelValue", false);
}
</script>

<template>
  <v-dialog :model-value="modelValue" max-width="600">
    <v-card prepend-icon="mdi-delete" :title="`${actionLabel} Team`">
      <v-card-text>
        Are you sure you want to
        <strong>{{ actionLabel.toLowerCase() }}</strong>
        this upcoming match?
      </v-card-text>

      <v-card-actions class="justify-end">
        <v-btn variant="text" @click="emit('update:modelValue', false)">
          Cancel
        </v-btn>
        <v-btn
          color="secondary"
          :loading="upcomingStore.loading"
          @click="confirm"
        >
          {{ actionLabel }}
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
