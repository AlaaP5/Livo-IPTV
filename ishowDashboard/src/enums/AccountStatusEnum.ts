export const AccountStatus = {
  UNVERIFIED: "UNVERIFIED",
  ACTIVE: "ACTIVE",
  DELETED: "DELETED",
  BANNED: "BANNED"
} as const;

export type ContentType = typeof AccountStatus[keyof typeof AccountStatus];


export const accountStatus = [
  { value: "UNVERIFIED", title: "UNVERIFIED" },
  { value: "ACTIVE", title: "ACTIVE" },
  { value: "DELETED", title: "DELETED" },
  { value: "BANNED", title: "BANNED" }
] as const;