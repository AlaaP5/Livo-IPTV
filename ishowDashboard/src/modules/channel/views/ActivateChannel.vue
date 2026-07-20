<script setup lang="ts">
import { computed } from "vue";
import type { IChannelResponse } from "../types/channel-response";
import channelService from "../services/channelService";
import { useChannelStore } from "../stores/channel";



interface Props {
  modelValue: boolean;
  channelItem: IChannelResponse | null;
}

const channelStore = useChannelStore();

const props = defineProps<Props>();
const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
  (e: "activated"): void;
}>();

const nextStatus = computed(() =>
  (props.channelItem?.active) ? "0" : "1"
);

const actionLabel = computed(() =>
  (props.channelItem?.active) ? "Deactivate" : "Activate"
);

async function confirm() {
  if (!props.channelItem) return;

  await channelService.activateChannel(props.channelItem.id, {
    active: nextStatus.value,
  });

  emit("activated");
  emit("update:modelValue", false);
}
</script>

<template>
  <v-dialog :model-value="modelValue" max-width="600">
    <v-card prepend-icon="mdi-delete" :title="`${actionLabel} channel`">

      <v-card-text>
        Are you sure you want to
        <strong>{{ actionLabel.toLowerCase() }}</strong>
        this channel?
      </v-card-text>

      <v-card-actions class="justify-end">
        <v-btn variant="text" @click="emit('update:modelValue', false)">
          Cancel
        </v-btn>
        <v-btn color="secondary" :loading="channelStore.loading" @click="confirm">
          {{ actionLabel }}
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
