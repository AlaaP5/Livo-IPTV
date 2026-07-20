import type { Actors, Trailer } from "../../../types/metadata";
import type { OriginalUploadMetadata } from "../../../types/metadata.video";

export interface ISeriesSeasonResponse {
  id: string;
  seriesId: string;
  seasonNumber: string;
  titleEn: string;
  titleAr: string;
  descriptionEn: string;
  descriptionAr: string;
  poster: OriginalUploadMetadata;
  trailer: Trailer;
  actors: Actors[];
  active: boolean;
  releaseYear: string;
  episodeCount: string;
  rating: string;
}