import { ROUTES_ENUM } from "../../../utils/constants";
import SeriesList from "../views/SeriesList.vue";
import ViewSeriesSeasons from "../views/ViewSeriesSeasons.vue";
import ViewSectionSeries from "../views/ViewSectionSeries.vue";
import ViewSeasonEpisodes from "../views/ViewSeasonEpisodes.vue";
import ViewSeriesSections from "../views/ViewSeriesSections.vue";
import ViewSeriesBanner from "../views/ViewSeriesBanner.vue";

export const seriesRoute = [
  {
    path: ROUTES_ENUM.SERIES.BASE + ROUTES_ENUM.SERIES.FILTER,
    name: "Series",
    component: SeriesList,
  },
  {
    path:
      ROUTES_ENUM.SERIES.BASE +
      ROUTES_ENUM.SECTION.BASE +
      ROUTES_ENUM.SECTION.SECTION_NAMA_PARA +
      ROUTES_ENUM.SECTION.SECTION_ID_PARA,
    name: "View Series For Section",
    component: ViewSectionSeries,
    props: true,
  },
  {
    path: ROUTES_ENUM.SERIES.BASE + ROUTES_ENUM.SERIES.VIEW_SERIES_SECTIONS,
    name: "View Series Sections",
    component: ViewSeriesSections,
  },
  {
    path:
      ROUTES_ENUM.SERIES.BASE +
      ROUTES_ENUM.SERIES.VIEW_SEASON_SERIES +
      ROUTES_ENUM.SERIES.SERIES_PARA,
    name: "View Seasons For Series",
    component: ViewSeriesSeasons,
    props: true,
  },
  {
    path:
      ROUTES_ENUM.SERIES.BASE +
      ROUTES_ENUM.SERIES.SERIES_PARA +
      ROUTES_ENUM.SERIES.VIEW_SEASON_SERIES +
      ROUTES_ENUM.SERIES.SEASON_PARA +
      ROUTES_ENUM.SERIES.VIEW_EPISODES,

    name: "View Episodes For Season In Series",
    component: ViewSeasonEpisodes,
    props: true,
  },
  {
    path: ROUTES_ENUM.SERIES.BASE + ROUTES_ENUM.SERIES.VIEW_SERIES_BANNER,
    name: "View Series Banner",
    component: ViewSeriesBanner,
  },
];
