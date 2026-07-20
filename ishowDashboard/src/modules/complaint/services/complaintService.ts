import { useApi } from "../../../composables/useApi";
import { HttpMethod } from "../../../enums/httpMethodEnum";
import { ROUTES_ENUM, SUCCES_MESSAGE } from "../../../utils/constants";
import { useToast } from "vue-toastification";
import type { IFilterComplaintRequest } from "../types/filter-request";
import { useComplaintStore } from "../stores/complaint";
import type { IComplaintResponse } from "../types/complaint-response";
import type { IShowComplaintRequest } from "../types/complaint-request";


class ComplaintService {
  async filterComplaints(payload: IFilterComplaintRequest) {
    const complaintStore = useComplaintStore();
    complaintStore.setLoading(true);

    try {
      console.log(payload);
      const response = await useApi<
        IFilterComplaintRequest,
        IComplaintResponse[]
      >(
        ROUTES_ENUM.ISHOW +
          ROUTES_ENUM.ADMIN +
          ROUTES_ENUM.COMPLAINT.BASE +
          ROUTES_ENUM.COMPLAINT.FILTER,
        HttpMethod.GET,
        payload,
      );
      console.log(response);

      return response;
    } finally {
      complaintStore.setLoading(false);
    }
  }


  async showComplaint(id: number, payload: IShowComplaintRequest) {
    const toast = useToast();
    const complaintStore = useComplaintStore();
    complaintStore.setLoading(true);

    try {
      const response = await useApi<IShowComplaintRequest, IComplaintResponse>(
        `${ROUTES_ENUM.ISHOW}${ROUTES_ENUM.ADMIN}${ROUTES_ENUM.COMPLAINT.BASE}/${id}${ROUTES_ENUM.COMPLAINT.READ}`,
        HttpMethod.GET,
        payload,
      );
      console.log(payload);
      console.log(response);

      if (response.status === SUCCES_MESSAGE) {
        toast.success(response.message || "Status updated successfully", {
          timeout: 5000,
        });
      } else {
        toast.error(response.message || "Status update failed", {
          timeout: 5000,
        });
      }

      return response;
    } finally {
      complaintStore.setLoading(false);
    }
  }
}

export default new ComplaintService();
