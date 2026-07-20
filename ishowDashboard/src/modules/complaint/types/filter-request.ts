import type { PaginationFilter } from "../../../types/filter.types";

export interface IFilterComplaintRequest extends PaginationFilter {
  status: string;
  phoneNumber: string;
  title: string;
}
