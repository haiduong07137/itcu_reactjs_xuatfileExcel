import { Grid, MuiThemeProvider, TextField, Button, TableHead, TableCell, TableRow, Checkbox, TablePagination, Radio, Dialog, DialogActions } from "@material-ui/core";
import { createMuiTheme } from "@material-ui/core/styles";
import React, { Component } from "react";
import ReactDOM from "react-dom";
import MaterialTable, { MTableToolbar, Chip, MTableBody, MTableHeader } from 'material-table';
import { useTranslation, withTranslation, Trans } from 'react-i18next';
import { searchByPage } from "../../ProductAttribute/ProductAttributeService";
import { ValidatorForm, TextValidator } from "react-material-ui-form-validator";
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import Draggable from 'react-draggable';
import Paper from '@material-ui/core/Paper';
function PaperComponent(props) {
  return (
    <Draggable handle="#draggable-dialog-title" cancel={'[class*="MuiDialogContent-root"]'}>
      <Paper {...props} />
    </Draggable>
  );
}
class SelectProductAttributePopup extends React.Component {
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
    selectedItem: null,
    keyword: '',
    shouldOpenProductDialog: false,
    attributes: []
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

  updatePageData = () => {
    var searchObject = {};
    searchObject.keyword = this.state.keyword;
    searchObject.pageIndex = this.state.page + 1;
    searchObject.pageSize = this.state.rowsPerPage;
    searchByPage(searchObject).then(({ data }) => {
      this.setState({ itemList: [...data.content], totalElements: data.totalElements })
    });
  }

  componentDidMount() {
    this.search();
  }

  handleClick = (item) => {
    let { attributes } = this.state;

    if (item && item.id) {
      let itemNotInList = false;
      if (attributes && attributes.length > 0) {
        if (attributes && attributes.length > 0) {
          const att = attributes.find(element => element.id == item.id);
          if (att && att.id) {
            itemNotInList = true;
          }
        }
      }
      else {
        attributes = [];
      }

      if (!itemNotInList) {
        attributes.unshift(item);
      }
    }

    this.setState({ attributes }, function () {
    });
  }

  componentWillMount() {
    let { open, handleClose, selectedItem, attributes } = this.props;
    //this.setState(item);
    this.setState({ attributes: attributes });
  }

  handleKeyDownEnterSearch = e => {
    if (e.key === 'Enter') {
      this.search();
    }
  };

  search() {
    this.setPage(0, function () {
      var searchObject = {};
      searchObject.keyword = this.state.keyword;
      searchObject.pageIndex = this.state.page;
      searchObject.pageSize = this.state.rowsPerPage;
      searchByPage(searchObject).then(({ data }) => {
        this.setState({ itemList: [...data.content], totalElements: data.totalElements })
      });
    });
  };

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
    let { keyword, shouldOpenProductDialog, itemList, attributes } = this.state;
    let columns = [
      // {
      //   title: t("general.select"),
      //   field: "custom",
      //   align: "left",
      //   width: "250",
      //   render: rowData => <Checkbox name="radSelected" value={rowData.isCheck}
      //     checked={rowData.isCheck}
      //     onClick={(event) => this.handleClick(event, rowData)}
      //   />
      // },
      { title: t("general.code"), field: "code", align: "left", width: "150" },
      { title: t("general.name"), field: "name", width: "150" },
    ];
    return (
      <Dialog onClose={handleClose} open={open} PaperComponent={PaperComponent} maxWidth={'md'} fullWidth>
        <DialogTitle style={{ cursor: 'move' }} id="draggable-dialog-title">
          <span className="mb-20">{t("ProductAttribute.select")}</span>
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
              title={t('general.list')}
              data={itemList}
              columns={columns}
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
                  <div style={{ witdth: "100%" }}>
                    <MTableToolbar {...props} />
                  </div>
                ),
              }}
              onSelectionChange={(rows) => this.setState({
                attributes: rows.map(row => ({
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
            onClick={() => handleSelect(attributes)}>
            {t('general.select')}
          </Button>
        </DialogActions>
      </Dialog>
    )
  }
}
export default SelectProductAttributePopup;