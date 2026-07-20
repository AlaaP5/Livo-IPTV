import type { PaginationFilter } from "../../../types/filter.types";

export interface IFilterClipRequest extends PaginationFilter {
  name?: string | null;
  status?: string | null;
  accountId?: number | null;
  isActive?: string | null;
  isPublish?: string | null;
  isKids?: string | null;
  isSport?: string | null;
  accessType?: string | null;
  badge: string | null;
}
