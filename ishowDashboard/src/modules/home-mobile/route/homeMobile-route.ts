import { ROUTES_ENUM } from "../../../utils/constants";
import HomeBannerList from "../views/HomeBannerList.vue";
import ViewHomeSectionChannels from "../views/ViewHomeSectionChannels.vue";
import ViewHomeSectionClips from "../views/ViewHomeSectionClips.vue";
import ViewHomeSectionMovies from "../views/ViewHomeSectionMovies.vue";
import ViewHomeSections from "../views/ViewHomeSections.vue";
import ViewHomeSectionSeries from "../views/ViewHomeSectionSeries.vue";
import ViewHomeSectionTvs from "../views/ViewHomeSectionTvs.vue";

export const homeMobileRoute = [
  {
    path: ROUTES_ENUM.MOBILE_HOME.BASE + ROUTES_ENUM.MOBILE_HOME.VIEW_SECTIONS,
    name: "View Home Sections",
    component: ViewHomeSections,
  },
  {
    path:
      ROUTES_ENUM.MOBILE_HOME.BASE +
      ROUTES_ENUM.SECTION.BASE +
      ROUTES_ENUM.SECTION.SECTION_NAMA_PARA +
      ROUTES_ENUM.SECTION.SECTION_ID_PARA,
    name: "View Series For Home Section",
    component: ViewHomeSectionSeries,
    props: true,
  },
  {
    path:
      ROUTES_ENUM.MOBILE_HOME.BASE +
      ROUTES_ENUM.SECTION.BASE +
      ROUTES_ENUM.SECTION.SECTION_NAMA_PARA +
      ROUTES_ENUM.SECTION.SECTION_ID_PARA,
    name: "View Movies For Home Section",
    component: ViewHomeSectionMovies,
    props: true,
  },
  {
    path:
      ROUTES_ENUM.MOBILE_HOME.BASE +
      ROUTES_ENUM.SECTION.BASE +
      ROUTES_ENUM.SECTION.SECTION_NAMA_PARA +
      ROUTES_ENUM.SECTION.SECTION_ID_PARA,
    name: "View Clips For Home Section",
    component: ViewHomeSectionClips,
    props: true,
  },
  {
    path:
      ROUTES_ENUM.MOBILE_HOME.BASE +
      ROUTES_ENUM.SECTION.BASE +
      ROUTES_ENUM.SECTION.SECTION_NAMA_PARA +
      ROUTES_ENUM.SECTION.SECTION_ID_PARA,
    name: "View Channels For Home Section",
    component: ViewHomeSectionChannels,
    props: true,
  },
  {
    path:
      ROUTES_ENUM.MOBILE_HOME.BASE +
      ROUTES_ENUM.SECTION.BASE +
      ROUTES_ENUM.SECTION.SECTION_NAMA_PARA +
      ROUTES_ENUM.SECTION.SECTION_ID_PARA,
    name: "View Tv Programs For Home Section",
    component: ViewHomeSectionTvs,
    props: true,
  },
  {
    path:
      ROUTES_ENUM.MOBILE_HOME.BASE +
      ROUTES_ENUM.MOBILE_HOME.BANNER +
      ROUTES_ENUM.MOBILE_HOME.BANNER_LIST,
    name: "View Home Banners",
    component: HomeBannerList,
  }
];
