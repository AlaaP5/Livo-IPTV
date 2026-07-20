import { useApi } from "../../../composables/useApi";
import { HttpMethod } from "../../../enums/httpMethodEnum";
import { ROUTES_ENUM, SUCCES_MESSAGE } from "../../../utils/constants";
import { useToast } from "vue-toastification";
import type { IActivateRequest } from "../../../types/active.types";
import type { IFilterChampionRequest } from "../types/filter-request";
import { useChampionStore } from "../stores/champion";
import type { IChampionResponse } from "../types/champion-response";
import type { IChampionRequest } from "../types/champion-request";

class ChampionService {
  async filterChampions(payload: IFilterChampionRequest) {
    const championStore = useChampionStore();
    championStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<
        IFilterChampionRequest,
        IChampionResponse[]
      >(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.CHAMPION.BASE +
          ROUTES_ENUM.CHAMPION.FILTER,
        HttpMethod.GET,
        payload,
      );
      console.log(response);

      return response;
    } finally {
      championStore.setLoading(false);
    }
  }

  async createChampion(payload: IChampionRequest) {
    const toast = useToast();
    const championStore = useChampionStore();
    championStore.setLoading(true);

    try {
      const formData = new FormData();

      if (!payload.imagePath) {
        throw new Error("No image selected");
      }

      formData.append("nameEn", payload.nameEn || "");
      formData.append("nameAr", payload.nameAr || "");
      formData.append("active", payload.active || "");
      formData.append("imagePath", payload.imagePath);

      console.log("FORM DATA:", [...formData.entries()]);

      const response = await useApi<FormData, IChampionResponse>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.CHAMPION.BASE +
          ROUTES_ENUM.CHAMPION.CREATE,
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
      championStore.setLoading(false);
    }
  }

  async updateChampion(payload: IChampionRequest) {
    const toast = useToast();
    const championStore = useChampionStore();
    championStore.setLoading(true);

    try {
      const formData = new FormData();

      formData.append("id", payload.id.toString() || "");
      formData.append("nameEn", payload.nameEn || "");
      formData.append("nameAr", payload.nameAr || "");
      formData.append("active", payload.active || "");

      if (payload.imagePath) {
        formData.append("imagePath", payload.imagePath);
      }
      const response = await useApi<FormData, IChampionResponse>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.CHAMPION.BASE +
          ROUTES_ENUM.CHAMPION.UPDATE,
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
      championStore.setLoading(false);
    }
  }

  async activateChampion(id: number, payload: IActivateRequest) {
    const toast = useToast();
    const championStore = useChampionStore();
    championStore.setLoading(true);

    try {
      const response = await useApi<IActivateRequest, IChampionResponse>(
        `${ROUTES_ENUM.ISHOW}${ROUTES_ENUM.ADMIN}${ROUTES_ENUM.CHAMPION.BASE}/${id}${ROUTES_ENUM.CHAMPION.ACTIVATE}`,
        HttpMethod.GET,
        payload,
      );
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
      championStore.setLoading(false);
    }
  }
}

export default new ChampionService();
