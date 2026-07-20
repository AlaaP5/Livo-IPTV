import type { Account } from "../../../types/metadata";

export interface IComplaintResponse {
  id: number;
  alternativePhone: string;
  email: string;
  title: string;
  description: Text;
  account: Account;
  isRead: boolean;
}
