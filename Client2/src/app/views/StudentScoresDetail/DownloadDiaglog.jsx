import React, { Component } from "react";
import ConstantList from "../../appConfig";
import {
  IconButton,
  Dialog,
  Button,
  Icon,
  Grid,
  FormControlLabel,
  TablePagination,
  Switch,
  DialogActions,
  Checkbox, TextField
} from "@material-ui/core";
import { ValidatorForm, TextValidator } from "react-material-ui-form-validator";
import FormControl from "@material-ui/core/FormControl";
import InputLabel from "@material-ui/core/InputLabel";
import Select from "@material-ui/core/Select";
import MenuItem from "@material-ui/core/MenuItem";
import Autocomplete from '@material-ui/lab/Autocomplete';
import DialogTitle from '@material-ui/core/DialogTitle';
import DialogContent from '@material-ui/core/DialogContent';
import Draggable from 'react-draggable';
import Paper from '@material-ui/core/Paper';
import MaterialTable, { MTableToolbar, Chip, MTableBody, MTableHeader } from 'material-table';
import { useTranslation, withTranslation, Trans } from 'react-i18next';
import { SimpleCard, EgretProgressBar, ShowDialog } from "egret";
import Person from '@material-ui/icons/Person';
import {
  searchByPage,
  deleteSelected,
  deleteAll,
  getItemById
} from "../Signature/SignatureService";
import { Breadcrumb, ConfirmationDialog } from 'egret'

import DateFnsUtils from "@date-io/date-fns"
import {
  MuiPickersUtilsProvider,
  KeyboardTimePicker,
  KeyboardDatePicker
} from "@material-ui/pickers";

import moment from "moment";

import { getExcel, getExcelStatus } from "./StudentScoresDetailService";
function PaperComponent(props) {
  return (
    <Draggable handle="#draggable-dialog-title" cancel={'[class*="MuiDialogContent-root"]'}>
      <Paper {...props} />
    </Draggable>
  );
}
function MaterialButton(props) {
  const { t, i18n } = useTranslation();
  const item = props.item;
  return <div>
    <IconButton title={t('Agency.assign')} onClick={() => props.onSelect(item, 0)}>
      <Icon style={{ color: 'red', }} color="primary">get_app</Icon>
    </IconButton>



  </div>;
}
class BDSListDiaglog extends Component {
  state = {
    name: "",
    code: "",
    listCategory: [],
    category: [],
    description: "",
    totalElements: 0,
    rowsPerPage: 10,
    page: 0,
    signatureId: "",
    keyword: '',
    shouldOpenConfirmationAssign: false,
    shouldOpenShowStatusDialog: false,
    agencyId: '',
    BDStaffId: '',
    date: new Date(),
    listSign: [],
    lineOne: "", lineTwo: "", lineThree: "", signName: "", isFormal: false
  };


  handleChange = (event, source) => {
    event.persist();
    if (source === "switch") {
      this.setState({ isActive: event.target.checked });
      return;
    }
    this.setState({
      [event.target.name]: event.target.value
    });
  };

  // handleFormSubmit = () => {
  //   let { id } = this.state;
  //   if (id) {
  //     updateData({
  //       ...this.state
  //     }).then(() => {
  //       this.props.handleOKEditClose1();
  //     });
  //   } else {
  //     addNewData({
  //       ...this.state
  //     }).then(() => {
  //       this.props.handleOKEditClose1();
  //     });
  //   }
  // };

  selectCategory = (categorySelected) => {
    this.setState({ category: categorySelected }, function () {
    });
  }

  setPage = (page) => {
    this.setState({ page }, function () {
      this.updatePageData()
    })
  }

  setRowsPerPage = (event) => {
    this.setState({ rowsPerPage: event.target.value, page: 0 }, function () {
      this.updatePageData();
    })
  }

  handleChangePage = (event, newPage) => {
    this.setPage(newPage)
  }


  handleTextChange = (event) => {
    this.setState({ [event.target.name]: event.target.value }, function () { })
  }

  handleChangeFormal = (event) => {
    this.setState({ ...this.state, [event.target.name]: event.target.checked } );
  }

  handleDialogClose = () => {
    this.setState(
      {
        shouldOpenConfirmationAssign: false,
      },
    )
  }


  updatePageData = () => {
    var searchObject = {};
    searchObject.keyword = this.state.keyword;
    searchObject.pageIndex = this.state.page + 1;
    searchObject.pageSize = this.state.rowsPerPage;
    searchByPage(searchObject).then(({ data }) => {
      this.setState({
        listSign: [...data.content]
      })
    });
  };


  exportExcel = () => {

    this.setState({ loading: true })
    var searchObject = {};
    searchObject.id = this.props.id
    searchObject.dateExport = moment(this.state.date).format('DD/MM/YYYY');
    searchObject.lineOne = this.state.lineOne
    searchObject.lineTwo = this.state.lineTwo
    searchObject.lineThree = this.state.lineThree
    searchObject.signName = this.state.signName
    searchObject.isFormal = this.state.isFormal
    getExcel(searchObject).then((result) => {
      const url = window.URL.createObjectURL(new Blob([result.data]))
      const link = document.createElement('a')
      link.href = url
      link.setAttribute('download', this.props.code + ".xls")
      document.body.appendChild(link)
      link.click();
      this.setState({ loading: false })
    })
    this.props.handleClose()
  }

  getFileExcel = () => {
    this.setState({ loading: true })
    var searchObject = {}
    searchObject.id = this.props.id
    searchObject.dateExport = moment(this.state.date).format('DD/MM/YYYY');
    searchObject.signatureId = this.state.signatureId
    searchObject.lineOne = this.state.lineOne
    searchObject.lineTwo = this.state.lineTwo
    searchObject.lineThree = this.state.lineThree
    searchObject.signName = this.state.signName
    searchObject.isFormal = this.state.isFormal
    getExcelStatus(searchObject).then((result) => {
      if (result.data?.logDetail?.length > 0) {
        this.state.description = result.data.logDetail;
        this.setState({
          shouldOpenShowStatusDialog: true, loading: false

        })
      } else {
        this.exportExcel()
      }
    })
  }




  search() {
    this.setState({ page: 0 }, function () {
      var searchObject = {}
      searchObject.keyword = this.state.keyword
      searchObject.pageIndex = this.state.page + 1
      searchObject.pageSize = this.state.rowsPerPage
      searchByPage(searchObject).then(({ data }) => {
        this.setState({
          itemList: [...data.content],
          totalElements: data.totalElements,
        });
      });
    })
  }

  componentWillMount() {
    this.setState({
      ...this.props.item,
    })
    // this.updatePageData();
    this.setState({
      listSign: [
        {
          "lineOne": "TL. HIỆU TRƯỞNG", "lineTwo": "KT.TRƯỞNG PHÒNG ĐÀO TẠO",
          "lineThree": "PHÓ TRƯỞNG PHÒNG", "signName": "TS. Nguyễn Đình Dũng"
        },
        {
          "lineOne": "KT. HIỆU TRƯỞNG", "lineTwo": "PHÓ HIỆU TRƯỞNG",
          "lineThree": "", "signName": "TS. Đỗ Đình Cường"
        },
        {
          "lineOne": "HIỆU TRƯỞNG", "lineTwo": "",
          "lineThree": "", "signName": "GS. TS. Nguyễn Văn A"
        },
        {
          "lineOne": "TL. HIỆU TRƯỞNG", "lineTwo": "TRƯỞNG PHÒNG ĐÀO TẠO",
          "lineThree": "", "signName": "PGS. TS. Phùng Trung Nghĩa"
        }
      ]
    })
  }

  handleDateChange(date) {
    this.setState({ date: date })
  }


  componentDidMount() {

  }

  render() {
    let { shouldOpenConfirmationAssign, date, shouldOpenShowStatusDialog, lineFour,
      lineOne, lineThree, lineTwo, signatureId, signName, loading, isFormal
    } = this.state;
    let { open, handleClose, handleOKEditClose, t, i18n } = this.props;
    // let columns = [
    //   { title: "TL Hiệu trưởng", field: "signPrincipal", align: "left", width: "150" },
    //   { title: "Tên phòng ban TL", field: "departName", width: "150" },
    //   { title: "Tên người TL", field: "signName", align: "left", width: "150" },
    //   {
    //     title: t("Action"),
    //     field: "custom",
    //     align: "left",
    //     width: "250",
    //     render: rowData => <MaterialButton item={rowData}
    //       onSelect={(rowData, method) => {
    //         if (method === 0) {
    //           this.setState({ signatureId: rowData.id }, function () {
    //             this.getFileExcel()
    //           })
    //         } else if (method === 1) {
    //           // this.handleDelete(rowData.id);
    //         } else {
    //           alert('Call Selected Here:' + rowData.id);
    //         }
    //       }}
    //     />
    //   },
    // ];
    return (
      <Dialog open={open} onClose={handleClose} PaperComponent={PaperComponent} maxWidth="xs" fullWidth={false}>
        <DialogTitle style={{ cursor: 'move' }} id="draggable-dialog-title">
          Cấu hình chữ ký
        </DialogTitle>
        <DialogContent>
          <Grid className="mb-10" container spacing={3}>
            <Grid item md={12} sm={12} xs={12}>
              <MuiPickersUtilsProvider utils={DateFnsUtils}>
                <KeyboardDatePicker
                  className=" w-100 mb-16 mr-20"
                  id="mui-pickers-date"
                  label="Ngày xuất excel"
                  value={date}
                  format="dd/MM/yyyy"
                  type="text"
                  name="date"
                  onChange={date => this.handleDateChange(date, "date")}
                  KeyboardButtonProps={{
                    "aria-label": "change date"
                  }}
                />
              </MuiPickersUtilsProvider>
              <Grid item md={12} sm={12} xs={12}>
                <Autocomplete
                  className=" w-100 mb-16 mr-20"
                  options={this.state?.listSign}
                  style={{ width: '100%' }}
                  onChange={(event, value) => {
                    if (value == null) {
                      this.setState({
                        signName: "",
                        lineOne: "",
                        lineTwo: "",
                        lineThree: ""
                      })

                    } else {
                      this.setState({
                        signName: value?.signName !== null ? value.signName : "",
                        lineOne: value?.lineOne !== null ? value.lineOne : "",
                        lineTwo: value?.lineTwo !== null ? value.lineTwo : "",
                        lineThree: value?.lineThree !== null ? value.lineThree : ""
                      })
                    }
                  }}
                  getOptionLabel={(option) => `${option.signName}  `}
                  renderInput={(params) => (
                    <TextField
                      {...params}
                      label="Người ký"
                      variant="outlined"
                    />
                  )}
                />


              </Grid>

              <Grid item md={12} sm={12} xs={12}>

                <TextField
                  label="Dòng 1"
                  className=" w-100 mb-16 mr-20"
                  type="text"
                  name="lineOne"
                  InputLabelProps={{ shrink: true }}
                  value={lineOne}
                  onKeyDown={this.handleKeyDownEnterSearch}
                  onChange={this.handleTextChange}
                />  </Grid>
              <Grid item md={12} sm={12} xs={12}>
                <TextField
                  label="Dòng 2"
                  className="w-100 mb-16 mr-20"
                  type="text"
                  name="lineTwo"
                  InputLabelProps={{ shrink: true }}
                  value={lineTwo}
                  onKeyDown={this.handleKeyDownEnterSearch}
                  onChange={this.handleTextChange}
                /></Grid>
              <Grid item md={12} sm={12} xs={12}>
                <TextField
                  label="Dòng 3"
                  className="w-100 mb-16 mr-20"
                  type="text"
                  name="lineThree"
                  InputLabelProps={{ shrink: true }}
                  value={lineThree}
                  onKeyDown={this.handleKeyDownEnterSearch}
                  onChange={this.handleTextChange}
                /></Grid>
              <Grid item md={12} sm={12} xs={12}>
                <TextField
                  label="Người ký"
                  className="w-100 mb-16 mr-20"
                  type="text"
                  InputLabelProps={{ shrink: true }}
                  name="signName"
                  value={signName}
                  onKeyDown={this.handleKeyDownEnterSearch}
                  onChange={this.handleTextChange}
                />
              </Grid>
              <Grid item md={12} sm={12} xs={12}>
                <FormControlLabel
                  control={<Checkbox checked={isFormal} onChange={this.handleChangeFormal} name="isFormal" />}
                  label="Khác chính quy"
                />
              </Grid>



            </Grid>
          </Grid>
        </DialogContent>
        <DialogActions>
          <div className="flex flex-space-between flex-middle mt-36">

            <Button
              variant="contained"
              color="primary"
              className="mr-36"
              disabled={loading}
              onClick={() => this.getFileExcel()}>
              Tải xuống
              </Button>
            <Button
              variant="contained"
              color="secondary"
              className="mr-36"
              onClick={() => this.props.handleClose()}>
              {t('general.cancel')}
            </Button>

          </div>
        </DialogActions>
        {shouldOpenConfirmationAssign && (
          <ConfirmationDialog
            open={shouldOpenConfirmationAssign}
            onConfirmDialogClose={handleClose}
            onYesClick={this.exportExcel}
            loading={loading}
            text="Bạn có muốn tải về không"
            agree={t('general.agree')}
            cancel={t('general.cancel')}
          />
        )}

        {shouldOpenShowStatusDialog && (
          <ShowDialog
            title="Error"
            open={shouldOpenShowStatusDialog}
            onConfirmDialogClose={() => this.setState({ shouldOpenConfirmationAssign: true })}
            text={this.state.description}
            cancel={'OK'}
          />
        )}
      </Dialog >
    )
  }
}

export default BDSListDiaglog;