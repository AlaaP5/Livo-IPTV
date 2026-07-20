import type { RouteRecordRaw } from "vue-router";
import { authRoute } from "../modules/auth/route/auth-route";
import { homeRoute } from "../modules/home/route/home-route";
import { accountRoute } from "../modules/account/route/account-route";
import { actorRoute } from "../modules/actor/route/actor-route";
import { adRoute } from "../modules/ad/route/ad-route";
import { tagRoute } from "../modules/tag/route/tag-route";
import { sectionRoute } from "../modules/section/route/section-route";
import { channelRoute } from "../modules/channel/route/channel-route";
import { movieRoute } from "../modules/movie/route/movie-route";
import { seriesRoute } from "../modules/series/route/series-route";
import { clipRoute } from "../modules/clip/route/clip-route";
import { tvRoute } from "../modules/tv-program/route/tvProgram-route";
import { channelEpgRoute } from "../modules/epg-channel/route/epg-route";
import { championRoute } from "../modules/champion/route/champion-route";
import { teamRoute } from "../modules/team/route/team-route";
import { upcomingMatchRoute } from "../modules/upcoming-match/route/upcomingMatch-route";
import { homeMobileRoute } from "../modules/home-mobile/route/homeMobile-route";
import { kidsRoute } from "../modules/kids/route/kids-route";
import { sportRoute } from "../modules/sport/route/sport-route";
import { complaintRoute } from "../modules/complaint/route/complaint-route";
import { helpCenterRoute } from "../modules/help-center/route/helpCenter-route";


export const routes: RouteRecordRaw[] = [
    ...authRoute,
    ...homeRoute,
    ...accountRoute,
    ...actorRoute,
    ...adRoute,
    ...tagRoute,
    ...sectionRoute,
    ...channelRoute,
    ...movieRoute,
    ...seriesRoute,
    ...clipRoute,
    ...tvRoute,
    ...channelEpgRoute,
    ...championRoute,
    ...teamRoute,
    ...upcomingMatchRoute,
    ...homeMobileRoute,
    ...kidsRoute,
    ...sportRoute,
    ...complaintRoute,
    ...helpCenterRoute
];
