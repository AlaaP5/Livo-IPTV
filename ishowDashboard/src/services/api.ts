import axios, { type AxiosInstance, type AxiosRequestConfig } from "axios";
import router from "../router";
import { ROUTES_ENUM } from "../utils/constants";

interface RetryAxiosRequestConfig extends AxiosRequestConfig {
  _retry?: boolean;
}

let baseUrlStage = "http://localhost:9001";
let baseUrlTest = "https://ishow-test.doremi.click";
let baseUrlProd = "https://religion.emaniyat.net";

let isRefreshing = false;

let failedQueue: {
  resolve: (token: string) => void;
  reject: (err: any) => void;
}[] = [];

const processQueue = (error: any, token: string | null = null) => {
  failedQueue.forEach((p) => {
    error ? p.reject(error) : p.resolve(token!);
  });
  failedQueue = [];
};

const forceLogout = () => {
  localStorage.removeItem("auth_token");
  localStorage.removeItem("refresh_token");
  router.replace({
    path: ROUTES_ENUM.AUTH.BASE + ROUTES_ENUM.AUTH.LOGIN,
  });
};

const apiClient: AxiosInstance = axios.create({
  baseURL: baseUrlTest, //baseUrlStage , baseUrlTest
  headers: {
    Accept: "application/json",
  },
});

apiClient.interceptors.request.use((config) => {
  const accessToken = localStorage.getItem("auth_token");
  const refreshToken = localStorage.getItem("refresh_token");

  const isRefreshRequest = config.url?.includes("/auth/refreshToken");
  const isLogoutRequest = config.url?.includes("/auth/logout");
  const isStreamRequest = config.url?.includes("/poster");

  if (isRefreshRequest || isLogoutRequest || isStreamRequest) {
    delete config.headers.Authorization;
  } else if (accessToken) {
    config.headers.Authorization = `Bearer ${accessToken}`;
  }

  if (refreshToken && (isRefreshRequest || isLogoutRequest)) {
    config.headers["X-Refresh-Token"] = refreshToken;
  }

  return config;
});

apiClient.interceptors.response.use(
  async (response) => {
    const originalRequest = response.config as RetryAxiosRequestConfig;

    const message = response.data?.message;
    const code = response.data?.code;

    const isRefreshRequest =
      originalRequest.url?.includes("/auth/refreshToken");
    const isLogoutRequest = originalRequest.url?.includes("/auth/logout");

    if (
      message === "TOKEN_EXPIRED" &&
      code === "TOKEN_EXPIRED" &&
      !originalRequest._retry &&
      !isRefreshRequest &&
      !isLogoutRequest
    ) {
      originalRequest._retry = true;
      return handleTokenExpired(originalRequest);
    }

    return response;
  },

  async (error) => {
    return Promise.reject(error);
  },
);

async function handleTokenExpired(originalRequest: RetryAxiosRequestConfig) {
  if (isRefreshing) {
    return new Promise((resolve, reject) => {
      failedQueue.push({ resolve, reject });
    }).then((token) => {
      originalRequest.headers = {
        ...originalRequest.headers,
        Authorization: `Bearer ${token}`,
      };
      return apiClient(originalRequest);
    });
  }

  isRefreshing = true;

  try {
    const refreshToken = localStorage.getItem("refresh_token");

    if (!refreshToken) {
      forceLogout();
      return Promise.reject("NO_REFRESH_TOKEN");
    }

    const refreshResponse = await apiClient.get(
      ROUTES_ENUM.ISHOW +
        ROUTES_ENUM.ADMIN +
        ROUTES_ENUM.AUTH.BASE +
        ROUTES_ENUM.AUTH.REFRESH_TOKEN,
    );

    const { access_token, refresh_token } = refreshResponse.data?.data || {};

    if (!access_token || !refresh_token) {
      forceLogout();
      return Promise.reject("INVALID_REFRESH_RESPONSE");
    }

    localStorage.setItem("auth_token", access_token);
    localStorage.setItem("refresh_token", refresh_token);

    apiClient.defaults.headers.common.Authorization = `Bearer ${access_token}`;

    processQueue(null, access_token);

    originalRequest.headers = {
      ...originalRequest.headers,
      Authorization: `Bearer ${access_token}`,
    };

    return apiClient(originalRequest);
  } catch (err) {
    processQueue(err, null);
    forceLogout();
    return Promise.reject(err);
  } finally {
    isRefreshing = false;
  }
}

export default apiClient;
