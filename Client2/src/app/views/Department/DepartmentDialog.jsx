import {
  Grid,
  DialogActions,
  MuiThemeProvider,
  TextField,
  Button,
  TableHead,
  TableCell,
  TableRow,
  Checkbox,
  TablePagination,
  Radio,
  Dialog,
} from '@material-ui/core'
import { createMuiTheme } from '@material-ui/core/styles'
import React, { Component } from 'react'
import ReactDOM from 'react-dom'
import MaterialTable, {
  MTableToolbar,
  Chip,
  MTableBody,
  MTableHeader,
} from 'material-table'
import { useTranslation, withTranslation, Trans } from 'react-i18next'
import DateFnsUtils from '@date-io/date-fns'
import { ValidatorForm, TextValidator } from 'react-material-ui-form-validator'
import DialogContent from '@material-ui/core/DialogContent'
import DialogTitle from '@material-ui/core/DialogTitle'
import Input from '@material-ui/core/Input'
import InputLabel from '@material-ui/core/InputLabel'
import MenuItem from '@material-ui/core/MenuItem'
import FormControl from '@material-ui/core/FormControl'
import Select from '@material-ui/core/Select'
import AsynchronousAutocomplete from '../utilities/AsynchronousAutocomplete'
import SelectDepartmentPopup from '../Component/Department/SelectDepartmentPopup'
import Draggable from 'react-draggable'
import {checkCode} from './DepartmentService'
import NotificationPopup from '../Component/NotificationPopup/NotificationPopup'
import {
  getByPage,
  deleteItem,
  saveItem,
  getItemById,
  getAll,
} from './DepartmentService'
import Paper from '@material-ui/core/Paper'
function PaperComponent(props) {
  return (
    <Draggable
      handle="#draggable-dialog-title"
      cancel={'[class*="MuiDialogContent-root"]'}
    >
      <Paper {...props} />
    </Draggable>
  )
}
class DepartmentDialog extends React.Component {
  constructor(props) {
    super(props)
    this.handleChange = this.handleChange.bind(this)
  }
  state = {
    id: '',
    rowsPerPage: 5,
    page: 0,
    data: [],
    totalElements: 0,
    itemList: [],
    shouldOpenEditorDialog: false,
    shouldOpenConfirmationDialog: false,
    shouldOpenDepartmentPopup: false,
    shouldOpenNotificationPopup:false,
    selectedItem: {},
    parent: '',
    type: 0,
    keyword: '',
    Notification:""
  }

  setPage = (page) => {
    this.setState({ page }, function () {
      this.updatePageData()
    })
  }

  setRowsPerPage = (event) => {
    this.setState({ rowsPerPage: event.target.value, page: 0 })
    this.updatePageData()
  }

  handleChangePage = (event, newPage) => {
    this.setPage(newPage)
  }

  updatePageData = () => {
    var searchObject = {}
    searchObject.keyword = ''
    searchObject.pageIndex = this.state.page + 1
    searchObject.pageSize = this.state.rowsPerPage
    getByPage(this.state.page, this.state.rowsPerPage).then(({ data }) => {
      this.setState({
        itemList: [...data.content],
        totalElements: data.totalElements,
      })
    })
  }

  componentDidMount() {
    this.updatePageData()
  }
  handleFormSubmit = () => {
    
    let { id, name, code, parent } = this.state;
    checkCode(id, code).then( data => {
      if(data.data) {
        this.setState({shouldOpenNotificationPopup: true,
          Notification:"Department.noti.dupli_code"})
        // alert("Mã phòng ban đã được sử dụng");
      } else {
        if(parent === ""){
          parent = null;
        }
        saveItem({id, name, code, parent}).then(() => {
          this.props.handleClose()
        })
      }
    })
    
  }

  handleClick = (event, item) => {
    //alert(item);
    if (item.id != null) {
      this.setState({ selectedValue: item.id, selectedItem: item })
    } else {
      this.setState({ selectedValue: item.id, selectedItem: null })
    }
  }
  componentWillMount() {
    const {handleDialogClose} = this.props
    this.setState(
      {
        ...this.props.item,
      },
      function () {
        let { parent } = this.state
        if (parent != null && parent.id != null) {
          this.setState({ parentId: parent.id })
        }
      }
    )
  }


  handleChange(event) {
    this.setState({
      [event.target.name]: event.target.value,
    })
  }

  handleDepartmentPopupClose = () => {
    this.setState({
      shouldOpenDepartmentPopup: false,
    })
  }
  handleSelectDepartment = (item) => {
    this.setState({ parent: { id: item.id, name: item.text } }, function () {
      this.handleDepartmentPopupClose();
    })
  }
  handleDialogClose =()=>{
    this.setState({shouldOpenNotificationPopup:false,})
  }
  render() {
    let searchObject = { pageIndex: 0, pageSize: 1000000 }
    const {
      t,
      i18n,
      handleClose,
      handleSelect,
      selectedItem,
      open,
      handleDialogClose
    } = this.props
    let {
      keyword,
      parent,
      name,
      code,
      type,
      shouldOpenDepartmentPopup,
      shouldOpenNotificationPopup
    } = this.state
    let columns = [
      { title: t('Department.name'), field: 'name', width: '150' },
      {
        title: t('Department.code'),
        field: 'code',
        align: 'left',
        width: '150',
      },
      {
        title: t('Department.type'),
        field: 'type',
        align: 'left',
        width: '150',
      },
      {
        title: t('general.action'),
        field: 'custom',
        align: 'left',
        width: '250',
        render: (rowData) => (
          <Radio
            name="radSelected"
            value={rowData.id}
            checked={this.state.selectedValue === rowData.id}
            onClick={(event) => this.handleClick(event, rowData)}
          />
        ),
      },
    ]
    return (
      <Dialog open={open} PaperComponent={PaperComponent}>
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
        <DialogTitle style={{ cursor: 'move' }} id="draggable-dialog-title">
          <h4 className="mb-20">{t('general.saveUpdate')}</h4>
        </DialogTitle>
        <ValidatorForm ref="form" onSubmit={this.handleFormSubmit}>
          <DialogContent>
            <Grid className="mb-16" container spacing={2}>
              <Grid item sm={12} xs={12}>
                <TextValidator
                  style ={{width:'82%'}}
                  InputProps={{
                    readOnly: true,
                  }}
                  label={t('Department.parent')}
                  className="w-80  mr-16"
                  value={parent.name || ''}
                />
                <Button
                  style={{float:'right'}}
                  className=" mt-10"
                  variant="contained"
                  color="primary"
                  onClick={() =>
                    this.setState({ shouldOpenDepartmentPopup: true, item: {} })
                  }
                >
                  {t('general.select')}
                </Button>
                {shouldOpenDepartmentPopup && (
                  <SelectDepartmentPopup
                    open={shouldOpenDepartmentPopup}
                    handleSelect={this.handleSelectDepartment}
                    selectedItem={parent != null ? parent : {}}
                    handleClose={this.handleDepartmentPopupClose}
                    t={t}
                    i18n={i18n}
                  />
                )}
              </Grid>

              <Grid item sm={6} xs={12}>
                <TextValidator
                  className="w-100"
                  label={<span><span style={{color:"red"}}>*</span>{t('Department.name')}</span>}

                  onChange={this.handleChange}
                  type="text"
                  name="name"
                  value={name}
                  validators={['required']}
                  errorMessages={['this field is required']}
                />
              </Grid>
              <Grid item sm={6} xs={12}>
                <TextValidator
                  className="w-100 "
                  
                  label={<span><span style={{color:"red"}}>*</span>{t('Department.code')}</span>}
                  onChange={this.handleChange}
                  type="text"
                  name="code"
                  value={code}
                  validators={['required']}
                  errorMessages={['this field is required']}
                />
              </Grid>
              {/* <Grid item sm={6} xs={12}>
                <TextValidator
                  className="w-101"
                  label={t("Department.type")}
                  onChange={this.handleChange}
                  type="number"
                  name="type"
                  value={type}
                  validators={["required"]}
                  errorMessages={["this field is required"]}
                />
              </Grid> */}
            </Grid>
          </DialogContent>
          <DialogActions>
            <div className="flex flex-space-between flex-middle">
            <Button
                variant="contained"
                className="mr-36"

                color="secondary"
                onClick={() => this.props.handleClose()}
              >
                {t('general.cancel')}
              </Button>
              <Button
                variant="contained"
                color="primary"
                type="submit"
              >
                {t('general.save')}
              </Button>
              
            </div>
          </DialogActions>
        </ValidatorForm>
      </Dialog>
    )
  }
}
export default DepartmentDialog
