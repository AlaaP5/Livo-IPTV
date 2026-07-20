import { ROUTES_ENUM } from "../../../utils/constants";
import ChannelEpgList from "../views/ChannelEpgList.vue";

export const channelEpgRoute = [
  {
    path: ROUTES_ENUM.CHANNEL_EPG.BASE + ROUTES_ENUM.CHANNEL_EPG.FILTER,
    name: "Channel Epgs",
    component: ChannelEpgList,
  }
];
