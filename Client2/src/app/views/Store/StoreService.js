import axios from "axios";
import ConstantList from "../../appConfig";
const API_PATH = ConstantList.API_ENPOINT + "/api/stores";

export const searchByPage = (searchObject) => {
  var url = API_PATH + "/searchByPage";
  return axios.post(url, searchObject);
};

export const getAllStores = () => {
  return axios.get(API_PATH+"/1/100000");  
};

export const getStoreByPage = (pageIndex, pageSize) => {
  var pageIndex = pageIndex;
  var params = pageIndex + "/" + pageSize;
  var url = API_PATH + "/"  + params;
  return axios.get(url);  
};
export const searchByText =  (keyword, pageIndex, pageSize) => {
  var pageIndex = pageIndex;
  var params = pageIndex + "/" + pageSize;
  return axios.post(API_PATH + "/searchByText/" + params, keyword);
};

export const getUserById = id => {
  return axios.get("/api/user", { data: id });
};
export const deleteItem = id => {
  return axios.delete(API_PATH +"/"+ id);
};

export const getItemById = id => {
  return axios.get(API_PATH +"/"+ id);
};

export const checkCode = (id, code) => {
  const config = { params: { id: id, code: code } };
  var url = API_PATH + "/checkCode";
  return axios.get(url, config);
};

export const addNewOrUpdateStore = Store => {
  return axios.post(API_PATH , Store);
};

// export const deleteCheckItem = id => {
//   return axios.delete(API_PATH+"/delete/"+id);
// };
