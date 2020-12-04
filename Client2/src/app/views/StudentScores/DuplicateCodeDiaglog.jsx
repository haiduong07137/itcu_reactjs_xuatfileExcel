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
import Person from '@material-ui/icons/Person';

import { searchByPage, getExcel, getListMajorClass,getListDuplicateCode } from "./StudentScoresService";
import { Breadcrumb, ConfirmationDialog } from 'egret'
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
      <Icon style={{ color: 'red', }} color="primary">delete</Icon>
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
    itemList: [],
    keyword: '',
    shouldOpenConfirmationAssign: false,
    agencyId: '',
    BDStaffId: '',
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
    this.setState({ keyword: event.target.value }, function () { })
  }

  handleDialogClose = () => {
    this.setState(
      {
        shouldOpenConfirmationAssign: false,
      },
      () => {
        this.updatePageData()
      }
    )
    this.props.handleOKEditClose()
  }


  updatePageData = () => {
    var searchObject = {};
    searchObject.keyword = this.state.keyword;
    searchObject.pageIndex = this.state.page + 1;
    searchObject.pageSize = this.state.rowsPerPage; 
    getListDuplicateCode(searchObject).then(({ data }) => {
      this.setState({ itemList: [...data.content], totalElements: data.totalElements });
    });
  };

   

  search() {
    this.setState({ page: 0 }, function () {
      var searchObject = {}
      searchObject.keyword = this.state.keyword
      searchObject.pageIndex = this.state.page + 1
      searchObject.pageSize = this.state.rowsPerPage
      getListDuplicateCode(searchObject).then(({ data }) => {
        this.setState({ itemList: [...data.content], totalElements: data.totalElements });
      })
    })
  }

  componentWillMount() { 
  }

  componentDidMount() { 
    this.updatePageData();
  }

  render() {
    let {
      keyword,
      rowsPerPage,
      page,
      totalElements,
      itemList, shouldOpenConfirmationAssign
    } = this.state;
    let { open, handleClose, handleOKEditClose1, t, i18n } = this.props;
    let columns = [
      { title: "Mã sinh viên", field: "code", align: "left", width: "150" },
      { title: "Tên sinh viên", field: "displayName", width: "150" },
      { title: "Lớp học", field: "studentClass", align: "left", width: "150" },
      { title: "Ngành học", field: "majors", align: "left", width: "150" },
      { title: "File Upload", field: "fromUploadedFile", align: "left", width: "150" },
      { title: "Khoá học", field: "course", width: "150" },
      { title: "Nơi sinh", field: "placeOfBirth", align: "left", width: "150" },
      {
        title: t("Action"),
        field: "custom",
        align: "left",
        width: "250",
        render: rowData => <MaterialButton item={rowData}
          onSelect={(rowData, method) => {
            if (method === 0) {
              this.props.history.push({
                pathname: ConstantList.ROOT_PATH + 'scores-detail',
                state: {
                  rowsPerPage: 10,
                  page: 0,
                  keyword: '',
                  studentCode: rowData.code,
                  studentName: rowData.displayName,
                  studentId: rowData.id
                },
              })
            } else if (method === 1) {
              // this.handleDelete(rowData.id);
            } else {
              alert('Call Selected Here:' + rowData.id);
            }
          }}
        />
      },
    ];
    return (
      <Dialog open={open} PaperComponent={PaperComponent} maxWidth="md" fullWidth={false}> 
          <DialogTitle style={{ cursor: 'move' }} id="draggable-dialog-title"> 
          </DialogTitle>
          <DialogContent>
            <Grid className="mb-10" container spacing={3}>
              <TextField
                label={t('general.enterSearch')}
                className="mb-16 mr-10 ml-10"
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
                {t('general.search')}

                {this.state.shouldOpenConfirmationAssign && (
                  <ConfirmationDialog
                    open={this.state.shouldOpenConfirmationAssign}
                    onConfirmDialogClose={this.handleDialogClose}
                    onYesClick={this.handleAssign}
                    text={t('general.sure_to_assign_1')+this.state.agencyName+t('general.sure_to_assign_2')+this.state.BDStaffName}
                    agree={t('general.agree')}
                    cancel={t('general.cancel')}
                  />
                )}

              </Button>
              <Grid item md={12} sm={12} xs={12}>
                <MaterialTable
                  title="Danh sách trùng mã sinh viên"
                  data={itemList}
                  columns={columns}
                  parentChildData={(row, rows) => rows.find(a => a.id === row.parentId)}
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

                  }}

                />
                <TablePagination
                  align="left"
                  className="px-16"
                  rowsPerPageOptions={[1, 2, 3, 5, 10, 25, 50, 100]}
                  component="div"
                  count={totalElements}
                  rowsPerPage={rowsPerPage}
                  page={page}
                  backIconButtonProps={{
                    "aria-label": "Previous Page"
                  }}
                  nextIconButtonProps={{
                    "aria-label": "Next Page"
                  }}
                  onChangePage={this.handleChangePage}
                  onChangeRowsPerPage={this.setRowsPerPage}
                />
              </Grid>
            </Grid>
          </DialogContent>
          <DialogActions>
            <div className="flex flex-space-between flex-middle mt-36">
              <Button variant="contained" color="secondary" className="mr-36" onClick={() => this.props.handleClose()}>{t('general.cancel')}</Button>

            </div>
          </DialogActions>
        
      </Dialog >
    )
  }
}

export default BDSListDiaglog;