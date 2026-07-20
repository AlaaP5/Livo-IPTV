<script setup lang="ts">
import channelService from '../services/channelService';
import { useChannelStore } from '../stores/channel';
import type { IChannelResponse } from '../types/channel-response';



interface Props {
  modelValue: boolean;
  channelItem: IChannelResponse | null;
}

const channelStore = useChannelStore();

const props = defineProps<Props>();
const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
  (e: "published"): void;
}>();

async function confirm() {
  if (!props.channelItem) return;

  await channelService.publishChannel(props.channelItem.id);

  emit("published");
  emit("update:modelValue", false);
}
</script>

<template>
  <v-dialog :model-value="modelValue" max-width="600">
    <v-card prepend-icon="mdi-publish" title="Publish Channel">

      <v-card-text>
        Are you sure you want to
        <strong>Publish</strong>
        this channel?
      </v-card-text>

      <v-card-actions class="justify-end">
        <v-btn variant="text" @click="emit('update:modelValue', false)">
          Cancel
        </v-btn>
        <v-btn color="secondary" :loading="channelStore.loading" @click="confirm">
          publish
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
