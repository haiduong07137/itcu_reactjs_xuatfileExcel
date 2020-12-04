import React, { Component } from "react";
import "date-fns";
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
  Input,
} from "@material-ui/core";
import {
  searchByPage,
  getExcel,
  getListMajorClass,
  deleteSelected,
  deleteAll,
  getItemById
} from "./StudentScoresService";
import MemberEditorDialog from "./StudentScoresEditorDialog";
import { makeStyles } from "@material-ui/core/styles";
import InputLabel from "@material-ui/core/InputLabel";
import FormHelperText from "@material-ui/core/FormHelperText";
import FormControl from "@material-ui/core/FormControl";
import Select from "@material-ui/core/Select";
import NativeSelect from "@material-ui/core/NativeSelect";
import DuplicateCodeDiaglog from "./DuplicateCodeDiaglog";
import Autocomplete from "@material-ui/lab/Autocomplete";
import CircularProgress from "@material-ui/core/CircularProgress";
import MaterialTable, {
  MTableToolbar,
  Chip,
  MTableBody,
  MTableHeader,
} from "material-table";
import { Breadcrumb, ConfirmationDialog } from "egret";
import Loading from "../../../egret/components/EgretLoadable/Loading";
import ConstantList from "../../appConfig";
import shortid from "shortid";
import UploadFormPopup from "../Component/UploadFormPopup/UploadFormPopup";
import {
  PersonAdd,
  PersonAddDisabled,
  Visibility,
  Lock,
  AssignmentInd,
} from "@material-ui/icons";
import { deleteAllEvent } from "../calendar/CalendarService";
import DateFnsUtils from "@date-io/date-fns"
import {
  MuiPickersUtilsProvider,
  KeyboardTimePicker,
  KeyboardDatePicker
} from "@material-ui/pickers";
import moment from "moment";
import DownloadDiaglog from './DownloadDiaglog';
import {
  searchByPage as getListSign 
} from "../Signature/SignatureService";
function MaterialButton(props) {
  const item = props.item;
  return (
    <React.Fragment>
      <IconButton
        color="primary"
        component="span"
        title="Xem chi tiết điểm"
        onClick={() => props.onSelect(item, 0)}
      >
        <Visibility />
      </IconButton>
      <IconButton onClick={() => props.onSelect(item, 1)}>
        <Icon color="primary">edit</Icon>
      </IconButton>
    </React.Fragment>
  );
}

class StudentScores extends Component {
  state = {
    rowsPerPage: 50,
    page: 0,
    keyword: "",
    userList: [],
    totalElements: 0,
    shouldOpenEditorDialog: false,
    shouldOpenConfirmationDialog: false,
    shouldOpenUploadExcelDialog: false,
    shouldOpenDownloadDialog: false,
    loading: false,
    classList: [],
    majorList: [],
    studentClass: "",
    majors: "",
    selectedItems: [], date: new Date(),
  };
  handleDateChange(date) {
    this.setState({ date: date })
  }
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
      shouldOpenEditorDialog: false,
      shouldOpenConfirmationDialog: false,
      shouldOpenUploadExcelDialog: false,
      shouldOpenDuplicateCode: false,
      shouldOpenConfirmationDeleteAllDialog: false,
      shouldOpenDownloadDialog: false
    });
    this.updatePageData();
  };

  handleDeleteUser = (user) => {
    this.setState({
      user,
      shouldOpenConfirmationDialog: true,
    });
  };

  handleConfirmationResponse = () => {
    const idList = this.state.selectedItems.map((item) => item.id);
    deleteSelected(idList).then(() => {
      this.handleDialogClose();
      this.updatePageData();
    });
  };

  handleDeleteAllConfirmationResponse = () => {
    deleteAll().then(() => {
      this.handleDialogClose();
      this.updatePageData();
    });
  };

  componentDidMount() { }

  search() {
    this.setState({ page: 0 }, function () {
      var searchObject = {};
      searchObject.keyword = this.state.keyword;
      searchObject.pageIndex = this.state.page + 1;
      searchObject.pageSize = this.state.rowsPerPage;
      searchObject.majors = this.state.majors;
      searchObject.studentClass = this.state.studentClass;
      searchByPage(searchObject).then(({ data }) => {
        this.setState({
          userList: [...data.content],
          totalElements: data.totalElements,
        });
      });
    });
  }

  handleTextChange = (event) => {
    this.setState({ keyword: event.target.value }, function () { });
  };

  handleKeyDownEnterSearch = (e) => {
    if (e.key === "Enter") {
      this.search();
    }
  };

  getListSign = () => {

    var searchObject = {};
    searchObject.keyword = this.state.keyword;
    searchObject.pageIndex = this.state.page + 1;
    searchObject.pageSize = this.state.rowsPerPage;
    getListSign(searchObject).then(({ data }) => {
      this.setState({
        listSign: [...data.content] 
      });

    });
    
 
  };

  componentWillMount() {
    this.updatePageData();
    this.getListMajorAndClass();
    // this.getListSign();
  }

  getFileExcel = () => {
    this.setState({ loading: true });
    var searchObject = {};
    searchObject.keyword = this.state.keyword;
    searchObject.pageIndex = this.state.page + 1;
    searchObject.pageSize = this.state.rowsPerPage;
    searchObject.majors = this.state.majors;
    searchObject.studentClass = this.state.studentClass;
    searchObject.dateExport = moment(this.state.date).format('DD/MM/YYYY');
    getExcel(searchObject).then((result) => {
      const url = window.URL.createObjectURL(new Blob([result.data]));
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", "Bang_Diem_Moi.xls");
      document.body.appendChild(link);
      link.click();
      this.setState({ loading: false });
    });
  };

  handleDateChange = (date, name) => {
    this.setState({
      [name]: date
    }, function () {
    });
  };

  updatePageData = () => {
    var searchObject = {};
    searchObject.keyword = this.state.keyword;
    searchObject.pageIndex = this.state.page + 1;
    searchObject.pageSize = this.state.rowsPerPage;
    searchObject.majors = this.state.majors;
    searchObject.studentClass = this.state.studentClass;
    searchByPage(searchObject).then(({ data }) => {
      this.setState({
        userList: [...data.content],
        totalElements: data.totalElements,
      });
    });
  };

  getListMajorAndClass = () => {
    getListMajorClass().then(({ data }) => { 
      this.setState({classList:[...data.listClass],majorList:[...data.listMajors]})
    })
  }

  render() {
    const { t, i18n } = this.props;
    let {
      rowsPerPage,
      page,
      userList,
      classList,
      majorList,
      studentClass,
      shouldOpenConfirmationDialog,
      majors,
      shouldOpenEditorDialog,
      shouldOpenUploadExcelDialog,
      keyword,
      loading,
      shouldOpenDuplicateCode,
      selectedItems,
      shouldOpenConfirmationDeleteAllDialog, date, item, shouldOpenDownloadDialog
    } = this.state;

    let columns = [
      { title: "Mã sinh viên", field: "code", align: "left", width: "150", },
      {
        title: "Tên sinh viên", field: "displayName", cellStyle: {
          width: 200,
          maxWidth: 200,
          whiteSpace: "nowrap",
          overflow: "hidden",
          textOverflow: "ellipsis",
        },
        headerStyle: {
          width: 150,
          maxWidth: 150,
          overflow: "hidden",
          textOverflow: "ellipsis",
          whiteSpace: "nowrap",
        },
      },
      { title: "Lớp học", field: "studentClass", align: "left", width: "150" },
      {
        title: "Ngành học", field: "majors", align: "left", cellStyle: {
          width: 200,
          maxWidth: 200,
          overflow: "hidden",
          textOverflow: "ellipsis",
        },
        headerStyle: {
          width: 200,
          maxWidth: 200,
          overflow: "hidden",
          textOverflow: "ellipsis",
          whiteSpace: "nowrap",
        },
      },
       {
        title: "Chuyên ngành", field: "specialized", align: "left", cellStyle: {
          width: 200,
          maxWidth: 200,
          overflow: "hidden",
          textOverflow: "ellipsis",
        },
        headerStyle: {
          width: 200,
          maxWidth: 200,
          overflow: "hidden",
          textOverflow: "ellipsis",
          whiteSpace: "nowrap",
        },
      },
      {
        title: "File Upload",
        field: "fromUploadedFile",
        align: "left",
        cellStyle: {
          width: 150,
          maxWidth: 150,
          whiteSpace: "nowrap",
          overflow: "hidden",
          textOverflow: "ellipsis",
        },
        headerStyle: {
          width: 150,
          maxWidth: 150,
          overflow: "hidden",
          textOverflow: "ellipsis",
          whiteSpace: "nowrap",
        },
      },
      {
        title: "Khoá học", field: "course", cellStyle: {
          width: 150,
          maxWidth: 150,
          whiteSpace: "nowrap",
          overflow: "hidden",
          textOverflow: "ellipsis",
        },
        headerStyle: {
          width: 150,
          maxWidth: 150,
          overflow: "hidden",
          textOverflow: "ellipsis",
          whiteSpace: "nowrap",
        },
      },
      {
        title: "Nơi sinh", field: "placeOfBirth", align: "left", cellStyle: {
          width: 150,
          maxWidth: 150,
          whiteSpace: "nowrap",
          overflow: "hidden",
          textOverflow: "ellipsis",
        },
        headerStyle: {
          width: 150,
          maxWidth: 150,
          overflow: "hidden",
          textOverflow: "ellipsis",
          whiteSpace: "nowrap",
        },
      },
      {
        title: t("Action"),
        field: "custom",
        align: "left",
        cellStyle: {
          width: 150,
          maxWidth: 150,
          whiteSpace: "nowrap",
          overflow: "hidden",
          textOverflow: "ellipsis",
        },
        headerStyle: {
          width: 150,
          maxWidth: 150,
          overflow: "hidden",
          textOverflow: "ellipsis",
          whiteSpace: "nowrap",
        },
        render: (rowData) => (
          <MaterialButton
            item={rowData}
            onSelect={(rowData, method) => {
              if (method === 0) {
                this.props.history.push({
                  pathname: ConstantList.ROOT_PATH + "scores-detail",
                  state: {
                    rowsPerPage: 10,
                    page: 0,
                    keyword: "",
                    studentCode: rowData.code,
                    studentName: rowData.displayName,
                    studentId: rowData.id,
                  },
                });
              } else if (method === 1) {
                getItemById(rowData.id).then(({ data }) => {
                  if (data != null && data.id != null) {
                    this.setState({
                      item: data
                    }, function () {
                      this.setState({ shouldOpenEditorDialog: true });
                    });
                  }
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
          <Breadcrumb routeSegments={[{ name: "Sinh viên" }]} />
        </div>
        <Grid container spacing={1}>
          <Grid item xs={12}>
            <Grid item xs={12}>
              <TextField
                label="Tìm theo tên hoặc mã "
                className="mb-16 mr-10"
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
                {t("general.search")}&nbsp;
            <Icon>search</Icon>
              </Button>
              <Button
                className="mb-16 mr-16 align-bottom"
                variant="contained"
                color="primary"
                onClick={() =>
                  this.setState({ shouldOpenConfirmationDeleteAllDialog: true })
                }
              >
                {t("general.delete_all")}
              </Button>
            </Grid>
            <Grid item container spacing={3} xs={12}>
              <Grid item xs={3}>
                <Autocomplete
                  options={this.state?.classList}
                  value={studentClass}
                  className="mb-16 mr-16 align-bottom"
                  variant="contained" 
                  getOptionLabel={(option) => option}
                  onChange={(event, value) => {
                    this.setState(
                      {
                        studentClass: value,
                        majors: "",
                      },
                      function () {
                        this.search();
                      }
                    );
                  }}
                  renderInput={(params) => (
                    <TextField
                      {...params}
                      value={studentClass}
                      label="Lớp học"
                      variant="outlined"
                    />
                  )}
                />
              </Grid>
              <Grid item xs={4}>
                <Autocomplete
                  options={this.state?.majorList}
                  value={majors}
                  className=" mr-16 align-bottom"
                  variant="contained" 
                  onChange={(event, value) => {
                    this.setState(
                      {
                        studentClass: "",
                        majors: value,
                      },
                      function () {
                        this.search();
                      }
                    );
                  }}
                  getOptionLabel={(option) => option}
                  renderInput={(params) => (
                    <TextField
                      {...params}
                      value={majors}
                      label="Ngành"
                      variant="outlined"
                    />
                  )}
                />
              </Grid>

              <Grid item xs={4}>


                <Button
                  className="mt-16 mr-32 "
                  variant="contained"
                  color="primary"
                  disabled={this.state.loading}
                  onClick={() => this.setState({ shouldOpenDownloadDialog: true })}
                >
                  Xuất Excel&nbsp;
                      <Icon>get_app</Icon>



                </Button>



                <Button
                  className="mt-16 mr-32 "
                  variant="contained"
                  color="secondary"
                  onClick={() => this.setState({ shouldOpenUploadExcelDialog: true })}
                >
                  Upload Excel&nbsp;
            <Icon>backup</Icon>
                </Button>
              </Grid>
            </Grid>
          </Grid>





          <Grid item xs={12}>
            <Card className="w-100 overflow-auto" elevation={6}>
              <MaterialTable
                title="Danh sách sinh viên"
                data={this.state.userList}
                columns={columns}
                //parentChildData={(row, rows) => rows.find(a => a.id === row.parentId)}
                parentChildData={(row, rows) => {
                  var list = rows.find((a) => a.id === row.parentId);
                  return list;
                }}
                options={{
                  selection: true,
                  actionsColumnIndex: -1,
                  paging: false,
                  search: false,
                }}
                components={{
                  Toolbar: (props) => <MTableToolbar {...props} />,
                }}
                onSelectionChange={(rows) => {
                  this.data = rows;
                  this.setState({ selectedItems: rows });
                }}
                actions={[
                  {
                    tooltip: t("general.delete_all"),
                    icon: "delete",
                    onClick: (evt, data) => {
                      this.setState({
                        shouldOpenConfirmationDialog: true,
                      });
                    },
                  },
                ]}
              />
              <div>
                {shouldOpenUploadExcelDialog && (
                  <UploadFormPopup
                    t={t}
                    i18n={i18n}
                    handleClose={this.handleDialogClose}
                    open={shouldOpenUploadExcelDialog}
                    handleOKEditClose={this.handleOKEditClose}
                    acceptType="xls;xlsx"
                    uploadUrl={
                      ConstantList.API_ENPOINT +
                      "/api/fileUpload/importStudentTranscript"
                    }
                  />
                )}
              </div>

              <TablePagination
                className="px-16"
                rowsPerPageOptions={[5, 10, 25,50,100]}
                component="div"
                count={this.state.totalElements}
                rowsPerPage={this.state.rowsPerPage}
                page={this.state.page}
                backIconButtonProps={{
                  "aria-label": "Previous Page",
                }}
                nextIconButtonProps={{
                  "aria-label": "Next Page",
                }}
                onChangePage={this.handleChangePage}
                onChangeRowsPerPage={this.setRowsPerPage}
              />
              {/*           
          {shouldOpenDuplicateCode && (
            <DuplicateCodeDiaglog
              t={t}
              i18n={i18n}
              handleClose={this.handleDialogClose}
              open={shouldOpenDuplicateCode}
              handleOKEditClose={this.handleOKEditClose}
              onChangePage={this.handleChangePage}
            /> */}

              {shouldOpenDownloadDialog && (
                <DownloadDiaglog
                  t={t}
                  i18n={i18n}
                  handleClose={this.handleDialogClose}
                  open={shouldOpenDownloadDialog}
                  handleOKEditClose={this.handleOKEditClose}
                  onChangePage={this.handleChangePage}
                  item={this.state.item}
                  handleDateChange={this.handleDateChange}
                  majors={this.state.majors}
                  studentClass={this.state.studentClass}
                  // listSign={this.state.listSign}
                />
              )}


              {shouldOpenEditorDialog && (
                <MemberEditorDialog t={t} i18n={i18n}
                  handleClose={this.handleDialogClose}
                  open={shouldOpenEditorDialog}
                  handleOKEditClose={this.handleOKEditClose}
                  item={this.state.item}
                />
              )}
              {shouldOpenConfirmationDialog && (
                <ConfirmationDialog
                  open={shouldOpenConfirmationDialog}
                  onConfirmDialogClose={this.handleDialogClose}
                  onYesClick={this.handleConfirmationResponse}
                  agree={t("general.delete")}
                  cancel={t("general.cancel")}
                  text={t("general.deleteSelectConfirm")}
                />
              )}
              {shouldOpenConfirmationDeleteAllDialog && (
                <ConfirmationDialog
                  open={shouldOpenConfirmationDeleteAllDialog}
                  onConfirmDialogClose={this.handleDialogClose}
                  onYesClick={this.handleDeleteAllConfirmationResponse}
                  agree={t("general.delete")}
                  cancel={t("general.cancel")}
                  text={t("general.deleteAllConfirm")}
                />
              )}

            </Card>
          </Grid>
        </Grid>
      </div >
    );
  }
}

export default StudentScores;
