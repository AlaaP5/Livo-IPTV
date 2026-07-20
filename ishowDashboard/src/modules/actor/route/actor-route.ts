import { ROUTES_ENUM } from "../../../utils/constants";
import ActorList from "../views/ActorList.vue";


export const actorRoute = [
  {
    path: ROUTES_ENUM.ACTOR.BASE + ROUTES_ENUM.ACTOR.FILTER,
    name: "Actors",
    component: ActorList,
  },
];
