export interface ILoginResponse {
  access_token: string;
  expires_in: number;
  refresh_expires_in: number;
  refresh_token: string;
  role: string;
  token_type: string;
}
