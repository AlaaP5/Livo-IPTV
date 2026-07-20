import { ROUTES_ENUM } from "../../../utils/constants";
import ClipList from "../views/ClipList.vue";
import ViewClipBanner from "../views/ViewClipBanner.vue";
import ViewClipSections from "../views/ViewClipSections.vue";
import ViewSectionClips from "../views/ViewSectionClips.vue";

export const clipRoute = [
  {
    path: ROUTES_ENUM.CLIP.BASE + ROUTES_ENUM.CLIP.FILTER,
    name: "Clips",
    component: ClipList,
  },
  {
    path:
      ROUTES_ENUM.CLIP.BASE +
      ROUTES_ENUM.SECTION.BASE +
      ROUTES_ENUM.SECTION.SECTION_NAMA_PARA +
      ROUTES_ENUM.SECTION.SECTION_ID_PARA,
    name: "View Clips For Section",
    component: ViewSectionClips,
    props: true,
  },
  {
    path: ROUTES_ENUM.CLIP.BASE + ROUTES_ENUM.CLIP.VIEW_CLIP_SECTIONS,
    name: "View Clips Sections",
    component: ViewClipSections,
  },
  {
    path: ROUTES_ENUM.CLIP.BASE + ROUTES_ENUM.CLIP.VIEW_CLIP_BANNER,
    name: "View Clip Banner",
    component: ViewClipBanner,
  },
];
