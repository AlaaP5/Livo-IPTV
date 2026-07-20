import type { AxiosProgressEvent } from "axios";
import apiClient from "./api";

class UploadService {
  async uploadFile(
    file: File,
    onProgress?: (progress: number, speed: string) => void
  ) {
    const formData = new FormData();

    formData.append("file", file); 
    formData.append("bookId", "1");
    formData.append("displayOrder", "30");
    formData.append("active", "0");

    const startTime = Date.now();

    const response = await apiClient.post(
      "/admin/book/file/upload",
      formData,
      {
        headers: {
          "Content-Type": "multipart/form-data",
        },

        onUploadProgress: (event: AxiosProgressEvent) => {
          if (!event.total) return;

          const progress = Math.round((event.loaded * 100) / event.total);

          const elapsed = Math.max((Date.now() - startTime) / 1000, 0.001);
          const rate = event.loaded / elapsed;
          const speed = this.formatSpeed(rate);

          onProgress?.(progress, speed);
        },
      }
    );

    console.log("UPLOAD RESPONSE:", response.data);

    return response.data;
  }

  private formatSpeed(bytesPerSecond: number): string {
    if (bytesPerSecond < 1024) return `${bytesPerSecond.toFixed(2)} B/s`;
    if (bytesPerSecond < 1024 * 1024)
      return `${(bytesPerSecond / 1024).toFixed(2)} KB/s`;
    return `${(bytesPerSecond / (1024 * 1024)).toFixed(2)} MB/s`;
  }
}

export default new UploadService();