export interface IUpcomingMatchRequest {
  id: number | null;
  homeTeamId: number | null;
  awayTeamId: number | null;
  championId: number | null;
  channelId: string | null;
  matchDate: string | null;
  active: string | null;
}
