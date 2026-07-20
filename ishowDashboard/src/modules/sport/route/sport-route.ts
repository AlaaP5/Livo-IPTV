import { ROUTES_ENUM } from "../../../utils/constants";
import ViewSportSectionChannels from "../views/ViewSportSectionChannels.vue";
import ViewSportSectionClips from "../views/ViewSportSectionClips.vue";
import ViewSportSectionMovies from "../views/ViewSportSectionMovies.vue";
import ViewSportSections from "../views/ViewSportSections.vue";
import ViewSportSectionSeries from "../views/ViewSportSectionSeries.vue";
import ViewSportSectionTvs from "../views/ViewSportSectionTvs.vue";


export const sportRoute = [
  {
    path: ROUTES_ENUM.SPORT.BASE + ROUTES_ENUM.SPORT.VIEW_SECTIONS,
    name: "View Sport Sections",
    component: ViewSportSections,
  },
  {
    path:
      ROUTES_ENUM.SPORT.BASE +
      ROUTES_ENUM.SECTION.BASE +
      ROUTES_ENUM.SECTION.SECTION_NAMA_PARA +
      ROUTES_ENUM.SECTION.SECTION_ID_PARA,
    name: "View Series For Sport Section",
    component: ViewSportSectionSeries,
    props: true,
  },
  {
    path:
      ROUTES_ENUM.SPORT.BASE +
      ROUTES_ENUM.SECTION.BASE +
      ROUTES_ENUM.SECTION.SECTION_NAMA_PARA +
      ROUTES_ENUM.SECTION.SECTION_ID_PARA,
    name: "View Movies For Sport Section",
    component: ViewSportSectionMovies,
    props: true,
  },
  {
    path:
      ROUTES_ENUM.SPORT.BASE +
      ROUTES_ENUM.SECTION.BASE +
      ROUTES_ENUM.SECTION.SECTION_NAMA_PARA +
      ROUTES_ENUM.SECTION.SECTION_ID_PARA,
    name: "View Clips For Sport Section",
    component: ViewSportSectionClips,
    props: true,
  },
  {
    path:
      ROUTES_ENUM.SPORT.BASE +
      ROUTES_ENUM.SECTION.BASE +
      ROUTES_ENUM.SECTION.SECTION_NAMA_PARA +
      ROUTES_ENUM.SECTION.SECTION_ID_PARA,
    name: "View Channels For Sport Section",
    component: ViewSportSectionChannels,
    props: true,
  },
  {
    path:
      ROUTES_ENUM.SPORT.BASE +
      ROUTES_ENUM.SECTION.BASE +
      ROUTES_ENUM.SECTION.SECTION_NAMA_PARA +
      ROUTES_ENUM.SECTION.SECTION_ID_PARA,
    name: "View Tv Programs For Sport Section",
    component: ViewSportSectionTvs,
    props: true,
  },
];
