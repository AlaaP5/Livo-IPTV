import type {
  AccessType,
  Actors,
  Badge,
  ContentLanguage,
  Tags,
  Trailer,
  TranscodeStatus,
} from "../../../types/metadata";
import type {
  OriginalUploadMetadata,
  TranscodeMetaData,
} from "../../../types/metadata.video";

export interface IFilterMovieResponse {
  movieId: string;
  titleEn: string;
  titleAr: string;
  descriptionEn: string;
  descriptionAr: string;
  badge: Badge;
  tags: Tags[];
  actors: Actors[];
  accessType: AccessType;
  releaseYear: string;
  transcodeMetaData: TranscodeMetaData;
  originalUploadMetadata: OriginalUploadMetadata;
  transcodeStatus: TranscodeStatus;
  hasRight: boolean;
  isPublish: boolean;
  isKids: boolean;
  isSports: boolean;
  active: boolean;
  language: ContentLanguage;
  rating: string;
  trailer: Trailer;
  poster: OriginalUploadMetadata;
  subtitles: OriginalUploadMetadata[];
  subtitleLanguages: ContentLanguage[];
  audioLanguages: ContentLanguage[];
}

export interface IMovieResponse {
  id: string;
  titleEn: string;
  titleAr: string;
  descriptionEn: string;
  descriptionAr: string;
  badge: Badge;
  tags: Tags[];
  actors: Actors[];
  accessType: AccessType;
  releaseYear: string;
  transcodeMetaData: TranscodeMetaData;
  originalUploadMetadata: OriginalUploadMetadata;
  transcodeStatus: TranscodeStatus;
  hasRight: boolean;
  isPublish: boolean;
  isKids: boolean;
  isSports: boolean;
  active: boolean;
  language: ContentLanguage;
  rating: string;
  duration: number;
  trailer: Trailer;
  poster: OriginalUploadMetadata;
  subtitles: OriginalUploadMetadata[];
  subtitleLanguages: ContentLanguage[];
  audioLanguages: ContentLanguage[];
}

export interface ISectionMovieResponse {
  movieId: string;
  titleEn: string;
  titleAr: string;
  poster: OriginalUploadMetadata;
  createDate: string;
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
