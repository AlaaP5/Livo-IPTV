import { useApi } from "../../../composables/useApi";
import { HttpMethod } from "../../../enums/httpMethodEnum";
import { ROUTES_ENUM, SUCCES_MESSAGE } from "../../../utils/constants";
import { useHelpCenterStore } from "../stores/helpCenter";
import type { IHelpCenterRequest } from "../types/helpCenter-request";
import type { IHelpCenterResponse } from "../types/helpCenter-response";

class HelpCenterService {
  async filterHelpCenters() {
    const helpCenterStore = useHelpCenterStore();
    helpCenterStore.setLoading(true);

    try {
      const response = await useApi<null, IHelpCenterResponse>(
        ROUTES_ENUM.ISHOW + ROUTES_ENUM.ADMIN + ROUTES_ENUM.HELP_CENTER.BASE,
        HttpMethod.GET,
        null,
      );
      console.log(response);

      return response;
    } finally {
      helpCenterStore.setLoading(false);
    }
  }

  async updateHelpCenters(helpCenterMetas: IHelpCenterRequest) {
    const helpCenterStore = useHelpCenterStore();
    helpCenterStore.setLoading(true);

    try {
      const response = await useApi<IHelpCenterRequest, IHelpCenterResponse>(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.HELP_CENTER.BASE +
          ROUTES_ENUM.HELP_CENTER.UPDATE,
        HttpMethod.POST,
        helpCenterMetas,
      );

      return response;
    } finally {
      helpCenterStore.setLoading(false);
    }
  }
}

export default new HelpCenterService();
