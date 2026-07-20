import { ROUTES_ENUM } from "../../../utils/constants";
import UpcomingMatchList from "../views/UpcomingMatchList.vue";


export const upcomingMatchRoute = [
  {
    path: ROUTES_ENUM.UPCOMING_MATCH.BASE + ROUTES_ENUM.UPCOMING_MATCH.FILTER,
    name: "Upcoming Match",
    component: UpcomingMatchList,
  },
];
