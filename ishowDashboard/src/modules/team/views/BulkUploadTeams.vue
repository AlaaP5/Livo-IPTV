<script setup lang="ts">
import { ref } from "vue";
import { useTeamStore } from "../stores/team";
import teamService from "../services/teamService";
import { rules } from "../../../composables/validations/rules";
import { watch } from "vue";

interface Props {
  modelValue: boolean;
}

const props = defineProps<Props>();
const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
  (e: "uploaded"): void;
}>();

const isValid = ref(false);

const formRef = ref();

const selectedFiles = ref<File[]>([]);

const teamStore = useTeamStore();

watch(
  () => props.modelValue,
  (val) => {
    if (!val) resetForm();
  },
);

function resetForm() {
  selectedFiles.value = [];
}

async function submit() {
  const valid = await formRef.value?.validate();

  if (!valid) return;

  if (!selectedFiles.value.length) return;

  const zipFile = selectedFiles.value[0];

  const response = await teamService.bulkUploadTeams(zipFile);

  if (response?.data !== undefined) {
    emit("uploaded");
    emit("update:modelValue", false);
  }
}
</script>

<template>
  <div>
    <v-dialog :model-value="modelValue" max-width="600" persistent>
      <v-card prepend-icon="mdi-folder-zip" title="Bulk Upload Teams">
        <v-divider />

        <v-form ref="formRef" v-model="isValid">
          <v-card-text>
            <v-row>
              <v-col cols="12">
                <v-file-input
                  v-model="selectedFiles"
                  label="ZIP File"
                  variant="outlined"
                  prepend-icon="mdi-zip-box"
                  accept=".zip"
                  show-size
                  :rules="[rules.required]"
                />
              </v-col>
            </v-row>
          </v-card-text>

          <v-card-actions class="justify-end">
            <v-btn variant="text" @click="emit('update:modelValue', false)"> Cancel </v-btn>

            <v-btn
              color="success"
              :loading="teamStore.loading"
              :disabled="!isValid"
              @click="submit"
            >
              Upload
            </v-btn>
          </v-card-actions>
        </v-form>
      </v-card>
    </v-dialog>
  </div>
</template>
