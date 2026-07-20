export interface ISeriesSeasonRequest {
  seasonId?: string;
  seriesId?: string;
  seasonNumber?: string;
  titleEn?: string;
  titleAr?: string;
  descriptionEn?: string;
  descriptionAr?: string;
  actors?: number[];
  releaseYear?: string;
  active?: string;
  trailer?: File | null;
  rating?: string;
  poster?: File | null;
}
