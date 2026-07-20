import { useApi } from "../../../composables/useApi";
import { HttpMethod } from "../../../enums/httpMethodEnum";
import { ROUTES_ENUM, SUCCES_MESSAGE } from "../../../utils/constants";
import { useToast } from "vue-toastification";
import type { IActivateRequest } from "../../../types/active.types";
import type { IFilterSectionRequest } from "../types/filter-request";
import { useSectionStore } from "../stores/section";
import type {
  ISectionPublishRequest,
  ISectionRequest,
} from "../types/section-request";
import type { ISectionResponse } from "../types/section-response";

class SectionService {
  async filterSections(payload: IFilterSectionRequest) {
    const sectionStore = useSectionStore();
    sectionStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<IFilterSectionRequest, ISectionResponse[]>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.SECTION.BASE +
          ROUTES_ENUM.SECTION.FILTER,
        HttpMethod.GET,
        payload,
      );
      console.log(response);

      return response;
    } finally {
      sectionStore.setLoading(false);
    }
  }

  async createSection(payload: ISectionRequest) {
    const toast = useToast();
    const sectionStore = useSectionStore();
    sectionStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<ISectionRequest, ISectionResponse>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.SECTION.BASE +
          ROUTES_ENUM.SECTION.CREATE,
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
      sectionStore.setLoading(false);
    }
  }

  async updateSection(payload: ISectionRequest) {
    const toast = useToast();
    const sectionStore = useSectionStore();
    sectionStore.setLoading(true);

    try {
      const response = await useApi<ISectionRequest, ISectionResponse>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.SECTION.BASE +
          ROUTES_ENUM.SECTION.UPDATE,
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
      sectionStore.setLoading(false);
    }
  }

  async activateSection(id: number, payload: IActivateRequest) {
    const toast = useToast();
    const sectionStore = useSectionStore();
    sectionStore.setLoading(true);

    try {
      const response = await useApi<IActivateRequest, ISectionResponse>(
        `${ROUTES_ENUM.ISHOW}${ROUTES_ENUM.ADMIN}${ROUTES_ENUM.SECTION.BASE}/${id}${ROUTES_ENUM.SECTION.ACTIVATE}`,
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
      sectionStore.setLoading(false);
    }
  }

  async publishSection(id: number, payload: ISectionPublishRequest) {
    const toast = useToast();
    const sectionStore = useSectionStore();
    sectionStore.setLoading(true);

    try {
      const response = await useApi<ISectionPublishRequest, ISectionResponse>(
        `${ROUTES_ENUM.ISHOW}${ROUTES_ENUM.ADMIN}${ROUTES_ENUM.SECTION.BASE}/${id}${ROUTES_ENUM.SECTION.PUBLISH}`,
        HttpMethod.GET,
        payload,
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
      sectionStore.setLoading(false);
    }
  }
}

export default new SectionService();
