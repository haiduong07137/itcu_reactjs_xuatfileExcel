import axios from "axios";
import ConstantList from "../../appConfig";
const API_PATH=ConstantList.API_ENPOINT+"/api/location";
export const getAll = () => {
  return axios.get(API_PATH);  
};

export const getByPage = (page, pageSize) => {
  var pageIndex = page+1;
  var params = pageIndex+"/"+pageSize;
  var url = API_PATH+"/"+params;
  return axios.get(url);  
};

export const getItemById = id => {
  var url = API_PATH+"/"+id;
  return axios.get(url);
};
export const deleteItem = id => {
  var url = API_PATH+"/"+id;
  return axios.delete(url);
};
export const saveItem = item => {
  var url = API_PATH;
  return axios.post(url, item);
};
