import axios from "axios";
import ConstantList from "../../appConfig";
const API_PATH = ConstantList.API_ENPOINT + "/api/upload-activity-log";

export const searchByPage = (searchObject) => {
  return axios.post(API_PATH + "/searchByPage", searchObject);
};
