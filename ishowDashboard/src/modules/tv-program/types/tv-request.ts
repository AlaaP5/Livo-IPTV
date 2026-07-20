import type { PaginationFilter } from "../../../types/filter.types";

export interface ITvRequest {
  tvProgramId?: string;
  titleEn?: string;
  titleAr?: string;
  descriptionEn?: string;
  descriptionAr?: string;
  badge?: string;
  tags?: number[];
  releaseYear?: string;
  hasRight?: string;
  isKids?: string;
  isSports?: string;
  active?: string;
  language?: string;
  poster?: File | null;
  subtitleLanguages: string[];
  audioLanguages: string[];
}

export interface ITvSectionRequest extends PaginationFilter {
  sectionId: number | null;
  isTop: string | null;
}

export interface IAddTvToSectionRequest {
  programId?: string;
  sectionId: number | null;
  isTop: string | null;
}

export interface IRemoveTvFromSectionRequest {
  tvId?: string;
  sectionId: number | null;
}
