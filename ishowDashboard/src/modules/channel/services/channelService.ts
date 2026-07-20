import { useApi } from "../../../composables/useApi";
import { HttpMethod } from "../../../enums/httpMethodEnum";
import { ROUTES_ENUM, SUCCES_MESSAGE } from "../../../utils/constants";
import { useToast } from "vue-toastification";
import { useChannelStore } from "../stores/channel";
import type { IFilterChannelRequest } from "../types/filter-request";
import type {
  IChannelResponse,
  ISectionChannelsResponse,
} from "../types/channel-response";
import type {
  IAddChannelToSectionRequest,
  IChannelRequest,
  IChannelSectionRequest,
  IRemoveChannelFromSectionRequest,
} from "../types/channel-request";
import type { IActivateRequest } from "../../../types/active.types";

class ChannelService {
  async filterChannels(payload: IFilterChannelRequest) {
    const channelStore = useChannelStore();
    channelStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<IFilterChannelRequest, IChannelResponse[]>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.CHANNEL.BASE +
          ROUTES_ENUM.CHANNEL.FILTER,
        HttpMethod.GET,
        payload,
      );
      console.log(response);

      return response;
    } finally {
      channelStore.setLoading(false);
    }
  }

  async createChannel(payload: IChannelRequest) {
    const toast = useToast();
    const channelStore = useChannelStore();
    channelStore.setLoading(true);

    try {
      const formData = new FormData();

      if (!payload.logo) {
        throw new Error("No logo selected");
      }

      formData.append("titleEn", payload.titleEn || "");
      formData.append("titleAr", payload.titleAr || "");
      formData.append("descriptionEn", payload.descriptionEn || "");
      formData.append("descriptionAr", payload.descriptionAr || "");
      formData.append("badge", payload.badge || "");
      formData.append("active", payload.active || "");
      formData.append("hasRight", payload.hasRight || "");
      formData.append("isKids", payload.isKids || "");
      formData.append("isSports", payload.isSports || "");

      formData.append("logo", payload.logo);

      if (payload.tags && payload.tags.length) {
        payload.tags?.forEach((tagId) => {
          formData.append("tags", String(tagId));
        });
      }

      console.log("FORM DATA:", [...formData.entries()]);

      const response = await useApi<FormData, IChannelResponse>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.CHANNEL.BASE +
          ROUTES_ENUM.CHANNEL.CREATE,
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
      channelStore.setLoading(false);
    }
  }

  async updateChannel(payload: IChannelRequest) {
    const toast = useToast();
    const channelStore = useChannelStore();
    channelStore.setLoading(true);

    try {
      const formData = new FormData();

      if (payload.logo) {
        formData.append("logo", payload.logo);
      }

      formData.append("id", payload.id || "");
      formData.append("titleEn", payload.titleEn || "");
      formData.append("titleAr", payload.titleAr || "");
      formData.append("descriptionEn", payload.descriptionEn || "");
      formData.append("descriptionAr", payload.descriptionAr || "");
      formData.append("badge", payload.badge || "");
      formData.append("active", payload.active || "");
      formData.append("hasRight", payload.hasRight || "");
      formData.append("isKids", payload.isKids || "");
      formData.append("isSports", payload.isSports || "");

      if (payload.tags && payload.tags.length) {
        payload.tags?.forEach((tagId) => {
          formData.append("tags", String(tagId));
        });
      }

      console.log("FORM DATA:", [...formData.entries()]);

      const response = await useApi<FormData, IChannelResponse>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.CHANNEL.BASE +
          ROUTES_ENUM.CHANNEL.UPDATE,
        HttpMethod.POST,
        formData,
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
      channelStore.setLoading(false);
    }
  }

  async activateChannel(id: string, payload: IActivateRequest) {
    const toast = useToast();
    const channelStore = useChannelStore();
    channelStore.setLoading(true);

    try {
      const response = await useApi<IActivateRequest, IChannelResponse>(
        `${ROUTES_ENUM.ISHOW}${ROUTES_ENUM.ADMIN}${ROUTES_ENUM.CHANNEL.BASE}/${id}${ROUTES_ENUM.CHANNEL.ACTIVATE}`,
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
      channelStore.setLoading(false);
    }
  }

  async publishChannel(id: string) {
    const toast = useToast();
    const channelStore = useChannelStore();
    channelStore.setLoading(true);

    try {
      const response = await useApi<null, IChannelResponse>(
        `${ROUTES_ENUM.ISHOW}${ROUTES_ENUM.ADMIN}${ROUTES_ENUM.CHANNEL.BASE}${ROUTES_ENUM.CHANNEL.PUBLISH}/${id}`,
        HttpMethod.GET,
        null,
      );
      console.log(id);
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
      channelStore.setLoading(false);
    }
  }

  async viewSectionChannels(payload: IChannelSectionRequest) {
    const channelStore = useChannelStore();
    channelStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<
        IChannelSectionRequest,
        ISectionChannelsResponse[]
      >(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.CHANNEL.BASE +
          ROUTES_ENUM.CHANNEL.VIEW_SECTION_CHANNELS,
        HttpMethod.GET,
        payload,
      );

      console.log(response);

      return response;
    } finally {
      channelStore.setLoading(false);
    }
  }

  async addChannelToSection(payload: IAddChannelToSectionRequest) {
    const toast = useToast();
    const channelStore = useChannelStore();
    channelStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<IAddChannelToSectionRequest, null>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.CHANNEL.BASE +
          ROUTES_ENUM.CHANNEL.ADD_CHANNEL_TO_SECTION,
        HttpMethod.GET,
        payload,
      );
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
      channelStore.setLoading(false);
    }
  }

  async removeChannelFromSection(payload: IRemoveChannelFromSectionRequest) {
    const toast = useToast();
    const channelStore = useChannelStore();
    channelStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<IRemoveChannelFromSectionRequest, null>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.CHANNEL.BASE +
          ROUTES_ENUM.CHANNEL.REMOVE_CHANNEL_FROM_SECTION,
        HttpMethod.GET,
        payload,
      );
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
      console.log(response);

      return response;
    } finally {
      channelStore.setLoading(false);
    }
  }

  async changeStatusChannel(payload: IAddChannelToSectionRequest) {
    const toast = useToast();
    const channelStore = useChannelStore();
    channelStore.setLoading(true);

    try {
      const url = `${ROUTES_ENUM.ISHOW}${ROUTES_ENUM.ADMIN}${ROUTES_ENUM.CHANNEL.BASE}${ROUTES_ENUM.CHANNEL.SECTION_IS_TOP}?sectionId=${payload.sectionId}&channelId=${payload.channelId}&isTop=${payload.isTop}`;

      const response = await useApi<null, null>(url, HttpMethod.PUT, null);

      if (response.status === SUCCES_MESSAGE) {
        toast.success(response.message || "Status updated successfully", {
          timeout: 5000,
        });
      } else {
        toast.error(response.message || "Status update failed", {
          timeout: 5000,
        });
      }
      console.log(response);

      return response;
    } finally {
      channelStore.setLoading(false);
    }
  }
}

export default new ChannelService();
