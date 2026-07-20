import { ROUTES_ENUM } from "../../../utils/constants";
import HelpCenterList from "../views/helpCenterList.vue";


export const helpCenterRoute = [
  {
    path: ROUTES_ENUM.HELP_CENTER.BASE,
    name: "Help Center",
    component: HelpCenterList,
  },
];
