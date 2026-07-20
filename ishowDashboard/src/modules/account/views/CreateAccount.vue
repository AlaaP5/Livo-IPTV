<script setup lang="ts">
import { reactive, ref, watch } from "vue";
import { rules } from "../../../composables/validations/rules";
import accountService from "../services/accountService";
import { useAccountStore } from "../stores/account";
import type { IAccountRequest } from "../types/account-request";
import { accountRole } from "../../../enums/AccountRoleEnum";
import { accountStatus } from "../../../enums/AccountStatusEnum";

interface Props {
  modelValue: boolean;
}

const props = defineProps<Props>();
const emit = defineEmits<{
  (e: "update:modelValue", value: boolean): void;
  (e: "created"): void;
}>();

const isValid = ref(false);

const accountStore = useAccountStore();

const roles = accountRole;

const formRef = ref();

const showPassword = ref(false);

const form = reactive<IAccountRequest>({
  id: 0,
  username: "",
  password: "",
  role: "",
  status: "",
});

const cases = accountStatus;

watch(
  () => props.modelValue,
  (val) => {
    if (!val) resetForm();
  },
);

function resetForm() {
  form.id = 0;
  form.username = "";
  form.password = "";
  form.role = "";
  form.status = "";
}

async function submit() {
  const valid = await formRef.value?.validate();
  if (!valid) return;

  await accountService.createAccount(form);
  emit("created");
  emit("update:modelValue", false);
}
</script>

<template>
  <v-dialog :model-value="modelValue" max-width="600" persistent>
    <v-card prepend-icon="mdi-plus" title="Create Account">
      <v-divider />

      <v-form ref="formRef" v-model="isValid">
        <v-card-text>
          <v-row>
            <v-col cols="12">
              <v-text-field
                v-model="form.username"
                label="User Name"
                variant="outlined"
                :rules="[rules.required]"
              />
            </v-col>

            <v-col cols="12">
              <v-text-field
                v-model="form.password"
                label="Password"
                :type="showPassword ? 'text' : 'password'"
                variant="outlined"
                :rules="[rules.required]"
                :append-inner-icon="showPassword ? 'mdi-eye-off' : 'mdi-eye'"
                @click:append-inner="showPassword = !showPassword"
              />
            </v-col>

            <v-col cols="12">
              <v-select
                v-model="form.role"
                label="Account Role"
                :items="roles"
                item-value="value"
                item-title="title"
                variant="outlined"
                clearable
                :rules="[rules.required]"
              />
            </v-col>

            <v-col cols="12">
              <v-select
                v-model="form.status"
                label="Account Status"
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
            :loading="accountStore.loading"
            @click="submit"
          >
            Create
          </v-btn>
        </v-card-actions>
      </v-form>
    </v-card>
  </v-dialog>
</template>
