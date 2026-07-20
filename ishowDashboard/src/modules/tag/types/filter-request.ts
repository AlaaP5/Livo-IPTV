import type { PaginationFilter } from "../../../types/filter.types";

export interface IFilterTagRequest extends PaginationFilter {
    active: string;
    title: string;
}
