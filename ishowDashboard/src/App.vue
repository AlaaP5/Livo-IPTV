<template>
  <v-container fluid>
    <DefaultLayout v-if="isLogin" />
    <router-view v-else />
  </v-container>
</template>

<script setup lang="ts">
import { computed, onMounted } from "vue";
import DefaultLayout from "./layouts/DefaultLayout.vue";
import { useAuthStore } from "./modules/auth/stores/auth";

const authStore = useAuthStore();

onMounted(() => {
  authStore.initialize();
});

const isLogin = computed(() => authStore.isAuthenticated);
</script>
