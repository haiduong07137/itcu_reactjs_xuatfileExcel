import axios from "axios";
import ConstantList from "../../appConfig";
const API_PATH=ConstantList.API_ENPOINT+"/api/organization";
export const getAll = () => {
  return axios.get(API_PATH);  
};
export const searchByPage = (keyword,page, pageSize) => {
  var pageIndex = page+1;
  var params = pageIndex+"/"+pageSize;
  var url = API_PATH+"/tree/full/"+params;
  return axios.post(url,{name:keyword});  
};

export const getByPage = (page, pageSize) => {
  var pageIndex = page+1;
  var params = pageIndex+"/"+pageSize;
  var url = API_PATH+"/tree/full/"+params;
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
