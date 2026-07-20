import { ROUTES_ENUM } from "../../../utils/constants";
import ChannelList from "../views/ChannelList.vue";
import ViewChannelBanner from "../views/ViewChannelBanner.vue";
import ViewChannelSections from "../views/ViewChannelSections.vue";
import ViewSectionChannels from "../views/ViewSectionChannels.vue";

export const channelRoute = [
  {
    path: ROUTES_ENUM.CHANNEL.BASE + ROUTES_ENUM.CHANNEL.FILTER,
    name: "Channels",
    component: ChannelList,
  },
  {
    path:
      ROUTES_ENUM.CHANNEL.BASE +
      ROUTES_ENUM.SECTION.BASE +
      ROUTES_ENUM.SECTION.SECTION_NAMA_PARA +
      ROUTES_ENUM.SECTION.SECTION_ID_PARA,
    name: "View Channels For Section",
    component: ViewSectionChannels,
    props: true,
  },
  {
    path: ROUTES_ENUM.CHANNEL.BASE + ROUTES_ENUM.CHANNEL.VIEW_CHANNEL_SECTIONS,
    name: "View Channel Sections",
    component: ViewChannelSections,
  },
  {
    path: ROUTES_ENUM.CHANNEL.BASE + ROUTES_ENUM.CHANNEL.VIEW_CHANNEL_BANNER,
    name: "View Channel Banner",
    component: ViewChannelBanner,
  }
];
