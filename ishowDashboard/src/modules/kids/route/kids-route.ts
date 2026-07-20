import { ROUTES_ENUM } from "../../../utils/constants";
import KidsBannerList from "../views/KidsBannerList.vue";
import ViewKidsSectionChannels from "../views/ViewKidsSectionChannels.vue";
import ViewKidsSectionClips from "../views/ViewKidsSectionClips.vue";
import ViewKidsSectionMovies from "../views/ViewKidsSectionMovies.vue";
import ViewKidsSections from "../views/ViewKidsSections.vue";
import ViewKidsSectionSeries from "../views/ViewKidsSectionSeries.vue";
import ViewKidsSectionTvs from "../views/ViewKidsSectionTvs.vue";

export const kidsRoute = [
  {
    path: ROUTES_ENUM.KIDS.BASE + ROUTES_ENUM.KIDS.VIEW_SECTIONS,
    name: "View Kids Sections",
    component: ViewKidsSections,
  },
  {
    path:
      ROUTES_ENUM.KIDS.BASE +
      ROUTES_ENUM.SECTION.BASE +
      ROUTES_ENUM.SECTION.SECTION_NAMA_PARA +
      ROUTES_ENUM.SECTION.SECTION_ID_PARA,
    name: "View Series For Kids Section",
    component: ViewKidsSectionSeries,
    props: true,
  },
  {
    path:
      ROUTES_ENUM.KIDS.BASE +
      ROUTES_ENUM.SECTION.BASE +
      ROUTES_ENUM.SECTION.SECTION_NAMA_PARA +
      ROUTES_ENUM.SECTION.SECTION_ID_PARA,
    name: "View Movies For Kids Section",
    component: ViewKidsSectionMovies,
    props: true,
  },
  {
    path:
      ROUTES_ENUM.KIDS.BASE +
      ROUTES_ENUM.SECTION.BASE +
      ROUTES_ENUM.SECTION.SECTION_NAMA_PARA +
      ROUTES_ENUM.SECTION.SECTION_ID_PARA,
    name: "View Clips For Kids Section",
    component: ViewKidsSectionClips,
    props: true,
  },
  {
    path:
      ROUTES_ENUM.KIDS.BASE +
      ROUTES_ENUM.SECTION.BASE +
      ROUTES_ENUM.SECTION.SECTION_NAMA_PARA +
      ROUTES_ENUM.SECTION.SECTION_ID_PARA,
    name: "View Channels For Kids Section",
    component: ViewKidsSectionChannels,
    props: true,
  },
  {
    path:
      ROUTES_ENUM.KIDS.BASE +
      ROUTES_ENUM.SECTION.BASE +
      ROUTES_ENUM.SECTION.SECTION_NAMA_PARA +
      ROUTES_ENUM.SECTION.SECTION_ID_PARA,
    name: "View Tv Programs For Kids Section",
    component: ViewKidsSectionTvs,
    props: true,
  },
  {
    path:
      ROUTES_ENUM.KIDS.BASE +
      ROUTES_ENUM.KIDS.BANNER +
      ROUTES_ENUM.KIDS.BANNER_LIST,
    name: "View Kids Banners",
    component: KidsBannerList,
  }
];
