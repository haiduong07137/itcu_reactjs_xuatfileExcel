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
  Grid
} from "@material-ui/core";
import { searchByPage, searchScoresStudentByPage, getExcel,getExcelStatus } from "./StudentScoresDetailService";
import DateEditorDialog from "./DateEditorDialog";

import MaterialTable, { MTableToolbar, Chip, MTableBody, MTableHeader } from 'material-table';
import { Breadcrumb, ConfirmationDialog } from "egret";
import DownloadDiaglog from './DownloadDiaglog'
import ConstantList from "../../appConfig";
import shortid from "shortid";
import UploadFormPopup from '../Component/UploadFormPopup/UploadFormPopup'
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
import { result } from "lodash";
function MaterialButton(props) {
  const item = props.item
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
    </React.Fragment>
  )

}



class StudentScoresDetail extends Component {
  state = {
    rowsPerPage: 100,
    page: 0,
    keyword: '',
    userList: [],
    totalElements: 0,
    shouldOpenEditorDialog: false,
    shouldOpenConfirmationDialog: false,
    shouldOpenUploadExcelDialog: false,
    studentCode: '', studentId: "", date: new Date(),shouldOpenDownloadDialog:false
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
      shouldOpenDownloadDialog:false,
      shouldOpenShowStatusDialog:false
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
    // deleteUser(this.state.user).then(() => {
    //   this.handleDialogClose();
    // });
  };



  search() {
    this.setState({ page: 0 }, function () {
      var searchObject = {}
      searchObject.keyword = this.state.keyword
      searchObject.pageIndex = this.state.page + 1
      searchObject.pageSize = this.state.rowsPerPage
      searchObject.code = this.state.studentCode
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

  componentDidMount() {
    if (this.props.history.location.state) {
      this.state.studentCode = this.props.history.location.state.studentCode
      this.state.studentId = this.props.history.location.state.studentId
      this.updatePageData()
    }
  }


  getFileExcel = () => {
   

    var studentDto = {}
    studentDto.id = this.state.studentId
    studentDto.dateExport = moment(this.state.date).format('DD/MM/YYYY');
  
    getExcel(studentDto).then((result) => {
      const url = window.URL.createObjectURL(new Blob([result.data]))
      const link = document.createElement('a')
      link.href = url
      link.setAttribute('download', 'Bang_Diem_Moi.xls')
      document.body.appendChild(link)
      link.click()
    })
  }

  updatePageData = () => {
    var searchObject = {}
    searchObject.keyword = this.state.keyword
    searchObject.pageIndex = this.state.page + 1
    searchObject.pageSize = this.state.rowsPerPage
    searchObject.code = this.state.studentCode
    searchByPage(searchObject).then(({ data }) => {
      this.setState({ userList: [...data.content], totalElements: data.totalElements })
    })
  };

  handleDateChange(date) {
    this.setState({ date: date })
  }

  render() {
    const { t, i18n } = this.props;
    let {
      rowsPerPage,
      page,
      userList,
      shouldOpenConfirmationDialog,
      shouldOpenEditorDialog,
      shouldOpenUploadExcelDialog,
      shouldOpenDownloadDialog,
      keyword, date
    } = this.state;

    let columns = [
       { title: "STT", field: "moduleOrder", align: "left", width: "150" },
      { title: "Tên môn học", field: "module", align: "left", width: "150" },
      { title: "Số tín chỉ", field: "credit", width: "150" },
      { title: "Điểm số", field: "score", align: "left", width: "150" },
      { title: "Điểm chữ", field: "mark", width: "150" }
      // {
      //   title: t("Action"),
      //   field: "custom",
      //   align: "left",
      //   width: "250",
      //   render: rowData => <MaterialButton item={rowData}
      //     onSelect={(rowData, method) => {
      //       if (method === 0) {
      //         this.props.history.push({
      //           pathname: ConstantList.ROOT_PATH + 'scores-detail',
      //           state: {
      //             rowsPerPage: 10,
      //             page: 0,
      //             keyword: rowData.code, 
      //           },
      //         })
      //       } else if (method === 1) {
      //         // this.handleDelete(rowData.id);
      //       } else {
      //         alert('Call Selected Here:' + rowData.id);
      //       }
      //     }}
      //   />
      // },
    ];

    return (
      <div className="m-sm-30">
        <div className="mb-sm-30">
          <Breadcrumb routeSegments={[{ name: "Bảng điểm" }]} />
        </div>
        <Grid container spacing={3}>
          <Grid item xs={12}>
            <Button
              className="mb-16 mr-16 ml-16 align-bottom"
              variant="contained"
              color="secondary"
              onClick={() => this.props.history.push({
                pathname: ConstantList.ROOT_PATH + 'student-scores'
              })}
            >
              <Icon>arrow_back</Icon>
             Trở lại&nbsp;
          </Button>
            <TextField
              label="Tìm theo tên môn"
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
              Tìm kiếm&nbsp;
              <Icon>search</Icon>
            </Button>
           

            <Button
              style={{ marginRight: '30px' }}
              className="mb-16 mr-36 align-bottom"
              variant="contained"
              color="primary"
              onClick={() => this.setState({shouldOpenDownloadDialog:true})}
            >
              Xuất Excel&nbsp;
              <Icon>get_app</Icon>
            </Button>
          </Grid>
        </Grid>
        <Card className="w-100 overflow-auto" elevation={6}>
          <MaterialTable
            title={"Danh sách điểm của sinh viên: " + this.props.history.location.state.studentName}
            data={this.state.userList}
            columns={columns}
            //parentChildData={(row, rows) => rows.find(a => a.id === row.parentId)}
            parentChildData={(row, rows) => {
              var list = rows.find(a => a.id === row.parentId);
              return list;
            }}
            options={{
              selection: false,
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
              // this.setState({selectedItems:rows});
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
          <div>
            {shouldOpenUploadExcelDialog && (
              <UploadFormPopup
                t={t}
                i18n={i18n}
                handleClose={this.handleDialogClose}
                open={shouldOpenUploadExcelDialog}
                handleOKEditClose={this.handleOKEditClose}
                acceptType="xls;xlsx"
                uploadUrl={ConstantList.API_ENPOINT + "/api/fileUpload/importStudentTranscript"}
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
              "aria-label": "Previous Page"
            }}
            nextIconButtonProps={{
              "aria-label": "Next Page"
            }}
            onChangePage={this.handleChangePage}
            onChangeRowsPerPage={this.setRowsPerPage}
          />

          {shouldOpenEditorDialog && (
            <DateEditorDialog
              handleClose={this.handleDialogClose}
              open={shouldOpenEditorDialog}
              studentId={this.state.studentId}
            />
          )}

          {shouldOpenDownloadDialog && (
            <DownloadDiaglog
              t={t}
              i18n={i18n}
              handleClose={this.handleDialogClose}
              open={shouldOpenDownloadDialog}
              handleOKEditClose={this.handleOKEditClose}
              onChangePage={this.handleChangePage}
              id={this.state.studentId}
              handleDateChange={this.handleDateChange} 
              code={this.state.studentCode}
            />
          )}

          {shouldOpenConfirmationDialog && (
            <ConfirmationDialog
              open={shouldOpenConfirmationDialog}
              onConfirmDialogClose={this.handleDialogClose}
              onYesClick={this.handleConfirmationResponse}
              text="Are you sure to delete?"
            />
          )}
        </Card>
      </div >
    );
  }
}

export default StudentScoresDetail;
