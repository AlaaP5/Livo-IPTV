
export interface IChannelEpgRequest {
  id?: string;
  channelId?: string;
  startDate?: string;
  endDate?: string;
  titleEn?: string;
  titleAr?: string;
  active?: string;
}

export interface IUploadEpgRequest {
    file: File;
    channelId: string;
}