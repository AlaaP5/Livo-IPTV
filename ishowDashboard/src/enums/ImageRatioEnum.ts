export const ImageRatio = {
  RATIO_16_9: { width: 16, height: 9, label: "16:9" },
  RATIO_1_1: { width: 1, height: 1, label: "1:1" },
  RATIO_9_16: { width: 9, height: 16, label: "9:16" },
  RATIO_2_3: { width: 2, height: 3, label: "2:3" }
} as const;

export type ImageRatioType =
  typeof ImageRatio[keyof typeof ImageRatio];
