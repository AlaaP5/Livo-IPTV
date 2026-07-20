import type { PaginationFilter } from "../../../types/filter.types";

export interface IFilterSeriesRequest extends PaginationFilter {
  name?: string | null;
  accountId?: number | null;
  isActive?: string | null;
  isPublish?: string | null;
  isKids?: string | null;
  isSport?: string | null;
  hasRight?: string | null;
  badge: string | null;
  language: string | null;
}

export interface IFilterSeasonEpisodes extends PaginationFilter {
  seriesId: string;
  seasonId: string;
}
