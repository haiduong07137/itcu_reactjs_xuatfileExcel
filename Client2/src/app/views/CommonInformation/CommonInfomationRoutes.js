import { EgretLoadable } from "egret";
import { authRoles } from "../../auth/authRoles";
import ConstantList from "../../appConfig";
import { useTranslation, withTranslation, Trans } from 'react-i18next';
const LocationManagement = EgretLoadable({
  loader: () => import("./LocationManagement")
});
const OrganizationManagement = EgretLoadable({
  loader: () => import("./OrganizationManagement")
});
const ViewComponent = withTranslation()(LocationManagement);
const ViewOrganizationComponent = withTranslation()(OrganizationManagement);
const commonInfomationRoutes = [
  {
    path:  ConstantList.ROOT_PATH+"CommonInformation/location",
    component: ViewComponent,
    auth: authRoles.admin
  },
  {
    path:  ConstantList.ROOT_PATH+"CommonInformation/organization",
    component: ViewOrganizationComponent,
    auth: authRoles.admin
  }  
];
export default commonInfomationRoutes;
