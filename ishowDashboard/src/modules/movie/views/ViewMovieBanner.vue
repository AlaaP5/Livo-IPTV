<script setup lang="ts">
import { ref, onMounted } from "vue";
import { MobilePage } from "../../../enums/MobilePageEnum";
import { ContentType } from "../../../enums/ContentTypeEnum";
import type { ISectionResponse } from "../../section/types/section-response";
import type { IFilterSectionRequest } from "../../section/types/filter-request";
import sectionService from "../../section/services/sectionService";
import router from "../../../router";

const isLoading = ref(false);
const section = ref<ISectionResponse | null>(null);

const credentials: IFilterSectionRequest = {
  page: 1,
  size: 10,
  active: "1",
  publish: "1",
  title: "BANNER",
  contentType: ContentType.MOVIES,
  sectionPage: MobilePage.MOVIES,
};

function goToViewSectionMovies(section: ISectionResponse) {
  if (!section) return;
  router.push({
    name: "View Movies For Section",
    params: {
      sectionId: section.id,
      sectionName: section.titleEn,
    },
  });
}

async function fetchSection() {
  isLoading.value = true;
  try {
    const response = await sectionService.filterSections(credentials);
    if (response.data && response.data.length > 0) {
      section.value = response.data[0];
    } else {
      section.value = null;
    }
  } catch (error) {
    console.error("Error fetching section:", error);
    section.value = null;
  } finally {
    isLoading.value = false;
  }
}

onMounted(() => {
  fetchSection();
});
</script>

<template>
  <v-container fluid>
    <v-row justify="center">
      <v-col cols="12" md="10" lg="8">
        <!-- Header -->
        <div class="d-flex justify-space-between align-center mb-6">
          <h1 class="title">Movie Banner</h1>
        </div>

        <!-- Loading State -->
        <v-card v-if="isLoading" class="pa-8 text-center" elevation="2">
          <v-progress-circular
            indeterminate
            color="primary"
            size="48"
          ></v-progress-circular>
          <p class="mt-4 text-body-1">Loading section details...</p>
        </v-card>

        <!-- No Data State -->
        <v-card v-else-if="!section" class="pa-8 text-center" elevation="2">
          <v-icon
            icon="mdi-folder-open"
            size="64"
            color="grey-lighten-1"
          ></v-icon>
          <p class="mt-4 text-h6">No section found</p>
          <p class="text-body-2 text-grey">No "BANNER" section exists yet</p>
        </v-card>

        <!-- Section Details Card -->
        <v-card v-else elevation="2">
          <v-card-title class="bg-grey-lighten-4">
            <span class="text-h6 font-weight-bold">Section Details</span>
          </v-card-title>

          <v-card-text class="pa-0">
            <v-list lines="two">
              <v-list-item>
                <template #prepend>
                  <v-icon color="primary">mdi-identifier</v-icon>
                </template>
                <v-list-item-title class="text-caption text-grey"
                  >Section ID</v-list-item-title
                >
                <v-list-item-subtitle class="text-body-1 font-weight-medium">
                  {{ section.id }}
                </v-list-item-subtitle>
              </v-list-item>

              <v-divider inset></v-divider>

              <v-list-item>
                <template #prepend>
                  <v-icon color="primary">mdi-format-title</v-icon>
                </template>
                <v-list-item-title class="text-caption text-grey"
                  >Title (English)</v-list-item-title
                >
                <v-list-item-subtitle class="text-body-1 font-weight-medium">
                  {{ section.titleEn }}
                </v-list-item-subtitle>
              </v-list-item>

              <v-divider inset></v-divider>

              <v-list-item>
                <template #prepend>
                  <v-icon color="primary">mdi-format-title</v-icon>
                </template>
                <v-list-item-title class="text-caption text-grey"
                  >Title (Arabic)</v-list-item-title
                >
                <v-list-item-subtitle class="text-body-1 font-weight-medium">
                  {{ section.titleAr }}
                </v-list-item-subtitle>
              </v-list-item>

              <v-divider inset></v-divider>

              <v-list-item>
                <template #prepend>
                  <v-icon color="primary">mdi-sort</v-icon>
                </template>
                <v-list-item-title class="text-caption text-grey"
                  >Order</v-list-item-title
                >
                <v-list-item-subtitle class="text-body-1 font-weight-medium">
                  {{ section.order }}
                </v-list-item-subtitle>
              </v-list-item>

              <v-divider inset></v-divider>

              <v-list-item>
                <template #prepend>
                  <v-icon color="primary">mdi-newspaper-variant</v-icon>
                </template>
                <v-list-item-title class="text-caption text-grey"
                  >Publish Status</v-list-item-title
                >
                <v-list-item-subtitle>
                  <v-chip
                    :color="section.publish ? 'green' : 'red'"
                    size="small"
                  >
                    {{ section.publish ? "Published" : "Not Published" }}
                  </v-chip>
                </v-list-item-subtitle>
              </v-list-item>

              <v-divider inset></v-divider>

              <v-list-item>
                <template #prepend>
                  <v-icon color="primary">mdi-check-circle</v-icon>
                </template>
                <v-list-item-title class="text-caption text-grey"
                  >Section Status</v-list-item-title
                >
                <v-list-item-subtitle>
                  <v-chip
                    :color="section.active ? 'green' : 'red'"
                    size="small"
                  >
                    {{ section.active ? "Active" : "Not Active" }}
                  </v-chip>
                </v-list-item-subtitle>
              </v-list-item>
            </v-list>
          </v-card-text>

          <!-- Actions -->
          <v-card-actions class="bg-grey-lighten-4 pa-4">
            <v-row dense>
              <v-col cols="12" sm="6" md="3">
                <v-btn
                  block
                  :disabled="!section.publish || !section.active"
                  color="primary"
                  prepend-icon="mdi-eye"
                  @click="goToViewSectionMovies(section)"
                  variant="flat"
                >
                  View Movies
                </v-btn>
              </v-col>
            </v-row>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<style scoped>
.v-list-item {
  min-height: 72px;
}

.v-card-actions {
  border-top: 1px solid rgba(0, 0, 0, 0.12);
}
</style>
