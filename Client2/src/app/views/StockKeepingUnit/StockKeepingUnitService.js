import axios from "axios";
import ConstantList from "../../appConfig";

export const getAllStockKeepingUnits = () => {
  return axios.get(ConstantList.API_ENPOINT+"/api/stockkeepingunit/1/100000");  
};

export const getStockKeepingUnitByPage = (pageIndex, pageSize) => {
  var pageIndex = pageIndex;
  var params = pageIndex + "/" + pageSize;
  var url = ConstantList.API_ENPOINT+"/api/stockkeepingunit/"+params;
  return axios.get(url);  
};
export const searchByPage =  (keyword, pageIndex, pageSize) => {
  var pageIndex = pageIndex;
  var params = pageIndex + "/" + pageSize;
  return axios.post(ConstantList.API_ENPOINT + "/api/stockkeepingunit/searchByText/" + params, keyword);
};

export const getUserById = id => {
  return axios.get("/api/user", { data: id });
};
export const deleteItem = id => {
  return axios.delete(ConstantList.API_ENPOINT+"/api/stockkeepingunit/"+id);
};

export const getItemById = id => {
  return axios.get(ConstantList.API_ENPOINT + "/api/stockkeepingunit/" + id);
};

export const checkCode = (id, code) => {
  const config = { params: {id: id, code: code } };
  var url = ConstantList.API_ENPOINT+"/api/stockkeepingunit/checkCode";
  return axios.get(url, config);
};

export const addNewOrUpdateStockKeepingUnit = StockKeepingUnit => {
  return axios.post(ConstantList.API_ENPOINT + "/api/stockkeepingunit", StockKeepingUnit);
};

export const deleteCheckItem = id => {
  return axios.delete(ConstantList.API_ENPOINT + "/api/stockkeepingunit/delete/" + id);
};