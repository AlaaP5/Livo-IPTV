import type { Badge, Tags } from "../../../types/metadata";
import type { OriginalUploadMetadata } from "../../../types/metadata.video";

export interface IChannelResponse {
  id: string;
  titleEn: string;
  titleAr: string;
  descriptionEn: string;
  descriptionAr: string;
  badge: string;
  tags: Tags[];
  logo: OriginalUploadMetadata;
  hasRight: boolean;
  isKids: boolean;
  active: boolean;
  isSports: boolean;
  isPublish: boolean;
}

export interface ISectionChannelsResponse {
  channelId: string;
  titleEn: string;
  titleAr: string;
  logo: OriginalUploadMetadata;
  badge: Badge;
  tags: Tags[];
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
