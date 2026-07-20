import type { PaginationFilter } from "../../../types/filter.types";

export interface IFilterAccountRequest extends PaginationFilter {
  userName: string;
  role: string;
  status: string;
}
