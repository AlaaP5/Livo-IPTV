import type { PaginationFilter } from "../../../types/filter.types";

export interface IFilterAdRequest extends PaginationFilter {
    startDate: string;
    endDate: string;
    active: string;
}
