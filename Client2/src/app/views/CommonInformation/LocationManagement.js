import React from "react";
import "react-tabulator/lib/css/bootstrap/tabulator_bootstrap.min.css"; // use Theme(s)
import { React15Tabulator, reactFormatter } from "react-tabulator"; // for React 15.x
import { saveAs } from 'file-saver';
import { getByPage, deleteItem, saveItem,getItemById} from "./LocationService";
import LocationEditorDialog from "./LocationEditorDialog";
import { Breadcrumb, ConfirmationDialog } from "egret";
import ConstantList from "../../appConfig";
import { useTranslation, withTranslation, Trans } from 'react-i18next';
import {
  IconButton,
  Table,
  TableHead,
  TableBody,
  TableRow,
  TableCell,
  Icon,
  TablePagination,
  TableContainer,
  Button,
  Card
} from "@material-ui/core";

function SimpleButton(props) {
  const { t, i18n } = useTranslation();
  const cellData = props.cell._cell.row.data;
  return <div> 
    <button onClick={() => props.onSelect(cellData,0)}>{t("Edit")}</button> 
    <button onClick={() => props.onSelect(cellData,1)}>{t("Delete")}</button> 
    </div>;
}
const ViewLocationEditorDialog = withTranslation()(LocationEditorDialog);
class LocationTableList extends React.Component {
  ref = null;
  state = {
    rowsPerPage: 5,
    page: 0,
    data: [],
    totalElements:0,
    itemList: [],
    shouldOpenEditorDialog: false,
    shouldOpenConfirmationDialog: false
  };
  SimpleButton=(props)=>{
    const cellData = props.cell._cell.row.data;
    return <div> 
      <button onClick={() => props.onSelect(cellData,0)}>Edit</button> 
      <button onClick={() => props.onSelect(cellData,1)}>Delete</button> 
      </div>;
  }
  setPage = page => {
    this.setState({ page });
  };

  setRowsPerPage = event => {
    this.setState({ rowsPerPage: event.target.value, page:0});
    this.updatePageData(0,event.target.value);
  };

  handleChangePage = (event, newPage) => {
    this.setPage(newPage);
    this.updatePageData(newPage,this.state.rowsPerPage);
  };
  handleDownload = () => {
    var blob = new Blob(["Hello, world!"], {type: "text/plain;charset=utf-8"});
    saveAs(blob, "hello world.txt");    
  }
  handleDialogClose = () => {
    this.setState({
      shouldOpenEditorDialog: false,
      shouldOpenConfirmationDialog: false
    });
    this.setPage(0);
    this.updatePageData(this.state.page,this.state.rowsPerPage);
  };

  handleDelete = id => {
    this.setState({
      id,
      shouldOpenConfirmationDialog: true
    });
  };

  handleConfirmationResponse = () => {
    deleteItem(this.state.id).then(() => {
      this.handleDialogClose();
    });
  };
  handleEditItem = item => {
    this.setState({
      item:item,
      shouldOpenEditorDialog: true
    });
  };
  componentDidMount() {
    this.updatePageData(this.state.page,this.state.rowsPerPage);
  }

  updatePageData = (pageIndex, pageSize) => { 
    getByPage(pageIndex,pageSize).then(({ data }) => this.setState({
      itemList: [...data.content], totalElements:data.totalElements
      }));
  };

  render() {
    const { t, i18n } = this.props;
    let columns = [
      { title: t("Name"), field: "name", width: "150"},
      { title: t("Code"), field: "code", align: "left", width: "150"},
      { title: t("IsActive"), field: "isActive", width: "20%"},
      {
        title: t("Action"),
        field: "custom",
        align: "left",
        width: "250",
        formatter: reactFormatter(
          <SimpleButton
            onSelect={(item, method) => {
              if(method===0){
                this.setState({ item: item, shouldOpenEditorDialog: true});
              }else {
                this.handleDelete(item.id);
              }
            }}
          />
        )
      }
    ];
    const options = {
      height: "90%",
      movableRows: false
    };
  let {
      rowsPerPage,
      page,
      shouldOpenConfirmationDialog,
      shouldOpenEditorDialog
    } = this.state;
    return (
      <div>
        <Button
          className="mb-16"
          variant="contained"
          color="primary"
          onClick={() => this.setState({ shouldOpenEditorDialog: true,item:{} })}
        >
          {t('CommonInformation.AddNew')}
        </Button>          
          {shouldOpenEditorDialog && (
            <ViewLocationEditorDialog
              handleClose={this.handleDialogClose}
              open={this.state.shouldOpenEditorDialog}
              item={this.state.item}
            />
          )}   
          
          {shouldOpenConfirmationDialog && (
            <ConfirmationDialog
              open={shouldOpenConfirmationDialog}
              onConfirmDialogClose={this.handleDialogClose}
              onYesClick={this.handleConfirmationResponse}
              text="Are you sure to delete?"
            />
          )
          }     
        <React15Tabulator
          ref={ref => (this.ref = ref)}
          columns={columns}
          data={this.state.itemList}
          rowClick={this.rowClick}
          options={options}
          data-custom-attr="test-custom-attribute"
          className="custom-css-class"
          resizableColumns="false" movableColumns="false" scrollToColumnIfVisible="true" selectableRollingSelection="false"
          //min-width="750px"
        />
          <TablePagination
            align="left"
            className="px-16"
            rowsPerPageOptions={[1,2,3,5, 10, 25]}
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
      </div>
    );
  }
}

export default LocationTableList;
