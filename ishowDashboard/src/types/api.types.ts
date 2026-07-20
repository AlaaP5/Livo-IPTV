export interface ApiResponse<T> {
  code: string;
  data: T | null;
  status: string;
  message: string;
  page: number;
  pageSize: number;
  totalCount: number;
}
