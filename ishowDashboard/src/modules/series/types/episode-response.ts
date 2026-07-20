import type { AccessType, Badge, TranscodeStatus } from "../../../types/metadata";
import type { OriginalUploadMetadata, TranscodeMetaData } from "../../../types/metadata.video";

export interface ISeasonEpisodesResponse {
  id: string;
  seriesId: string;
  seasonId: string;
  episodeNumber: number;
  titleEn: string;
  titleAr: string;
  poster: OriginalUploadMetadata;
  active: boolean;
  isPublish: boolean;
  transcodeStatus: TranscodeStatus;
  accessType: AccessType;
  badge: Badge;
  transcodeMetaData: TranscodeMetaData;
  originalUploadMetadata: OriginalUploadMetadata;
  subtitles: OriginalUploadMetadata[];
  rate: number;
  duration: number;
}