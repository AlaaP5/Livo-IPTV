import type { Trailer } from "../../../types/metadata";
import type { OriginalUploadMetadata } from "../../../types/metadata.video";

export interface ITvSeasonResponse {
  id: string;
  tvProgramId: string;
  seasonNumber: string;
  titleEn: string;
  titleAr: string;
  descriptionEn: string;
  descriptionAr: string;
  poster: OriginalUploadMetadata;
  trailer: Trailer;
  active: boolean;
  releaseYear: string;
  episodeCount: string;
}