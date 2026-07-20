import type { ChampionMeta, ChannelMeta, TeamMeta } from "../../../types/metadata";

export interface IUpcomingMatchResponse {
  id: number;
  homeTeam: TeamMeta;
  awayTeam: TeamMeta;
  champion: ChampionMeta;
  channel: ChannelMeta;
  matchDate: string;
  active: boolean;
}
