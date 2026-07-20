import { ROUTES_ENUM } from "../../../utils/constants";
import AccountList from "../views/AccountList.vue";


export const accountRoute = [
  {
    path: ROUTES_ENUM.ACCOUNT.BASE + ROUTES_ENUM.ACCOUNT.FILTER,
    name: "Accounts",
    component: AccountList,
  },
];
