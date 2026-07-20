<script setup lang="ts">
import { reactive, ref, watch } from "vue";
import { rules } from "../../../composables/validations/rules";
import { createBooleanOptions } from "../../../utils/status-options";
import { useUpcomingMatchStore } from "../stores/upcoming-match";
import type { IUpcomingMatchRequest } from "../types/upcomingMatch-request";
import upcomingMatchService from "../services/upcomingMatchService";
import type {
  ILookupRequest,
  ILookupResponse,
} from "../../../types/lookup.types";
import lookupService from "../../../services/lookup";

interface Props {
  modelValue: boolean;
}

const props = defineProps<Props>();
const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
  (e: "created"): void;
}>();

const isValid = ref(false);

const upcomingStore = useUpcomingMatchStore();

const formRef = ref();

const form = reactive<IUpcomingMatchRequest>({
  id: 0,
  homeTeamId: null,
  awayTeamId: null,
  championId: null,
  channelId: null,
  matchDate: "",
  active: "1",
});

const cases = createBooleanOptions("Active", "Not Active");

const lookupChannelRequest = reactive<ILookupRequest>({
  name: "",
});

const lookupHomeTeamRequest = reactive<ILookupRequest>({
  name: "",
});

const lookupAwayTeamRequest = reactive<ILookupRequest>({
  name: "",
});

const lookupChampionRequest = reactive<ILookupRequest>({
  name: "",
});

const channels = ref<ILookupResponse[]>([]);

const homeTeams = ref<ILookupResponse[]>([]);

const awayTeams = ref<ILookupResponse[]>([]);

const champions = ref<ILookupResponse[]>([]);

async function getChannels() {
  channels.value = await lookupService.getAllChannels(lookupChannelRequest);
}

async function getHomeTeams() {
  homeTeams.value = await lookupService.getAllTeams(lookupHomeTeamRequest);
}

async function getAwayTeams() {
  awayTeams.value = await lookupService.getAllTeams(lookupAwayTeamRequest);
}

async function getChampions() {
  champions.value = await lookupService.getAllChampions(lookupChampionRequest);
}

watch(
  () => props.modelValue,
  (val) => {
    if (!val) resetForm();
  },
);

function resetForm() {
  form.id = 0;
  form.homeTeamId = null;
  form.awayTeamId = null;
  form.championId = null;
  form.channelId = null;
  form.matchDate = "";
  form.active = "1";
}

async function submit() {
  const valid = await formRef.value?.validate();
  if (!valid) return;

  await upcomingMatchService.createUpcomingMatch(form);
  emit("created");
  emit("update:modelValue", false);
}
</script>

<template>
  <v-dialog :model-value="modelValue" max-width="700" persistent>
    <v-card prepend-icon="mdi-plus" title="Create Upcoming Match">
      <v-divider />

      <v-form ref="formRef" v-model="isValid">
        <v-card-text>
          <v-row>
            <v-col cols="12" md="6">
              <v-select
                v-model="form.channelId"
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
                    label="Search Teams"
                    dense
                    clearable
                    outlined
                    class="mx-2 mt-2"
                  ></v-text-field> </template
              ></v-select>
            </v-col>

            <v-col cols="12" md="6">
              <v-select
                v-model="form.homeTeamId"
                :items="homeTeams"
                item-title="name"
                item-value="id"
                label="Home Teams"
                variant="outlined"
                clearable
                outlined
                dense
              >
                <template v-slot:prepend-item>
                  <v-text-field
                    v-model="lookupHomeTeamRequest.name"
                    @input="getHomeTeams"
                    label="Search Home Teams"
                    dense
                    clearable
                    outlined
                    class="mx-2 mt-2"
                  ></v-text-field> </template
              ></v-select>
            </v-col>

            <v-col cols="12" md="6">
              <v-select
                v-model="form.awayTeamId"
                :items="awayTeams"
                item-title="name"
                item-value="id"
                label="Away Teams"
                variant="outlined"
                clearable
                outlined
                dense
              >
                <template v-slot:prepend-item>
                  <v-text-field
                    v-model="lookupAwayTeamRequest.name"
                    @input="getAwayTeams"
                    label="Search Away Teams"
                    dense
                    clearable
                    outlined
                    class="mx-2 mt-2"
                  ></v-text-field> </template
              ></v-select>
            </v-col>

            <v-col cols="12" md="6">
              <v-select
                v-model="form.championId"
                :items="champions"
                item-title="name"
                item-value="id"
                label="Champions"
                variant="outlined"
                clearable
                outlined
                dense
              >
                <template v-slot:prepend-item>
                  <v-text-field
                    v-model="lookupChampionRequest.name"
                    @input="getChampions"
                    label="Search Champions"
                    dense
                    clearable
                    outlined
                    class="mx-2 mt-2"
                  ></v-text-field> </template
              ></v-select>
            </v-col>

            <v-col cols="12" md="6">
              <v-text-field
                v-model="form.matchDate"
                label="Match Date & Time"
                type="datetime-local"
                variant="outlined"
                :rules="[rules.required]"
              />
            </v-col>

            <v-col cols="12" md="6">
              <v-select
                v-model="form.active"
                label="Channel Status"
                :items="cases"
                :rules="[rules.required]"
                item-value="value"
                item-title="title"
                variant="outlined"
                outlined
                clearable
              ></v-select>
            </v-col>
          </v-row>
        </v-card-text>

        <v-card-actions class="justify-end">
          <v-btn variant="text" @click="emit('update:modelValue', false)">
            Cancel
          </v-btn>

          <v-btn
            color="secondary"
            :loading="upcomingStore.loading"
            :disabled="!isValid"
            @click="submit"
          >
            Create
          </v-btn>
        </v-card-actions>
      </v-form>
    </v-card>
  </v-dialog>
</template>
