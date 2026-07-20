<script setup lang="ts">
import { computed } from "vue";
import type { ITeamResponse } from "../types/team-response";
import { useTeamStore } from "../stores/team";
import teamService from "../services/teamService";

interface Props {
  modelValue: boolean;
  teamItem: ITeamResponse | null;
}

const teamStore = useTeamStore();

const props = defineProps<Props>();
const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
  (e: "activated"): void;
}>();

const nextStatus = computed(() => (props.teamItem?.active ? "0" : "1"));

const actionLabel = computed(() =>
  props.teamItem?.active ? "Deactivate" : "Activate",
);

async function confirm() {
  if (!props.teamItem) return;

  await teamService.activateTeam(props.teamItem.id, {
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
        this team?
      </v-card-text>

      <v-card-actions class="justify-end">
        <v-btn variant="text" @click="emit('update:modelValue', false)">
          Cancel
        </v-btn>
        <v-btn
          color="secondary"
          :loading="teamStore.loading"
          @click="confirm"
        >
          {{ actionLabel }}
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
