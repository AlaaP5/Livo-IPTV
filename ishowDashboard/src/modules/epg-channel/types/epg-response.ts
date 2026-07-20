import type { IMongoLookupResponse } from "../../../types/lookup.types";

export interface IChannelEpgResponse {
  id: string;
  channel: IMongoLookupResponse;
  startDate: string;
  endDate: string;
  titleEn: string;
  titleAr: string;
  active: boolean;
}