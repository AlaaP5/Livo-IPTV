import { ROUTES_ENUM } from "../../../utils/constants";
import SectionList from "../views/SectionList.vue";


export const sectionRoute = [
  {
    path: ROUTES_ENUM.SECTION.BASE + ROUTES_ENUM.SECTION.FILTER,
    name: "Sections",
    component: SectionList,
  },
];
