import { EgretLoadable } from "egret";
import ConstantList from "../../appConfig";
import { useTranslation, withTranslation, Trans } from 'react-i18next';
const StockKeepingUnitTable = EgretLoadable({
  //loader: () => import("./BsTableExample")
  loader: () => import("./StockKeepingUnitTable")
  //loader: () => import("./AdazzleTable")
  //loader: () => import("./React15TabulatorSample")
});
const ViewComponent = withTranslation()(StockKeepingUnitTable);

const StockKeepingUnitRoutes = [
  {
    path:  ConstantList.ROOT_PATH + "list/stock_keeping_unit",
    exact: true,
    component: ViewComponent
  }
];

export default StockKeepingUnitRoutes;
