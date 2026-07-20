import { ref } from "vue";

export function useVideoDuration() {
  const duration = ref<number>(0);
  const loading = ref(false);
  const error = ref<string | null>(null);

  async function getDuration(file: File | null | undefined) {
    if (!file) {
      duration.value = 0;
      return 0;
    }

    loading.value = true;
    error.value = null;

    try {
      const seconds = await calculateDuration(file);

      duration.value = Math.floor(seconds);

      return duration.value;
    } catch (err: any) {
      duration.value = 0;
      error.value = err.message || "Failed to load video duration";

      return 0;
    } finally {
      loading.value = false;
    }
  }

  function calculateDuration(file: File): Promise<number> {
    return new Promise((resolve, reject) => {
      const video = document.createElement("video");

      video.preload = "metadata";

      video.onloadedmetadata = () => {
        URL.revokeObjectURL(video.src);

        resolve(video.duration);
      };

      video.onerror = () => {
        reject(new Error("Failed to load video metadata"));
      };

      video.src = URL.createObjectURL(file);
    });
  }

  return {
    duration,
    loading,
    error,
    getDuration,
  };
}