import type { AxiosResponse } from "axios";
import apiClient from "../services/api";
import { HttpMethod } from "../enums/httpMethodEnum";
import type { ApiResponse } from "../types/api.types";
import { useToast } from "vue-toastification";

const toast = useToast();

export async function useApi<T, V>(
  endpoint: string,
  method: HttpMethod = HttpMethod.GET,
  payload?: T | null,
): Promise<ApiResponse<V>> {
  try {
    let response: AxiosResponse<ApiResponse<V>>;

    const isFormData = payload instanceof FormData;

    switch (method) {
      case HttpMethod.POST:
        response = await apiClient.post(endpoint, payload, {
          headers: isFormData
            ? { "Content-Type": "multipart/form-data" }
            : undefined,
        });
        break;
      case HttpMethod.PUT:
        response = await apiClient.put(endpoint, payload);
        break;
      case HttpMethod.PATCH:
        response = await apiClient.patch(endpoint, payload);
        break;
      case HttpMethod.DELETE:
        response = await apiClient.delete(endpoint);
        break;
      case HttpMethod.GET:
      default:
        response = await apiClient.get(endpoint, {
          params: payload,
        });
    }

    return response.data;
  } catch (err: any) {
    toast.error(err?.message || "Request failed", {
      timeout: 5000
    });
    return {
      status: "FAILED",
      message: err?.message || "Error",
      data: null,
      page: 1,
      pageSize: 10,
      totalCount: 0,
      code: "FAILED",
    };
  }
}
