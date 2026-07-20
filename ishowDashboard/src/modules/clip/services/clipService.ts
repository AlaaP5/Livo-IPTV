import { useApi } from "../../../composables/useApi";
import { HttpMethod } from "../../../enums/httpMethodEnum";
import { ROUTES_ENUM, SUCCES_MESSAGE } from "../../../utils/constants";
import { useToast } from "vue-toastification";
import type {
  IAddClipToSectionRequest,
  IClipRequest,
  IClipSectionRequest,
  IRemoveClipFromSectionRequest,
} from "../types/clip-request";
import { useClipStore } from "../stores/clip";
import type {
  IClipResponse,
  IFilterClipResponse,
  ISectionClipResponse,
} from "../types/clip-response";
import type { IFilterClipRequest } from "../types/filter-request";

class ClipService {
  async filterClips(payload: IFilterClipRequest) {
    const clipStore = useClipStore();
    clipStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<IFilterClipRequest, IFilterClipResponse[]>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.CLIP.BASE +
          ROUTES_ENUM.CLIP.FILTER,
        HttpMethod.POST,
        payload,
      );
      console.log(response);

      return response;
    } finally {
      clipStore.setLoading(false);
    }
  }

  async createClip(payload: IClipRequest) {
    const toast = useToast();
    const clipStore = useClipStore();
    clipStore.setLoading(true);

    try {
      const formData = new FormData();

      if (payload.file) {
        formData.append("file", payload.file);
      }

      if (payload.poster) {
        formData.append("poster", payload.poster);
      }

      formData.append("titleEn", payload.titleEn || "");
      formData.append("titleAr", payload.titleAr || "");
      formData.append("descriptionEn", payload.descriptionEn || "");
      formData.append("descriptionAr", payload.descriptionAr || "");
      formData.append("badge", payload.badge || "");
      formData.append("accessType", payload.accessType || "");
      formData.append("duration", payload.duration?.toString() || "");
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

      const response = await useApi<FormData, IClipResponse>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.CLIP.BASE +
          ROUTES_ENUM.CLIP.CREATE,
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
      clipStore.setLoading(false);
    }
  }

  async updateClip(payload: IClipRequest) {
    const toast = useToast();
    const clipStore = useClipStore();
    clipStore.setLoading(true);

    try {
      const formData = new FormData();

      formData.append("clipId", payload.clipId || "");
      formData.append("titleEn", payload.titleEn || "");
      formData.append("titleAr", payload.titleAr || "");
      formData.append("descriptionEn", payload.descriptionEn || "");
      formData.append("descriptionAr", payload.descriptionAr || "");
      formData.append("badge", payload.badge || "");
      formData.append("accessType", payload.accessType || "");
      formData.append("duration", payload.duration?.toString() || "");
      formData.append("active", payload.active || "");
      formData.append("hasRight", payload.hasRight || "");
      formData.append("isKids", payload.isKids || "");
      formData.append("isSports", payload.isSports || "");

      if (payload.file) {
        formData.append("file", payload.file);
      }

      if (payload.poster) {
        formData.append("poster", payload.poster);
      }

      if (payload.tags && payload.tags.length) {
        payload.tags?.forEach((tagId) => {
          formData.append("tags", String(tagId));
        });
      }

      console.log("FORM DATA:", [...formData.entries()]);

      const response = await useApi<FormData, IClipResponse>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.CLIP.BASE +
          ROUTES_ENUM.CLIP.UPDATE,
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
      clipStore.setLoading(false);
    }
  }

  async getClip(id?: string) {
    const clipStore = useClipStore();
    clipStore.setLoading(true);

    try {
      const response = await useApi<null, IClipResponse>(
        `${ROUTES_ENUM.ISHOW}${ROUTES_ENUM.ADMIN}${ROUTES_ENUM.CLIP.BASE}/${id}`,
        HttpMethod.GET,
        null,
      );
      console.log(id);
      console.log(response);

      return response.data;
    } finally {
      clipStore.setLoading(false);
    }
  }

  async publishClip(id: string) {
    const toast = useToast();
    const clipStore = useClipStore();
    clipStore.setLoading(true);

    try {
      const response = await useApi<null, IClipResponse>(
        `${ROUTES_ENUM.ISHOW}${ROUTES_ENUM.ADMIN}${ROUTES_ENUM.CLIP.BASE}${ROUTES_ENUM.CLIP.PUBLISH}/${id}`,
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
      clipStore.setLoading(false);
    }
  }

  async viewSectionClips(payload: IClipSectionRequest) {
    const clipStore = useClipStore();
    clipStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<
        IClipSectionRequest,
        ISectionClipResponse[]
      >(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.CLIP.BASE +
          ROUTES_ENUM.CLIP.VIEW_SECTION_CLIPS,
        HttpMethod.GET,
        payload,
      );

      console.log(response);

      return response;
    } finally {
      clipStore.setLoading(false);
    }
  }

  async addClipToSection(payload: IAddClipToSectionRequest) {
    const toast = useToast();
    const clipStore = useClipStore();
    clipStore.setLoading(true);

    try {
      // payload.clipId = "69ff2fa9f684d370d5b2e1a2";
      console.log(payload);
      const response = await useApi<IAddClipToSectionRequest, null>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.CLIP.BASE +
          ROUTES_ENUM.CLIP.ADD_CLIP_TO_SECTION,
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
      clipStore.setLoading(false);
    }
  }

  async removeClipFromSection(payload: IRemoveClipFromSectionRequest) {
    const toast = useToast();
    const clipStore = useClipStore();
    clipStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<IRemoveClipFromSectionRequest, null>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.CLIP.BASE +
          ROUTES_ENUM.CLIP.REMOVE_CLIP_FROM_SECTION,
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
      clipStore.setLoading(false);
    }
  }

  async changeStatusClip(payload: IAddClipToSectionRequest) {
    const toast = useToast();
    const clipStore = useClipStore();
    clipStore.setLoading(true);

    try {
      const url = `${ROUTES_ENUM.ISHOW}${ROUTES_ENUM.ADMIN}${ROUTES_ENUM.CLIP.BASE}${ROUTES_ENUM.CLIP.SECTION_IS_TOP}?sectionId=${payload.sectionId}&clipId=${payload.clipId}&isTop=${payload.isTop}`;

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

      return response;
    } finally {
      clipStore.setLoading(false);
    }
  }
}

export default new ClipService();
