import type { ContentType } from "../../../enums/ContentTypeEnum";
import type { MobilePage } from "../../../enums/MobilePageEnum";

export interface ISectionRequest {
  id: number;
  titleAr: string;
  titleEn: string;
  order: number | null;
  active: string;
  page: MobilePage | null;
  contentType: ContentType | null;
} 

export interface ISectionPublishRequest {
  publish: string;
}
