import { useApi } from "../../../composables/useApi";
import { HttpMethod } from "../../../enums/httpMethodEnum";
import { ROUTES_ENUM, SUCCES_MESSAGE } from "../../../utils/constants";
import { useToast } from "vue-toastification";
import type { IActivateRequest } from "../../../types/active.types";
import type { IFilterTeamRequest } from "../types/filter-request";
import { useTeamStore } from "../stores/team";
import type { ITeamResponse } from "../types/team-response";
import type { ITeamRequest } from "../types/team-request";

class TeamService {
  async filterTeams(payload: IFilterTeamRequest) {
    const teamStore = useTeamStore();
    teamStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<IFilterTeamRequest, ITeamResponse[]>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.TEAM.BASE +
          ROUTES_ENUM.TEAM.FILTER,
        HttpMethod.GET,
        payload,
      );
      console.log(response);

      return response;
    } finally {
      teamStore.setLoading(false);
    }
  }

  async createTeam(payload: ITeamRequest) {
    const toast = useToast();
    const teamStore = useTeamStore();
    teamStore.setLoading(true);

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

      const response = await useApi<FormData, ITeamResponse>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.TEAM.BASE +
          ROUTES_ENUM.TEAM.CREATE,
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
      teamStore.setLoading(false);
    }
  }

  async updateTeam(payload: ITeamRequest) {
    const toast = useToast();
    const teamStore = useTeamStore();
    teamStore.setLoading(true);

    try {
      const formData = new FormData();

      formData.append("id", payload.id.toString() || "");
      formData.append("nameEn", payload.nameEn || "");
      formData.append("nameAr", payload.nameAr || "");
      formData.append("active", payload.active || "");

      if (payload.imagePath) {
        formData.append("imagePath", payload.imagePath);
      }
      const response = await useApi<FormData, ITeamResponse>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.TEAM.BASE +
          ROUTES_ENUM.TEAM.UPDATE,
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
      teamStore.setLoading(false);
    }
  }

  async activateTeam(id: number, payload: IActivateRequest) {
    const toast = useToast();
    const teamStore = useTeamStore();
    teamStore.setLoading(true);

    try {
      const response = await useApi<IActivateRequest, ITeamResponse>(
        `${ROUTES_ENUM.ISHOW}${ROUTES_ENUM.ADMIN}${ROUTES_ENUM.TEAM.BASE}/${id}${ROUTES_ENUM.TEAM.ACTIVATE}`,
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
      teamStore.setLoading(false);
    }
  }

  async bulkUploadTeams(zipFile: File) {
    const toast = useToast();
    const teamStore = useTeamStore();

    teamStore.setLoading(true);

    try {
      const formData = new FormData();

      formData.append("zip", zipFile);

      const response = await useApi<FormData, number>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.TEAM.BASE +
          ROUTES_ENUM.TEAM.BULK_UPLOAD,
        HttpMethod.POST,
        formData,
      );

      console.log(response);

      if (response.status === SUCCES_MESSAGE) {
        toast.success(response.message || "Teams uploaded successfully", {
          timeout: 5000,
        });
      } else {
        toast.error(response.message || "Bulk upload failed", {
          timeout: 5000,
        });
      }

      return response;
    } finally {
      teamStore.setLoading(false);
    }
  }
}

export default new TeamService();
