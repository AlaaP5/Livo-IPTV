import { ROUTES_ENUM } from "../../../utils/constants";
import ChampionList from "../views/ChampionList.vue";


export const championRoute = [
  {
    path: ROUTES_ENUM.CHAMPION.BASE + ROUTES_ENUM.CHAMPION.FILTER,
    name: "Champions",
    component: ChampionList,
  },
];
