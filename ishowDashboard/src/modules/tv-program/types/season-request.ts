export interface ITvSeasonRequest {
  seasonId?: string;
  tvProgramId?: string;
  seasonNumber?: string;
  titleEn?: string;
  titleAr?: string;
  descriptionEn?: string;
  descriptionAr?: string;
  releaseYear?: string;
  active?: string;
  trailer?: File | null;
  poster?: File | null;
}
