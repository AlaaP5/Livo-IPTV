import { ROUTES_ENUM } from "../../../utils/constants";
import TvList from "../views/TvList.vue";
import ViewSeasonEpisodes from "../views/ViewSeasonEpisodes.vue";
import ViewSectionTvs from "../views/ViewSectionTvs.vue";
import ViewTvBanner from "../views/ViewTvBanner.vue";
import ViewTvSeasons from "../views/ViewTvSeasons.vue";
import ViewTvSections from "../views/ViewTvSections.vue";

export const tvRoute = [
  {
    path: ROUTES_ENUM.TV_PROGRAM.BASE + ROUTES_ENUM.TV_PROGRAM.FILTER,
    name: "Tvs",
    component: TvList,
  },
  {
    path: ROUTES_ENUM.TV_PROGRAM.BASE + ROUTES_ENUM.TV_PROGRAM.VIEW_TV_SECTIONS,
    name: "View Tv Programs Sections",
    component: ViewTvSections,
  },
  {
    path:
      ROUTES_ENUM.TV_PROGRAM.BASE +
      ROUTES_ENUM.SECTION.BASE +
      ROUTES_ENUM.SECTION.SECTION_NAMA_PARA +
      ROUTES_ENUM.SECTION.SECTION_ID_PARA,
    name: "View Tv Programs For Section",
    component: ViewSectionTvs,
    props: true,
  },
  {
    path: ROUTES_ENUM.TV_PROGRAM.BASE + ROUTES_ENUM.TV_PROGRAM.VIEW_TV_BANNER,
    name: "View Tv Program Banner",
    component: ViewTvBanner,
  },
  {
    path:
      ROUTES_ENUM.TV_PROGRAM.BASE +
      ROUTES_ENUM.TV_PROGRAM.VIEW_SEASON_TV +
      ROUTES_ENUM.TV_PROGRAM.TV_PARA,
    name: "View Seasons For Tv Programs",
    component: ViewTvSeasons,
    props: true,
  },
  {
    path:
      ROUTES_ENUM.TV_PROGRAM.BASE +
      ROUTES_ENUM.TV_PROGRAM.TV_PARA +
      ROUTES_ENUM.TV_PROGRAM.VIEW_SEASON_TV +
      ROUTES_ENUM.TV_PROGRAM.SEASON_PARA +
      ROUTES_ENUM.TV_PROGRAM.VIEW_EPISODES,

    name: "View Episodes For Season",
    component: ViewSeasonEpisodes,
    props: true,
  },
];
