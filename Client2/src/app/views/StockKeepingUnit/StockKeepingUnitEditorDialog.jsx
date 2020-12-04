import React, { Component } from "react";
import {
  Dialog,
  Button,
  Grid,
  FormControlLabel,
  Switch, DialogActions
} from "@material-ui/core";
import { ValidatorForm, TextValidator } from "react-material-ui-form-validator";
import { getAllStockKeepingUnits, addNewOrUpdateStockKeepingUnit, searchByPage, checkCode } from './StockKeepingUnitService';
import DialogTitle from '@material-ui/core/DialogTitle';
import DialogContent from '@material-ui/core/DialogContent';
import Draggable from 'react-draggable';
import Paper from '@material-ui/core/Paper';
import NotificationPopup from '../Component/NotificationPopup/NotificationPopup'

function PaperComponent(props) {
  return (
    <Draggable handle="#draggable-dialog-title" cancel={'[class*="MuiDialogContent-root"]'}>
      <Paper {...props} />
    </Draggable>
  );
}
class StockKeepingUnitEditorDialog extends Component {
  state = {
    name: "",
    code: "",
    coefficient: 0,
    shouldOpenNotificationPopup: false
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

  handleFormSubmit = () => {
    let { id } = this.state;
    let { code } = this.state;
    //Nếu trả về false là code chưa sử dụng có thể dùng
    checkCode(id, code).then((result) => {
      //Nếu trả về true là code đã được sử dụng
      if (result.data) {
        this.setState({shouldOpenNotificationPopup: true,
          Notification:"StockKeepingUnit.noti.dupli_code"})
        // alert("Code đã được sử dụng");
      } else {
        if (id) {
          addNewOrUpdateStockKeepingUnit({
            ...this.state
          }).then(() => {
            this.props.handleOKEditClose();
          });
        } else {
          addNewOrUpdateStockKeepingUnit({
            ...this.state
          }).then(() => {
            this.props.handleOKEditClose();
          });
        }
      }
    });
    
  };

  componentWillMount() {

  }
  componentDidMount() {
    let { open, handleClose, item } = this.props;
    this.setState({
      ...this.props.item
    }, function () {

    }
    );
  }

  handleDialogClose =()=>{
    this.setState({shouldOpenNotificationPopup:false,})
  }

  render() {
    let { open, handleClose, handleOKEditClose, t, i18n } = this.props;
    let {
      id,
      name,
      code,
      coefficient,
      shouldOpenNotificationPopup
    } = this.state;
    return (
      <Dialog open={open} PaperComponent={PaperComponent} maxWidth="md">
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
            <Grid className="mb-16" container spacing={4}>
              <Grid item sm={12} xs={12}>
                <TextValidator
                  className="w-100 mb-16"
                  
                  label={<span><span style={{color:"red"}}>*</span>{t('StockKeepingUnit.code')}</span>}
                  onChange={this.handleChange}
                  type="text"
                  name="code"
                  value={code}
                  validators={["required"]}
                  errorMessages={["This field is required"]}
                />
                <TextValidator
                  className="w-100 mb-16"
                  label={<span><span style={{color:"red"}}>*</span>{t('StockKeepingUnit.name')}</span>}
                  onChange={this.handleChange}
                  type="text"
                  name="name"
                  value={name}
                  validators={["required"]}
                  errorMessages={["This field is required"]}
                />
                <TextValidator
                  className="w-100 mb-16"
                  label={t('StockKeepingUnit.coefficient')}
                  onChange={this.handleChange}
                  type="number"
                  name="coefficient"
                  value={coefficient}
                  validators={['minNumber:0', 'matchRegexp:^[0-9]+$']}
                />
              </Grid>
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
    );
  }
}

export default StockKeepingUnitEditorDialog;
