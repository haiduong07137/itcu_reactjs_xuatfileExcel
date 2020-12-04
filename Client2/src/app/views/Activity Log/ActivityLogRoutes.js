import { EgretLoadable } from "egret";
import ConstantList from "../../appConfig";
import { useTranslation, withTranslation, Trans } from "react-i18next";
const ActivityLogTable = EgretLoadable({
  loader: () => import("./ActivityLogTable"),
});

const ViewComponent = withTranslation()(ActivityLogTable);

const activityLogRoutes = [
  {
    path: ConstantList.ROOT_PATH + "activity-log",
    exact: true,
    component: ViewComponent,
  },
];

export default activityLogRoutes;
