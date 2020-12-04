import React from "react";
import { Redirect } from "react-router-dom";

import dashboardRoutes from "./views/dashboard/DashboardRoutes";
import utilitiesRoutes from "./views/utilities/UtilitiesRoutes";
import sessionRoutes from "./views/sessions/SessionRoutes";
import materialRoutes from "./views/material-kit/MaterialRoutes";
import chartsRoute from "./views/charts/ChartsRoute";
import dragAndDropRoute from "./views/Drag&Drop/DragAndDropRoute";
import invoiceRoutes from "./views/invoice/InvoioceRoutes";
import calendarRoutes from "./views/calendar/CalendarRoutes";
import crudRoute from "./views/CRUD/CrudRoutes";
import inboxRoute from "./views/inbox/InboxRoutes";
import formsRoutes from "./views/forms/FormsRoutes";
import mapRoutes from "./views/map/MapRoutes";
import chatRoutes from "./views/chat-box/ChatRoutes";
import todoRoutes from "./views/todo/TodoRoutes";
import pageLayoutRoutes from "./views/page-layouts/PageLayoutRoutees";
import ListRoute from "./views/list/ListRoute";
import administrativeUnitRoutes from "./views/AdministrativeUnit/AdministrativeUnitRoutes";
import datatablePageRootes from "./views/MDBDataTable/DatatablePageRootes";
import tablePageRoutes from "./views/MDBDataTable/TablePageRoutes";
import homeRoutes from "./views/home/HomeRoutes";
import otherRoutes from "./views/others/OtherRoutes";
import scrumBoardRoutes from "./views/scrum-board/ScrumBoardRoutes";
import ecommerceRoutes from "./views/ecommerce/EcommerceRoutes";
import userRoutes from "./views/User/UserRoutes";
import StudentScoresRoutes from "./views/StudentScores/StudentScoresRoutes";
import SignatureRoutes from "./views/Signature/SignatureRoutes";
import StudentInfoRoutes from "./views/StudentInfo/StudentInfoRoutes";
import StudentScoresDetailRoutes from "./views/StudentScoresDetail/StudentScoresDetailRoutes";
import departmentRoutes from "./views/Department/DepartmentRoutes";
import roleRoutes from "./views/Role/RoleRoutes";
import ConstantList from "./appConfig";
import CommonObjectTypeRoutes from "./views/CommonObjectType/CommonObjectTypeRoutes";
import CommonObjectRoutes from "./views/CommonObject/CommonObjectRoutes";
import StockKeepingUnitRoutes from "./views/StockKeepingUnit/StockKeepingUnitRoutes";
import StoreRoutes from "./views/Store/StoreRoutes";
import activityLogRoutes from "./views/Activity Log/ActivityLogRoutes";

const redirectRoute = [
  {
    path: ConstantList.ROOT_PATH,
    exact: true,
    component: () => <Redirect to={ConstantList.HOME_PAGE} />, //Luôn trỏ về HomePage được khai báo trong appConfig
  },
];

const errorRoute = [
  {
    component: () => <Redirect to={ConstantList.ROOT_PATH + "session/404"} />,
  },
];

const routes = [
  ...homeRoutes,
  ...sessionRoutes,
  ...dashboardRoutes,
  ...administrativeUnitRoutes,
  ...SignatureRoutes,
  ...datatablePageRootes,
  ...tablePageRoutes,
  ...StudentScoresRoutes,
  ...StudentInfoRoutes,
  ...materialRoutes,
  ...utilitiesRoutes,
  ...chartsRoute,
  ...dragAndDropRoute,
  ...StudentScoresDetailRoutes,
  ...activityLogRoutes,
  ...calendarRoutes,
  ...invoiceRoutes,
  ...crudRoute,
  ...inboxRoute,
  ...formsRoutes,
  ...mapRoutes,
  ...chatRoutes,
  ...todoRoutes,
  ...CommonObjectTypeRoutes,
  ...CommonObjectRoutes,
  ...StockKeepingUnitRoutes,
  ...scrumBoardRoutes,
  ...ecommerceRoutes,
  ...pageLayoutRoutes,
  ...otherRoutes,
  ...ListRoute,
  ...departmentRoutes,
  ...userRoutes,
  ...roleRoutes,
  ...redirectRoute,
  ...StoreRoutes,
  ...errorRoute,
];

export default routes;
