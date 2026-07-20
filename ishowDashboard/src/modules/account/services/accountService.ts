import { useApi } from "../../../composables/useApi";
import { HttpMethod } from "../../../enums/httpMethodEnum";
import { ROUTES_ENUM, SUCCES_MESSAGE } from "../../../utils/constants";
import { useToast } from "vue-toastification";
import type { IFilterAccountRequest } from "../types/filter-request";
import { useAccountStore } from "../stores/account";
import type { IAccountResponse } from "../types/account-response";
import type { IAccountRequest } from "../types/account-request";
import type { IChangePasswordRequest } from "../types/change-password-request";
import type { IActivateAccountRequest } from "../types/activate-request";

class AccountService {
  async filterAccounts(payload: IFilterAccountRequest) {
    const accountStore = useAccountStore();
    accountStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<IFilterAccountRequest, IAccountResponse[]>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.ACCOUNT.BASE +
          ROUTES_ENUM.ACCOUNT.FILTER,
        HttpMethod.GET,
        payload,
      );
      console.log(response);

      return response;
    } finally {
      accountStore.setLoading(false);
    }
  }

  async createAccount(payload: IAccountRequest) {
    const toast = useToast();
    const accountStore = useAccountStore();
    accountStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<IAccountRequest, IAccountResponse>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.ACCOUNT.BASE +
          ROUTES_ENUM.ACCOUNT.CREATE,
        HttpMethod.POST,
        payload,
      );
      console.log(response);

      if (response.status === SUCCES_MESSAGE) {
        toast.success(response.message || "Created successfully", {
          timeout: 5000,
        });
      } else {
        toast.error(response.message || "Created failure", {
          timeout: 5000,
        });
      }
    } finally {
      accountStore.setLoading(false);
    }
  }

  async changePassword(payload: IChangePasswordRequest) {
    const toast = useToast();
    const accountStore = useAccountStore();
    accountStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<IChangePasswordRequest, IAccountResponse>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.ACCOUNT.BASE +
          ROUTES_ENUM.ACCOUNT.CHANGE_PASSWORD,
        HttpMethod.POST,
        payload,
      );
      console.log(response);

      if (response.status === SUCCES_MESSAGE) {
        toast.success(response.message || "Updated successfully", {
          timeout: 5000,
        });
      } else {
        toast.error(response.message || "Updated failure", {
          timeout: 5000,
        });
      }

      return response;
    } finally {
      accountStore.setLoading(false);
    }
  }

  async activateAccount(id: number, payload: IActivateAccountRequest) {
    const toast = useToast();
    const accountStore = useAccountStore();
    accountStore.setLoading(true);

    try {
      const response = await useApi<IActivateAccountRequest, IAccountResponse>(
        `${ROUTES_ENUM.ISHOW}${ROUTES_ENUM.ADMIN}${ROUTES_ENUM.ACCOUNT.BASE}/${id}${ROUTES_ENUM.ACCOUNT.ACTIVATE}`,
        HttpMethod.GET,
        payload,
      );
      console.log(id);
      console.log(payload);
      console.log(response);

      if (response.status === SUCCES_MESSAGE) {
        toast.success(response.message || "Status updated successfully", {
          timeout: 5000,
        });
      } else {
        toast.error(response.message || "Status update failed", {
          timeout: 5000,
        });
      }

      return response;
    } finally {
      accountStore.setLoading(false);
    }
  }
}

export default new AccountService();
