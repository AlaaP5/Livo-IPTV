<script setup lang="ts">
import { reactive, ref } from "vue";
import { rules } from "../../../composables/validations/rules";
import type {
  ILookupRequest,
  ILookupResponse,
} from "../../../types/lookup.types";
import lookupService from "../../../services/lookup";
import { useChannelEpgStore } from "../stores/channelEpg";
import type { IUploadEpgRequest } from "../types/epg-request";
import channelEpgService from "../services/channelEpgService";

interface Props {
  modelValue: boolean;
}

const props = defineProps<Props>();
const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
}>();

const isValid = ref(false);

const formRef = ref();

const epgStore = useChannelEpgStore();

const selectedFiles = ref<File[]>([]);

const channels = ref<ILookupResponse[]>([]);

const lookupRequest = reactive<ILookupRequest>({
  name: "",
});

const form = reactive<IUploadEpgRequest>({
  channelId: "",
  file: null as unknown as File,
});

async function getChannels() {
  channels.value = await lookupService.getAllChannels(lookupRequest);
}

async function submit() {
  if (!selectedFiles.value.length) return;

  form.file = selectedFiles.value[0];

  await channelEpgService.uploadChannelEpg(form);

  resetForm();
  emit("update:modelValue", false);
}

function resetForm() {
  form.channelId = "";
  form.file = null as unknown as File;
}
</script>

<template>
  <v-dialog
    :model-value="modelValue"
    max-width="800"
    persistent
    @update:model-value="emit('update:modelValue', $event)"
  >
    <v-card prepend-icon="mdi-plus" title="Create Channel Epg">
      <v-divider></v-divider>

      <v-form ref="formRef" v-model="isValid">
        <v-card-text>
          <v-row>
            <v-col cols="12" sm="6">
              <v-select
                v-model="form.channelId"
                :items="channels"
                item-title="name"
                item-value="id"
                label="Channel"
                variant="outlined"
                :rules="[rules.required]"
                clearable
                outlined
                dense
              >
                <template v-slot:prepend-item>
                  <v-text-field
                    v-model="lookupRequest.name"
                    @input="getChannels"
                    label="Search Channels"
                    dense
                    clearable
                    outlined
                    class="mx-2 mt-2"
                  ></v-text-field> </template
              ></v-select>
            </v-col>

            <v-col cols="12" sm="6">
              <v-file-input
                v-model="selectedFiles"
                label="Xml File"
                variant="outlined"
                show-size
                prepend-icon="mdi-image"
                accept=".xml"
              />
            </v-col>
          </v-row>
        </v-card-text>

        <v-divider />

        <v-card-actions>
          <v-spacer />

          <v-btn
            variant="text"
            @click="
              resetForm();
              emit('update:modelValue', false);
            "
          >
            Cancel
          </v-btn>

          <v-btn
            color="secondary"
            :loading="epgStore.loading"
            :disabled="!isValid || !selectedFiles.length"
            @click="submit"
          >
            Create
          </v-btn>
        </v-card-actions>
      </v-form>
    </v-card>
  </v-dialog>
</template>
