import { useApi } from "../../../composables/useApi";
import { HttpMethod } from "../../../enums/httpMethodEnum";
import router from "../../../router";
import { ROUTES_ENUM } from "../../../utils/constants";
import { useAuthStore } from "../stores/auth";
import type { ILoginRequest } from "../types/login-request";
import type { ILoginResponse } from "../types/login-response";

class AuthService {
  async login(payload: ILoginRequest) {
    const authStore = useAuthStore();
    authStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<ILoginRequest, ILoginResponse>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.AUTH.BASE +
          ROUTES_ENUM.AUTH.LOGIN,
        HttpMethod.POST,
        payload,
      );
      console.log(response);

      if (response.status === "SUCCESS" && response.data) {
        authStore.setAuth(payload.username, response.data);
        router.push({ path: ROUTES_ENUM.HOME });
      }

      return response;
    } finally {
      authStore.setLoading(false);
    }
  }

  async logout() {
    const authStore = useAuthStore();

    try {
      await useApi(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.AUTH.BASE +
          ROUTES_ENUM.AUTH.LOGOUT,
        HttpMethod.POST,
      );
    } catch {
    } finally {
      authStore.clear();
      router.push({ path: ROUTES_ENUM.AUTH.BASE + ROUTES_ENUM.AUTH.LOGIN });
    }
  }
}

export default new AuthService();
