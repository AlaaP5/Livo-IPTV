<script setup lang="ts">
import { computed } from "vue";
import kidsService from "../services/kidsService";
import type { IKidsBannerResponse } from "../types/kids-response";
import { useKidsStore } from "../stores/kids";

const dialog = defineModel<boolean>({
  required: true,
});

const kidsStore = useKidsStore();

const props = defineProps<{
  bannerItem: IKidsBannerResponse | null;
}>();

const emit = defineEmits<{
  removed: [];
}>();

const removeBanner = async () => {
  if (!props.bannerItem) return;

  try {
    await kidsService.removeBanner(
      props.bannerItem.contentId,
      props.bannerItem.contentType,
    );

    emit("removed");

    dialog.value = false;
  } catch (error) {
    console.error(error);
  }
};
</script>

<template>
  <v-dialog v-model="dialog" max-width="500">
    <v-card prepend-icon="mdi-delete" title="Remove Banner">
      <v-card-text v-if="bannerItem"> Are you sure you want to remove this banner? </v-card-text>

      <v-card-actions class="justify-end">
        <v-btn color="grey" variant="text" @click="dialog = false">
          Cancel
        </v-btn>

        <v-btn color="red" :loading="kidsStore.loading" @click="removeBanner">
          Remove
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>
