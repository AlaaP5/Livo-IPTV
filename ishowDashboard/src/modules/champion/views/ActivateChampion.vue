<script setup lang="ts">
import { computed } from "vue";
import type { IChampionResponse } from "../types/champion-response";
import { useChampionStore } from "../stores/champion";
import championService from "../services/championService";

interface Props {
  modelValue: boolean;
  championItem: IChampionResponse | null;
}

const championStore = useChampionStore();

const props = defineProps<Props>();
const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
  (e: "activated"): void;
}>();

const nextStatus = computed(() => (props.championItem?.active ? "0" : "1"));

const actionLabel = computed(() =>
  props.championItem?.active ? "Deactivate" : "Activate",
);

async function confirm() {
  if (!props.championItem) return;

  await championService.activateChampion(props.championItem.id, {
    active: nextStatus.value,
  });

  emit("activated");
  emit("update:modelValue", false);
}
</script>

<template>
  <v-dialog :model-value="modelValue" max-width="600">
    <v-card prepend-icon="mdi-delete" :title="`${actionLabel} Champion`">
      <v-card-text>
        Are you sure you want to
        <strong>{{ actionLabel.toLowerCase() }}</strong>
        this champion?
      </v-card-text>

      <v-card-actions class="justify-end">
        <v-btn variant="text" @click="emit('update:modelValue', false)">
          Cancel
        </v-btn>
        <v-btn
          color="secondary"
          :loading="championStore.loading"
          @click="confirm"
        >
          {{ actionLabel }}
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
