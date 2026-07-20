import type {
  Badge,
  ContentLanguage,
  Tags,
} from "../../../types/metadata";
import type { OriginalUploadMetadata } from "../../../types/metadata.video";

export interface ITvResponse {
  tvProgramId: string;
  id: string;
  titleEn: string;
  titleAr: string;
  descriptionEn: string;
  descriptionAr: string;
  poster: OriginalUploadMetadata;
  badge: Badge;
  tags: Tags[];
  language: ContentLanguage;
  hasRight: boolean;
  isPublish: boolean;
  isKids: boolean;
  isSports: boolean;
  active: boolean;
  releaseYear: string;
  subtitleLanguages: ContentLanguage[];
  audioLanguages: ContentLanguage[];
}

export interface ISectionTvResponse {
  tvProgramId: string;
  titleEn: string;
  titleAr: string;
  descriptionEn: string;
  descriptionAr: string;
  poster: OriginalUploadMetadata;
  releaseYear: number;
  language: ContentLanguage;
  badge: Badge;
  tags: Tags[];
  createDate: string;
  seasonCount: number;
  active: boolean;
  hasRight: boolean;
  isTop: boolean;
  isKids: boolean;
  isSports: boolean;
  isPublish: boolean;
  sectionId: number;
  sectionTitleAr: string;
  sectionTitleEn: string;
  sectionOrder: number;
  sectionActive: boolean;
  sectionPublish: boolean;
}
