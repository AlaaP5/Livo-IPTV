import { ROUTES_ENUM } from "../../../utils/constants";
import AdList from "../../ad/views/AdList.vue";


export const adRoute = [
  {
    path: ROUTES_ENUM.AD.BASE + ROUTES_ENUM.AD.FILTER,
    name: "Ads",
    component: AdList,
  },
];
