import React, { Component } from 'react'
import {
  Dialog,
  Button,
  Grid,
  IconButton,
  Icon,
  InputLabel,
  FormControl,
  MenuItem,
  Select,
  Checkbox,
  TextField,
  FormControlLabel,
  DialogActions,
} from '@material-ui/core'
import { useTranslation, withTranslation, Trans } from 'react-i18next'
import Autocomplete from '@material-ui/lab/Autocomplete'
import MaterialTable, {
  MTableToolbar,
  Chip,
  MTableBody,
  MTableHeader,
} from 'material-table'
import { ValidatorForm, TextValidator } from 'react-material-ui-form-validator'
import {
  getUserByUsername,
  saveUser,
  saveUserDepartment,
  addNewUser,
  getAllRoles,
  saveUserDepartments,
  listByUserId,
  deleteById,
} from './UserService'
import SelectDepartmentPopup from '../Component/Department/SelectDepartmentPopup'
import { find } from 'lodash'
function MaterialButton(props) {
  const { t, i18n } = useTranslation()
  const item = props.item
  return (
    <div>
      {/* <IconButton onClick={() => props.onSelect(item, 0)}>
      <Icon color="primary">edit</Icon>
    </IconButton> */}
      <IconButton onClick={() => props.onSelect(item, 1)}>
        <Icon color="error">delete</Icon>
      </IconButton>
    </div>
  )
}
class UserEditorDialog extends Component {
  constructor(props) {
    super(props)
  }
  state = {
    userDepartmentId: '',
    department: {},
    isAddNew: false,
    listRole: [],
    roles: [],
    active: true,
    email: '',
    person: {},
    username: '',
    changePass: true,
    password: '',
    confirmPassword: '',
    userDepartments: [],
    id: '',
  }

  listGender = [
    { id: 'M', name: 'Nam' },
    { id: 'F', name: 'Nữ' },
    { id: 'U', name: 'Không rõ' },
  ]

  handleChange = (event, source) => {
    event.persist()
    if (source === 'switch') {
      this.setState({ isActive: event.target.checked })
      return
    }
    if (source === 'changePass') {
      this.setState({ changePass: event.target.checked })
      return
    }
    if (source === 'active') {
      this.setState({ active: event.target.checked })
      return
    }
    if (source === 'displayName') {
      let { person } = this.state
      person = person ? person : {}
      person.displayName = event.target.value
      this.setState({ person: person })
      return
    }
    if (source === 'gender') {
      let { person } = this.state
      person = person ? person : {}
      person.gender = event.target.value
      this.setState({ person: person })
      return
    }
    this.setState({
      [event.target.name]: event.target.value,
    })
  }
  openSelectDepartmentPopup = () => {
    this.setState({
      shouldOpenSelectDepartmentPopup: true,
    })
  }
  handleSelectDepartmentPopupClose = () => {
    this.setState({
      openSelectDepartmentPopup: false,
    })
  }
  handleSelectUseDepartment = (item) => {
    let { userDepartments } = this.state
    let department = { id: item.id, name: item.text, text: item.text }
    if (userDepartments.map((el) => el.department.id).indexOf(item.id) < 0) {
      userDepartments.push({ department, isMainDepartment: false })
    }
    this.setState({ userDepartments })
    // this.setState({ department }, function () {
    // });
    this.handleSelectDepartmentPopupClose()
  }
  handleFormSubmit = () => {
    let { id, userDepartments, email } = this.state

    getUserByUsername(this.state.username).then(({ data }) => {
      if (data && data.id) {
        if (!id || (id && data.id != id)) {
          alert('Tên đăng nhập đã tồn tại!')
          return
        }
      }

      getUserByUsername(this.state.username).then(({ data2 }) => {
        if (data2 && data2.id) {
          alert('Địa chỉ email đã tồn tại!')
          return
        }
      })
      saveUser({
        ...this.state,
      }).then((user) => {
        if (user.data != null && user.data.id != null) {
          let userDepartment = {}
          userDepartment.id = this.state.userDepartmentId
          userDepartment.user = user.data
          userDepartment.department = this.state.department
          userDepartment.isMainDepartment = true
          // saveUserDepartment(userDepartment).then(() => {
          //   this.props.handleOKEditClose();
          // });

          saveUserDepartments(
            userDepartments.map((element) => {
              return {
                id: element.id,
                user: { id: user.data.id },
                department: element.department,
                isMainDepartment: element.isMainDepartment,
              }
            })
          )
        }
        this.props.handleOKEditClose()
      })
    })
  }

  selectRoles = (rolesSelected) => {
    this.setState({ roles: rolesSelected }, function () {})
  }

  componentWillMount() {
    let { open, handleClose, item, department, userDepartmentId } = this.props
    this.setState(item)
    this.setState({ department, userDepartmentId })
    if (item && item.id) {
      listByUserId(item.id).then(({ data }) => {
        this.setState({ userDepartments: data })
      })
    }
  }

  componentDidMount() {
    // custom rule will have name 'isPasswordMatch'
    ValidatorForm.addValidationRule('isPasswordMatch', (value) => {
      if (value !== this.state.password) {
        return false
      }
      return true
    })

    getAllRoles().then(({ data }) => {
      this.setState({
        listRole: data,
      })
    })
  }

  render() {
    let { open, handleClose, handleOKEditClose, t, i18n } = this.props
    let {
      id,
      isAddNew,
      listRole,
      roles,
      active,
      email,
      person,
      username,
      changePass,
      password,
      confirmPassword,
      openSelectDepartmentPopup,
      department,
      userDepartments,
    } = this.state

    let columnsUserDepartment = [
      {
        title: t('manage.isMainDepartment'),
        field: 'custom',
        width: '150',
        align: 'center',
        render: (rowData) => (
          <Checkbox
            checked={rowData.isMainDepartment}
            onChange={(event, checked) => {
              if (checked == true) {
                userDepartments.forEach((el) => (el.isMainDepartment = false))
                const finder = userDepartments.find(
                  (el) =>
                    el.department && el.department.id === rowData.department.id
                )
                if (finder) {
                  finder.isMainDepartment = true
                  this.setState({ userDepartments })
                }
              }
            }}
          />
        ),
      },
      { title: t('manage.department'), field: 'department.name', width: '150' },
      {
        title: t('Action'),
        field: 'custom',
        align: 'left',
        width: '250',
        render: (rowData) => (
          <MaterialButton
            item={rowData}
            onSelect={(rowData, method) => {
              if (method === 0) {
              } else if (method === 1) {
                for (let index = 0; index < userDepartments.length; index++) {
                  const item = userDepartments[index]
                  if (
                    rowData.department &&
                    item.department &&
                    rowData.department.id === item.department.id
                  ) {
                    if (rowData.id) {
                      deleteById(rowData.id).then(({ data }) => {
                      })
                    }
                    userDepartments.splice(index, 1)
                    this.setState({ userDepartments })
                    break
                  }
                }
              } else {
                alert('Call Selected Here:' + rowData.id)
              }
            }}
          />
        ),
      },
    ]

    return (
      <Dialog
        onClose={handleClose}
        open={open}
        maxWidth={'md'}
        fullWidth={true}
      >
        <div className="p-24">
          <h4 className="mb-20">{t('general.saveUpdate')}</h4>
          <ValidatorForm ref="form" onSubmit={this.handleFormSubmit}>
            <Grid className="mb-16" container spacing={2}>
              <Grid item sm={6} xs={12}>
                <TextValidator
                  className="w-100 mb-16"
                 
                  label={<span><span style={{color:"red"}}>*</span>{t('user.displayName')}</span>}
                  onChange={(displayName) =>
                    this.handleChange(displayName, 'displayName')
                  }
                  type="text"
                  name="name"
                  value={person ? person.displayName : ''}
                  validators={['required']}
                  errorMessages={['this field is required']}
                />
              </Grid>
              <Grid item sm={6} xs={12}>
                <FormControl fullWidth={true}>
                  <InputLabel htmlFor="gender-simple">
                    {t('user.gender')}
                  </InputLabel>
                  <Select
                    value={person ? person.gender : ''}
                    onChange={(gender) => this.handleChange(gender, 'gender')}
                    inputProps={{
                      name: 'gender',
                      id: 'gender-simple',
                    }}
                  >
                    {this.listGender.map((item) => {
                      return (
                        <MenuItem key={item.id} value={item.id}>
                          {item.name}
                        </MenuItem>
                      )
                    })}
                  </Select>
                </FormControl>
              </Grid>
              <Grid item sm={6} xs={12}>
                <TextValidator
                  InputProps={{
                    readOnly: !isAddNew,
                  }}
                  className="w-100 mb-16"
                  label={<span><span style={{color:"red"}}>*</span>{t('user.username')}</span>}
                  
                  // label={t('user.username')}
                  onChange={this.handleChange}
                  type="text"
                  name="username"
                  value={username}
                  validators={['required']}
                  errorMessages={['this field is required']}
                />
              </Grid>
              <Grid item sm={6} xs={12}>
                <TextValidator
                  className="w-100 mb-16"
                  label={<span><span style={{color:"red"}}>*</span>{t('user.email')}</span>}
                  
                  onChange={this.handleChange}
                  type="email"
                  name="email"
                  value={email}
                  validators={['required', 'isEmail']}
                  errorMessages={[
                    'This field is required',
                    'Email is not valid',
                  ]}
                />
              </Grid>
              <Grid item sm={12} xs={12}>
                {listRole && (
                  <Autocomplete
                    style={{ width: '100%' }}
                    multiple
                    id="combo-box-demo"
                    defaultValue={roles}
                    options={listRole}
                    getOptionSelected={(option, value) =>
                      option.id === value.id
                    }
                    getOptionLabel={(option) => option.authority}
                    onChange={(event, value) => {
                      this.selectRoles(value)
                    }}
                    renderInput={(params) => (
                      <TextValidator
                        {...params}
                        value={roles}
                        // label={t('user.role')}
                        label={<span><span style={{color:"red"}}>*</span>{t('user.role')}</span>}
                        
                        fullWidth
                        validators={['required']}
                        errorMessages={[t('user.please_select_permission')]}
                      />
                    )}
                  />
                )}
              </Grid>
              {!isAddNew && (
                <Grid item sm={6} xs={12}>
                  <FormControlLabel
                    value={changePass}
                    className="mb-16"
                    name="changePass"
                    onChange={(changePass) =>
                      this.handleChange(changePass, 'changePass')
                    }
                    control={<Checkbox checked={changePass} />}
                    label={t('user.changePass')}
                  />
                </Grid>
              )}
              <Grid item sm={6} xs={12}>
                <FormControlLabel
                  value={active}
                  className="mb-16"
                  name="active"
                  onChange={(active) => this.handleChange(active, 'active')}
                  control={<Checkbox checked={active} />}
                  label={t('user.active')}
                />
              </Grid>
              {changePass != null && changePass == true ? (
                <Grid container spacing={2}>
                  <Grid item sm={6} xs={12}>
                    <TextValidator
                      className="mb-16 w-100"
                      label={<span><span style={{color:"red"}}>*</span>{t('user.pass')}</span>}

                      // label={t('password')}
                      variant="outlined"
                      onChange={this.handleChange}
                      name="password"
                      type="password"
                      value={password}
                      validators={['required']}
                      errorMessages={['This field is required']}
                    />
                  </Grid>
                  <Grid item sm={6} xs={12}>
                    <TextValidator
                      className="mb-16 w-100"
                      label={<span><span style={{color:"red"}}>*</span>{t('user.re_pass')}</span>}

                      // label={t('re_password')}
                      variant="outlined"
                      onChange={this.handleChange}
                      name="confirmPassword"
                      type="password"
                      value={confirmPassword}
                      validators={['required', 'isPasswordMatch']}
                      errorMessages={[
                        'This field is required',
                        'Password mismatch',
                      ]}
                    />
                  </Grid>
                </Grid>
              ) : (
                <div></div>
              )}

              <Grid item md={6} sm={6} xs={6}>
                {/* <TextValidator
                  InputProps={{
                    readOnly: true,
                  }}
                  label={t("user.department")}
                  className="w-80  mr-16"
                  value={department != null ? department.name : ''}
                /> */}
                <Button
                  className=" mt-10"
                  variant="contained"
                  color="primary"
                  onClick={() =>
                    this.setState({ openSelectDepartmentPopup: true, item: {} })
                  }
                >
                  {t('general.select')}
                </Button>
                {openSelectDepartmentPopup && (
                  <SelectDepartmentPopup
                    open={openSelectDepartmentPopup}
                    handleSelect={this.handleSelectUseDepartment}
                    selectedItem={department != null ? department : {}}
                    handleClose={this.handleSelectDepartmentPopupClose}
                    t={t}
                    i18n={i18n}
                  />
                )}
              </Grid>
              <Grid item sm={12} xs="12">
                <MaterialTable
                  title={t('manage.departmentList')}
                  data={userDepartments}
                  columns={columnsUserDepartment}
                  options={{
                    selection: false,
                    actionsColumnIndex: 0,
                    paging: false,
                    search: false,
                  }}
                  components={{
                    Toolbar: (props) => <MTableToolbar {...props} />,
                  }}
                  onSelectionChange={(rows) => {
                    this.data = rows
                  }}
                />
              </Grid>
            </Grid>

            <DialogActions>
              <div className="flex flex-space-between flex-middle">
                <Button
                  variant="contained"
                  color="secondary"
                  className="mr-36"
                  onClick={() => this.props.handleClose()}
                >
                  {t('general.cancel')}
                </Button>
                <Button variant="contained" color="primary" type="submit">
                  {t('general.save')}
                </Button>
              </div>
            </DialogActions>
          </ValidatorForm>
        </div>
      </Dialog>
    )
  }
}

export default UserEditorDialog
