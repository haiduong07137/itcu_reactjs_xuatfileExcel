import axios from "axios";
import ConstantList from "../../appConfig";
const API_PATH = ConstantList.API_ENPOINT + "/api/signature";
const API_TRANSCRIPTS = ConstantList.API_ENPOINT + "/api/transcript";
const API_DOWNLOAD = ConstantList.API_ENPOINT + "/api/fileDownload";

export const searchByPage = (searchObject) => {
  var url = API_PATH + "/searchByPage";
  return axios.post(url, searchObject);
};

export const getItemById = id => { 
  var url = API_PATH + "/" + id;
  return axios.get(url);
};

 
export const updateData = signature => {
  var url = API_PATH + "/" +  signature.id;
  return axios.put(url, signature);
};

export const addNewData = signature => {
  var url = API_PATH + "/"  ;
  return axios.post(url, signature);
};
  

export const deleteSelected = (idList) => {
  let url = API_PATH + "/deleteMultiple";
  return axios({
    url,
    method: "DELETE",
    data: idList,
  });
};

export const deleteAll = () => {
  let url = API_PATH + "/deleteAll";
  return axios({
    url,
    method: "DELETE",
  });
};
