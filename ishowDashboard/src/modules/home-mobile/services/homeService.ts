import { useApi } from "../../../composables/useApi";
import { HttpMethod } from "../../../enums/httpMethodEnum";
import { ROUTES_ENUM, SUCCES_MESSAGE } from "../../../utils/constants";
import { useToast } from "vue-toastification";
import type {
  IAddMovieToSectionRequest,
  IMovieSectionRequest,
  IRemoveMovieFromSectionRequest,
} from "../../movie/types/movie-request";
import type { ISectionMovieResponse } from "../../movie/types/movie-response";
import type {
  IAddSeriesToSectionRequest,
  IRemoveSeriesFromSectionRequest,
  ISeriesSectionRequest,
} from "../../series/types/series-request";
import type { ISectionSeriesResponse } from "../../series/types/series-response";
import type {
  IAddClipToSectionRequest,
  IClipSectionRequest,
  IRemoveClipFromSectionRequest,
} from "../../clip/types/clip-request";
import type { ISectionClipResponse } from "../../clip/types/clip-response";
import type {
  IAddChannelToSectionRequest,
  IChannelSectionRequest,
  IRemoveChannelFromSectionRequest,
} from "../../channel/types/channel-request";
import type { ISectionChannelsResponse } from "../../channel/types/channel-response";
import type {
  IAddTvToSectionRequest,
  IRemoveTvFromSectionRequest,
  ITvSectionRequest,
} from "../../tv-program/types/tv-request";
import type { ISectionTvResponse } from "../../tv-program/types/tv-response";
import { useHomeStore } from "../stores/home";
import type { IHomeBannerResponse } from "../types/home-response";

class HomeService {
  async viewSectionMovies(payload: IMovieSectionRequest) {
    const homeStore = useHomeStore();
    homeStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<
        IMovieSectionRequest,
        ISectionMovieResponse[]
      >(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.MOBILE_HOME.BASE +
          ROUTES_ENUM.MOBILE_HOME.VIEW_SECTION_MOVIES,
        HttpMethod.GET,
        payload,
      );

      console.log(response);

      return response;
    } finally {
      homeStore.setLoading(false);
    }
  }

  async addMovieToSection(payload: IAddMovieToSectionRequest) {
    const toast = useToast();
    const homeStore = useHomeStore();
    homeStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<IAddMovieToSectionRequest, null>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.MOBILE_HOME.BASE +
          ROUTES_ENUM.MOBILE_HOME.ADD_MOVIE_TO_SECTION,
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
      homeStore.setLoading(false);
    }
  }

  async removeMovieFromSection(payload: IRemoveMovieFromSectionRequest) {
    const toast = useToast();
    const homeStore = useHomeStore();
    homeStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<IRemoveMovieFromSectionRequest, null>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.MOBILE_HOME.BASE +
          ROUTES_ENUM.MOBILE_HOME.REMOVE_MOVIE_FROM_SECTION,
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
      homeStore.setLoading(false);
    }
  }

  async changeStatusMovie(payload: IAddMovieToSectionRequest) {
    const toast = useToast();
    const homeStore = useHomeStore();
    homeStore.setLoading(true);

    try {
      const url = `${ROUTES_ENUM.ISHOW}${ROUTES_ENUM.ADMIN}${ROUTES_ENUM.MOBILE_HOME.BASE}${ROUTES_ENUM.MOBILE_HOME.MOVIE_SECTION_IS_TOP}?sectionId=${payload.sectionId}&movieId=${payload.movieId}&isTop=${payload.isTop}`;

      const response = await useApi<null, null>(url, HttpMethod.PUT, null);

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
      homeStore.setLoading(false);
    }
  }

  async viewSectionSeries(payload: ISeriesSectionRequest) {
    const homeStore = useHomeStore();
    homeStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<
        ISeriesSectionRequest,
        ISectionSeriesResponse[]
      >(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.MOBILE_HOME.BASE +
          ROUTES_ENUM.MOBILE_HOME.VIEW_SECTION_SERIES,
        HttpMethod.GET,
        payload,
      );

      console.log(response);

      return response;
    } finally {
      homeStore.setLoading(false);
    }
  }

  async addSeriesToSection(payload: IAddSeriesToSectionRequest) {
    const toast = useToast();
    const homeStore = useHomeStore();
    homeStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<IAddSeriesToSectionRequest, null>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.MOBILE_HOME.BASE +
          ROUTES_ENUM.MOBILE_HOME.ADD_SERIES_TO_SECTION,
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
      homeStore.setLoading(false);
    }
  }

  async removeSeriesFromSection(payload: IRemoveSeriesFromSectionRequest) {
    const toast = useToast();
    const homeStore = useHomeStore();
    homeStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<IRemoveSeriesFromSectionRequest, null>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.MOBILE_HOME.BASE +
          ROUTES_ENUM.MOBILE_HOME.REMOVE_SERIES_FROM_SECTION,
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
      homeStore.setLoading(false);
    }
  }

  async changeStatusSeries(payload: IAddSeriesToSectionRequest) {
    const toast = useToast();
    const homeStore = useHomeStore();
    homeStore.setLoading(true);

    try {
      const url = `${ROUTES_ENUM.ISHOW}${ROUTES_ENUM.ADMIN}${ROUTES_ENUM.MOBILE_HOME.BASE}${ROUTES_ENUM.MOBILE_HOME.SERIES_SECTION_IS_TOP}?sectionId=${payload.sectionId}&seriesId=${payload.seriesId}&isTop=${payload.isTop}`;

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
      homeStore.setLoading(false);
    }
  }

  async viewSectionClips(payload: IClipSectionRequest) {
    const homeStore = useHomeStore();
    homeStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<
        IClipSectionRequest,
        ISectionClipResponse[]
      >(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.MOBILE_HOME.BASE +
          ROUTES_ENUM.MOBILE_HOME.VIEW_SECTION_CLIP,
        HttpMethod.GET,
        payload,
      );

      console.log(response);

      return response;
    } finally {
      homeStore.setLoading(false);
    }
  }

  async addClipToSection(payload: IAddClipToSectionRequest) {
    const toast = useToast();
    const homeStore = useHomeStore();
    homeStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<IAddClipToSectionRequest, null>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.MOBILE_HOME.BASE +
          ROUTES_ENUM.MOBILE_HOME.ADD_CLIP_TO_SECTION,
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
      homeStore.setLoading(false);
    }
  }

  async removeClipFromSection(payload: IRemoveClipFromSectionRequest) {
    const toast = useToast();
    const homeStore = useHomeStore();
    homeStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<IRemoveClipFromSectionRequest, null>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.MOBILE_HOME.BASE +
          ROUTES_ENUM.MOBILE_HOME.REMOVE_CLIP_FROM_SECTION,
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
      homeStore.setLoading(false);
    }
  }

  async changeStatusClip(payload: IAddClipToSectionRequest) {
    const toast = useToast();
    const homeStore = useHomeStore();
    homeStore.setLoading(true);

    try {
      const url = `${ROUTES_ENUM.ISHOW}${ROUTES_ENUM.ADMIN}${ROUTES_ENUM.MOBILE_HOME.BASE}${ROUTES_ENUM.MOBILE_HOME.CLIP_SECTION_IS_TOP}?sectionId=${payload.sectionId}&clipId=${payload.clipId}&isTop=${payload.isTop}`;

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
      homeStore.setLoading(false);
    }
  }

  async viewSectionChannels(payload: IChannelSectionRequest) {
    const homeStore = useHomeStore();
    homeStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<
        IChannelSectionRequest,
        ISectionChannelsResponse[]
      >(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.MOBILE_HOME.BASE +
          ROUTES_ENUM.MOBILE_HOME.VIEW_SECTION_CHANNELS,
        HttpMethod.GET,
        payload,
      );

      console.log(response);

      return response;
    } finally {
      homeStore.setLoading(false);
    }
  }

  async addChannelToSection(payload: IAddChannelToSectionRequest) {
    const toast = useToast();
    const homeStore = useHomeStore();
    homeStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<IAddChannelToSectionRequest, null>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.MOBILE_HOME.BASE +
          ROUTES_ENUM.MOBILE_HOME.ADD_CHANNEL_TO_SECTION,
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
      homeStore.setLoading(false);
    }
  }

  async removeChannelFromSection(payload: IRemoveChannelFromSectionRequest) {
    const toast = useToast();
    const homeStore = useHomeStore();
    homeStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<IRemoveChannelFromSectionRequest, null>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.MOBILE_HOME.BASE +
          ROUTES_ENUM.MOBILE_HOME.REMOVE_CHANNEL_FROM_SECTION,
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
      homeStore.setLoading(false);
    }
  }

  async changeStatusChannel(payload: IAddChannelToSectionRequest) {
    const toast = useToast();
    const homeStore = useHomeStore();
    homeStore.setLoading(true);

    try {
      const url = `${ROUTES_ENUM.ISHOW}${ROUTES_ENUM.ADMIN}${ROUTES_ENUM.MOBILE_HOME.BASE}${ROUTES_ENUM.MOBILE_HOME.CHANNEL_SECTION_IS_TOP}?sectionId=${payload.sectionId}&channelId=${payload.channelId}&isTop=${payload.isTop}`;

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
      homeStore.setLoading(false);
    }
  }

  async viewSectionTvs(payload: ITvSectionRequest) {
    const homeStore = useHomeStore();
    homeStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<ITvSectionRequest, ISectionTvResponse[]>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.MOBILE_HOME.BASE +
          ROUTES_ENUM.MOBILE_HOME.VIEW_SECTION_TVS,
        HttpMethod.GET,
        payload,
      );

      console.log(response);

      return response;
    } finally {
      homeStore.setLoading(false);
    }
  }

  async addTvToSection(payload: IAddTvToSectionRequest) {
    const toast = useToast();
    const homeStore = useHomeStore();
    homeStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<IAddTvToSectionRequest, null>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.MOBILE_HOME.BASE +
          ROUTES_ENUM.MOBILE_HOME.ADD_TV_TO_SECTION,
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
      homeStore.setLoading(false);
    }
  }

  async removeTvFromSection(payload: IRemoveTvFromSectionRequest) {
    const toast = useToast();
    const homeStore = useHomeStore();
    homeStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<IRemoveTvFromSectionRequest, null>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.MOBILE_HOME.BASE +
          ROUTES_ENUM.MOBILE_HOME.REMOVE_TV_FROM_SECTION,
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
      homeStore.setLoading(false);
    }
  }

  async changeStatusTv(payload: IAddTvToSectionRequest) {
    const toast = useToast();
    const homeStore = useHomeStore();
    homeStore.setLoading(true);

    try {
      const url = `${ROUTES_ENUM.ISHOW}${ROUTES_ENUM.ADMIN}${ROUTES_ENUM.MOBILE_HOME.BASE}${ROUTES_ENUM.MOBILE_HOME.TV_SECTION_IS_TOP}?sectionId=${payload.sectionId}&tvProgramId=${payload.tvProgramId}&isTop=${payload.isTop}`;

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
      homeStore.setLoading(false);
    }
  }

  async addMovieBanner(movieId: string) {
    const homeStore = useHomeStore();
    homeStore.setLoading(true);
    try {
      return await useApi(
        `${ROUTES_ENUM.ISHOW}${ROUTES_ENUM.ADMIN}${ROUTES_ENUM.MOBILE_HOME.BASE}${ROUTES_ENUM.MOBILE_HOME.BANNER}${ROUTES_ENUM.MOBILE_HOME.MOVIE_ADD}?movieId=${movieId}`,
        HttpMethod.POST,
        null,
      );
    } finally {
      homeStore.setLoading(false);
    }
  }

  async addSeriesBanner(seriesId: string) {
    const homeStore = useHomeStore();
    homeStore.setLoading(true);
    try {
      return await useApi(
        `${ROUTES_ENUM.ISHOW}${ROUTES_ENUM.ADMIN}${ROUTES_ENUM.MOBILE_HOME.BASE}${ROUTES_ENUM.MOBILE_HOME.BANNER}${ROUTES_ENUM.MOBILE_HOME.SERIES_ADD}?seriesId=${seriesId}`,
        HttpMethod.POST,
        null,
      );
    } finally {
      homeStore.setLoading(false);
    }
  }

  async addChannelBanner(channelId: string) {
    const homeStore = useHomeStore();
    homeStore.setLoading(true);
    try {
      return await useApi(
        `${ROUTES_ENUM.ISHOW}${ROUTES_ENUM.ADMIN}${ROUTES_ENUM.MOBILE_HOME.BASE}${ROUTES_ENUM.MOBILE_HOME.BANNER}${ROUTES_ENUM.MOBILE_HOME.CHANNEL_ADD}?channelId=${channelId}`,
        HttpMethod.POST,
        null,
      );
    } finally {
      homeStore.setLoading(false);
    }
  }

  async addTvProgramBanner(tvProgramId: string) {
    const homeStore = useHomeStore();
    homeStore.setLoading(true);
    try {
      return await useApi(
        `${ROUTES_ENUM.ISHOW}${ROUTES_ENUM.ADMIN}${ROUTES_ENUM.MOBILE_HOME.BASE}${ROUTES_ENUM.MOBILE_HOME.BANNER}${ROUTES_ENUM.MOBILE_HOME.TV_PROGRAM_ADD}?tvProgramId=${tvProgramId}`,
        HttpMethod.POST,
        null,
      );
    } finally {
      homeStore.setLoading(false);
    }
  }

  async addClipBanner(clipId: string) {
    const homeStore = useHomeStore();
    homeStore.setLoading(true);
    try {
      return await useApi(
        `${ROUTES_ENUM.ISHOW}${ROUTES_ENUM.ADMIN}${ROUTES_ENUM.MOBILE_HOME.BASE}${ROUTES_ENUM.MOBILE_HOME.BANNER}${ROUTES_ENUM.MOBILE_HOME.CLIP_ADD}?clipId=${clipId}`,
        HttpMethod.POST,
        null,
      );
    } finally {
      homeStore.setLoading(false);
    }
  }

  async getBannerList() {
    const homeStore = useHomeStore();
    homeStore.setLoading(true);

    try {
      const response = await useApi<null, IHomeBannerResponse[]>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.MOBILE_HOME.BASE +
          ROUTES_ENUM.MOBILE_HOME.BANNER +
          ROUTES_ENUM.MOBILE_HOME.BANNER_LIST,
        HttpMethod.GET,
        null,
      );

      console.log(response);

      return response;
    } finally {
      homeStore.setLoading(false);
    }
  }

  async removeBanner(contentId: string, contentType: string) {
    const toast = useToast();
    const homeStore = useHomeStore();
    homeStore.setLoading(true);

    try {
      const response = await useApi<null, null>(
        `${ROUTES_ENUM.ISHOW}${ROUTES_ENUM.ADMIN}${ROUTES_ENUM.MOBILE_HOME.BASE}${ROUTES_ENUM.MOBILE_HOME.BANNER}${ROUTES_ENUM.MOBILE_HOME.BANNER_REMOVE}?contentId=${contentId}&contentType=${contentType}`,
        HttpMethod.DELETE,
        null,
      );

      if (response.status === SUCCES_MESSAGE) {
        toast.success(response.message);
      } else {
        toast.error(response.message);
      }

      return response;
    } catch (error) {
      toast.error("Failed to remove banner");
      throw error;
    } finally {
      homeStore.setLoading(false);
    }
  }
}

export default new HomeService();
