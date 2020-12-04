import Cookies from 'universal-cookie';
import ConstantList from "../appConfig";
//import { useCookies } from 'react-cookie';
const cookies = new Cookies();   
class localStorageService {
  ls = window.localStorage
  setItem(key, value) {
    cookies.set(key,value, { path: ConstantList.COOKIE_PATH });
    return true
  }

  getItem(key) {
    let value = cookies.get(key);
    return value;
  }

  removeItem(key) {
    cookies.remove(key, { path: ConstantList.COOKIE_PATH });
  }
  getLoginUser(){
    return this.getItem('auth_user');
  }
  
}

export default new localStorageService();