<script setup lang="ts">
import { reactive, ref } from "vue";
import { useAuthStore } from "../stores/auth";
import authService from "../services/authService";
import type { ILoginRequest } from "../types/login-request";

const authStore = useAuthStore();

const showPassword = ref(false);

const isValid = ref(true);

const credentials = reactive<ILoginRequest>({
  username: "",
  password: "",
});

const rules = {
  username: [(v: string) => !!v || "the username is required"],
  password: [
    (v: string) => !!v || "the password is required",
    (v: string) => v.length >= 5 || "the password must be more 4 characters",
  ],
};

async function handleLogin() {
  await authService.login(credentials);
}
</script>

<template>
  <v-container>
    <v-row justify="center">
      <v-col cols="12" sm="8" md="6" lg="4">
        <div class="text-center mb-8">
          <h1 class="text-h5 font-weight-bold mb-2 mt-15">Ishow Dashboard</h1>
          <p class="text-grey">Log in to continue</p>
        </div>

        <v-card elevation="8" rounded="lg">
          <v-card-text class="pa-8">
            <v-form ref="form" v-model="isValid">
              <v-text-field
                v-model="credentials.username"
                label="Username"
                prepend-inner-icon="mdi-account"
                :rules="rules.username"
                class="mb-2"
              />

              <v-text-field
                v-model="credentials.password"
                label="Password"
                prepend-inner-icon="mdi-lock"
                :type="showPassword ? 'text' : 'password'"
                :append-inner-icon="showPassword ? 'mdi-eye-off' : 'mdi-eye'"
                :rules="rules.password"
                @click:append-inner="showPassword = !showPassword"
                class="mb-4"
              />

              <v-btn
                color="primary"
                size="large"
                block
                :loading="authStore.loading"
                :disabled="!isValid"
                @click="handleLogin"
              >
                <v-icon start>mdi-login</v-icon>
                log in
              </v-btn>
            </v-form>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<style scoped>
.v-main {
  min-height: 100vh;
}
</style>
