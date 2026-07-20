export interface ILookupResponse {
  id: number;
  name: string;
}

export interface IMongoLookupResponse {
  id: string;
  name: string;
}

export interface ILookupRequest {
  name: string;
}

export interface ILookupCityRequest extends ILookupRequest {
  provinceId: number | null;
}
