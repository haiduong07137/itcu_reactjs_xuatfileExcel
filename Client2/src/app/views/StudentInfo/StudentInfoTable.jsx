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
  Input
} from "@material-ui/core";
import { searchByPage, getExcel, getListMajorClass, deleteSelected, getItemById, deleteAll } from "./StudentInfoService";
import { makeStyles } from '@material-ui/core/styles';
import InputLabel from '@material-ui/core/InputLabel';
import FormHelperText from '@material-ui/core/FormHelperText';
import FormControl from '@material-ui/core/FormControl';
import Select from '@material-ui/core/Select';
import NativeSelect from '@material-ui/core/NativeSelect';
import DownloadDiaglog from './DownloadDiaglog'
import Autocomplete from '@material-ui/lab/Autocomplete'
import CircularProgress from "@material-ui/core/CircularProgress";
import MaterialTable, { MTableToolbar, Chip, MTableBody, MTableHeader } from 'material-table';
import { Breadcrumb, ConfirmationDialog } from "egret";
import Loading from '../../../egret/components/EgretLoadable/Loading'
import ConstantList from "../../appConfig";
import shortid from "shortid";
import UploadFormPopup from '../Component/UploadFormPopup/UploadFormPopup'
import StudentDialog from './StudentInfosEditorDialog'
import {
  PersonAdd,
  PersonAddDisabled,
  Visibility,
  Lock,
  AssignmentInd,
} from '@material-ui/icons'
import DateFnsUtils from "@date-io/date-fns"
import {
  MuiPickersUtilsProvider,
  KeyboardTimePicker,
  KeyboardDatePicker
} from "@material-ui/pickers";
import moment from "moment";
import {
  searchByPage as getListSign
} from "../Signature/SignatureService";
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
toast.configure();
function MaterialButton(props) {
  const item = props.item
  return (
    <React.Fragment>
      <IconButton
        color="primary"
        component="span"
        title="Tải xuống phụ lục"
        onClick={() => props.onSelect(item, 0)}
      >
        <Icon>get_app</Icon>
      </IconButton>
      <IconButton onClick={() => props.onSelect(item, 1)}>
        <Icon color="primary">edit</Icon>
      </IconButton>
    </React.Fragment>
  )

}

class StudentScores extends Component {
  state = {
    rowsPerPage: 50,
    page: 0,
    keyword: '',
    userList: [], listSign: [],
    totalElements: 0,
    shouldOpenEditorDialog: false,
    shouldOpenConfirmationDialog: false, shouldDownloadDiaglog: false,
    shouldOpenUploadExcelDialog: false, loading: false, classList: [], majorList: [], studentClass: '', majors: '',
    uid: '', date: new Date(), shouldOpenConfirmationDeleteAllDialog: false,
    fromFile: [], trainingSystem: []
  };

  setPage = page => {
    this.setState({ page }, function () {
      this.updatePageData();
    })
  };





  setRowsPerPage = event => {

    this.setState({ rowsPerPage: event.target.value, page: 0 }, function () {
      this.updatePageData();
    })
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
      shouldOpenDownloadDialog: false,
      shouldOpenConfirmationDeleteAllDialog: false,
      shouldOpenShowStatusDialog: false
    });
    this.updatePageData();
  };

  handleDeleteUser = user => {
    this.setState({
      user,
      shouldOpenConfirmationDialog: true
    });
  };

  handleConfirmationResponse = () => {
    const idList = this.state.selectedItems.map((item) => item.id);
    deleteSelected(idList).then(() => {
      this.handleDialogClose();
      this.updatePageData();
        toast.info("Xoá thành công");
    });
  };

  componentDidMount() {

  }

  search() {
    this.setState({ page: 0 }, function () {
      var searchObject = {}
      searchObject.keyword = this.state.keyword
      searchObject.pageIndex = this.state.page + 1
      searchObject.pageSize = this.state.rowsPerPage
      searchObject.majors = this.state.majors
      searchObject.studentClass = this.state.studentClass
      searchByPage(searchObject).then(({ data }) => {
        this.setState({
          userList: [...data.content],
          totalElements: data.totalElements,
        })
      })
    })
  }

  handleTextChange = (event) => {
    this.setState({ keyword: event.target.value }, function () { })
  }

  handleKeyDownEnterSearch = (e) => {
    if (e.key === 'Enter') {
      this.search()
    }
  }

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
    this.getListSign();
    this.getListMajorAndClass()
  }

  getFileExcel = (id) => {
    this.setState({ loading: true })
    var searchObject = {}
    searchObject.id = id
    searchObject.dateExport = moment(this.state.date).format('DD/MM/YYYY');
    getExcel(searchObject).then((result) => {
      const url = window.URL.createObjectURL(new Blob([result.data]))
      const link = document.createElement('a')
      link.href = url
      link.setAttribute('download', 'Phu_Luc_Van_Bang.xls')
      document.body.appendChild(link)
      link.click();
      this.setState({ loading: false })
    })
  }


  updatePageData = () => {
    var searchObject = {}
    searchObject.keyword = this.state.keyword
    searchObject.pageIndex = this.state.page + 1
    searchObject.pageSize = this.state.rowsPerPage
    searchObject.majors = this.state.majors
    searchObject.studentClass = this.state.studentClass
    searchByPage(searchObject).then(({ data }) => {
      this.setState({ userList: [...data.content], totalElements: data.totalElements })
    })
  };

  getListMajorAndClass = () => {
    getListMajorClass().then(({ data }) => { 
      this.setState({classList:[...data.listClass],majorList:[...data.listMajors]})
    })
  }

  handleDeleteAllConfirmationResponse = () => {
    deleteAll().then(() => {
      this.handleDialogClose();
      this.updatePageData();
      toast.info("Xoá thành công");
    });
  };

  handleDateChange(date) {
    this.setState({ date: date })
  }

  render() {
    const { t, i18n } = this.props;
    let {
      rowsPerPage,
      page,
      userList, classList, majorList,
      studentClass,
      shouldOpenConfirmationDialog, majors,
      shouldOpenEditorDialog,
      shouldOpenUploadExcelDialog,
      keyword, loading, shouldOpenDuplicateCode, date, data, shouldOpenDownloadDialog, shouldOpenConfirmationDeleteAllDialog
    } = this.state;

    let columns = [{
      title: t("Action"),
      field: "custom",
      align: "left",
      width: "250",
      render: rowData => <MaterialButton item={rowData}
        onSelect={(rowData, method) => {
          if (method === 0) {
            // this.getFileExcel(rowData.id)
            this.setState({
              item: rowData
            }, function () {
              this.setState({ shouldOpenDownloadDialog: true });
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
            alert('Call Selected Here:' + rowData.id);
          }
        }}
      />
    },
    { title: "Mã sinh viên", field: "code", align: "left", width: "150" },
    {
      title: "Tên sinh viên", field: "fullName", cellStyle: {
        width: 150,
        maxWidth: 150,
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
    { title: "Ngày sinh", field: "birthday", width: "150" },
    { title: "Giới tính", field: "gender", width: "150" },
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
    { title: "Dân tộc", field: "ethnicity", width: "150" },
    { title: "Quốc tịch", field: "nationality", width: "150" },
    { title: "TCTL", field: "allCredit", width: "150" },
    { title: "Thang điểm 10", field: "scoreTen", width: "150" },
    { title: "Thang điểm 4", field: "scoreFour", width: "150" },
    { title: "Xếp loại TN", field: "rankGraduating", width: "150" },
    {
      title: "Lớp học", field: "studentClass", align: "left", cellStyle: {
        width: 150,
        maxWidth: 150,
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
      title: "Hệ đào tạo", field: "trainingSystem", align: "left", cellStyle: {
        width: 150,
        maxWidth: 150,
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
      title: "Ngành học ", field: "majors", align: "left", cellStyle: {
        width: 300,
        maxWidth: 300,
        overflow: "hidden",
        textOverflow: "ellipsis",
      },
      headerStyle: {
        width: 300,
        maxWidth: 300,
        overflow: "hidden",
        textOverflow: "ellipsis",
        whiteSpace: "nowrap",
      },
    },
    { title: "Hình thức đào tạo", field: "typeTraining", align: "left", width: "150" },
    { title: "Trình độ đào tạo", field: "levelTraining", align: "left", width: "150" },
    { title: "Ngày nhập học", field: "dateJoin", align: "left", width: "150" },
    { title: "Ngày tốt nghiệp", field: "dateOut", align: "left", width: "150" },
    { title: "Số hiệu bằng", field: "numberDiploma", align: "left", width: "150" },
    { title: "Ghi chú", field: "note", align: "left", width: "150" },
    { title: "Ngôn ngữ đào tạo", field: "trainingLanguage", align: "left", width: "150" },

    { title: "File Upload", field: "fromUploadedFile", align: "left", width: "150" },

    ];

    return (
      <div className="m-sm-30">
        <div className="mb-sm-30">
          <Breadcrumb routeSegments={[{ name: "Phụ lục" }]} />
        </div>
        <Grid container spacing={3}>
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
                  className="w-100 mb-16 mr-16 align-bottom"
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
                  className="w-100 mr-16 align-bottom"
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
                  color="secondary"
                  onClick={() => this.setState({ shouldOpenUploadExcelDialog: true })}
                >
                  Upload Excel&nbsp;
            <Icon>backup</Icon>
                </Button>


                <Button
                  className="mt-16 mr-32 "
                  variant="contained"
                  color="primary"
                  disabled={this.state.loading}
                  onClick={() => this.setState({ shouldOpenDownloadDialog: true,item:null })}
                >
                  Xuất Excel&nbsp;
                      <Icon>get_app</Icon>



                </Button>

              </Grid>
            </Grid>
          </Grid>



        </Grid>
        <Card className="w-100 overflow-auto" elevation={6}>
          <MaterialTable
            title="Danh sách sinh viên"
            data={this.state.userList}
            columns={columns}
            //parentChildData={(row, rows) => rows.find(a => a.id === row.parentId)}
            parentChildData={(row, rows) => {
              var list = rows.find(a => a.id === row.parentId);
              return list;
            }}
            options={{
              selection: true,
              actionsColumnIndex: -1,
              paging: false,
              search: false
            }}
            components={{
              Toolbar: props => (
                <MTableToolbar {...props} />
              ),
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
                uploadUrl={ConstantList.API_ENPOINT + "/api/fileUpload/importThongTinPhuLuc"}
              />
            )}
          </div>

          <TablePagination
            className="px-16"
            rowsPerPageOptions={[5, 10, 25, 50, 100]}
            component="div"
            count={this.state.totalElements}
            rowsPerPage={this.state.rowsPerPage}
            page={this.state.page}
            backIconButtonProps={{
              "aria-label": "Previous Page"
            }}
            nextIconButtonProps={{
              "aria-label": "Next Page"
            }}
            onChangePage={this.handleChangePage}
            onChangeRowsPerPage={this.setRowsPerPage}
          />

          {shouldOpenDownloadDialog && (
            <DownloadDiaglog
              t={t}
              i18n={i18n}
              handleClose={this.handleDialogClose}
              open={shouldOpenDownloadDialog}
              handleOKEditClose={this.handleOKEditClose}
              onChangePage={this.handleChangePage}
              item={this.state?.item}
              handleDateChange={this.handleDateChange}
              majors={this.state.majors}
              studentClass={this.state.studentClass}
              listSign={this.state.listSign}
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

          {shouldOpenEditorDialog && (
            <StudentDialog
              t={t} i18n={i18n}
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
        </Card>
      </div>
    );
  }
}

export default StudentScores;
