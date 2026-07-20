import { ROUTES_ENUM } from "../../../utils/constants";
import LogIn from "../views/Login.vue";

export const authRoute = [
  {
    path: ROUTES_ENUM.AUTH.BASE + ROUTES_ENUM.AUTH.LOGIN,
    name: "Login",
    component: LogIn,
  },
];
