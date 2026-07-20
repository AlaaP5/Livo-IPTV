import { useApi } from "../../../composables/useApi";
import { HttpMethod } from "../../../enums/httpMethodEnum";
import { ROUTES_ENUM, SUCCES_MESSAGE } from "../../../utils/constants";
import { useToast } from "vue-toastification";
import type { IActivateRequest } from "../../../types/active.types";
import { useUpcomingMatchStore } from "../stores/upcoming-match";
import type { IFilterUpcomingMatchRequest } from "../types/filter-request";
import type { IUpcomingMatchResponse } from "../types/upcomingMatch-response";
import type { IUpcomingMatchRequest } from "../types/upcomingMatch-request";

class UpcomingMatchService {
  async filterUpcomingMatch(payload: IFilterUpcomingMatchRequest) {
    const upcomingStore = useUpcomingMatchStore();
    upcomingStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<
        IFilterUpcomingMatchRequest,
        IUpcomingMatchResponse[]
      >(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.UPCOMING_MATCH.BASE +
          ROUTES_ENUM.UPCOMING_MATCH.FILTER,
        HttpMethod.POST,
        payload,
      );
      console.log(response);

      return response;
    } finally {
      upcomingStore.setLoading(false);
    }
  }

  async createUpcomingMatch(payload: IUpcomingMatchRequest) {
    const toast = useToast();
    const upcomingStore = useUpcomingMatchStore();
    upcomingStore.setLoading(true);

    try {

      const response = await useApi<IUpcomingMatchRequest, IUpcomingMatchResponse>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.UPCOMING_MATCH.BASE +
          ROUTES_ENUM.UPCOMING_MATCH.CREATE,
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
      upcomingStore.setLoading(false);
    }
  }

  async updateUpcomingMatch(payload: IUpcomingMatchRequest) {
    const toast = useToast();
    const upcomingStore = useUpcomingMatchStore();
    upcomingStore.setLoading(true);

    try {

      const response = await useApi<IUpcomingMatchRequest, IUpcomingMatchResponse>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.UPCOMING_MATCH.BASE +
          ROUTES_ENUM.UPCOMING_MATCH.UPDATE,
        HttpMethod.POST,
        payload,
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
      upcomingStore.setLoading(false);
    }
  }

  async activateUpcomingMatch(id: number, payload: IActivateRequest) {
    const toast = useToast();
    const upcomingStore = useUpcomingMatchStore();
    upcomingStore.setLoading(true);

    try {
      const response = await useApi<IActivateRequest, IUpcomingMatchResponse>(
        `${ROUTES_ENUM.ISHOW}${ROUTES_ENUM.ADMIN}${ROUTES_ENUM.UPCOMING_MATCH.BASE}/${id}${ROUTES_ENUM.UPCOMING_MATCH.ACTIVATE}`,
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
      upcomingStore.setLoading(false);
    }
  }
}

export default new UpcomingMatchService();
