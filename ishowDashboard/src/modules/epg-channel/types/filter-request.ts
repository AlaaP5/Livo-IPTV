import type { PaginationFilter } from "../../../types/filter.types";

export interface IFilterChannelEpgRequest extends PaginationFilter {
  channelId?: string | null;
  startDate?: string | null;
  endDate?: string | null;
  title?: string | null;
  active?: string | null;
}