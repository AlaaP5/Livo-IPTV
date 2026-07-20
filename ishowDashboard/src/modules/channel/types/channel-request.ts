import type { PaginationFilter } from "../../../types/filter.types";

export interface IChannelRequest {
  id?: string;
  titleEn?: string;
  titleAr?: string;
  descriptionEn?: string;
  descriptionAr?: string;
  badge?: string;
  tags?: number[];
  hasRight?: string;
  isKids?: string;
  active?: string;
  isSports?: string;
  logo?: File | null;
}

export interface IChannelSectionRequest extends PaginationFilter {
  sectionId: number | null;
  isTop: string | null;
}

export interface IAddChannelToSectionRequest {
  channelId?: string;
  sectionId: number | null;
  isTop: string | null;
}

export interface IRemoveChannelFromSectionRequest {
  channelId?: string;
  sectionId: number;
}
