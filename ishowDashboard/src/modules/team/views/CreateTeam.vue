<script setup lang="ts">
import { reactive, ref, watch } from "vue";
import { rules } from "../../../composables/validations/rules";
import { createBooleanOptions } from "../../../utils/status-options";
import { useTeamStore } from "../stores/team";
import type { ITeamRequest } from "../types/team-request";
import teamService from "../services/teamService";


interface Props {
  modelValue: boolean;
}

const props = defineProps<Props>();
const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
  (e: "created"): void;
}>();

const isValid = ref(false);

const selectedFiles = ref<File[]>([]);

const teamStore = useTeamStore();

const formRef = ref();

const form = reactive<ITeamRequest>({
  id: 0,
  nameEn: "",
  nameAr: "",
  imagePath: null as unknown as File,
  active: "1",
});

const cases = createBooleanOptions("Active", "Not Active");

watch(
  () => props.modelValue,
  (val) => {
    if (!val) resetForm();
  },
);

function resetForm() {
  form.id = 0;
  form.nameEn = "";
  form.nameAr = "";
  form.imagePath = null as unknown as File;
  form.active = "1";
}

async function submit() {
  const valid = await formRef.value?.validate();
  if (!valid) return;

  if (!selectedFiles.value.length) return;

  form.imagePath = selectedFiles.value[0];

  await teamService.createTeam(form);
  emit("created");
  emit("update:modelValue", false);
}
</script>

<template>
  <v-dialog :model-value="modelValue" max-width="700" persistent>
    <v-card prepend-icon="mdi-plus" title="Create Team">
      <v-divider />

      <v-form ref="formRef" v-model="isValid">
        <v-card-text>
          <v-row>
            <v-col cols="12" md="6">
              <v-text-field
                v-model="form.nameEn"
                label="English Name"                
                :rules="[rules.required]"
                variant="outlined"
              />
            </v-col>

            <v-col cols="12" md="6">
              <v-text-field
                v-model="form.nameAr"
                label="Arabic Name"
                :rules="[rules.required]"
                variant="outlined"
                dir="rtl"
              />
            </v-col>

            <v-col cols="12">
              <v-file-input
                v-model="selectedFiles"
                label="Team Image"
                variant="outlined"
                show-size
                prepend-icon="mdi-image"
                accept="image/*"
              />
            </v-col>

            <v-col cols="12">
              <v-select
                v-model="form.active"
                label="Status"
                :items="cases"
                item-value="value"
                item-title="title"
                variant="outlined"
              />
            </v-col>
          </v-row>
        </v-card-text>

        <v-card-actions class="justify-end">
          <v-btn variant="text" @click="emit('update:modelValue', false)">
            Cancel
          </v-btn>

          <v-btn
            color="secondary"
            :loading="teamStore.loading"
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
