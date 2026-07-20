<template>
  <v-app-bar app color="primary" dark style="height: 70px">
    <div
      class="d-flex align-center clickable-title"
      @click="$emit('toggle-drawer')"
    >
      <v-icon class="ml-3 mr-2" color="secondary">mdi-list-box</v-icon>
      <div class="title-rand">Rand</div>
    </div>

    <v-spacer />

    <v-menu>
      <template #activator="{ props }">
        <v-btn
          class="btn"
          style="margin-bottom: 17px; margin-right: 15px"
          v-bind="props"
          icon
        >
          <v-icon color="secondary">mdi-account-circle</v-icon>
        </v-btn>
      </template>

      <v-list>
        <v-list-item>
          <v-icon start color="secondary">mdi-account-circle</v-icon>
          <v-list-item-title style="color: #2b539e; font-weight: bold;">{{ authStore.username }}</v-list-item-title>
        </v-list-item>
        <v-list-item @click="logout">
          <v-icon start color="secondary">mdi-logout</v-icon>
          <v-list-item-title style="color: #2b539e;">Logout</v-list-item-title>
        </v-list-item>
        <v-list-item @click="openChangePassword">
          <v-icon start color="secondary">mdi-lock-reset</v-icon>
          <v-list-item-title style="color: #2b539e;">Change Password</v-list-item-title>
        </v-list-item>
      </v-list>
    </v-menu>
  </v-app-bar>


  <v-dialog v-model="showChangePassword" max-width="500" persistent>
    <v-card prepend-icon="mdi-lock-reset" title="Change Password">
      <v-divider />

      <v-form ref="formRef" v-model="isValid">
        <v-card-text>
          <v-text-field
            v-model="credentials.password"
            label="New Password"
            :type="showPassword ? 'text' : 'password'"
            variant="outlined"
            :append-inner-icon="showPassword ? 'mdi-eye-off' : 'mdi-eye'"
            @click:append-inner="showPassword = !showPassword"
            :rules="[rules.required]"
          />
        </v-card-text>

        <v-card-actions class="justify-end">
          <v-btn variant="text" @click="closeDialog">Cancel</v-btn>
          <v-btn color="secondary" :loading="accountStore.loading" @click="submitPassword"> Save </v-btn>
        </v-card-actions>
      </v-form>
    </v-card>
  </v-dialog>
</template>

<script setup lang="ts">
import { reactive, ref } from "vue";
import authService from "../modules/auth/services/authService";
import { rules } from "../composables/validations/rules";
import accountService from "../modules/account/services/accountService";
import type { IChangePasswordRequest } from "../modules/account/types/change-password-request";
import { useAccountStore } from "../modules/account/stores/account";
import { useAuthStore } from "../modules/auth/stores/auth";

const authStore = useAuthStore();

const showChangePassword = ref(false);
const showPassword = ref(false);
const accountStore = useAccountStore();

const isValid = ref(false);
const formRef = ref<any>(null);

const credentials = reactive<IChangePasswordRequest>({
  password: ""
});

const logout = async () => {
  try {
    await authService.logout();
  } catch (err) {}
};

const openChangePassword = () => {
  credentials.password = "";
  showPassword.value = false;
  showChangePassword.value = true;
};

const closeDialog = () => {
  showChangePassword.value = false;
};

const submitPassword = async () => {
  const valid = await formRef.value?.validate();
  if (!valid) return;

  try {
    await accountService.changePassword(credentials);
    closeDialog();
  } catch (err) {
    console.error("Password change failed", err);
  }
};
</script>

<style scoped>
.clickable-title {
  cursor: pointer;
  height: 100%;
  margin: 5px 0 0 5px;
}

.title-rand {
  font-size: 1.25rem;
  font-weight: bold;
  letter-spacing: 0.5px;
}
</style>
