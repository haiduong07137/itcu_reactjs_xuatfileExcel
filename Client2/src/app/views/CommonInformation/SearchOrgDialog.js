import { Grid, MuiThemeProvider,TextField,Button,TableHead,TableCell, TableRow,Checkbox,TablePagination,Radio  } from "@material-ui/core";
import { createMuiTheme } from "@material-ui/core/styles";
import React, { Component } from "react";
import ReactDOM from "react-dom";
import MaterialTable, { MTableToolbar,Chip,MTableBody,MTableHeader } from 'material-table';
import { useTranslation, withTranslation, Trans } from 'react-i18next';
import { getByPage, deleteItem, saveItem,getItemById} from "./OrganizationService";
class SearchOrgDialog extends React.Component {
  constructor(props) {
    super(props);
    //this.state = {value: ''};
    this.handleChange = this.handleChange.bind(this);
  }
    state = {
      rowsPerPage: 5,
      page: 0,
      data: [],
      totalElements:0,
      itemList: [],
      shouldOpenEditorDialog: false,
      shouldOpenConfirmationDialog: false,
      selectedItem:{},
      keyword:'',
      value: ''
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
    updatePageData = () => { 
      getByPage(this.state.page,this.state.rowsPerPage).then(({ data }) =>{ 
        var list = this.getListFlatData(data.content);
          this.setState({itemList: [...data.content], totalElements:data.totalElements, flatData:[...list]})
        }
        );
    };

    componentDidMount() {
      this.updatePageData(this.state.page,this.state.rowsPerPage);
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
      list.push({id:-1,name:"Không chọn"});
      for(var i=0;i<itemList.length;i++){
        let item = itemList[i];
        let childs= this.getAllChild(item);
        list.push(...childs);
      }
      return list;
    }
  
    handleClick = (event, item) => {
      //alert(item);
      if(item.id>-1){
        this.setState({selectedValue:item.id, selectedItem:item});
      }else{
        this.setState({selectedValue:item.id, selectedItem:null});
      }
      
    }
    componentWillMount() {
      let { open, handleClose,selectedItem } = this.props;
      //this.setState(item);
      this.setState({selectedValue:selectedItem.id});
    }
    search =(keyword)=>{
      alert('Search here :'+keyword);
    }
    
    handleChange(event) {
      this.setState({value: event.target.value});
    }
  
    render() {
      const { t, i18n, handleClose,handleSelect, selectedItem } = this.props;
      let {keyword} = this.state;
      let columns = [
        { title: t("Name"), field: "name", width: "150"},
        { title: t("Code"), field: "code", align: "left", width: "150"},
        
        {
          title: t("Action"),
          field: "custom",
          align: "left",
          width: "250",
          render: rowData =><Radio name="radSelected" value={rowData.id} checked={this.state.selectedValue === rowData.id} onClick={(event) => this.handleClick(event, rowData)}          
        />  
        },
      ];     
      return (
        <div>
        <MaterialTable title="Basic Tree Data Preview" data={this.state.flatData} columns={columns}
          parentChildData={(row, rows) => {            
              var list= rows.find(a => a.id === row.parentId);
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
              <div style={{witdth:"100%"}}>
                <MTableToolbar {...props} />
                <div style={{padding: '0px 40px'}}>
                    <Button className="mb-16" variant="contained" color="primary" onClick={() => handleSelect(this.state.selectedItem)}>
                      {t('Select')}
                    </Button> 
                    
                    <Button className="mb-16" variant="contained" color="primary"  onClick={() => handleClose()}>{t('Cancel')}</Button>
                    {/* <TextField id="keyword"  name="keyword"  value={this.state.keyword} onChange={this.handleTextChange}></TextField> */}
                    <input type="text" value={this.state.value} onChange={this.handleChange} />
                    <Button className="mb-16" variant="contained" color="primary"  onClick={() => this.search(this.state.keyword)}>{t('Search')}</Button>
                </div>
              </div>
            ),
            }}            
          onSelectionChange={(rows) => {
            this.data=rows;
          }}
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
  export default SearchOrgDialog;