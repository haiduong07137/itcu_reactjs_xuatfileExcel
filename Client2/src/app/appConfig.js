const APPLICATION_PATH = "/";
const VOUCHER_TYPE = {
    StockOut: -1,//Xuất kho vật tư
    StockIn: 1,//Nhập kho vật tư
    Allocation: 2,//Cấp phát
    Transfer: 3//Điều chuyển
};
//const APPLICATION_PATH="/asset_develop/";//Đặt homepage tại package.json giống như tại đây nếu deploy develop
module.exports = Object.freeze({
    //ROOT_PATH : "/egret/",
    ROOT_PATH: APPLICATION_PATH,
    ACTIVE_LAYOUT: "layout1",//layout1 = vertical, layout2=horizontal
    API_ENPOINT: "http://localhost:8099/ictuedu",    //local
    // API_ENPOINT: "http://globits.net:9081/ictuedu",    //local
    LOGIN_PAGE: APPLICATION_PATH + "session/signin",//Nếu là Spring
    HOME_PAGE: APPLICATION_PATH + "session/signin",//Nếu là Spring    //HOME_PAGE:APPLICATION_PATH+"dashboard/learning-management"//Nếu là Keycloak
    //HOME_PAGE:APPLICATION_PATH+"landing3",//Link trang landing khi bắt đầu
});