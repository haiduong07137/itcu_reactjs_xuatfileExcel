import React, { Component } from "react";
import {
  Dialog,
  Button,
  Grid,
  DialogActions
} from "@material-ui/core";
import { ValidatorForm, TextValidator } from "react-material-ui-form-validator";
import { getAllStores, addNewOrUpdateStore, searchByPage, checkCode } from './StoreService';
import DialogTitle from '@material-ui/core/DialogTitle';
import DialogContent from '@material-ui/core/DialogContent';
import Draggable from 'react-draggable';
import Paper from '@material-ui/core/Paper';
function PaperComponent(props) {
  return (
    <Draggable handle="#draggable-dialog-title" cancel={'[class*="MuiDialogContent-root"]'}>
      <Paper {...props} />
    </Draggable>
  );
}
class StoreEditorDialog extends Component {
  state = {
    name: "",
    code: "",
    address: ""
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

    checkCode(id, code).then((result) => {
      //Nếu trả về true là code đã được sử dụng
      if (result.data) {
        alert("Code đã được sử dụng");
      } else {
        if (id) {
          addNewOrUpdateStore({
            ...this.state
          }).then(() => {
            this.props.handleOKEditClose();
          });
        } else {
          addNewOrUpdateStore({
            ...this.state
          }).then(() => {
            this.props.handleOKEditClose();
          });
        }
      }
    });
  };

  componentWillMount() {
    let { open, handleClose, item } = this.props;
    this.setState({
      ...this.props.item
    }, function () {

    }
    );
  }
  componentDidMount() {
  }

  render() {
    let { open, handleClose, handleOKEditClose, t, i18n } = this.props;
    let {
      name,
      address,
      code
    } = this.state;

    return (
      <Dialog open={open} PaperComponent={PaperComponent} maxWidth="md">
        <DialogTitle style={{ cursor: 'move' }} id="draggable-dialog-title">
          <span className="mb-20">{t('general.saveUpdate')}</span>
        </DialogTitle>
        <ValidatorForm ref="form" onSubmit={this.handleFormSubmit}>
        <DialogContent>
            <Grid className="mb-16" container spacing={4}>
              <Grid item sm={12} xs={12}>
                <TextValidator
                  className="w-100 mb-16"
                  
                  label={<span><span style={{color:"red"}}>*</span>{t('Store.code')}</span>}
                  onChange={this.handleChange}
                  type="text"
                  name="code"
                  value={code}
                  validators={["required"]}
                  errorMessages={["This field is required"]}
                />
                <TextValidator
                  className="w-100 mb-16"
                  label={<span><span style={{color:"red"}}>*</span>{t('Store.name')}</span>}

                  onChange={this.handleChange}
                  type="text"
                  name="name"
                  value={name}
                  validators={["required"]}
                  errorMessages={["This field is required"]}
                />
                <TextValidator
                  className="w-100 mb-16"
                  label={<span><span style={{color:"red"}}>*</span>{t('Store.address')}</span>}
                  onChange={this.handleChange}
                  type="text"
                  name="address"
                  value={address}
                  validators={["required"]}
                  errorMessages={["This field is required"]}
                />
              </Grid>
            </Grid>
        </DialogContent>
        <DialogActions>
          <div className="flex flex-space-between flex-middle">
            <Button
              className="mr-36"
              variant="contained"
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

export default StoreEditorDialog;
