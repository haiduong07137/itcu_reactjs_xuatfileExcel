import axios from "axios";
import ConstantList from "../../appConfig";
const API_PATH = ConstantList.API_ENPOINT + "/api/appendix"; 
const API_TRANSCRIPTS = ConstantList.API_ENPOINT + "/api/transcript"; 
const API_DOWNLOAD = ConstantList.API_ENPOINT + "/api/fileDownload"; 

export const searchByPage = (searchObject) => {
  var url = API_PATH + "/searchByPage";
  return axios.post(url, searchObject);
};

export const getListMajorClass = () => {
  var url = API_PATH + "/getListMajorClass";
  return axios.post(url);
};

export const getItemById = id => { 
  var url = API_PATH + "/" + id;
  return axios.get(url);
};
export const updateData = appendix => {
  var url = API_PATH + "/" +  appendix.id;
  return axios.put(url, appendix);
};
 
export const getListDuplicateCode = (searchObject) => {
  var url = API_PATH + "/searchByPageDuplicateStudentCode";
  return axios.post(url,searchObject);
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

export const getExcel = (searchObject) => {
  var urll = API_DOWNLOAD + "/excelAppendix";
  return axios({
    url: urll,
    method: 'POST',
    responseType: 'blob', // important
     data:searchObject
  }) 
};

  
export const getExcelStatus = (searchObject) => {
  var url = API_DOWNLOAD + "/excelAppendixStatus";
  return axios.post(url,searchObject);
};