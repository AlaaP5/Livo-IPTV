import { useApi } from "../../../composables/useApi";
import { HttpMethod } from "../../../enums/httpMethodEnum";
import { ROUTES_ENUM, SUCCES_MESSAGE } from "../../../utils/constants";
import { useToast } from "vue-toastification";
import type { IFilterMovieRequest } from "../types/filter-request";
import { useMovieStore } from "../stores/movie";
import type {
  IFilterMovieResponse,
  IMovieResponse,
  ISectionMovieResponse,
} from "../types/movie-response";
import type {
  IAddMovieToSectionRequest,
  IMovieRequest,
  IMovieSectionRequest,
  IRemoveMovieFromSectionRequest,
} from "../types/movie-request";

class MovieService {
  async filterMovies(payload: IFilterMovieRequest) {
    const movieStore = useMovieStore();
    movieStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<IFilterMovieRequest, IFilterMovieResponse[]>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.MOVIE.BASE +
          ROUTES_ENUM.MOVIE.FILTER,
        HttpMethod.POST,
        payload,
      );
      console.log(response);

      return response;
    } finally {
      movieStore.setLoading(false);
    }
  }

  async createMovie(payload: IMovieRequest) {
    const toast = useToast();
    const movieStore = useMovieStore();
    movieStore.setLoading(true);

    try {
      const formData = new FormData();

      if (!payload.file) {
        throw new Error("No file selected");
      }

      if (!payload.trailer) {
        throw new Error("No trailer selected");
      }

      if (!payload.poster) {
        throw new Error("No poster selected");
      }

      if (!payload.subtitles) {
        throw new Error("No subtitles selected");
      }

      formData.append("titleEn", payload.titleEn || "");
      formData.append("titleAr", payload.titleAr || "");
      formData.append("descriptionEn", payload.descriptionEn || "");
      formData.append("descriptionAr", payload.descriptionAr || "");
      formData.append("badge", payload.badge || "");
      formData.append("accessType", payload.accessType || "");
      formData.append("releaseYear", payload.releaseYear || "");
      formData.append("language", payload.language || "");
      formData.append("rating", payload.rating || "");
      formData.append("duration", payload.duration?.toString() || "");
      formData.append("active", payload.active || "");
      formData.append("hasRight", payload.hasRight || "");
      formData.append("isKids", payload.isKids || "");
      formData.append("isSports", payload.isSports || "");

      formData.append("file", payload.file);

      formData.append("trailer", payload.trailer);

      formData.append("poster", payload.poster);

      payload.subtitles.forEach((file) => {
        formData.append("subtitles", file);
      });

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

      const response = await useApi<FormData, IMovieResponse>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.MOVIE.BASE +
          ROUTES_ENUM.MOVIE.CREATE,
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
      movieStore.setLoading(false);
    }
  }

  async updateMovie(payload: IMovieRequest) {
    const toast = useToast();
    const movieStore = useMovieStore();
    movieStore.setLoading(true);

    try {
      const formData = new FormData();

      formData.append("movieId", payload.movieId || "");
      formData.append("titleEn", payload.titleEn || "");
      formData.append("titleAr", payload.titleAr || "");
      formData.append("descriptionEn", payload.descriptionEn || "");
      formData.append("descriptionAr", payload.descriptionAr || "");
      formData.append("badge", payload.badge || "");
      formData.append("duration", payload.duration?.toString() || "");
      formData.append("accessType", payload.accessType || "");
      formData.append("releaseYear", payload.releaseYear || "");
      formData.append("language", payload.language || "");
      formData.append("rating", payload.rating || "");
      formData.append("active", payload.active || "");
      formData.append("hasRight", payload.hasRight || "");
      formData.append("isKids", payload.isKids || "");
      formData.append("isSports", payload.isSports || "");

      if (payload.file) {
        formData.append("file", payload.file);
      }

      if (payload.trailer) {
        formData.append("trailer", payload.trailer);
      }

      if (payload.poster) {
        formData.append("poster", payload.poster);
      }

      if (payload.subtitles) {
        payload.subtitles.forEach((file) => {
          formData.append("subtitles", file || "");
        });
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

      const response = await useApi<FormData, IMovieResponse>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.MOVIE.BASE +
          ROUTES_ENUM.MOVIE.UPDATE,
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
      movieStore.setLoading(false);
    }
  }

  async getMovie(id?: string) {
    const movieStore = useMovieStore();
    movieStore.setLoading(true);

    try {
      const response = await useApi<null, IMovieResponse>(
        `${ROUTES_ENUM.ISHOW}${ROUTES_ENUM.ADMIN}${ROUTES_ENUM.MOVIE.BASE}/${id}`,
        HttpMethod.GET,
        null,
      );
      console.log(id);
      console.log(response);

      return response.data;
    } finally {
      movieStore.setLoading(false);
    }
  }

  async publishMovie(id: string) {
    const toast = useToast();
    const movieStore = useMovieStore();
    movieStore.setLoading(true);

    try {
      const response = await useApi<null, IMovieResponse>(
        `${ROUTES_ENUM.ISHOW}${ROUTES_ENUM.ADMIN}${ROUTES_ENUM.MOVIE.BASE}${ROUTES_ENUM.MOVIE.PUBLISH}/${id}`,
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
      movieStore.setLoading(false);
    }
  }

  async viewSectionMovies(payload: IMovieSectionRequest) {
    const movieStore = useMovieStore();
    movieStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<
        IMovieSectionRequest,
        ISectionMovieResponse[]
      >(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.MOVIE.BASE +
          ROUTES_ENUM.MOVIE.VIEW_SECTION_MOVIES,
        HttpMethod.GET,
        payload,
      );

      console.log(response);

      return response;
    } finally {
      movieStore.setLoading(false);
    }
  }

  async addMovieToSection(payload: IAddMovieToSectionRequest) {
    const toast = useToast();
    const movieStore = useMovieStore();
    movieStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<IAddMovieToSectionRequest, null>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.MOVIE.BASE +
          ROUTES_ENUM.MOVIE.ADD_MOVIE_TO_SECTION,
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
      movieStore.setLoading(false);
    }
  }

  async removeMovieFromSection(payload: IRemoveMovieFromSectionRequest) {
    const toast = useToast();
    const movieStore = useMovieStore();
    movieStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<IRemoveMovieFromSectionRequest, null>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.MOVIE.BASE +
          ROUTES_ENUM.MOVIE.REMOVE_MOVIE_FROM_SECTION,
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
      movieStore.setLoading(false);
    }
  }

  async changeStatusMovie(payload: IAddMovieToSectionRequest) {
    const toast = useToast();
    const movieStore = useMovieStore();
    movieStore.setLoading(true);

    try {
      const url = `${ROUTES_ENUM.ISHOW}${ROUTES_ENUM.ADMIN}${ROUTES_ENUM.MOVIE.BASE}${ROUTES_ENUM.MOVIE.SECTION_IS_TOP}?sectionId=${payload.sectionId}&movieId=${payload.movieId}&isTop=${payload.isTop}`;

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
      movieStore.setLoading(false);
    }
  }
}

export default new MovieService();
