import axios from "axios";
import ConstantList from "../../appConfig";
const API_PATH = ConstantList.API_ENPOINT + "/api/transcript";  
const API_DOWNLOAD = ConstantList.API_ENPOINT + "/api/fileDownload"; 

export const searchByPage = (searchObject) => {
  var url = API_PATH + "/getScoreByStudentCode";
  return axios.post(url, searchObject);
};

 

export const searchScoresStudentByPage = (searchObject) => {
  var url = API_PATH + "/getScoreByStudentCode";
  return axios.post(url, searchObject);
};

export const getExcelStatus = (searchObject) => {
  var url = API_DOWNLOAD  + "/excelStatus";
  return axios.post(url, searchObject);
};

export const getExcel = (list) => {
  var urll = API_DOWNLOAD + "/excel";
  return axios({
    url: urll,
    method: 'POST',
    responseType: 'blob', // important
    data:list
  }) 
};

 