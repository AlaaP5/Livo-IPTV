/**
 * Get the color for a transcode status
 * @param status - The transcode status string
 * @returns The Vuetify color name
 */
export const getTranscodeStatusColor = (status: string): string => {
  const statusLower =
    typeof status === "string" ? status.toLowerCase() : status;

  switch (statusLower) {
    case "completed":
    case "ready":
    case "done":
      return "green";
    case "processing":
    case "transcoding":
      return "orange";
    case "pending":
    case "queued":
      return "blue";
    case "failed":
    case "error":
      return "red";
    case "cancelled":
      return "grey";
    default:
      return "grey";
  }
};

/**
 * Get the display text for a transcode status (optional utility)
 * @param status - The transcode status string
 * @returns The formatted display text
 */
export const getTranscodeStatusText = (status: string): string => {
  const statusLower =
    typeof status === "string" ? status.toLowerCase() : status;

  switch (statusLower) {
    case "completed":
    case "ready":
    case "done":
      return "Completed";
    case "processing":
    case "transcoding":
      return "Processing";
    case "pending":
    case "queued":
      return "Pending";
    case "failed":
    case "error":
      return "Failed";
    case "cancelled":
      return "Cancelled";
    default:
      return status || "Unknown";
  }
};

/**
 * Check if transcode is completed (optional utility)
 * @param status - The transcode status string
 * @returns boolean indicating if completed
 */
export const isTranscodeCompleted = (status: string): boolean => {
  const statusLower =
    typeof status === "string" ? status.toLowerCase() : status;
  return ["completed", "ready", "done"].includes(statusLower);
};

/**
 * Check if transcode is in progress (optional utility)
 * @param status - The transcode status string
 * @returns boolean indicating if in progress
 */
export const isTranscodeInProgress = (status: string): boolean => {
  const statusLower =
    typeof status === "string" ? status.toLowerCase() : status;
  return ["processing", "transcoding", "pending", "queued"].includes(
    statusLower,
  );
};

/**
 * Check if transcode failed (optional utility)
 * @param status - The transcode status string
 * @returns boolean indicating if failed
 */
export const isTranscodeFailed = (status: string): boolean => {
  const statusLower =
    typeof status === "string" ? status.toLowerCase() : status;
  return ["failed", "error"].includes(statusLower);
};
