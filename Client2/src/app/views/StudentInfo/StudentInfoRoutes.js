import { EgretLoadable } from "egret";

import { useTranslation, withTranslation, Trans } from 'react-i18next';
import ConstantList from "../../appConfig";
const StudentInfoTable = EgretLoadable({
  loader: () => import("./StudentInfoTable")
});
const ViewComponent = withTranslation()(StudentInfoTable);
const studenInfoRoutes = [
  {
    path:  ConstantList.ROOT_PATH+"student-info",
    exact: true,
    component: ViewComponent
  }
];

export default studenInfoRoutes;
