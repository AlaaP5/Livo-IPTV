import type { ContentType } from "../../../enums/ContentTypeEnum";
import type { MobilePage } from "../../../enums/MobilePageEnum";
import type { PaginationFilter } from "../../../types/filter.types";

export interface IFilterSectionRequest extends PaginationFilter {
    active: string;
    publish: string;
    title: string;
    sectionPage: MobilePage | null;
    contentType: ContentType | null;
}