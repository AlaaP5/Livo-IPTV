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
import { useSportStore } from "../stores/sport";


class SportService {
  async viewSectionMovies(payload: IMovieSectionRequest) {
    const sportStore = useSportStore();
    sportStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<
        IMovieSectionRequest,
        ISectionMovieResponse[]
      >(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.SPORT.BASE +
          ROUTES_ENUM.SPORT.VIEW_SECTION_MOVIES,
        HttpMethod.GET,
        payload,
      );

      console.log(response);

      return response;
    } finally {
      sportStore.setLoading(false);
    }
  }

  async addMovieToSection(payload: IAddMovieToSectionRequest) {
    const toast = useToast();
    const sportStore = useSportStore();
    sportStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<IAddMovieToSectionRequest, null>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.SPORT.BASE +
          ROUTES_ENUM.SPORT.ADD_MOVIE_TO_SECTION,
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
      sportStore.setLoading(false);
    }
  }

  async removeMovieFromSection(payload: IRemoveMovieFromSectionRequest) {
    const toast = useToast();
    const sportStore = useSportStore();
    sportStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<IRemoveMovieFromSectionRequest, null>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.SPORT.BASE +
          ROUTES_ENUM.SPORT.REMOVE_MOVIE_FROM_SECTION,
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
      sportStore.setLoading(false);
    }
  }

  async changeStatusMovie(payload: IAddMovieToSectionRequest) {
    const toast = useToast();
    const sportStore = useSportStore();
    sportStore.setLoading(true);

    try {
      const url = `${ROUTES_ENUM.ISHOW}${ROUTES_ENUM.ADMIN}${ROUTES_ENUM.SPORT.BASE}${ROUTES_ENUM.SPORT.MOVIE_SECTION_IS_TOP}?sectionId=${payload.sectionId}&movieId=${payload.movieId}&isTop=${payload.isTop}`;

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
      sportStore.setLoading(false);
    }
  }

  async viewSectionSeries(payload: ISeriesSectionRequest) {
    const sportStore = useSportStore();
    sportStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<
        ISeriesSectionRequest,
        ISectionSeriesResponse[]
      >(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.SPORT.BASE +
          ROUTES_ENUM.SPORT.VIEW_SECTION_SERIES,
        HttpMethod.GET,
        payload,
      );

      console.log(response);

      return response;
    } finally {
      sportStore.setLoading(false);
    }
  }

  async addSeriesToSection(payload: IAddSeriesToSectionRequest) {
    const toast = useToast();
    const sportStore = useSportStore();
    sportStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<IAddSeriesToSectionRequest, null>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.SPORT.BASE +
          ROUTES_ENUM.SPORT.ADD_SERIES_TO_SECTION,
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
      sportStore.setLoading(false);
    }
  }

  async removeSeriesFromSection(payload: IRemoveSeriesFromSectionRequest) {
    const toast = useToast();
    const sportStore = useSportStore();
    sportStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<IRemoveSeriesFromSectionRequest, null>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.SPORT.BASE +
          ROUTES_ENUM.SPORT.REMOVE_SERIES_FROM_SECTION,
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
      sportStore.setLoading(false);
    }
  }

  async changeStatusSeries(payload: IAddSeriesToSectionRequest) {
    const toast = useToast();
    const sportStore = useSportStore();
    sportStore.setLoading(true);

    try {
      const url = `${ROUTES_ENUM.ISHOW}${ROUTES_ENUM.ADMIN}${ROUTES_ENUM.SPORT.BASE}${ROUTES_ENUM.SPORT.SERIES_SECTION_IS_TOP}?sectionId=${payload.sectionId}&seriesId=${payload.seriesId}&isTop=${payload.isTop}`;

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
      sportStore.setLoading(false);
    }
  }

  async viewSectionClips(payload: IClipSectionRequest) {
    const sportStore = useSportStore();
    sportStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<
        IClipSectionRequest,
        ISectionClipResponse[]
      >(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.SPORT.BASE +
          ROUTES_ENUM.SPORT.VIEW_SECTION_CLIP,
        HttpMethod.GET,
        payload,
      );

      console.log(response);

      return response;
    } finally {
      sportStore.setLoading(false);
    }
  }

  async addClipToSection(payload: IAddClipToSectionRequest) {
    const toast = useToast();
    const sportStore = useSportStore();
    sportStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<IAddClipToSectionRequest, null>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.SPORT.BASE +
          ROUTES_ENUM.SPORT.ADD_CLIP_TO_SECTION,
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
      sportStore.setLoading(false);
    }
  }

  async removeClipFromSection(payload: IRemoveClipFromSectionRequest) {
    const toast = useToast();
    const sportStore = useSportStore();
    sportStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<IRemoveClipFromSectionRequest, null>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.SPORT.BASE +
          ROUTES_ENUM.SPORT.REMOVE_CLIP_FROM_SECTION,
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
      sportStore.setLoading(false);
    }
  }

  async changeStatusClip(payload: IAddClipToSectionRequest) {
    const toast = useToast();
    const sportStore = useSportStore();
    sportStore.setLoading(true);

    try {
      const url = `${ROUTES_ENUM.ISHOW}${ROUTES_ENUM.ADMIN}${ROUTES_ENUM.SPORT.BASE}${ROUTES_ENUM.SPORT.CLIP_SECTION_IS_TOP}?sectionId=${payload.sectionId}&clipId=${payload.clipId}&isTop=${payload.isTop}`;

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
      sportStore.setLoading(false);
    }
  }

  async viewSectionChannels(payload: IChannelSectionRequest) {
    const sportStore = useSportStore();
    sportStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<
        IChannelSectionRequest,
        ISectionChannelsResponse[]
      >(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.SPORT.BASE +
          ROUTES_ENUM.SPORT.VIEW_SECTION_CHANNELS,
        HttpMethod.GET,
        payload,
      );

      console.log(response);

      return response;
    } finally {
      sportStore.setLoading(false);
    }
  }

  async addChannelToSection(payload: IAddChannelToSectionRequest) {
    const toast = useToast();
    const sportStore = useSportStore();
    sportStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<IAddChannelToSectionRequest, null>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.SPORT.BASE +
          ROUTES_ENUM.SPORT.ADD_CHANNEL_TO_SECTION,
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
      sportStore.setLoading(false);
    }
  }

  async removeChannelFromSection(payload: IRemoveChannelFromSectionRequest) {
    const toast = useToast();
    const sportStore = useSportStore();
    sportStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<IRemoveChannelFromSectionRequest, null>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.SPORT.BASE +
          ROUTES_ENUM.SPORT.REMOVE_CHANNEL_FROM_SECTION,
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
      sportStore.setLoading(false);
    }
  }

  async changeStatusChannel(payload: IAddChannelToSectionRequest) {
    const toast = useToast();
    const sportStore = useSportStore();
    sportStore.setLoading(true);

    try {
      const url = `${ROUTES_ENUM.ISHOW}${ROUTES_ENUM.ADMIN}${ROUTES_ENUM.SPORT.BASE}${ROUTES_ENUM.SPORT.CHANNEL_SECTION_IS_TOP}?sectionId=${payload.sectionId}&channelId=${payload.channelId}&isTop=${payload.isTop}`;

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
      sportStore.setLoading(false);
    }
  }

  async viewSectionTvs(payload: ITvSectionRequest) {
    const sportStore = useSportStore();
    sportStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<ITvSectionRequest, ISectionTvResponse[]>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.SPORT.BASE +
          ROUTES_ENUM.SPORT.VIEW_SECTION_TVS,
        HttpMethod.GET,
        payload,
      );

      console.log(response);

      return response;
    } finally {
      sportStore.setLoading(false);
    }
  }

  async addTvToSection(payload: IAddTvToSectionRequest) {
    const toast = useToast();
    const sportStore = useSportStore();
    sportStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<IAddTvToSectionRequest, null>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.SPORT.BASE +
          ROUTES_ENUM.SPORT.ADD_TV_TO_SECTION,
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
      sportStore.setLoading(false);
    }
  }

  async removeTvFromSection(payload: IRemoveTvFromSectionRequest) {
    const toast = useToast();
    const sportStore = useSportStore();
    sportStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<IRemoveTvFromSectionRequest, null>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.SPORT.BASE +
          ROUTES_ENUM.SPORT.REMOVE_TV_FROM_SECTION,
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
      sportStore.setLoading(false);
    }
  }

  async changeStatusTv(payload: IAddTvToSectionRequest) {
    const toast = useToast();
    const sportStore = useSportStore();
    sportStore.setLoading(true);

    try {
      const url = `${ROUTES_ENUM.ISHOW}${ROUTES_ENUM.ADMIN}${ROUTES_ENUM.SPORT.BASE}${ROUTES_ENUM.SPORT.TV_SECTION_IS_TOP}?sectionId=${payload.sectionId}&tvProgramId=${payload.tvProgramId}&isTop=${payload.isTop}`;

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
      sportStore.setLoading(false);
    }
  }
}

export default new SportService();
