```vue
<script setup lang="ts">
import { onMounted, reactive, ref, watch } from "vue";
import { rules } from "../../../composables/validations/rules";
import type {
  ILookupRequest,
  ILookupResponse,
} from "../../../types/lookup.types";
import lookupService from "../../../services/lookup";

import type { IChannelEpgResponse } from "../types/epg-response";
import type { IChannelEpgRequest } from "../types/epg-request";

import channelEpgService from "../services/channelEpgService";
import { useChannelEpgStore } from "../stores/channelEpg";

interface Props {
  modelValue: boolean;
  epgItem: IChannelEpgResponse | null;
}

const props = defineProps<Props>();

const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
  (e: "updated"): void;
}>();

const epgStore = useChannelEpgStore();

const isValid = ref(false);

const formRef = ref();

const channels = ref<ILookupResponse[]>([]);

const lookupRequest = reactive<ILookupRequest>({
  name: "",
});

const form = reactive<IChannelEpgRequest>({
  id: "",
  channelId: "",
  startDate: "",
  endDate: "",
  titleEn: "",
  titleAr: "",
  active: "1",
});

watch(
  () => props.epgItem,
  (value) => {
    if (!value) return;

    lookupRequest.name = value.channel.name;

    getChannels();

    form.id = value.id || "";
    form.channelId = value.channel.id || "";
    form.startDate = value.startDate || "";
    form.endDate = value.endDate || "";
    form.titleEn = value.titleEn || "";
    form.titleAr = value.titleAr || "";
    form.active = value.active ? "1" : "0";
  },
  { immediate: true }
);

async function getChannels() {
  channels.value = await lookupService.getAllChannels(lookupRequest);
}

async function submit() {
  const { valid } = await formRef.value.validate();

  if (!valid) return;

  await channelEpgService.updateChannelEpg(form);

  emit("updated");

  resetForm();

  emit("update:modelValue", false);
}

function resetForm() {
  form.id = "";
  form.channelId = "";
  form.startDate = "";
  form.endDate = "";
  form.titleEn = "";
  form.titleAr = "";
  form.active = "1";
}

onMounted(async () => {
  await getChannels();
});
</script>

<template>
  <v-dialog
    :model-value="modelValue"
    max-width="900"
    persistent
    @update:model-value="emit('update:modelValue', $event)"
  >
    <v-card prepend-icon="mdi-pencil" title="Update Channel Epg">
      <v-divider />

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
              >
                <template #prepend-item>
                  <v-text-field
                    v-model="lookupRequest.name"
                    @input="getChannels"
                    label="Search Channels"
                    density="compact"
                    class="mx-2 mt-2"
                  />
                </template>
              </v-select>
            </v-col>

            <v-col cols="12" sm="6">
              <v-text-field
                v-model="form.titleEn"
                label="Title English"
                variant="outlined"
                :rules="[rules.required]"
              />
            </v-col>

            <v-col cols="12" sm="6">
              <v-text-field
                v-model="form.titleAr"
                label="Title Arabic"
                variant="outlined"
                :rules="[rules.required]"
              />
            </v-col>

            <v-col cols="12" sm="6">
              <v-text-field
                v-model="form.startDate"
                type="datetime-local"
                label="Start Date"
                variant="outlined"
                :rules="[rules.required]"
              />
            </v-col>

            <v-col cols="12" sm="6">
              <v-text-field
                v-model="form.endDate"
                type="datetime-local"
                label="End Date"
                variant="outlined"
                :rules="[rules.required]"
              />
            </v-col>

            <v-col cols="12" sm="6">
              <v-select
                v-model="form.active"
                :items="[
                  { title: 'Active', value: '1' },
                  { title: 'Not Active', value: '0' },
                ]"
                item-title="title"
                item-value="value"
                label="Status"
                variant="outlined"
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
            :disabled="!isValid"
            @click="submit"
          >
            Update
          </v-btn>
        </v-card-actions>
      </v-form>
    </v-card>
  </v-dialog>
</template>
```
