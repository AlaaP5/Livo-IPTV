<script setup lang="ts">
import type {
  ILookupRequest,
  ILookupResponse,
} from "../../../types/lookup.types";
import lookupService from "../../../services/lookup";
import { reactive, ref, watch } from "vue";
import { useKidsStore } from "../stores/kids";
import type { IAddChannelToSectionRequest } from "../../channel/types/channel-request";
import kidsService from "../services/kidsService";


interface Props {
  modelValue: boolean;
  sectionId: number | null;
}

const kidsStore = useKidsStore();

const isValid = ref(false);

const topOptions = [
  { value: "1", title: "Top" },
  { value: "0", title: "Not Top" },
];

const lookupChannelRequest = reactive<ILookupRequest>({
  name: "",
});

const channels = ref<ILookupResponse[]>([]);

const props = defineProps<Props>();
const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
  (e: "added"): void;
}>();

const credentials = reactive<IAddChannelToSectionRequest>({
  sectionId: null,
  isTop: null,
  channelId: "",
});

const resetCredentials = () => {
  credentials.sectionId = props.sectionId;
  credentials.isTop = null;
  credentials.channelId = "";
  lookupChannelRequest.name = "";
};

watch(
  () => props.modelValue,
  (isOpen) => {
    if (isOpen) {
      resetCredentials();
      getChannels();
    }
  }
);

watch(
  () => props.sectionId,
  (newSectionId) => {
    credentials.sectionId = newSectionId;
  },
  { immediate: true }
);


async function getChannels() {
  channels.value = await lookupService.getAllKidsChannels(lookupChannelRequest);
}

async function confirm() {

  await kidsService.addChannelToSection(credentials);

  emit("added");
  emit("update:modelValue", false);
  resetCredentials();
}
</script>

<template>
  <v-dialog :model-value="modelValue" max-width="800">
    <v-card prepend-icon="mdi-publish" title="Add Channel To Section">
      <v-divider></v-divider>
      <v-form ref="formRef" v-model="isValid">
        <v-card-text>
          <v-row>
            <v-col cols="14" sm="4">
              <v-select
                v-model="credentials.channelId"
                :items="channels"
                item-title="name"
                item-value="id"
                label="Channels"
                variant="outlined"
                clearable
                outlined
                dense
              >
                <template v-slot:prepend-item>
                  <v-text-field
                    v-model="lookupChannelRequest.name"
                    @input="getChannels"
                    label="Search Channels"
                    dense
                    clearable
                    outlined
                    class="mx-2 mt-2"
                  ></v-text-field> </template
              ></v-select>
            </v-col>

            <v-col cols="12" md="4">
              <v-select
                v-model="credentials.isTop"
                label="Is Top"
                :items="topOptions"
                item-value="value"
                item-title="title"
                variant="outlined"
                outlined
                clearable
              ></v-select>
            </v-col>
          </v-row>
        </v-card-text>
      </v-form>
      <v-divider />

      <v-card-actions class="justify-end">
        <v-btn variant="text" @click="emit('update:modelValue', false)">
          Cancel
        </v-btn>
        <v-btn color="secondary" :loading="kidsStore.loading" @click="confirm">
          add
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
