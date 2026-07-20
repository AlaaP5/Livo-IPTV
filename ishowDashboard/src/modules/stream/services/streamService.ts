import { ROUTES_ENUM } from "../../../utils/constants";
import axios from "axios";


let baseStream = "https://ishow-test.doremi.click";


class StreamService {
  private streamClient = axios.create({
    baseURL: baseStream,
    headers: {
      "API-KEY": "KouAuYO6qoQVbLLsbTvEkQCNaXGvFr89FqEizw3OM9c=",
    },
  });

  async fetchImage(dbPath: string): Promise<string> {
    const response = await this.streamClient.get(ROUTES_ENUM.STREAM.VIEW, {
      params: { dbPath },
      responseType: "blob",
    });
    return URL.createObjectURL(response.data);
  }
}

export default new StreamService();
