import { useApi } from "../composables/useApi";
import { HttpMethod } from "../enums/httpMethodEnum";
import type { ILookupRequest, ILookupResponse } from "../types/lookup.types";
import { ROUTES_ENUM } from "../utils/constants";

class LookupService {
  async getAllAccounts(payload: ILookupRequest) {
    try {
      const response = await useApi<ILookupRequest, ILookupResponse[]>(
        ROUTES_ENUM.ISHOW + ROUTES_ENUM.LOOKUP.BASE + ROUTES_ENUM.LOOKUP.ACCOUNT,
        HttpMethod.GET,
        payload,
      );

      return response.data ?? [];
    } finally {
    }
  }

  async getAllTags(payload: ILookupRequest) {
    try {
      const response = await useApi<ILookupRequest, ILookupResponse[]>(
        ROUTES_ENUM.ISHOW + ROUTES_ENUM.LOOKUP.BASE + ROUTES_ENUM.LOOKUP.TAG,
        HttpMethod.GET,
        payload,
      );

      return response.data ?? [];
    } finally {
    }
  }

  async getAllActors(payload: ILookupRequest) {
    try {
      const response = await useApi<ILookupRequest, ILookupResponse[]>(
        ROUTES_ENUM.ISHOW + ROUTES_ENUM.LOOKUP.BASE + ROUTES_ENUM.LOOKUP.ACTOR,
        HttpMethod.GET,
        payload,
      );

      return response.data ?? [];
    } finally {
    }
  }

  async getAllSections(payload: ILookupRequest) {
    try {
      const response = await useApi<ILookupRequest, ILookupResponse[]>(
        ROUTES_ENUM.ISHOW + ROUTES_ENUM.LOOKUP.BASE + ROUTES_ENUM.LOOKUP.SECTION,
        HttpMethod.GET,
        payload,
      );

      return response.data ?? [];
    } finally {
    }
  }

  async getAllMovies(payload: ILookupRequest) {
    try {
      const response = await useApi<ILookupRequest, ILookupResponse[]>(
       ROUTES_ENUM.ISHOW +  ROUTES_ENUM.LOOKUP.BASE + ROUTES_ENUM.LOOKUP.MOVIE,
        HttpMethod.GET,
        payload,
      );

      return response.data ?? [];
    } finally {
    }
  }

  async getAllKidsMovies(payload: ILookupRequest) {
    try {
      const response = await useApi<ILookupRequest, ILookupResponse[]>(
       ROUTES_ENUM.ISHOW +  ROUTES_ENUM.LOOKUP.BASE + ROUTES_ENUM.LOOKUP.KIDS_MOVIE,
        HttpMethod.GET,
        payload,
      );

      console.log("test")
      console.log(response.data)

      return response.data ?? [];
    } finally {
    }
  }

  async getAllSportMovies(payload: ILookupRequest) {
    try {
      const response = await useApi<ILookupRequest, ILookupResponse[]>(
       ROUTES_ENUM.ISHOW +  ROUTES_ENUM.LOOKUP.BASE + ROUTES_ENUM.LOOKUP.SPORT_MOVIE,
        HttpMethod.GET,
        payload,
      );

      return response.data ?? [];
    } finally {
    }
  }

  async getAllClips(payload: ILookupRequest) {
    try {
      const response = await useApi<ILookupRequest, ILookupResponse[]>(
        ROUTES_ENUM.ISHOW + ROUTES_ENUM.LOOKUP.BASE + ROUTES_ENUM.LOOKUP.CLIP,
        HttpMethod.GET,
        payload,
      );

      return response.data ?? [];
    } finally {
    }
  }

  async getAllKidsClips(payload: ILookupRequest) {
    try {
      const response = await useApi<ILookupRequest, ILookupResponse[]>(
       ROUTES_ENUM.ISHOW +  ROUTES_ENUM.LOOKUP.BASE + ROUTES_ENUM.LOOKUP.KIDS_CLIP,
        HttpMethod.GET,
        payload,
      );

      return response.data ?? [];
    } finally {
    }
  }

  async getAllSportClips(payload: ILookupRequest) {
    try {
      const response = await useApi<ILookupRequest, ILookupResponse[]>(
       ROUTES_ENUM.ISHOW +  ROUTES_ENUM.LOOKUP.BASE + ROUTES_ENUM.LOOKUP.SPORT_CLIP,
        HttpMethod.GET,
        payload,
      );

      return response.data ?? [];
    } finally {
    }
  }

  async getAllSeries(payload: ILookupRequest) {
    try {
      const response = await useApi<ILookupRequest, ILookupResponse[]>(
        ROUTES_ENUM.ISHOW + ROUTES_ENUM.LOOKUP.BASE + ROUTES_ENUM.LOOKUP.SERIES,
        HttpMethod.GET,
        payload,
      );

      return response.data ?? [];
    } finally {
    }
  }

  async getAllKidsSeries(payload: ILookupRequest) {
    try {
      const response = await useApi<ILookupRequest, ILookupResponse[]>(
       ROUTES_ENUM.ISHOW +  ROUTES_ENUM.LOOKUP.BASE + ROUTES_ENUM.LOOKUP.KIDS_SERIES,
        HttpMethod.GET,
        payload,
      );

      return response.data ?? [];
    } finally {
    }
  }

  async getAllSportSeries(payload: ILookupRequest) {
    try {
      const response = await useApi<ILookupRequest, ILookupResponse[]>(
       ROUTES_ENUM.ISHOW +  ROUTES_ENUM.LOOKUP.BASE + ROUTES_ENUM.LOOKUP.SPORT_SERIES,
        HttpMethod.GET,
        payload,
      );

      return response.data ?? [];
    } finally {
    }
  }

  async getAllChannels(payload: ILookupRequest) {
    try {
      const response = await useApi<ILookupRequest, ILookupResponse[]>(
        ROUTES_ENUM.ISHOW + ROUTES_ENUM.LOOKUP.BASE + ROUTES_ENUM.LOOKUP.CHANNEL,
        HttpMethod.GET,
        payload,
      );
      console.log(response);
      return response.data ?? [];
    } finally {
    }
  }

  async getAllKidsChannels(payload: ILookupRequest) {
    try {
      const response = await useApi<ILookupRequest, ILookupResponse[]>(
       ROUTES_ENUM.ISHOW +  ROUTES_ENUM.LOOKUP.BASE + ROUTES_ENUM.LOOKUP.KIDS_CHANNEL,
        HttpMethod.GET,
        payload,
      );

      return response.data ?? [];
    } finally {
    }
  }

  async getAllSportChannels(payload: ILookupRequest) {
    try {
      const response = await useApi<ILookupRequest, ILookupResponse[]>(
       ROUTES_ENUM.ISHOW +  ROUTES_ENUM.LOOKUP.BASE + ROUTES_ENUM.LOOKUP.SPORT_CHANNEL,
        HttpMethod.GET,
        payload,
      );

      return response.data ?? [];
    } finally {
    }
  }

  async getAllTvPrograms(payload: ILookupRequest) {
    try {
      const response = await useApi<ILookupRequest, ILookupResponse[]>(
        ROUTES_ENUM.ISHOW + ROUTES_ENUM.LOOKUP.BASE + ROUTES_ENUM.LOOKUP.TV_PROGRAMS,
        HttpMethod.GET,
        payload,
      );
      console.log(response);
      return response.data ?? [];
    } finally {
    }
  }

  async getAllKidsTvPrograms(payload: ILookupRequest) {
    try {
      const response = await useApi<ILookupRequest, ILookupResponse[]>(
       ROUTES_ENUM.ISHOW +  ROUTES_ENUM.LOOKUP.BASE + ROUTES_ENUM.LOOKUP.KIDS_TV_PROGRAMS,
        HttpMethod.GET,
        payload,
      );

      return response.data ?? [];
    } finally {
    }
  }

  async getAllSportTvPrograms(payload: ILookupRequest) {
    try {
      const response = await useApi<ILookupRequest, ILookupResponse[]>(
       ROUTES_ENUM.ISHOW +  ROUTES_ENUM.LOOKUP.BASE + ROUTES_ENUM.LOOKUP.SPORT_TV_PROGRAMS,
        HttpMethod.GET,
        payload,
      );

      return response.data ?? [];
    } finally {
    }
  }

  async getAllTeams(payload: ILookupRequest) {
    try {
      const response = await useApi<ILookupRequest, ILookupResponse[]>(
        ROUTES_ENUM.ISHOW + ROUTES_ENUM.LOOKUP.BASE + ROUTES_ENUM.LOOKUP.TEAM,
        HttpMethod.GET,
        payload,
      );
      console.log(response);
      return response.data ?? [];
    } finally {
    }
  }

  async getAllChampions(payload: ILookupRequest) {
    try {
      const response = await useApi<ILookupRequest, ILookupResponse[]>(
        ROUTES_ENUM.ISHOW + ROUTES_ENUM.LOOKUP.BASE + ROUTES_ENUM.LOOKUP.CHAMPION,
        HttpMethod.GET,
        payload,
      );
      console.log(response);
      return response.data ?? [];
    } finally {
    }
  }
}

export default new LookupService();
