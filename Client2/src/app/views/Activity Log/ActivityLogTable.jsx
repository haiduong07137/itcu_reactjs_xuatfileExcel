import React, { Component } from "react";
import {
  IconButton,
  Table,
  TableHead,
  TableBody,
  TableRow,
  TableCell,
  Icon,
  TablePagination,
  Button,
  Card,
  TextField,
  Grid,
} from "@material-ui/core";
import MaterialTable, {
  MTableToolbar,
  Chip,
  MTableBody,
  MTableHeader,
} from "material-table";
import { useTranslation, withTranslation, Trans } from "react-i18next";
import { Breadcrumb, ConfirmationDialog, ShowDialog } from "egret";
import ConstantList from "../../appConfig";
import { searchByPage } from "./ActivityLogService";
import shortid from "shortid";
import moment from "moment";
import { SUCCESSFUL, FAILED } from "./ActivityLogConstants";
function MaterialButton(props) {
  const item = props.item;
  const { t, i18n } = useTranslation();
  return (
    <React.Fragment>
      <IconButton
        color="primary"
        component="span"
        title={t("ActivityLog.view_log")}
        onClick={() => props.onSelect(item, 0)}
      >
        <Icon>article</Icon>
      </IconButton>
    </React.Fragment>
  );
}

class ActivityLogTable extends Component {
  state = {
    rowsPerPage: 10,
    page: 0,
    keyword: "",
    totalElements: 0,
    activityLogList: [],
    shouldOpenLogDialog: false,
    log: "",
  };

  setPage = (page) => {
    this.setState({ page }, function () {
      this.updatePageData();
    });
  };

  setRowsPerPage = (event) => {
    this.setState({ rowsPerPage: event.target.value, page: 0 }, function () {
      this.updatePageData();
    });
  };

  handleChangePage = (event, newPage) => {
    this.setPage(newPage);
  };

  handleDialogClose = () => {
    this.setState({
      shouldOpenLogDialog: false,
    });
  };

  search() {
    this.setState({ page: 0 }, function () {
      var searchObject = {};
      searchObject.keyword = this.state.keyword;
      searchObject.pageIndex = this.state.page + 1;
      searchObject.pageSize = this.state.rowsPerPage;
      searchByPage(searchObject).then(({ data }) => {
        this.setState({
          activityLogList: [...data.content],
          totalElements: data.totalElements,
        });
      });
    });
  }

  handleTextChange = (event) => {
    this.setState({ keyword: event.target.value }, function () {});
  };

  handleKeyDownEnterSearch = (e) => {
    if (e.key === "Enter") {
      this.search();
    }
  };

  componentDidMount() {
    this.updatePageData();
  }

  updatePageData = () => {
    var searchObject = {};
    searchObject.keyword = this.state.keyword;
    searchObject.pageIndex = this.state.page + 1;
    searchObject.pageSize = this.state.rowsPerPage;
    searchByPage(searchObject).then(({ data }) => {
      this.setState({
        activityLogList: [...data.content],
        totalElements: data.totalElements,
      });
    });
  };

  render() {
    const { t, i18n } = this.props;
    let {
      rowsPerPage,
      page,
      totalElements,
      shouldOpenLogDialog,
      keyword,
      log,
    } = this.state;

    let columns = [
      {
        title: t("ActivityLog.upload_time"),
        field: "uploadTime",
        align: "left",
        width: "150",
        render: (rowData) => {
          return (
            <p>{moment(rowData.uploadTime).format("DD/MM/YYYY HH:MM:SS")}</p>
          );
        },
      },
      { title: t("ActivityLog.file_name"), field: "fileName", width: "150" },
      {
        title: t("ActivityLog.status"),
        field: "status",
        align: "left",
        width: "150",
        render: (rowData) => {
          switch (rowData.status) {
            case SUCCESSFUL:
              return t("ActivityLog.status_successful");
            case FAILED:
              return t("ActivityLog.status_failed");
            default:
              return "";
          }
        },
      },
      {
        title: t("ActivityLog.log_detail"),
        field: "logDetail",
        width: "150",
        render: (rowData) => (
          <MaterialButton
            item={rowData}
            onSelect={(rowData, method) => {
              if (method === 0) {
                this.setState({
                  shouldOpenLogDialog: true,
                  log: rowData.logDetail,
                });
              } else {
                alert("Call Selected Here:" + rowData.id);
              }
            }}
          />
        ),
      },
    ];

    return (
      <div className="m-sm-30">
        <div className="mb-sm-30">
          <Breadcrumb
            routeSegments={[{ name: t("ActivityLog.activity_log") }]}
          />
        </div>
        <Grid container spacing={3}>
          <Grid item xs={12}>
            <TextField
              label={t("ActivityLog.search")}
              className="mb-16 mr-20"
              type="text"
              name="keyword"
              value={keyword}
              onKeyDown={this.handleKeyDownEnterSearch}
              onChange={this.handleTextChange}
            />
            <Button
              className="mb-16 mr-16 align-bottom"
              variant="contained"
              color="primary"
              onClick={() => this.search(keyword)}
            >
              <span style={{ marginRight: "5px" }}>{t("general.search")}</span>
              <Icon>search</Icon>
            </Button>
          </Grid>
          <Grid item xs={12}>
            <Card className="w-100 overflow-auto" elevation={6}>
              <MaterialTable
                title={t("ActivityLog.activity_log")}
                data={this.state.activityLogList}
                columns={columns}
                parentChildData={(row, rows) => {
                  var list = rows.find((a) => a.id === row.parentId);
                  return list;
                }}
                options={{
                  selection: false,
                  actionsColumnIndex: -1,
                  paging: false,
                  search: false,
                }}
                components={{
                  Toolbar: (props) => <MTableToolbar {...props} />,
                }}
                onSelectionChange={(rows) => {
                  this.data = rows;
                }}
                // actions={[
                //   {
                //     tooltip: 'Remove All Selected Users',
                //     icon: 'delete',
                //     onClick: (evt, data) => {
                //       this.handleDeleteAll(data);
                //       alert('You want to delete ' + data.length + ' rows');
                //     }
                //   },
                // ]}
              />
              <TablePagination
                className="px-16"
                rowsPerPageOptions={[5, 10, 25]}
                component="div"
                count={totalElements}
                rowsPerPage={rowsPerPage}
                page={page}
                backIconButtonProps={{
                  "aria-label": "Previous Page",
                }}
                nextIconButtonProps={{
                  "aria-label": "Next Page",
                }}
                onChangePage={this.handleChangePage}
                onChangeRowsPerPage={this.setRowsPerPage}
              />
            </Card>
            <ShowDialog
              title={t("ActivityLog.log_detail")}
              open={shouldOpenLogDialog}
              onConfirmDialogClose={this.handleDialogClose}
              text={log}
              cancel={"OK"}
            />
          </Grid>
        </Grid>
      </div>
    );
  }
}

export default ActivityLogTable;
