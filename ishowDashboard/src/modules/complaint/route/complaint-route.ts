import { ROUTES_ENUM } from "../../../utils/constants";
import ComplaintList from "../views/ComplaintList.vue";


export const complaintRoute = [
  {
    path: ROUTES_ENUM.COMPLAINT.BASE + ROUTES_ENUM.COMPLAINT.FILTER,
    name: "Complaints",
    component: ComplaintList,
  },
];
