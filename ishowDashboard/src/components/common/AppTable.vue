<script setup lang="ts">
import type { DataTableHeader } from "../../types/data-table-header.types";

interface Props {
  headers: DataTableHeader[];
  items: any[];
  loading?: boolean;

  totalItems?: number;
  page?: number;
  itemsPerPage?: number;

  height?: string | number;
  disableSort?: boolean;
  hideDefaultFooter?: boolean;
}

withDefaults(defineProps<Props>(), {
  loading: false,
  page: 1,
  itemsPerPage: 10,
  totalItems: 0,
  disableSort: true,
});

const emit = defineEmits<{
  (e: "update:page", value: number): void;
  (e: "update:items-per-page", value: number): void;
}>();

function getValue(item: any, key: string) {
  return key.split(".").reduce((obj, k) => obj?.[k], item);
}
</script>

<template>
  <v-card>
    <v-data-table-server
      :headers="headers"
      :items="items"
      :items-length="totalItems"
      :page="page"
      :items-per-page="itemsPerPage"
      :items-per-page-options="[5, 10, 25, 50, 100]"
      :loading="loading"
      :height="height"
      :disable-sort="disableSort"
      class="elevation-0"
      @update:page="emit('update:page', $event)"
      @update:items-per-page="emit('update:items-per-page', $event)"
    >
      <template v-if="hideDefaultFooter" #bottom></template>

      <template
        v-for="header in headers.filter((h) => h.key !== 'actions')"
        :key="header.key"
        #[`item.${header.key}`]="{ item }"
      >
        <slot :name="`item.${header.key}`" :item="item">
          {{ getValue(item, header.key) }}
        </slot>
      </template>

      <!-- ACTIONS COLUMN -->
      <template #item.actions="{ item }">
        <slot name="actions" :item="item" />
      </template>

      <!-- NO DATA -->
      <template #no-data>
        <div class="text-center py-8">
          <v-icon size="64">mdi-inbox</v-icon>
          <p>No data found</p>
        </div>
      </template>

      <!-- LOADING -->
      <template #loading>
        <v-skeleton-loader type="table-row@5" />
      </template>
    </v-data-table-server>
  </v-card>
</template>
