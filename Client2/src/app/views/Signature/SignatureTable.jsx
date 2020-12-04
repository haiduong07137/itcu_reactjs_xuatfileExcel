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
  searchByPage ,
  deleteSelected,
  deleteAll,
  getItemById
} from "./SignatureService"; 
import { makeStyles } from "@material-ui/core/styles";
import InputLabel from "@material-ui/core/InputLabel";
import FormHelperText from "@material-ui/core/FormHelperText";
import FormControl from "@material-ui/core/FormControl";
import Select from "@material-ui/core/Select";
import NativeSelect from "@material-ui/core/NativeSelect"; 
import Autocomplete from "@material-ui/lab/Autocomplete";
import CircularProgress from "@material-ui/core/CircularProgress";
import MaterialTable, {
  MTableToolbar,
  Chip,
  MTableBody,
  MTableHeader,
} from "material-table";
import { Breadcrumb, ConfirmationDialog } from "egret";
import MemberEditorDialog from "./SignatureEditorDialog"
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
function MaterialButton(props) {
  const item = props.item;
  return (
    <React.Fragment>
      {/* <IconButton
        color="primary"
        component="span"
        title="Xem chi tiết điểm"
        onClick={() => props.onSelect(item, 0)}
      >
        <Visibility />
      </IconButton> */}
      <IconButton onClick={() => props.onSelect(item, 1)}>
        <Icon color="primary">edit</Icon>
      </IconButton>
    </React.Fragment>
  );
}

class StudentScores extends Component {
  state = {
    rowsPerPage: 10,
    page: 0,
    keyword: "",
    userList: [],
    totalElements: 0,
    shouldOpenEditorDialog: false,
    shouldOpenConfirmationDialog: false,
    shouldOpenUploadExcelDialog: false,
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
    });
    this.updatePageData();
  };

  handleOKEditClose = () =>{
    this.setState({
      shouldOpenEditorDialog: false,
      shouldOpenConfirmationDialog: false,
      shouldOpenUploadExcelDialog: false,
      shouldOpenDuplicateCode: false,
      shouldOpenConfirmationDeleteAllDialog: false,
    });
    this.updatePageData();
  }

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

  handleEditItem = (item) => {
    this.setState({
      item: item,
      shouldOpenEditorDialog: true,
    })
  }

  componentWillMount() {
    this.updatePageData(); 
  }

  

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
      shouldOpenConfirmationDeleteAllDialog, date,item
    } = this.state;

    let columns = [
      { title: "TL Hiệu trưởng", field: "signPrincipal", align: "left", width: "150" },
      { title: "Tên phòng ban TL", field: "departName", width: "150" },
      { title: "Tên người TL", field: "signName", align: "left", width: "150" },
         {
        title: t("Action"),
        field: "custom",
        align: "left",
        width: "250",
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
            <Button
              className="mb-16 mr-16 align-bottom"
              variant="contained"
              color="primary"
              onClick={() => {
                this.handleEditItem({
                  startDate: new Date(),
                  endDate: new Date(),
                })
              }}
            >
              {t('general.add')}
            </Button>

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
                rowsPerPageOptions={[5, 10, 25]}
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
