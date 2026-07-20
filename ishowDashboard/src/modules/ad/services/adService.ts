import { useApi } from "../../../composables/useApi";
import { HttpMethod } from "../../../enums/httpMethodEnum";
import { ROUTES_ENUM, SUCCES_MESSAGE } from "../../../utils/constants";
import { useToast } from "vue-toastification";
import type { IActivateRequest } from "../../../types/active.types";
import type { IFilterAdRequest } from "../types/filter-request";
import { useAdStore } from "../stores/ad";
import type { IAdResponse } from "../types/ad-response";
import type { IAdRequest } from "../types/ad-request";

class AdService {
  async filterAds(payload: IFilterAdRequest) {
    const adStore = useAdStore();
    adStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<IFilterAdRequest, IAdResponse[]>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.AD.BASE +
          ROUTES_ENUM.AD.FILTER,
        HttpMethod.GET,
        payload,
      );
      console.log(response);

      return response;
    } finally {
      adStore.setLoading(false);
    }
  }

  async createAd(payload: IAdRequest) {
    const toast = useToast();
    const adStore = useAdStore();
    adStore.setLoading(true);

    try {
      const formData = new FormData();

      if (!payload.imagePath) {
        throw new Error("No image selected");
      }

      formData.append("deepLink", payload.deepLink || "");
      formData.append("startDate", payload.startDate || "");
      formData.append("endDate", payload.endDate || "");
      formData.append("active", payload.active || "");
      formData.append("imagePath", payload.imagePath);

      console.log("FORM DATA:", [...formData.entries()]);

      const response = await useApi<FormData, IAdResponse>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.AD.BASE +
          ROUTES_ENUM.AD.CREATE,
        HttpMethod.POST,
        formData,
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
      adStore.setLoading(false);
    }
  }

  async updateAd(payload: IAdRequest) {
    const toast = useToast();
    const adStore = useAdStore();
    adStore.setLoading(true);

    try {
      const formData = new FormData();

      formData.append("id", payload.id.toString() || "");
      formData.append("deepLink", payload.deepLink || "");
      formData.append("startDate", payload.startDate || "");
      formData.append("endDate", payload.endDate || "");
      formData.append("active", payload.active || "");

      if (payload.imagePath) {
        formData.append("imagePath", payload.imagePath);
      }
      const response = await useApi<FormData, IAdResponse>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.AD.BASE +
          ROUTES_ENUM.AD.UPDATE,
        HttpMethod.POST,
        formData,
      );

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
      adStore.setLoading(false);
    }
  }

  async activateAd(id: number, payload: IActivateRequest) {
    const toast = useToast();
    const adStore = useAdStore();
    adStore.setLoading(true);

    try {
      const response = await useApi<IActivateRequest, IAdResponse>(
        `${ROUTES_ENUM.ISHOW}${ROUTES_ENUM.ADMIN}${ROUTES_ENUM.AD.BASE}/${id}${ROUTES_ENUM.AD.ACTIVATE}`,
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
      adStore.setLoading(false);
    }
  }
}

export default new AdService();
