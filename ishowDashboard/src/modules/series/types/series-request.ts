import type { PaginationFilter } from "../../../types/filter.types";

export interface ISeriesRequest {
  seriesId?: string;
  titleEn?: string;
  titleAr?: string;
  descriptionEn?: string;
  descriptionAr?: string;
  badge?: string;
  tags?: number[];
  actors?: number[];
  releaseYear?: string;
  hasRight?: string;
  isKids?: string;
  isSports?: string;
  active?: string;
  language?: string;
  rate?: string;
  poster?: File | null;
  subtitleLanguages: string[];
  audioLanguages: string[];
}


export interface ISeriesSectionRequest extends PaginationFilter {
  sectionId: number | null;
  isTop: string | null;
}


export interface IAddSeriesToSectionRequest {
  seriesId?: string;
  sectionId: number | null;
  isTop: string | null;
}


export interface IRemoveSeriesFromSectionRequest {
  seriesId?: string;
  sectionId: number | null;
}
