import { ROUTES_ENUM } from "../../../utils/constants";
import TeamList from "../views/TeamList.vue";


export const teamRoute = [
  {
    path: ROUTES_ENUM.TEAM.BASE + ROUTES_ENUM.TEAM.FILTER,
    name: "Teams",
    component: TeamList,
  },
];
