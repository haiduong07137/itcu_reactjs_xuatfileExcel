import { Grid, MuiThemeProvider,TextField,Button,TableHead,TableCell, TableRow,Checkbox,TablePagination } from "@material-ui/core";
import { createMuiTheme } from "@material-ui/core/styles";
import React, { Component } from "react";
import ReactDOM from "react-dom";
import MaterialTable, { MTableToolbar,Chip,MTableBody,MTableHeader } from 'material-table';
import { useTranslation, withTranslation, Trans } from 'react-i18next';
import { getByPage, deleteItem, saveItem,getItemById,searchByPage} from "./OrganizationService";
import OrganizationEditorDialog from "./OrganizationEditorDialog";
import { Breadcrumb, ConfirmationDialog } from "egret";
function MaterialButton(props) {
  const { t, i18n } = useTranslation();
  const item =props.item;
  return <div> 
    <button onClick={() => props.onSelect(item,0)}>{t("Edit")}</button> 
    <button onClick={() => props.onSelect(item,1)}>{t("Delete")}</button> 
    </div>;
}
class TreeListData extends React.Component {
    state = {
      rowsPerPage: 5,
      page: 0,
      data: [],
      totalElements:0,
      itemList: [],
      shouldOpenEditorDialog: false,
      shouldOpenConfirmationDialog: false,
      keyword: ''
    }  
    constructor(props) {
      super(props);
      //this.state = {keyword: ''};
      this.handleTextChange = this.handleTextChange.bind(this);
    }
    handleTextChange(event) {
      this.setState({value: event.target.value});
    }
    searchOrganization(keyword){
      searchByPage(keyword,this.state.page,this.state.rowsPerPage).then(({ data }) =>{ 
        var list = this.getListFlatData(data.content);
          this.setState({itemList: [...data.content], totalElements:data.totalElements, flatData:[...list]})
        }
        );    
    }
    getAllChild(item){
      let list = [];
      list.push(item);
      if(item.subDepartments!=null){
        for(var i=0;i<item.subDepartments.length;i++){
          var child = item.subDepartments[i];
          child.parentId= item.id;
          list.push(...this.getAllChild(child));
        }
      }
      return list;
    };
    getListFlatData(itemList){
      let list =[];
      for(var i=0;i<itemList.length;i++){
        let item = itemList[i];
        let childs= this.getAllChild(item);
        list.push(...childs);
      }
      return list;
    }
    handleChange(event) {
      this.setState({value: event.target.value});
    }    
    componentDidMount() {
      this.updatePageData();
    }    
    updatePageData = () => { 
      getByPage(this.state.page,this.state.rowsPerPage).then(({ data }) =>{ 
          var list = this.getListFlatData(data.content);
          this.setState({itemList: [...data.content], totalElements:data.totalElements, flatData:[...list]})
        }
        );
    };    
    setPage = page => {
      this.setState({ page },function(){
        this.updatePageData();
       })
    };
  
    setRowsPerPage = event => {
      this.setState({ rowsPerPage: event.target.value, page:0});
      this.updatePageData();
    };
  
    handleChangePage = (event, newPage) => {
      this.setPage(newPage);
    }; 
    
    handleOKEditClose = () => {
      this.setState({
        shouldOpenEditorDialog: false,
        shouldOpenConfirmationDialog: false
      });
      this.setPage(0);
    };
  
    handleDelete = id => {
      this.setState({
        id,
        shouldOpenConfirmationDialog: true
      });
    };
    handleDialogClose = () => {
      this.setState({
        shouldOpenEditorDialog: false,
        shouldOpenConfirmationDialog: false
      });
    };
  
    handleOKEditClose = () => {
      this.setState({
        shouldOpenEditorDialog: false,
        shouldOpenConfirmationDialog: false
      });
      this.setPage(0);
    };
  
    handleDelete = id => {
      this.setState({
        id,
        shouldOpenConfirmationDialog: true
      });
    };
  
    handleConfirmationResponse = () => {
      deleteItem(this.state.id).then(() => {
        this.updatePageData();
        this.handleDialogClose()
      });
    };
    handleEditItem = item => {
      this.setState({
        item:item,
        shouldOpenEditorDialog: true
      });
    };
    componentDidMount() {
      this.updatePageData();
    }
  
  
    handleDeleteAll=(data)=>{
      this.setState({test:data});
      this.handleEditItem({});
    }
    render() {
    const { t, i18n} = this.props;
    let columns = [
      { title: t("Name"), field: "name", width: "150"},
      { title: t("Code"), field: "code", align: "left", width: "150"},
      {
        title: t("Action"),
        field: "custom",
        align: "left",
        width: "250",
        render: rowData =><MaterialButton item={rowData}
        onSelect={(rowData, method) => {
          if(method===0){
            getItemById(rowData.id).then(({data})=>{
              if(data.parent===null){
                data.parent={};
              }
              this.setState({
                item:data,
                shouldOpenEditorDialog: true
              });
            })        
          }else if(method===1){
            this.handleDelete(rowData.id);
          }else{
            alert('Call Selected Here:'+rowData.id);
          }
        }}
      />
      },
    ];    
      return (
        <div>
            <div style={{padding: '0px 40px'}}>
                <Button
                  className="mb-16"
                  variant="contained"
                  color="primary"
                  onClick = {()=>{
                      this.handleEditItem({});
                    }
                  }
                >
                  {t('CommonInformation.AddNew')}
                </Button> 
                <Button className="mb-16" variant="contained" color="primary">
                  {t('Delete')}
                </Button>
              <TextField type="text" value={this.state.value} onChange={this.handleTextChange} />
              <Button className="mb-16" variant="contained" color="primary" onClick={()=>this.searchOrganization(this.state.value)}>
                  {t('Search')}
              </Button>              
          </div>
               
        <div>
          {this.state.shouldOpenEditorDialog && (
            <OrganizationEditorDialog t={t} i18n={i18n}
              handleClose={this.handleDialogClose}
              open={this.state.shouldOpenEditorDialog}
              handleOKEditClose ={this.handleOKEditClose}
              item={this.state.item}
            />
          )}   
          
          {this.state.shouldOpenConfirmationDialog && (
            <ConfirmationDialog
              open={this.state.shouldOpenConfirmationDialog}
              onConfirmDialogClose={this.handleDialogClose}
              onYesClick={this.handleConfirmationResponse}
              text="Are you sure to delete?"
            />
          )}          
        </div>          
        <MaterialTable
          title="Basic Tree Data Preview"
          data={this.state.flatData}
          columns={columns}
          //parentChildData={(row, rows) => rows.find(a => a.id === row.parentId)}
          parentChildData={(row, rows) => {            
           var list= rows.find(a => a.id === row.parentId);
            return list;
          }}
          options={{
            selection: true,
            actionsColumnIndex: -1,
            paging: false,
            search: true
          }}      

          components={{
            Toolbar: props => (
                  <MTableToolbar {...props} />
            ),
            }}            
          onSelectionChange={(rows) => {
            this.data=rows;
          }}
          actions={[
            {
              tooltip: 'Remove All Selected Users',
              icon: 'delete',
              onClick: (evt, data) =>{ 
                this.handleDeleteAll(data);
                alert('You want to delete ' + data.length + ' rows');
              }
            },
            // {
            //   tooltip: 'Add New Item',
            //   icon: 'add',
            //   isFreeAction:true,
            //   onClick: (evt, data) => {handleEditItem({})}
            // }
          ]}           
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
      )
    }
  }
  export default TreeListData;