<script setup lang="ts">
import { computed } from "vue";
import channelEpgService from "../services/channelEpgService";
import type { IChannelEpgResponse } from "../types/epg-response";
import { useChannelEpgStore } from "../stores/channelEpg";



interface Props {
  modelValue: boolean;
  epgItem: IChannelEpgResponse | null;
}

const epgStore = useChannelEpgStore();

const props = defineProps<Props>();
const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
}>();

const nextStatus = computed(() =>
  (props.epgItem?.active) ? "0" : "1"
);

const actionLabel = computed(() =>
  (props.epgItem?.active) ? "Deactivate" : "Activate"
);

async function confirm() {
  if (!props.epgItem) return;

  await channelEpgService.activateChannelEpg(props.epgItem.id, {
    active: nextStatus.value,
  });
  emit("update:modelValue", false);
}
</script>

<template>
  <v-dialog :model-value="modelValue" max-width="600">
    <v-card prepend-icon="mdi-delete" :title="`${actionLabel} Channel Epg`">

      <v-card-text>
        Are you sure you want to
        <strong>{{ actionLabel.toLowerCase() }}</strong>
        this channel epg?
      </v-card-text>

      <v-card-actions class="justify-end">
        <v-btn variant="text" @click="emit('update:modelValue', false)">
          Cancel
        </v-btn>
        <v-btn color="secondary" :loading="epgStore.loading" @click="confirm">
          {{ actionLabel }}
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
