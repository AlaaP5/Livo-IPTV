export const MobilePage = {
  HOME: "HOME",
  SPORT: "SPORT",
  KIDS: "KIDS",
  CHANNELS: "CHANNELS",
  MOVIES: "MOVIES",
  SERIES: "SERIES",
  TV_PROGRAMS: "TV_PROGRAMS",
  CLIPS: "CLIPS"
} as const;

export type MobilePage = typeof MobilePage[keyof typeof MobilePage];


export const mobilePages = [
  { value: "HOME", title: "Home" },
  { value: "SPORT", title: "Sport" },
  { value: "KIDS", title: "Kids" },
  { value: "CHANNELS", title: "Channels" },
  { value: "MOVIES", title: "Movies" },
  { value: "SERIES", title: "Series" },
  { value: "TV_PROGRAMS", title: "TV programs" },
  { value: "CLIPS", title: "Clips" }
] as const;