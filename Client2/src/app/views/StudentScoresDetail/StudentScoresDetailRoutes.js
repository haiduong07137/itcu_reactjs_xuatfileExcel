import { EgretLoadable } from "egret";

import { useTranslation, withTranslation, Trans } from 'react-i18next';
import ConstantList from "../../appConfig";
const StudentScoresDetailTable = EgretLoadable({
  loader: () => import("./StudentScoresDetailTable")
});
const ViewComponent = withTranslation()(StudentScoresDetailTable);
const StudentScoresDetailRoutes = [
  {
    path:  ConstantList.ROOT_PATH+"scores-detail",
    exact: true,
    component: ViewComponent
  }
];

export default StudentScoresDetailRoutes;
