import type { PaginationFilter } from "../../../types/filter.types";

export interface IFilterChannelRequest extends PaginationFilter {
  name?: string | null;
  accountId?: number | null;
  isActive?: string;
  isPublish?: string | null;
  isKids?: string | null;
  isSport?: string | null;
  badge?: string | null;
  tagId?: number | null;
}
