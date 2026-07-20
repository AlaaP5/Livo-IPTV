import { useApi } from "../../../composables/useApi";
import { HttpMethod } from "../../../enums/httpMethodEnum";
import { ROUTES_ENUM, SUCCES_MESSAGE } from "../../../utils/constants";
import { useToast } from "vue-toastification";
import type {
  IFilterSeasonEpisodes,
  IFilterSeriesRequest,
} from "../types/filter-request";
import { useSeriesStore } from "../stores/series";
import type {
  ISectionSeriesResponse,
  ISeriesResponse,
} from "../types/series-response";
import type {
  IAddSeriesToSectionRequest,
  IRemoveSeriesFromSectionRequest,
  ISeriesRequest,
  ISeriesSectionRequest,
} from "../types/series-request";
import type { ISeriesSeasonResponse } from "../types/season-response";
import type { ISeriesSeasonRequest } from "../types/season-request";
import type { ISeasonEpisodeRequest } from "../types/episode-request";
import type { ISeasonEpisodesResponse } from "../types/episode-response";

class SeriesService {
  async filterSeries(payload: IFilterSeriesRequest) {
    const seriesStore = useSeriesStore();
    seriesStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<IFilterSeriesRequest, ISeriesResponse[]>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.SERIES.BASE +
          ROUTES_ENUM.SERIES.FILTER,
        HttpMethod.POST,
        payload,
      );
      console.log(response);

      return response;
    } finally {
      seriesStore.setLoading(false);
    }
  }

  async createSeries(payload: ISeriesRequest) {
    const toast = useToast();
    const seriesStore = useSeriesStore();
    seriesStore.setLoading(true);

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
      formData.append("rate", payload.rate || "");
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

      if (payload.actors && payload.actors.length) {
        payload.actors?.forEach((actorId) => {
          formData.append("actors", String(actorId));
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

      const response = await useApi<FormData, ISeriesResponse>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.SERIES.BASE +
          ROUTES_ENUM.SERIES.CREATE,
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
      seriesStore.setLoading(false);
    }
  }

  async updateSeries(payload: ISeriesRequest) {
    const toast = useToast();
    const seriesStore = useSeriesStore();
    seriesStore.setLoading(true);

    try {
      const formData = new FormData();

      formData.append("seriesId", payload.seriesId || "");
      formData.append("titleEn", payload.titleEn || "");
      formData.append("titleAr", payload.titleAr || "");
      formData.append("descriptionEn", payload.descriptionEn || "");
      formData.append("descriptionAr", payload.descriptionAr || "");
      formData.append("badge", payload.badge || "");
      formData.append("releaseYear", payload.releaseYear || "");
      formData.append("language", payload.language || "");
      formData.append("rate", payload.rate || "");
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

      if (payload.actors && payload.actors.length) {
        payload.actors?.forEach((actorId) => {
          formData.append("actors", String(actorId));
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

      const response = await useApi<FormData, ISeriesResponse>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.SERIES.BASE +
          ROUTES_ENUM.SERIES.UPDATE,
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
      seriesStore.setLoading(false);
    }
  }

  async getSeries(id?: string) {
    const seriesStore = useSeriesStore();
    seriesStore.setLoading(true);

    try {
      const response = await useApi<null, ISeriesResponse>(
        `${ROUTES_ENUM.ISHOW}${ROUTES_ENUM.ADMIN}${ROUTES_ENUM.SERIES.BASE}/${id}`,
        HttpMethod.GET,
        null,
      );
      console.log(id);
      console.log(response);

      return response.data;
    } finally {
      seriesStore.setLoading(false);
    }
  }

  async publishSeries(id: string) {
    const toast = useToast();
    const seriesStore = useSeriesStore();
    seriesStore.setLoading(true);

    try {
      const response = await useApi<null, ISeriesResponse>(
        `${ROUTES_ENUM.ISHOW}${ROUTES_ENUM.ADMIN}${ROUTES_ENUM.SERIES.BASE}${ROUTES_ENUM.SERIES.PUBLISH}/${id}`,
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
      seriesStore.setLoading(false);
    }
  }

  async viewSectionSeries(payload: ISeriesSectionRequest) {
    const seriesStore = useSeriesStore();
    seriesStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<
        ISeriesSectionRequest,
        ISectionSeriesResponse[]
      >(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.SERIES.BASE +
          ROUTES_ENUM.SERIES.VIEW_SECTION_SERIES,
        HttpMethod.GET,
        payload,
      );

      console.log(response);

      return response;
    } finally {
      seriesStore.setLoading(false);
    }
  }

  async addSeriesToSection(payload: IAddSeriesToSectionRequest) {
    const toast = useToast();
    const seriesStore = useSeriesStore();
    seriesStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<IAddSeriesToSectionRequest, null>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.SERIES.BASE +
          ROUTES_ENUM.SERIES.ADD_SERIES_TO_SECTION,
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
      seriesStore.setLoading(false);
    }
  }

  async removeSeriesFromSection(payload: IRemoveSeriesFromSectionRequest) {
    const toast = useToast();
    const seriesStore = useSeriesStore();
    seriesStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<IRemoveSeriesFromSectionRequest, null>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.SERIES.BASE +
          ROUTES_ENUM.SERIES.REMOVE_SERIES_FROM_SECTION,
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
      seriesStore.setLoading(false);
    }
  }

  async changeStatusSeries(payload: IAddSeriesToSectionRequest) {
    const toast = useToast();
    const seriesStore = useSeriesStore();
    seriesStore.setLoading(true);

    try {
      const url = `${ROUTES_ENUM.ISHOW}${ROUTES_ENUM.ADMIN}${ROUTES_ENUM.SERIES.BASE}${ROUTES_ENUM.SERIES.SECTION_IS_TOP}?sectionId=${payload.sectionId}&seriesId=${payload.seriesId}&isTop=${payload.isTop}`;

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
      seriesStore.setLoading(false);
    }
  }

  async getSeriesSeasons(id: string) {
    const seriesStore = useSeriesStore();
    seriesStore.setLoading(true);

    try {
      console.log(id);
      const response = await useApi<null, ISeriesSeasonResponse[]>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.SERIES.BASE +
          `/${id}/season`,
        HttpMethod.GET,
        null,
      );
      console.log(response);

      return response;
    } finally {
      seriesStore.setLoading(false);
    }
  }

  async createSeason(payload: ISeriesSeasonRequest) {
    const toast = useToast();
    const seriesStore = useSeriesStore();
    seriesStore.setLoading(true);

    try {
      const formData = new FormData();

      if (!payload.poster) {
        throw new Error("No poster selected");
      }

      if (!payload.trailer) {
        throw new Error("No trailer selected");
      }

      formData.append("seriesId", payload.seriesId || "");
      formData.append("titleEn", payload.titleEn || "");
      formData.append("titleAr", payload.titleAr || "");
      formData.append("descriptionEn", payload.descriptionEn || "");
      formData.append("descriptionAr", payload.descriptionAr || "");
      formData.append("seasonNumber", payload.seasonNumber || "");
      formData.append("releaseYear", payload.releaseYear || "");
      formData.append("rating", payload.rating || "");
      formData.append("active", payload.active || "");

      formData.append("poster", payload.poster);

      formData.append("trailer", payload.trailer);

      if (payload.actors && payload.actors.length) {
        payload.actors?.forEach((actorId) => {
          formData.append("actors", String(actorId));
        });
      }

      console.log("FORM DATA:", [...formData.entries()]);

      const response = await useApi<FormData, ISeriesSeasonResponse>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.SERIES.BASE +
          ROUTES_ENUM.SERIES.CREATE_SEASON,
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
      seriesStore.setLoading(false);
    }
  }

  async updateSeason(payload: ISeriesSeasonRequest) {
    const toast = useToast();
    const seriesStore = useSeriesStore();

    seriesStore.setLoading(true);

    try {
      const formData = new FormData();

      formData.append("seasonId", payload.seasonId || "");
      formData.append("seriesId", payload.seriesId || "");
      formData.append("titleEn", payload.titleEn || "");
      formData.append("titleAr", payload.titleAr || "");
      formData.append("descriptionEn", payload.descriptionEn || "");
      formData.append("descriptionAr", payload.descriptionAr || "");
      formData.append("seasonNumber", payload.seasonNumber || "");
      formData.append("releaseYear", payload.releaseYear || "");
      formData.append("rating", payload.rating || "");
      formData.append("active", payload.active || "");

      if (payload.poster) {
        formData.append("poster", payload.poster);
      }

      if (payload.trailer) {
        formData.append("trailer", payload.trailer);
      }

      if (payload.actors?.length) {
        payload.actors.forEach((actorId) => {
          formData.append("actors", String(actorId));
        });
      }

      console.log("FORM DATA:", [...formData.entries()]);

      const response = await useApi<FormData, ISeriesSeasonResponse>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.SERIES.BASE +
          ROUTES_ENUM.SERIES.UPDATE_SEASON,
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
      seriesStore.setLoading(false);
    }
  }

  async getSeasonEpisodes(payload: IFilterSeasonEpisodes) {
    const seriesStore = useSeriesStore();
    seriesStore.setLoading(true);

    try {
      console.log(
        `Series ID: ${payload.seriesId}, Season ID: ${payload.seasonId}`,
      );
      const response = await useApi<null, ISeasonEpisodesResponse[]>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.SERIES.BASE +
          `/${payload.seriesId}/season/${payload.seasonId}/episode?page=${payload.page}&size=${payload.size}`,
        HttpMethod.GET,
        null,
      );
      console.log(response);

      return response;
    } finally {
      seriesStore.setLoading(false);
    }
  }

  async createEpisode(payload: ISeasonEpisodeRequest) {
    const toast = useToast();
    const seriesStore = useSeriesStore();
    seriesStore.setLoading(true);

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

      formData.append("seriesId", payload.seriesId || "");
      formData.append("seasonId", payload.seasonId || "");
      formData.append("episodeNumber", payload.episodeNumber || "");
      formData.append("titleEn", payload.titleEn || "");
      formData.append("titleAr", payload.titleAr || "");
      formData.append("active", payload.active || "");
      formData.append("rate", payload.rate || "");
      formData.append("duration", payload.duration?.toString() || "");
      formData.append("accessType", payload.accessType || "");
      formData.append("badge", payload.badge || "");

      formData.append("file", payload.file);

      formData.append("poster", payload.poster);

      payload.subtitles.forEach((file) => {
        formData.append("subtitles", file);
      });

      console.log("FORM DATA:", [...formData.entries()]);

      const response = await useApi<FormData, ISeasonEpisodesResponse>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.SERIES.BASE +
          ROUTES_ENUM.SERIES.CREATE_EPISODE,
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
      seriesStore.setLoading(false);
    }
  }

  async updateEpisode(payload: ISeasonEpisodeRequest) {
    const toast = useToast();
    const seriesStore = useSeriesStore();
    seriesStore.setLoading(true);

    try {
      const formData = new FormData();

      formData.append("episodeId", payload.episodeId || "");
      formData.append("seriesId", payload.seriesId || "");
      formData.append("seasonId", payload.seasonId || "");
      formData.append("episodeNumber", payload.episodeNumber || "");
      formData.append("titleEn", payload.titleEn || "");
      formData.append("titleAr", payload.titleAr || "");
      formData.append("active", payload.active || "");
      formData.append("rate", payload.rate || "");
      formData.append("duration", payload.duration?.toString() || "");
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

      const response = await useApi<FormData, ISeasonEpisodesResponse>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.SERIES.BASE +
          ROUTES_ENUM.SERIES.UPDATE_EPISODE,
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
      seriesStore.setLoading(false);
    }
  }
}

export default new SeriesService();
