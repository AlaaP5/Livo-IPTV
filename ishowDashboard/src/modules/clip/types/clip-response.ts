import type {
  AccessType,
  Badge,
  Tags,
  TranscodeStatus,
} from "../../../types/metadata";
import type {
  OriginalUploadMetadata,
  TranscodeMetaData,
} from "../../../types/metadata.video";

export interface IFilterClipResponse {
  clipId: string;
  titleEn: string;
  titleAr: string;
  badge: Badge;
  tags: Tags[];
  accessType: AccessType;
  transcodeStatus: TranscodeStatus;
  hasRight: boolean;
  isPublish: boolean;
  isKids: boolean;
  isSports: boolean;
  active: boolean;
  duration: number;
  poster: OriginalUploadMetadata;
}

export interface IClipResponse {
  id: string;
  titleEn: string;
  titleAr: string;
  descriptionEn: string;
  descriptionAr: string;
  badge: Badge;
  tags: Tags[];
  accessType: AccessType;
  duration: number;
  transcodeMetaData: TranscodeMetaData;
  originalUploadMetadata: OriginalUploadMetadata;
  transcodeStatus: TranscodeStatus;
  hasRight: boolean;
  isPublish: boolean;
  isKids: boolean;
  isSports: boolean;
  active: boolean;
  poster: OriginalUploadMetadata;
}

export interface ISectionClipResponse {
  clipId: string;
  titleEn: string;
  titleAr: string;
  poster: OriginalUploadMetadata;
  createDate: string;
  isTop: boolean;
  isKids: boolean;
  isSports: boolean;
  isPublish: boolean;
  duration: number;
  badge: Badge;
  tags: Tags[];
  accessType: AccessType;
  sectionId: number;
  sectionTitleAr: string;
  sectionTitleEn: string;
  sectionOrder: number;
  sectionActive: boolean;
  sectionPublish: boolean;
}
