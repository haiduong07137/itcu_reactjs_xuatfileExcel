import { EgretLoadable } from "egret";
import ConstantList from "../../appConfig";
import { useTranslation, withTranslation, Trans } from 'react-i18next';
const EgretCalendar = EgretLoadable({
  loader: () => import("./EgretCalendar")
});
const EventEditor = EgretLoadable({
  loader: () => import("./EventEditor")
});
const ViewListEventComponent = withTranslation()(EgretCalendar);
const ViewEventEditorComponent = withTranslation()(EventEditor);
const calendarRoutes = [
  {
    path:  ConstantList.ROOT_PATH+"calendar",
    exact: true,
    component: ViewListEventComponent
  },
  {
    path:  ConstantList.ROOT_PATH+"calendar/edit",
    exact: true,
    component: ViewEventEditorComponent
  }
];

export default calendarRoutes;
