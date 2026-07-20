import type { PaginationFilter } from "../../../types/filter.types";

export interface IClipRequest {
  clipId?: string;
  titleEn?: string;
  titleAr?: string;
  descriptionEn?: string;
  descriptionAr?: string;
  badge?: string;
  tags?: number[];
  accessType?: string;
  duration?: number;
  hasRight?: string;
  isKids?: string;
  isSports?: string;
  active?: string;
  file?: File | null;
  poster?: File | null;
}

export interface IClipSectionRequest extends PaginationFilter {
  sectionId: number | null;
  isTop: string | null;
}

export interface IAddClipToSectionRequest {
  clipId?: string;
  sectionId: number | null;
  isTop: string | null;
}

export interface IRemoveClipFromSectionRequest {
  clipId?: string;
  sectionId: number | null;
}
