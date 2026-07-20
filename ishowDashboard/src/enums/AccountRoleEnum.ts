export const AccountRole = {
  USER: "USER",
  CONTENT: "CONTENT",
  ADMIN: "ADMIN",
  SUPER_ADMIN: "SUPER_ADMIN",
  MTN_MARKETING: "MTN_MARKETING",
  MTN_CS: "MTN_CS"
} as const;

export type ContentType = typeof AccountRole[keyof typeof AccountRole];


export const accountRole = [
  { value: "CONTENT", title: "CONTENT" },
  { value: "ADMIN", title: "ADMIN" },
  { value: "SUPER_ADMIN", title: "SUPER_ADMIN" },
  { value: "MTN_MARKETING", title: "MTN_MARKETING" },
  { value: "MTN_CS", title: "MTN_CS" }
] as const;