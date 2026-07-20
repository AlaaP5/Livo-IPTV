import { ROUTES_ENUM } from "../../../utils/constants";
import TagList from "../views/TagList.vue";


export const tagRoute = [
  {
    path: ROUTES_ENUM.TAG.BASE + ROUTES_ENUM.TAG.FILTER,
    name: "Tags",
    component: TagList,
  },
];
