export interface ISeasonEpisodeRequest {
  episodeId?: string;
  seriesId?: string;
  seasonId?: string;
  episodeNumber?: string;
  titleEn?: string;
  titleAr?: string;
  active?: string;
  rate?: string;
  duration?: number;
  accessType?: string;
  badge?: string;
  poster?: File | null;
  file?: File | null;
  subtitles?: File[] | null;
}
