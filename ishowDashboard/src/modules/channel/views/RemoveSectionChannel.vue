<script setup lang="ts">
import { reactive, watch } from "vue";
import type { ISectionChannelsResponse } from "../types/channel-response";
import { useChannelStore } from "../stores/channel";
import type { IRemoveChannelFromSectionRequest } from "../types/channel-request";
import channelService from "../services/channelService";

interface Props {
  modelValue: boolean;
  sectionChannelItem: ISectionChannelsResponse | null;
}

const channelStore = useChannelStore();

const removeRequest = reactive<IRemoveChannelFromSectionRequest>({
  channelId: "",
  sectionId: 0,
});

const props = defineProps<Props>();
const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
  (e: "removed"): void;
}>();

watch(
  () => props.sectionChannelItem,
  (newItem) => {
    if (newItem) {
      removeRequest.channelId = newItem.channelId;
      removeRequest.sectionId = newItem.sectionId;
    }
  },
  { immediate: true },
);

async function confirm() {
  if (!props.sectionChannelItem) return;

  await channelService.removeChannelFromSection(removeRequest);

  emit("removed");
  emit("update:modelValue", false);
}
</script>

<template>
  <v-dialog :model-value="modelValue" max-width="600">
    <v-card prepend-icon="mdi-delete" title="Remove Channel from section">
      <v-card-text>
        Are you sure you want to
        <strong>Remove</strong>
        this channel from section?
      </v-card-text>

      <v-card-actions class="justify-end">
        <v-btn variant="text" @click="emit('update:modelValue', false)">
          Cancel
        </v-btn>
        <v-btn
          color="secondary"
          :loading="channelStore.loading"
          @click="confirm"
        >
          remove
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
