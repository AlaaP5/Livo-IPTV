import { useApi } from "../../../composables/useApi";
import { HttpMethod } from "../../../enums/httpMethodEnum";
import { ROUTES_ENUM, SUCCES_MESSAGE } from "../../../utils/constants";
import { useToast } from "vue-toastification";
import type { IFilterTagRequest } from "../types/filter-request";
import { useTagStore } from "../stores/tag";
import type { ITagResponse } from "../types/tag-response";
import type { ITagRequest } from "../types/tag-request";
import type { IActivateRequest } from "../../../types/active.types";

class TagService {
  async filterTags(payload: IFilterTagRequest) {
    const tagStore = useTagStore();
    tagStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<IFilterTagRequest, ITagResponse[]>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.TAG.BASE +
          ROUTES_ENUM.TAG.FILTER,
        HttpMethod.GET,
        payload,
      );
      console.log(response);

      return response;
    } finally {
      tagStore.setLoading(false);
    }
  }

  async createTag(payload: ITagRequest) {
    const toast = useToast();
    const tagStore = useTagStore();
    tagStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<ITagRequest, ITagResponse>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.TAG.BASE +
          ROUTES_ENUM.TAG.CREATE,
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
      tagStore.setLoading(false);
    }
  }

  async updateTag(payload: ITagRequest) {
    const toast = useToast();
    const tagStore = useTagStore();
    tagStore.setLoading(true);

    try {
      const response = await useApi<ITagRequest, ITagResponse>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.TAG.BASE +
          ROUTES_ENUM.TAG.UPDATE,
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
      tagStore.setLoading(false);
    }
  }

  async activateTag(id: number, payload: IActivateRequest) {
    const toast = useToast();
    const tagStore = useTagStore();
    tagStore.setLoading(true);

    try {
      const response = await useApi<IActivateRequest, ITagResponse>(
        `${ROUTES_ENUM.ISHOW}${ROUTES_ENUM.ADMIN}${ROUTES_ENUM.TAG.BASE}/${id}${ROUTES_ENUM.TAG.ACTIVATE}`,
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
      tagStore.setLoading(false);
    }
  }
}

export default new TagService();
