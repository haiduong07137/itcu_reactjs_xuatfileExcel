import { Grid, MuiThemeProvider, TextField, Button, TableHead, TableCell, TableRow, Checkbox, TablePagination, Radio, Dialog, DialogActions } from "@material-ui/core";
import { createMuiTheme } from "@material-ui/core/styles";
import React, { Component } from "react";
import ReactDOM from "react-dom";
import MaterialTable, { MTableToolbar, Chip, MTableBody, MTableHeader } from 'material-table';
import { useTranslation, withTranslation, Trans } from 'react-i18next';
import { ValidatorForm, TextValidator } from "react-material-ui-form-validator";
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import Draggable from 'react-draggable';
import Paper from '@material-ui/core/Paper';
import { searchByPage, searchProductInVoucherByPage } from "../../Product/ProductService";
import ConstantList from "../../../appConfig";

function PaperComponent(props) {
  return (
    <Draggable handle="#draggable-dialog-title" cancel={'[class*="MuiDialogContent-root"]'}>
      <Paper {...props} />
    </Draggable>
  );
}
class SelectProductAllPopop extends React.Component {
  constructor(props) {
    super(props);
    this.handleChange = this.handleChange.bind(this);
  }
  state = {
    rowsPerPage: 5,
    page: 0,
    data: [],
    totalElements: 0,
    itemList: [],
    shouldOpenEditorDialog: false,
    shouldOpenConfirmationDialog: false,
    selectedItem: {},
    keyword: '',
    shouldOpenProductDialog: false,
    products: [],
    voucherType: null,
    voucherId: null
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

  componentDidMount() {
    this.updatePageData(this.state.page, this.state.rowsPerPage);
  }

  handleClick = (event, item) => {
    item.isCheck = event.target.checked;
    let { products } = this.state;
    if (products == null) {
      products = [];
    }
    if (products != null && products.length == 0 && item.isCheck == true) {
      let p = {};
      p.Product = item;

      products.push(p);
    }
    else {
      let itemInList = false;
      products.forEach((el) => {
        if (el.Product.id == item.id) {
          itemInList = true;
        }
      });
      if (!itemInList && item.isCheck == true) {
        let p = {};
        p.Product = item;

        products.push(p);
      }
      else {
        function check(value) {
          return value >= item.id;
        }
        let index = products.findIndex(check);
        products.slice(index, 1);
      }
    }
    this.setState({ products: products }, function () {
    });
    // else {
    //   this.setState({ selectedValue: null, selectedItem: null });
    // }
  }

  componentWillMount() {
    let { open, handleClose, selectedItem, products, voucherType, voucherId } = this.props;
    //this.setState(item);
    this.setState({ products, voucherType, voucherId });
  }

  handleKeyDownEnterSearch = e => {
    if (e.key === 'Enter') {
      this.search();
    }
  };

  search() {
    this.setPage(0, function () {
      var searchObject = {};
      searchObject.voucherType = this.state.voucherType;
      searchObject.voucherId = this.state.voucherId;
      searchObject.keyword = this.state.keyword;
      searchObject.pageIndex = this.state.page;
      searchObject.pageSize = this.state.rowsPerPage;
      if (searchObject.voucherType) {
        searchProductInVoucherByPage(searchObject).then(({ data }) => {
          this.setState({ itemList: [...data.content], totalElements: data.totalElements })
        });
      }
      else {
        searchByPage(searchObject).then(({ data }) => {
          this.setState({ itemList: [...data.content], totalElements: data.totalElements })
        });
      }
    });
  };

  updatePageData = () => {
    var searchObject = {};
    searchObject.voucherType = this.state.voucherType;
    searchObject.voucherId = this.state.voucherId;
    searchObject.keyword = this.state.keyword;
    searchObject.pageIndex = this.state.page + 1;
    searchObject.pageSize = this.state.rowsPerPage;
    if (searchObject.voucherType) {
      searchProductInVoucherByPage(searchObject).then(({ data }) => {
        this.setState({ itemList: [...data.content], totalElements: data.totalElements })
      });
    }
    else {
      searchByPage(searchObject).then(({ data }) => {
        this.setState({ itemList: [...data.content], totalElements: data.totalElements })
      });
    }
  }

  handleChange = (event, source) => {
    event.persist();
    this.setState({
      [event.target.name]: event.target.value
    });
  };

  handleOpenProductDialog = () => {
    this.setState({
      shouldOpenProductDialog: true
    })
  }

  handleDialogProductClose = () => {
    this.setState({
      shouldOpenProductDialog: false
    })
  }

  handleOKEditClose = () => {
    this.setState({
      shouldOpenProductDialog: false
    });
    this.updatePageData();
  };

  render() {
    const { t, i18n, handleClose, handleSelect, selectedItem, open } = this.props;
    let { keyword, shouldOpenProductDialog, itemList, products, voucherType, voucherId,
      totalElements, rowsPerPage, page } = this.state;
    let columns = [
      { title: t("Product.code"), field: "code", align: "left", width: "150" },
      { title: t("Product.name"), field: "name", align: "left", width: "150" },
    ];
    let columnsInSearchByVoucher = [
      { title: t("Product.code"), field: "code", align: "left", width: "150px" },
      { title: t("Product.name"), field: "name", align: "left", width: "150" },
      { title: t("StockKeepingUnit.title"), field: "sku.name", align: "left", width: "150px" },
      {
        title: t("InventoryDeliveryVoucher.quantity_volume"), field: "", align: "left", width: "150px",
        render: rowData => <span>
          {(rowData.skuType === 1) ? rowData.remainingQuantity : rowData.remainingVolume}
        </span>
      },
      {
        title: t("Product.price"), field: "price", align: "left", width: "200px",
        render: (rowData) => {
          let number = new Number(rowData.price);
          if (number != null) {
            let plainNumber = number.toFixed(0).replace(/(\d)(?=(\d{3})+(?!\d))/g, '$1,');
            return plainNumber;
          }
        }
      }
    ];
    return (
      <Dialog onClose={handleClose} open={open} PaperComponent={PaperComponent} maxWidth={'lg'} fullWidth>
        <DialogTitle style={{ cursor: 'move' }} id="draggable-dialog-title">
          <span className="mb-20">{t("Product.title")}</span>
        </DialogTitle>
        <DialogContent>
          <Grid item xs={12}>
            <TextValidator
              label={t('general.enterSearch')}
              className="mb-16 mr-10 w-60"
              name="keyword"
              value={keyword}
              onKeyDown={this.handleKeyDownEnterSearch}
              onChange={this.handleChange} />
            <Button
              className="mb-16 mr-16 align-bottom"
              variant="contained"
              color="primary"
              onClick={() => this.search()}>
              {t('general.search')}
            </Button>
            {itemList.length !== 0 ? '' :
              <Button className="mb-16 mr-16 align-bottom"
                variant="contained"
                color="primary"
                onClick={() => this.handleOpenProductDialog()}
              >
                {t('Product.add')}
              </Button>
            }
          </Grid>
          <Grid item xs={12}>
            <MaterialTable
              data={itemList}
              columns={voucherType ? columnsInSearchByVoucher : columns}

              options={{
                toolbar: false,
                selection: true,
                actionsColumnIndex: -1,
                paging: false,
                search: false
              }}
              components={{
                Toolbar: props => (
                  <div style={{ witdth: "100%" }}>
                    <MTableToolbar {...props} />
                  </div>
                ),
              }}
              onSelectionChange={(rows) => this.setState({
                products: rows.map(row => ({
                  ...row,
                  tableData: {
                    ...row.tableData,
                    checked: false
                  }
                }))
              })}
            />
            <TablePagination
              align="left"
              className="px-16"
              rowsPerPageOptions={[1, 2, 3, 5, 10, 25]}
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
        </DialogContent>
        <DialogActions>
          <Button
            className="mb-16 mr-36 align-bottom"
            variant="contained"
            color="secondary"
            onClick={() => handleClose()}>{t('general.cancel')}</Button>
          <Button className="mb-16 mr-16 align-bottom"
            variant="contained"
            color="primary"
            onClick={() => handleSelect(products)}>
            {t('general.select')}
          </Button>
        </DialogActions>
      </Dialog>
    )
  }
}
export default SelectProductAllPopop;