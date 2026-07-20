import { useApi } from "../../../composables/useApi";
import { HttpMethod } from "../../../enums/httpMethodEnum";
import { ROUTES_ENUM, SUCCES_MESSAGE } from "../../../utils/constants";
import { useToast } from "vue-toastification";
import type { IActivateRequest } from "../../../types/active.types";
import { useChannelEpgStore } from "../stores/channelEpg";
import type { IFilterChannelEpgRequest } from "../types/filter-request";
import type { IChannelEpgResponse } from "../types/epg-response";
import type {
  IChannelEpgRequest,
  IUploadEpgRequest,
} from "../types/epg-request";

class ChannelEpgService {
  async filterChannelEpgs(payload: IFilterChannelEpgRequest) {
    const epgStore = useChannelEpgStore();
    epgStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<
        IFilterChannelEpgRequest,
        IChannelEpgResponse[]
      >(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.CHANNEL_EPG.BASE +
          ROUTES_ENUM.CHANNEL_EPG.FILTER,
        HttpMethod.GET,
        payload,
      );
      console.log(response);

      return response;
    } finally {
      epgStore.setLoading(false);
    }
  }

  async createChannelEpg(payload: IChannelEpgRequest) {
    const toast = useToast();
    const epgStore = useChannelEpgStore();
    epgStore.setLoading(true);

    try {
      console.log(payload);

      const response = await useApi<IChannelEpgRequest, IChannelEpgResponse>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.CHANNEL_EPG.BASE +
          ROUTES_ENUM.CHANNEL_EPG.CREATE,
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
      epgStore.setLoading(false);
    }
  }

  async updateChannelEpg(payload: IChannelEpgRequest) {
    const toast = useToast();
    const epgStore = useChannelEpgStore();
    epgStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<IChannelEpgRequest, IChannelEpgResponse>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.CHANNEL_EPG.BASE +
          ROUTES_ENUM.CHANNEL_EPG.UPDATE,
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
      epgStore.setLoading(false);
    }
  }

  async activateChannelEpg(id: string, payload: IActivateRequest) {
    const toast = useToast();
    const epgStore = useChannelEpgStore();
    epgStore.setLoading(true);

    try {
      const response = await useApi<IActivateRequest, IChannelEpgResponse>(
        `${ROUTES_ENUM.ISHOW}${ROUTES_ENUM.ADMIN}${ROUTES_ENUM.CHANNEL_EPG.BASE}/${id}${ROUTES_ENUM.CHANNEL_EPG.ACTIVATE}`,
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
      epgStore.setLoading(false);
    }
  }

  async uploadChannelEpg(payload: IUploadEpgRequest) {
    const toast = useToast();
    const epgStore = useChannelEpgStore();

    epgStore.setLoading(true);

    try {
      const formData = new FormData();

      formData.append("file", payload.file);
      formData.append("channelId", payload.channelId);

      const response = await useApi<FormData, IChannelEpgResponse>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.CHANNEL_EPG.BASE +
          ROUTES_ENUM.CHANNEL_EPG.UPLOAD,
        HttpMethod.POST,
        formData,
      );

      console.log(response);

      if (response.status === SUCCES_MESSAGE) {
        toast.success(response.message || "Upload successful", {
          timeout: 5000,
        });
      } else {
        toast.error(response.message || "Upload failed", {
          timeout: 5000,
        });
      }

      return response;
    } finally {
      epgStore.setLoading(false);
    }
  }
}

export default new ChannelEpgService();
