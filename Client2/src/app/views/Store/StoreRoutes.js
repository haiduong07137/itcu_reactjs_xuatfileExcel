import { EgretLoadable } from "egret";
import ConstantList from "../../appConfig";
import { useTranslation, withTranslation, Trans } from 'react-i18next';
const StoreTable = EgretLoadable({
  //loader: () => import("./BsTableExample")
  loader: () => import("./StoreTable")
  //loader: () => import("./AdazzleTable")
  //loader: () => import("./React15TabulatorSample")
});
const ViewComponent = withTranslation()(StoreTable);

const StoreRoutes = [
  {
    path:  ConstantList.ROOT_PATH + "list/stores",
    exact: true,
    component: ViewComponent
  }
];

export default StoreRoutes;
