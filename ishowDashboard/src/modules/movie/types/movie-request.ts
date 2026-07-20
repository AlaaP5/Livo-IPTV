import type { PaginationFilter } from "../../../types/filter.types";

export interface IMovieRequest {
  movieId?: string;
  titleEn?: string;
  titleAr?: string;
  descriptionEn?: string;
  descriptionAr?: string;
  badge?: string;
  tags?: number[];
  actors?: number[];
  accessType?: string;
  releaseYear?: string;
  hasRight?: string;
  isKids?: string;
  isSports?: string;
  active?: string;
  language?: string;
  rating?: string;
  duration?: number;
  trailer?: File | null;
  file?: File | null;
  poster?: File | null;
  subtitles?: File[];
  subtitleLanguages: string[];
  audioLanguages: string[];
}

export interface IMovieSectionRequest extends PaginationFilter {
  sectionId: number | null;
  isTop: string | null;
}

export interface IAddMovieToSectionRequest {
  movieId?: string;
  sectionId: number | null;
  isTop: string | null;
}

export interface IRemoveMovieFromSectionRequest {
  movieId?: string;
  sectionId: number | null;
}
