import React, { Component } from "react";
import {
  Grid,
  IconButton,
  Icon,
  TablePagination,
  Button,
  TextField,
  FormControl,
  Input,InputAdornment,
} from "@material-ui/core";
import MaterialTable, { MTableToolbar, Chip, MTableBody, MTableHeader } from 'material-table';
import { getStockKeepingUnitByPage, deleteItem, getItemById, searchByPage,deleteCheckItem } from "./StockKeepingUnitService";
import StockKeepingUnitEditorDialog from "./StockKeepingUnitEditorDialog";
import { Breadcrumb, ConfirmationDialog } from "egret";
import { useTranslation, withTranslation, Trans } from 'react-i18next';
import shortid from "shortid";
import { saveAs } from 'file-saver';
import { Helmet } from 'react-helmet';
import { makeStyles, withStyles } from '@material-ui/core/styles'
import SearchIcon from '@material-ui/icons/Search';
import Tooltip from '@material-ui/core/Tooltip';
import { Link } from "react-router-dom";
import NotificationPopup from '../Component/NotificationPopup/NotificationPopup'

const LightTooltip = withStyles((theme) => ({
  tooltip: {
    backgroundColor: theme.palette.common.white,
    color: 'rgba(0, 0, 0, 0.87)',
    boxShadow: theme.shadows[1],
    fontSize: 11,
    marginLeft: '-1.5em'
    }  
}))(Tooltip);



function MaterialButton(props) {
  const { t, i18n } = useTranslation();
  const item = props.item;
  return <div className="none_wrap">
    <LightTooltip title={t('general.editIcon')} placement= "right-end" enterDelay={300} leaveDelay={200}
      PopperProps={{
        popperOptions: {modifiers: {offset: {enabled: true,offset: '10px, 0px',},},},
      }} >
      <IconButton onClick={() => props.onSelect(item, 0)}>
        <Icon color="primary">edit</Icon>
      </IconButton>
    </LightTooltip>
    <LightTooltip title={t('general.deleteIcon')} placement= "right-end" enterDelay={300} leaveDelay={200}
      PopperProps={{
        popperOptions: {modifiers: {offset: {enabled: true,offset: '10px, 0px',},},},
      }} >
      <IconButton onClick={() => props.onSelect(item, 1)}>
        <Icon color="error">delete</Icon>
      </IconButton>
    </LightTooltip>
  </div>;
}

class StockKeepingUnitTable extends React.Component {
  state = {
    keyword: '',
    rowsPerPage: 10,
    page: 0,
    StockKeepingUnit: [],
    item: {},
    shouldOpenEditorDialog: false,
    shouldOpenConfirmationDialog: false,
    selectAllItem: false,
    selectedList: [],
    totalElements: 0,
    shouldOpenConfirmationDeleteAllDialog: false,
    shouldOpenConfirmationDeleteListDialog: false,
    shouldOpenNotificationPopup: false
  };
  numSelected = 0;
  rowCount = 0;

  handleTextChange = event => {
    this.setState({ keyword: event.target.value }, function () {
    })
  };

  handleKeyDownEnterSearch = e => {
    if (e.key === 'Enter') {
      this.search();
    }
  };

  setPage = page => {
    this.setState({ page }, function () {
      this.updatePageData();
    })
  };

  setRowsPerPage = event => {
    this.setState({ rowsPerPage: event.target.value, page: 0 }, function () {
      this.updatePageData();
    });
  };

  handleChangePage = (event, newPage) => {
    this.setPage(newPage);
  };

  search() {
    this.setState({ page: 0 }, function () {
      var searchObject = {};
      searchObject.keyword = this.state.keyword;
      searchByPage(searchObject, this.state.page + 1, this.state.rowsPerPage).then(({ data }) => {
        this.setState({ itemList: [...data.content], totalElements: data.totalElements })
      });
    });
  }
  checkData = () => {
    if(!this.data || this.data.length === 0) {
      this.setState({shouldOpenNotificationPopup: true,
        Notification:"general.noti_check_data"})
      // alert("Chưa chọn dữ liệu");
    } else if(this.data.length === this.state.itemList.length) {
      // alert("Bạn có muốn xoá toàn bộ");
      
      this.setState({ shouldOpenConfirmationDeleteAllDialog: true }) 

    } else {     
      this.setState({ shouldOpenConfirmationDeleteListDialog: true }) 
      
    }
  }

  updatePageData = () => {
    getStockKeepingUnitByPage(this.state.page + 1, this.state.rowsPerPage).then(({ data }) => {
      this.setState({ itemList: [...data.content], totalElements: data.totalElements })
    }
    );
  };

  handleDownload = () => {
    var blob = new Blob(["Hello, world!"], { type: "text/plain;charset=utf-8" });
    saveAs(blob, "hello world.txt");
  }
  handleDialogClose = () => {
    this.setState({
      shouldOpenEditorDialog: false,
      shouldOpenConfirmationDialog: false,
      shouldOpenConfirmationDeleteAllDialog: false,
     shouldOpenConfirmationDeleteListDialog: false,
     shouldOpenNotificationPopup: false
    });
  };

  handleOKEditClose = () => {
    this.setState({
      shouldOpenEditorDialog: false,
      shouldOpenConfirmationDialog: false
    });
    this.updatePageData();
  };

  handleDeleteStockKeepingUnit = id => {
    this.setState({
      id,
      shouldOpenConfirmationDialog: true
    });
  };

  handleEditStockKeepingUnit = item => {
    getItemById(item.id).then((result) => {
      this.setState({
        item: result.data,
        shouldOpenEditorDialog: true
      });
    });
  };

  handleConfirmationResponse = () => {
    if(this.state.itemList.length === 1) {
      let count = this.state.page -1;
      this.setState({
        page : count
      })
    }
    deleteCheckItem(this.state.id).then((res) => {
      this.updatePageData();
      this.handleDialogClose()
  }).catch((err) =>{
    this.setState({shouldOpenNotificationPopup: true,
      Notification:"StockKeepingUnit.noti.use"})
    // alert('Đơn vị tính đang sử dụng, không thể xóa')
    // this.handleDialogClose()
  })
  };

  componentDidMount() {
    this.updatePageData();
  }

  handleEditItem = item => {
    this.setState({
      item: item,
      shouldOpenEditorDialog: true
    });
  };

  handleClick = (event, item) => {
    let { StockKeepingUnit } = this.state;
    if (item.checked == null) {
      item.checked = true;
    } else {
      item.checked = !item.checked;
    }
    var selectAllItem = true;
    for (var i = 0; i < StockKeepingUnit.length; i++) {
      if (StockKeepingUnit[i].checked == null || StockKeepingUnit[i].checked == false) {
        selectAllItem = false;
      }
      if (StockKeepingUnit[i].id == item.id) {
        StockKeepingUnit[i] = item;
      }
    }
    this.setState({ selectAllItem: selectAllItem, StockKeepingUnit: StockKeepingUnit });

  };
  handleSelectAllClick = (event) => {
    let { StockKeepingUnit } = this.state;
    for (var i = 0; i < StockKeepingUnit.length; i++) {
      StockKeepingUnit[i].checked = !this.state.selectAllItem;
    }
    this.setState({ selectAllItem: !this.state.selectAllItem, StockKeepingUnit: StockKeepingUnit });
  };

  handleDelete = id => {
    this.setState({
      id,
      shouldOpenConfirmationDialog: true
    });
  };

  async handleDeleteList(list) {
    let listAlert =[];
    for (var i = 0; i < list.length; i++) {

      try {
        await deleteCheckItem(list[i].id);
      } catch (error) {
        listAlert.push(list[i].name);
      }    
    }
    this.handleDialogClose()
    if(listAlert.length === list.length) {
      this.setState({shouldOpenNotificationPopup: true,
        Notification:"StockKeepingUnit.noti.use_all"})
      // alert("Đơn vị tính đều đã sử dụng");
    } else if(listAlert.length >0) {
      this.setState({shouldOpenNotificationPopup: true,
        Notification:"StockKeepingUnit.noti.deleted_unused"})
      // alert("Đã xoá những đơn vị tính chưa sử dụng");
    }
  }

  handleDeleteAll = (event) => {
    //alert(this.data.length);
    this.handleDeleteList(this.data).then(() => {
      this.updatePageData();
      // this.handleDialogClose();
      this.data = null
    }
    );
  };

  render() {
    const { t, i18n } = this.props;
    let {
      keyword,
      rowsPerPage,
      page,
      totalElements,
      itemList,
      item,
      shouldOpenConfirmationDialog,
      shouldOpenEditorDialog,
      shouldOpenConfirmationDeleteAllDialog,
      shouldOpenConfirmationDeleteListDialog ,
      shouldOpenNotificationPopup     
    } = this.state;
    let TitlePage = t("StockKeepingUnit.title");

    let columns = [
      { title: t("StockKeepingUnit.code"), field: "code", width: "150" },
      { title: t("StockKeepingUnit.name"), field: "name", align: "left", width: "150" },
      { title: t("StockKeepingUnit.coefficient"), field: "coefficient", align: "left", width: "150" },
      {
        title: t("general.action"),
        field: "custom",
        align: "left",
        width: "250",
        render: rowData => <MaterialButton item={rowData}
          onSelect={(rowData, method) => {
            if (method === 0) {
              getItemById(rowData.id).then(({ data }) => {
                if (data === null) {
                  data = {};
                }
                this.setState({
                  item: data,
                  shouldOpenEditorDialog: true
                });
              })
            } else if (method === 1) {
              this.handleDelete(rowData.id);
            } else {
              alert('Call Selected Here:' + rowData.id);
            }
          }}
        />
      },
    ];

    return (
      <div className="m-sm-30">
        <Helmet>
          <title>{TitlePage} | {t('web_site')}</title>
        </Helmet>
        <div className="mb-sm-30">
          {/* <Breadcrumb routeSegments={[{ name: t('StockKeepingUnit.title') }]} />
           */}
           
           <Breadcrumb routeSegments={[
            { name: t("Dashboard.category"),path: "/list/stock_keeping_unit" },
             { name: TitlePage }]} />
        </div>

        <Grid container spacing={2} justify="space-between">
        <Grid item md={3} xs={12} >
            <Button
              className="mb-16 mr-16 align-bottom"
              variant="contained"
              color="primary"
              onClick={() => {
                this.handleEditItem({ startDate: new Date(), endDate: new Date() });
              }
              }
            >
              {t('general.add')}
            </Button>
            <Button className="mb-16 mr-36 align-bottom" variant="contained" color="primary"
              // onClick={() => this.setState({ shouldOpenConfirmationDeleteAllDialog: true })}
              onClick={()=> this.checkData()}>
              {t('general.delete')}
            </Button>

            {shouldOpenConfirmationDeleteAllDialog && (
              <ConfirmationDialog
                open={shouldOpenConfirmationDeleteAllDialog}
                onConfirmDialogClose={this.handleDialogClose}
                onYesClick={this.handleDeleteAll}
                text={t('general.deleteAllConfirm')}
                agree={t('general.agree')}
                cancel={t('general.cancel')}
              />
            )}
            {shouldOpenNotificationPopup && (
              <NotificationPopup
                title={t('general.noti')}
                open={shouldOpenNotificationPopup}
                // onConfirmDialogClose={this.handleDialogClose}
                onYesClick={this.handleDialogClose}
                text={t(this.state.Notification)}
                agree={t('general.agree')}
              />
            )}  
            {this.state.shouldOpenConfirmationDeleteListDialog && (
              <ConfirmationDialog
                open={this.state.shouldOpenConfirmationDeleteListDialog}
                onConfirmDialogClose={this.handleDialogClose}
                onYesClick={this.handleDeleteAll}
                text={t('general.deleteConfirm')}
                agree={t('general.agree')}
                cancel={t('general.cancel')}
              />
            )}
            {/* <TextField
              label={t('StockKeepingUnit.filter')}
              className="mb-16 mr-10"
              style={{width:"40%"}}
              type="text"
              name="keyword"
              value={keyword}
              onKeyDown={this.handleKeyDownEnterSearch}
              onChange={this.handleTextChange} />
            <Button
              className="mb-16 mr-16 align-bottom"
              variant="contained"
              color="primary" onClick={() => this.search(keyword)}>
              {t('general.search')}
            </Button> */}
            </Grid>
            <Grid item md={6} sm={12} xs={12} >
            <FormControl fullWidth>
                <Input
                    className='search_box w-100'
                    onChange={this.handleTextChange}
                    onKeyDown={this.handleKeyDownEnterSearch}
                    placeholder={t("StockKeepingUnit.filter")}
                    id="search_box"
                    startAdornment={
                        <InputAdornment style ={{marginRight:"2%"}}>
                             <Link> <SearchIcon 
                            onClick={() => this.search(keyword)}
                            style ={{position:"absolute",
                            top:"0",
                            right:"0"
                          }} /></Link>
                        </InputAdornment>
                    }
                />
          </FormControl>
          </Grid>
          <Grid item xs={12}>
            <div>
              {shouldOpenEditorDialog && (
                <StockKeepingUnitEditorDialog t={t} i18n={i18n}
                  handleClose={this.handleDialogClose}
                  open={shouldOpenEditorDialog}
                  handleOKEditClose={this.handleOKEditClose}
                  item={item}
                />
              )}

              {shouldOpenConfirmationDialog && (
                <ConfirmationDialog
                  title={t("general.confirm")}
                  open={shouldOpenConfirmationDialog}
                  onConfirmDialogClose={this.handleDialogClose}
                  onYesClick={this.handleConfirmationResponse}
                  text={t('general.deleteConfirm')}
                  agree={t('general.agree')}
                  cancel={t('general.cancel')}
                />
              )}
            </div>
            <MaterialTable
              title={t('general.list')}
              data={itemList}
              columns={columns}
              //parentChildData={(row, rows) => rows.find(a => a.id === row.parentId)}
              parentChildData={(row, rows) => {
                var list = rows.find(a => a.id === row.parentId);
                return list;
              }}
              localization={{
                body: {
                  emptyDataSourceMessage: `${t('general.emptyDataMessageTable')}`
                }
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
            <TablePagination
              align="left"
              className="px-16"
              rowsPerPageOptions={[1, 2, 3, 5, 10, 25, 50, 100]}
              component="div"
              count={totalElements}
              rowsPerPage={rowsPerPage}
              labelRowsPerPage={t('general.rows_per_page')}

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
      </div>
    );
  }
}

export default StockKeepingUnitTable;
