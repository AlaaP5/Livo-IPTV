import type {
  OriginalUploadMetadata,
  TranscodeMetaData,
} from "./metadata.video";

export const Badge = {
  NONE: "NONE",
  HOT: "HOT",
  FREE: "FREE",
  NEW: "NEW",
} as const;

export type Badge = (typeof Badge)[keyof typeof Badge];

export const badges = [
  { value: "NONE", title: "NONE" },
  { value: "HOT", title: "HOT" },
  { value: "FREE", title: "FREE" },
  { value: "NEW", title: "NEW" },
] as const;
// -------------------------------------------------

export const AccessType = {
  FREE: "FREE",
  PAID: "PAID",
  LOCKED: "LOCKED",
} as const;

export type AccessType = (typeof AccessType)[keyof typeof AccessType];

export const accessTypes = [
  { value: "FREE", title: "FREE" },
  { value: "PAID", title: "PAID" },
  { value: "LOCKED", title: "LOCKED" },
  { value: "NEW", title: "NEW" },
] as const;
// ----------------------------------------------------------------

export const TranscodeStatus = {
  PENDING: "PENDING",
  PROCESSING: "PROCESSING",
  COMPLETED: "COMPLETED",
  FAILED: "FAILED"
} as const;

export type TranscodeStatus = (typeof TranscodeStatus)[keyof typeof TranscodeStatus];


export const transcodes = [
  { value: "PENDING", title: "PENDING" },
  { value: "PROCESSING", title: "PROCESSING" },
  { value: "COMPLETED", title: "COMPLETED" },
  { value: "FAILED", title: "FAILED" },
] as const;
// ----------------------------------------------------------------

export const ContentLanguage = {
  ARABIC: "Arabic",
  ENGLISH: "English",
  SPANISH: "Spanish",
  RUSSIAN: "Russian",
  KOREAN: "Korean",
} as const;

export type ContentLanguage =
  (typeof ContentLanguage)[keyof typeof ContentLanguage];

  export const languages = [
  { value: "Arabic", title: "ARABIC" },
  { value: "English", title: "ENGLISH" },
  { value: "Spanish", title: "SPANISH" },
  { value: "Russian", title: "RUSSIAN" },
  { value: "Korean", title: "KOREAN" },
] as const;
// -------------------------------------------------------------------------------

export interface Actors {
  id: number;
  nameAr: string;
  nameEn: string;
  generatedImagePath: string;
  fullImagePath: string;
}
// ---------------------

export interface Tags {
  id: number;
  titleAr: string;
  titleEn: string;
  active: boolean;
}
// -------------------

export interface Trailer {
  transcodeMetaData: TranscodeMetaData;
  originalUploadMetadata: OriginalUploadMetadata;
}

export interface TeamMeta extends Actors{}

export interface ChampionMeta extends Actors{}

export interface ChannelMeta {
  id: number;
  titleAr: string;
  titleEn: string;
  logo: OriginalUploadMetadata;
}

export interface Account {
  id: number;
  username: string;
  gsm: string;
  user_id: string;
}

export interface IHelpCenterMeta {
  question: string;
  answer: string;
  order: number;
}
