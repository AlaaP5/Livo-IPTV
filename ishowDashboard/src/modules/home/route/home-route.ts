import { ROUTES_ENUM } from "../../../utils/constants";
import Home from "../views/Home.vue";

export const homeRoute = [
  {
    path: ROUTES_ENUM.HOME,
    name: "Home",
    component: Home,
  },
];
