import { EgretLoadable } from "egret";

import { useTranslation, withTranslation, Trans } from 'react-i18next';
import ConstantList from "../../appConfig";
const SignatureTable = EgretLoadable({
  loader: () => import("./SignatureTable")
});
const ViewComponent = withTranslation()(SignatureTable);
const SignatureRoutes = [
  {
    path:  ConstantList.ROOT_PATH+"signature",
    exact: true,
    component: ViewComponent
  }
];

export default SignatureRoutes;
