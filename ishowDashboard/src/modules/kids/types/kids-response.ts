import type { Tags } from "../../../types/metadata";

export interface IKidsBannerResponse {
  contentId: string;
  contentArabicTitle: string;
  contentEnglishTitle: string;
  poster: string;
  contentType: string;
  tagMeta: Tags[];
  createAt: string;
  isKids: boolean;
}