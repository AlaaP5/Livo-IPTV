import type {
  Actors,
  Badge,
  ContentLanguage,
  Tags,
} from "../../../types/metadata";
import type { OriginalUploadMetadata } from "../../../types/metadata.video";

export interface ISeriesResponse {
  seriesId: string;
  id: string;
  titleEn: string;
  titleAr: string;
  descriptionEn: string;
  descriptionAr: string;
  poster: OriginalUploadMetadata;
  badge: Badge;
  tags: Tags[];
  actors: Actors[];
  language: ContentLanguage;
  hasRight: boolean;
  isPublish: boolean;
  isKids: boolean;
  isSports: boolean;
  active: boolean;
  releaseYear: string;
  rate: string;
  rating: string;
  subtitleLanguages: ContentLanguage[];
  audioLanguages: ContentLanguage[];
}

export interface ISectionSeriesResponse {
  seriesId: string;
  titleEn: string;
  titleAr: string;
  descriptionEn: string;
  descriptionAr: string;
  poster: OriginalUploadMetadata;
  releaseYear: number;
  language: ContentLanguage;
  badge: Badge;
  tags: Tags[];
  actors: Actors[];
  createDate: string;
  rating: number;
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
