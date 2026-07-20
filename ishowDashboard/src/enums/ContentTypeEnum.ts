export const ContentType = {
  CHANNELS: "CHANNELS",
  MOVIES: "MOVIES",
  SERIES: "SERIES",
  TV_PROGRAMS: "TV_PROGRAMS",
  CLIPS: "CLIPS"
} as const;

export type ContentType = typeof ContentType[keyof typeof ContentType];



export const contentTypes = [
  { value: "CHANNELS", title: "Channels" },
  { value: "MOVIES", title: "Movies" },
  { value: "SERIES", title: "Series" },
  { value: "TV_PROGRAMS", title: "TV Programs" },
  { value: "CLIPS", title: "Clips" }
] as const;