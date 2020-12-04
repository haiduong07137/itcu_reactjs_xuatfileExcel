import ConstantList from "./appConfig";
export const navigations = [
  {
    name: "Dashboard.dashboard",
    icon: "dashboard",
    path: ConstantList.ROOT_PATH + "dashboard/analytics",
  },
  {
    name: "Dashboard.student_score",
    icon: "dashboard",
    path: ConstantList.ROOT_PATH + "student-scores",
  },
  {
    name: "Dashboard.appendix",
    icon: "dashboard",
    path: ConstantList.ROOT_PATH + "student-info",
  }, 
  {
    name: "Dashboard.history_log",
    icon: "dashboard",
    path: ConstantList.ROOT_PATH + "activity-log",
  },

  , {
    name: "Dashboard.manage",
    icon: "engineering",
    children: [
      // {
      //   name: "manage.profile_is_logged",
      //   path: ConstantList.ROOT_PATH + "manage/profile_is_logged",
      //   icon: "P"
      // },
      {
        name: "manage.user",
        path: ConstantList.ROOT_PATH + "manage/user",
        icon: "keyboard_arrow_right"
      }
    ]
  }
];
