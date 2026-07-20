import { useApi } from "../../../composables/useApi";
import { HttpMethod } from "../../../enums/httpMethodEnum";
import { ROUTES_ENUM, SUCCES_MESSAGE } from "../../../utils/constants";
import { useToast } from "vue-toastification";
import type { IActorRequest } from "../types/actor-request";
import { useActorStore } from "../stores/actor";
import type { IActorResponse } from "../types/actor-response";
import type { IActivateRequest } from "../../../types/active.types";
import type { IFilterActorRequest } from "../types/filter-request";

class ActorService {
  async filterActors(payload: IFilterActorRequest) {
    const actorStore = useActorStore();
    actorStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<IFilterActorRequest, IActorResponse[]>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.ACTOR.BASE +
          ROUTES_ENUM.ACTOR.FILTER,
        HttpMethod.GET,
        payload,
      );
      console.log(response);

      return response;
    } finally {
      actorStore.setLoading(false);
    }
  }

  async createActor(payload: IActorRequest) {
    const toast = useToast();
    const actorStore = useActorStore();
    actorStore.setLoading(true);

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

      const response = await useApi<FormData, IActorResponse>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.ACTOR.BASE +
          ROUTES_ENUM.ACTOR.CREATE,
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
      actorStore.setLoading(false);
    }
  }

  async updateActor(payload: IActorRequest) {
    const toast = useToast();
    const actorStore = useActorStore();
    actorStore.setLoading(true);

    try {
      const formData = new FormData();

      formData.append("id", payload.id.toString() || "");
      formData.append("nameEn", payload.nameEn || "");
      formData.append("nameAr", payload.nameAr || "");
      formData.append("active", payload.active || "");

      if (payload.imagePath) {
        formData.append("imagePath", payload.imagePath);
      }

      console.log("FORM DATA:", [...formData.entries()]);

      const response = await useApi<FormData, IActorResponse>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.ACTOR.BASE +
          ROUTES_ENUM.ACTOR.UPDATE,
        HttpMethod.POST,
        formData,
      );

      console.log(response)

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
      actorStore.setLoading(false);
    }
  }

  async activateActor(id: number, payload: IActivateRequest) {
    const toast = useToast();
    const actorStore = useActorStore();
    actorStore.setLoading(true);

    try {
      const response = await useApi<IActivateRequest, IActorResponse>(
        `${ROUTES_ENUM.ISHOW}${ROUTES_ENUM.ADMIN}${ROUTES_ENUM.ACTOR.BASE}/${id}${ROUTES_ENUM.ACTOR.ACTIVATE}`,
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
      actorStore.setLoading(false);
    }
  }
}

export default new ActorService();
