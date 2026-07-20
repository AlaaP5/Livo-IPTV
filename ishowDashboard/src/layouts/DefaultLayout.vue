<template>
  <v-app>
    <AppBar @toggle-drawer="drawer = !drawer" />
    <SideBar v-model="drawer" />

    <v-main>
      <router-view />
    </v-main>
  </v-app>
</template>

<script setup lang="ts">
import { ref } from "vue";
import AppBar from "./AppBar.vue";
import SideBar from "./SideBar.vue";

const drawer = ref(true);
</script>
