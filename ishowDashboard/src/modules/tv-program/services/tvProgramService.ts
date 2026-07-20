import { useApi } from "../../../composables/useApi";
import { HttpMethod } from "../../../enums/httpMethodEnum";
import { ROUTES_ENUM, SUCCES_MESSAGE } from "../../../utils/constants";
import { useToast } from "vue-toastification";
import type {
  IFilterSeasonEpisodes,
  IFilterTvRequest,
} from "../types/filter-request";
import { useTvProgramStore } from "../stores/tvProgram";
import type { ISectionTvResponse, ITvResponse } from "../types/tv-response";
import type {
  IAddTvToSectionRequest,
  IRemoveTvFromSectionRequest,
  ITvRequest,
  ITvSectionRequest,
} from "../types/tv-request";
import type { ITvSeasonResponse } from "../types/season-response";
import type { ITvSeasonRequest } from "../types/season-request";
import type { ISeasonEpisodeResponse } from "../types/episode-response";
import type { ISeasonEpisodeRequest } from "../types/episode-request";

class TvService {
  async filterTvs(payload: IFilterTvRequest) {
    const tvStore = useTvProgramStore();
    tvStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<IFilterTvRequest, ITvResponse[]>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.TV_PROGRAM.BASE +
          ROUTES_ENUM.TV_PROGRAM.FILTER,
        HttpMethod.POST,
        payload,
      );
      console.log(response);

      return response;
    } finally {
      tvStore.setLoading(false);
    }
  }

  async createTv(payload: ITvRequest) {
    const toast = useToast();
    const tvStore = useTvProgramStore();
    tvStore.setLoading(true);

    try {
      const formData = new FormData();

      if (!payload.poster) {
        throw new Error("No poster selected");
      }

      formData.append("titleEn", payload.titleEn || "");
      formData.append("titleAr", payload.titleAr || "");
      formData.append("descriptionEn", payload.descriptionEn || "");
      formData.append("descriptionAr", payload.descriptionAr || "");
      formData.append("badge", payload.badge || "");
      formData.append("releaseYear", payload.releaseYear || "");
      formData.append("language", payload.language || "");
      formData.append("active", payload.active || "");
      formData.append("hasRight", payload.hasRight || "");
      formData.append("isKids", payload.isKids || "");
      formData.append("isSports", payload.isSports || "");

      formData.append("poster", payload.poster);

      if (payload.tags && payload.tags.length) {
        payload.tags?.forEach((tagId) => {
          formData.append("tags", String(tagId));
        });
      }

      if (payload.audioLanguages?.length) {
        payload.audioLanguages.forEach((language) => {
          formData.append("audioLanguages", language);
        });
      }

      if (payload.subtitleLanguages?.length) {
        payload.subtitleLanguages.forEach((language) => {
          formData.append("subtitleLanguages", language);
        });
      }

      console.log("FORM DATA:", [...formData.entries()]);

      const response = await useApi<FormData, ITvResponse>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.TV_PROGRAM.BASE +
          ROUTES_ENUM.TV_PROGRAM.CREATE,
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
      tvStore.setLoading(false);
    }
  }

  async publishTv(id: string) {
    const toast = useToast();
    const tvStore = useTvProgramStore();
    tvStore.setLoading(true);

    try {
      const response = await useApi<null, ITvResponse>(
        `${ROUTES_ENUM.ISHOW}${ROUTES_ENUM.ADMIN}${ROUTES_ENUM.TV_PROGRAM.BASE}${ROUTES_ENUM.TV_PROGRAM.PUBLISH}/${id}`,
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
      tvStore.setLoading(false);
    }
  }

  async updateTv(payload: ITvRequest) {
    const toast = useToast();
    const tvStore = useTvProgramStore();
    tvStore.setLoading(true);

    try {
      const formData = new FormData();

      formData.append("tvProgramId", payload.tvProgramId || "");
      formData.append("titleEn", payload.titleEn || "");
      formData.append("titleAr", payload.titleAr || "");
      formData.append("descriptionEn", payload.descriptionEn || "");
      formData.append("descriptionAr", payload.descriptionAr || "");
      formData.append("badge", payload.badge || "");
      formData.append("releaseYear", payload.releaseYear || "");
      formData.append("language", payload.language || "");
      formData.append("active", payload.active || "");
      formData.append("hasRight", payload.hasRight || "");
      formData.append("isKids", payload.isKids || "");
      formData.append("isSports", payload.isSports || "");

      if (payload.poster) {
        formData.append("poster", payload.poster || "");
      }

      if (payload.tags && payload.tags.length) {
        payload.tags?.forEach((tagId) => {
          formData.append("tags", String(tagId));
        });
      }

      if (payload.audioLanguages?.length) {
        payload.audioLanguages.forEach((language) => {
          formData.append("audioLanguages", language);
        });
      }

      if (payload.subtitleLanguages?.length) {
        payload.subtitleLanguages.forEach((language) => {
          formData.append("subtitleLanguages", language);
        });
      }

      console.log("FORM DATA:", [...formData.entries()]);

      const response = await useApi<FormData, ITvResponse>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.TV_PROGRAM.BASE +
          ROUTES_ENUM.TV_PROGRAM.UPDATE,
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
      tvStore.setLoading(false);
    }
  }

  async getTv(id?: string) {
    const tvStore = useTvProgramStore();
    tvStore.setLoading(true);

    try {
      const response = await useApi<null, ITvResponse>(
        `${ROUTES_ENUM.ISHOW}${ROUTES_ENUM.ADMIN}${ROUTES_ENUM.TV_PROGRAM.BASE}/${id}`,
        HttpMethod.GET,
        null,
      );
      console.log(id);
      console.log(response);

      return response.data;
    } finally {
      tvStore.setLoading(false);
    }
  }

  async viewSectionTvs(payload: ITvSectionRequest) {
    const tvStore = useTvProgramStore();
    tvStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<ITvSectionRequest, ISectionTvResponse[]>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.TV_PROGRAM.BASE +
          ROUTES_ENUM.TV_PROGRAM.VIEW_SECTION_TV,
        HttpMethod.GET,
        payload,
      );

      console.log(response);

      return response;
    } finally {
      tvStore.setLoading(false);
    }
  }

  async addTvToSection(payload: IAddTvToSectionRequest) {
    const toast = useToast();
    const tvStore = useTvProgramStore();
    tvStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<IAddTvToSectionRequest, null>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.TV_PROGRAM.BASE +
          ROUTES_ENUM.TV_PROGRAM.ADD_TV_TO_SECTION,
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
      tvStore.setLoading(false);
    }
  }

  async removeTvFromSection(payload: IRemoveTvFromSectionRequest) {
    const toast = useToast();
    const tvStore = useTvProgramStore();
    tvStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<IRemoveTvFromSectionRequest, null>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.TV_PROGRAM.BASE +
          ROUTES_ENUM.TV_PROGRAM.REMOVE_TV_FROM_SECTION,
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
      tvStore.setLoading(false);
    }
  }

  async changeStatusTv(payload: IAddTvToSectionRequest) {
    const toast = useToast();
    const tvStore = useTvProgramStore();
    tvStore.setLoading(true);

    try {
      const url = `${ROUTES_ENUM.ISHOW}${ROUTES_ENUM.ADMIN}${ROUTES_ENUM.TV_PROGRAM.BASE}${ROUTES_ENUM.TV_PROGRAM.SECTION_IS_TOP}?sectionId=${payload.sectionId}&tvProgramId=${payload.tvProgramId}&isTop=${payload.isTop}`;

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
      tvStore.setLoading(false);
    }
  }

  async getTvSeasons(id: string) {
    const tvStore = useTvProgramStore();
    tvStore.setLoading(true);

    try {
      console.log(id);
      const response = await useApi<null, ITvSeasonResponse[]>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.TV_PROGRAM.BASE +
          `/${id}/season`,
        HttpMethod.GET,
        null,
      );
      console.log(response);

      return response;
    } finally {
      tvStore.setLoading(false);
    }
  }

  async createSeason(payload: ITvSeasonRequest) {
    const toast = useToast();
    const tvStore = useTvProgramStore();
    tvStore.setLoading(true);

    try {
      const formData = new FormData();

      if (!payload.poster) {
        throw new Error("No poster selected");
      }

      if (!payload.trailer) {
        throw new Error("No trailer selected");
      }

      formData.append("tvProgramId", payload.tvProgramId || "");
      formData.append("titleEn", payload.titleEn || "");
      formData.append("titleAr", payload.titleAr || "");
      formData.append("descriptionEn", payload.descriptionEn || "");
      formData.append("descriptionAr", payload.descriptionAr || "");
      formData.append("seasonNumber", payload.seasonNumber || "");
      formData.append("releaseYear", payload.releaseYear || "");
      formData.append("active", payload.active || "");

      formData.append("poster", payload.poster);

      formData.append("trailer", payload.trailer);

      console.log("FORM DATA:", [...formData.entries()]);

      const response = await useApi<FormData, ITvSeasonResponse>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.TV_PROGRAM.BASE +
          ROUTES_ENUM.TV_PROGRAM.CREATE_SEASON,
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
      tvStore.setLoading(false);
    }
  }

  async updateSeason(payload: ITvSeasonRequest) {
    const toast = useToast();
    const tvStore = useTvProgramStore();

    tvStore.setLoading(true);

    try {
      const formData = new FormData();

      formData.append("seasonId", payload.seasonId || "");
      formData.append("tvProgramId", payload.tvProgramId || "");
      formData.append("titleEn", payload.titleEn || "");
      formData.append("titleAr", payload.titleAr || "");
      formData.append("descriptionEn", payload.descriptionEn || "");
      formData.append("descriptionAr", payload.descriptionAr || "");
      formData.append("seasonNumber", payload.seasonNumber || "");
      formData.append("releaseYear", payload.releaseYear || "");
      formData.append("active", payload.active || "");

      if (payload.poster) {
        formData.append("poster", payload.poster);
      }

      if (payload.trailer) {
        formData.append("trailer", payload.trailer);
      }

      console.log("FORM DATA:", [...formData.entries()]);

      const response = await useApi<FormData, ITvSeasonResponse>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.TV_PROGRAM.BASE +
          ROUTES_ENUM.TV_PROGRAM.UPDATE_SEASON,
        HttpMethod.POST,
        formData,
      );

      if (response.status === SUCCES_MESSAGE) {
        toast.success(response.message || "Updated successfully", {
          timeout: 5000,
        });
      } else {
        toast.error(response.message || "Update failed", {
          timeout: 5000,
        });
      }
    } finally {
      tvStore.setLoading(false);
    }
  }

  async getSeasonEpisodes(payload: IFilterSeasonEpisodes) {
    const tvStore = useTvProgramStore();
    tvStore.setLoading(true);

    try {
      console.log(
        `TV ID: ${payload.tvProgramId}, Season ID: ${payload.seasonId}`,
      );
      const response = await useApi<null, ISeasonEpisodeResponse[]>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.TV_PROGRAM.BASE +
          `/${payload.tvProgramId}/season/${payload.seasonId}/episode?page=${payload.page}&size=${payload.size}`,
        HttpMethod.GET,
        null,
      );
      console.log(response);

      return response;
    } finally {
      tvStore.setLoading(false);
    }
  }

  async createEpisode(payload: ISeasonEpisodeRequest) {
    const toast = useToast();
    const tvStore = useTvProgramStore();
    tvStore.setLoading(true);

    try {
      const formData = new FormData();

      if (!payload.file) {
        throw new Error("No file selected");
      }

      if (!payload.poster) {
        throw new Error("No poster selected");
      }

      if (!payload.subtitles) {
        throw new Error("No subtitles selected");
      }

      formData.append("tvProgramId", payload.tvProgramId || "");
      formData.append("seasonId", payload.seasonId || "");
      formData.append("episodeNumber", payload.episodeNumber || "");
      formData.append("titleEn", payload.titleEn || "");
      formData.append("titleAr", payload.titleAr || "");
      formData.append("active", payload.active || "");
      formData.append("accessType", payload.accessType || "");
      formData.append("badge", payload.badge || "");

      formData.append("file", payload.file);

      formData.append("poster", payload.poster);

      payload.subtitles.forEach((file) => {
        formData.append("subtitles", file);
      });

      console.log("FORM DATA:", [...formData.entries()]);

      const response = await useApi<FormData, ISeasonEpisodeResponse>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.TV_PROGRAM.BASE +
          ROUTES_ENUM.TV_PROGRAM.CREATE_EPISODE,
        HttpMethod.POST,
        formData,
      );

      console.log(response);

      if (response.status === SUCCES_MESSAGE) {
        toast.success(response.message || "Episode created successfully", {
          timeout: 5000,
        });
      } else {
        toast.error(response.message || "Episode creation failed", {
          timeout: 5000,
        });
      }
    } finally {
      tvStore.setLoading(false);
    }
  }

  async updateEpisode(payload: ISeasonEpisodeRequest) {
    const toast = useToast();
    const tvStore = useTvProgramStore();
    tvStore.setLoading(true);

    try {
      const formData = new FormData();

      formData.append("episodeId", payload.episodeId || "");
      formData.append("tvProgramId", payload.tvProgramId || "");
      formData.append("seasonId", payload.seasonId || "");
      formData.append("episodeNumber", payload.episodeNumber || "");
      formData.append("titleEn", payload.titleEn || "");
      formData.append("titleAr", payload.titleAr || "");
      formData.append("active", payload.active || "");
      formData.append("accessType", payload.accessType || "");
      formData.append("badge", payload.badge || "");

      if (payload.file) {
        formData.append("file", payload.file);
      }

      if (payload.poster) {
        formData.append("poster", payload.poster);
      }

      if (payload.subtitles && payload.subtitles.length > 0) {
        payload.subtitles.forEach((file) => {
          formData.append("subtitles", file);
        });
      }

      console.log("FORM DATA:", [...formData.entries()]);

      const response = await useApi<FormData, ISeasonEpisodeResponse>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.TV_PROGRAM.BASE +
          ROUTES_ENUM.TV_PROGRAM.UPDATE_EPISODE,
        HttpMethod.POST,
        formData,
      );

      console.log(response);

      if (response.status === SUCCES_MESSAGE) {
        toast.success(response.message || "Episode updated successfully", {
          timeout: 5000,
        });
      } else {
        toast.error(response.message || "Episode update failed", {
          timeout: 5000,
        });
      }
    } finally {
      tvStore.setLoading(false);
    }
  }
}

export default new TvService();
