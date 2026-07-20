<script setup lang="ts">
import { computed } from "vue";
import type { IAdResponse } from "../types/ad-response";
import { useAdStore } from "../stores/ad";
import adService from "../services/adService";



interface Props {
  modelValue: boolean;
  adItem: IAdResponse | null;
}

const adStore = useAdStore();

const props = defineProps<Props>();
const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
  (e: "activated"): void;
}>();

const nextStatus = computed(() =>
  (props.adItem?.active) ? "0" : "1"
);

const actionLabel = computed(() =>
  (props.adItem?.active) ? "Deactivate" : "Activate"
);

async function confirm() {
  if (!props.adItem) return;

  await adService.activateAd(props.adItem.id, {
    active: nextStatus.value,
  });

  emit("activated");
  emit("update:modelValue", false);
}
</script>

<template>
  <v-dialog :model-value="modelValue" max-width="600">
    <v-card prepend-icon="mdi-delete" :title="`${actionLabel} Ad`">

      <v-card-text>
        Are you sure you want to
        <strong>{{ actionLabel.toLowerCase() }}</strong>
        this ad?
      </v-card-text>

      <v-card-actions class="justify-end">
        <v-btn variant="text" @click="emit('update:modelValue', false)">
          Cancel
        </v-btn>
        <v-btn color="secondary" :loading="adStore.loading" @click="confirm">
          {{ actionLabel }}
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
