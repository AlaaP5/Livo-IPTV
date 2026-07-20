import type { ContentType } from "../../../enums/ContentTypeEnum";
import type { MobilePage } from "../../../enums/MobilePageEnum";

export interface ISectionResponse {
  id: number;
  titleAr: string;
  titleEn: string;
  order: number;
  active: boolean;
  publish: boolean;
  page: MobilePage;
  contentType: ContentType;
}
