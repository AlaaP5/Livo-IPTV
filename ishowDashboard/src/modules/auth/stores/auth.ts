import { defineStore } from "pinia";
import { ref, computed } from "vue";
import type { ILoginResponse } from "../types/login-response";

export const useAuthStore = defineStore("auth", () => {
  const auth = ref<ILoginResponse | null>(null);
  const username = ref<string | null>("");
  const loading = ref(false);

  const isAuthenticated = computed(() => !!auth.value?.access_token);

  function setAuth(user_name: string, authData: ILoginResponse | null) {
    auth.value = authData;
    username.value = user_name;

    if (authData) {
      localStorage.setItem("auth", JSON.stringify(authData));
      localStorage.setItem("auth_token", authData.access_token);
      localStorage.setItem("refresh_token", authData.refresh_token);
      localStorage.setItem("role_name", authData.role);
      localStorage.setItem("user_name", user_name);
    } else {
      clear();
    }
  }

  function setLoading(value: boolean) {
    loading.value = value;
  }

  function clear() {
    auth.value = null;
    username.value = null;
    localStorage.removeItem("auth");
    localStorage.removeItem("auth_token");
    localStorage.removeItem("refresh_token");
    localStorage.removeItem("role_name");
    localStorage.removeItem("user_name");
  }

  function initialize() {
    try {
      const saved = localStorage.getItem("auth");
      if (!saved) return;

      auth.value = JSON.parse(saved);
      username.value = localStorage.getItem("user_name");

      if (auth.value?.access_token) {
        localStorage.setItem("auth_token", auth.value.access_token);
      } else {
        clear();
      }
    } catch {
      clear();
    }
  }

  return {
    auth,
    username,
    loading,
    isAuthenticated,
    setAuth,
    setLoading,
    clear,
    initialize,
  };
});
