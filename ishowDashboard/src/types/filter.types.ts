export interface ListFilter extends PaginationFilter {
  active: string;
  search: string;
}

export interface PaginationFilter {
  page: number;
  size: number;
}
