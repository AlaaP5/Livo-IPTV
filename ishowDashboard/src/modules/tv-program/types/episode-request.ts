export interface ISeasonEpisodeRequest {
  episodeId?: string;
  tvProgramId?: string;
  seasonId?: string;
  episodeNumber?: string;
  titleEn?: string;
  titleAr?: string;
  active?: string;
  accessType?: string;
  badge?: string;
  poster?: File | null;
  file?: File | null;
  subtitles?: File[] | null;
}
