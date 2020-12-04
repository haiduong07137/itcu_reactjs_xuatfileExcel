import { EgretLoadable } from "egret";

import { useTranslation, withTranslation, Trans } from 'react-i18next';
import ConstantList from "../../appConfig";
const StudentScoresTable = EgretLoadable({
  loader: () => import("./StudentScoresTable")
});
const ViewComponent = withTranslation()(StudentScoresTable);
const studenScoresRoutes = [
  {
    path:  ConstantList.ROOT_PATH+"student-scores",
    exact: true,
    component: ViewComponent
  }
];

export default studenScoresRoutes;
