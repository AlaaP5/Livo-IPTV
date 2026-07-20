<script setup lang="ts">
import { ref } from "vue";
import uploadService from "../../services/uploadService";

const file = ref<File | File[] | null>(null);
const progress = ref(0);
const speed = ref("0 KB/s");
const uploading = ref(false);

function getFile(): File | null {
  if (!file.value) return null;

  if (Array.isArray(file.value)) {
    return file.value[0] ?? null;
  }

  return file.value;
}

async function uploadFile() {
  const selectedFile = getFile();

  if (!selectedFile) return;

  uploading.value = true;
  progress.value = 0;

  try {
    const response = await uploadService.uploadFile(selectedFile, (p, s) => {
      progress.value = p;
      speed.value = s;
    });

    if (response.status === "SUCCESS") {
      console.log("Upload success");
    } else {
      console.error("Upload failed:", response.message);
    }
  } catch (error) {
    console.error("Upload error:", error);
  } finally {
    uploading.value = false;
  }
}
</script>

<template>
  <v-card class="pa-4" max-width="500">
    <v-file-input
      v-model="file"
      label="Select file"
      prepend-icon="mdi-upload"
      variant="outlined"
      :multiple="false"
    />

    <v-btn
      class="mt-4"
      color="primary"
      :disabled="!file || uploading"
      @click="uploadFile"
    >
      Upload
    </v-btn>

    <div v-if="uploading" class="mt-4">
      <v-progress-linear
        :model-value="progress"
        height="10"
        striped
        color="blue"
      />

      <div class="mt-2 text-caption">
        {{ progress }}% • {{ speed }}
      </div>
    </div>
  </v-card>
</template>