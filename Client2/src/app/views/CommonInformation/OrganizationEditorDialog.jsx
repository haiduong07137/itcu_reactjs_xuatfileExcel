import React, { Component } from "react";
import {
  Dialog,
  Button,
  Grid,
  FormControlLabel,
  Switch
} from "@material-ui/core";
import { ValidatorForm, TextValidator,TextField  } from "react-material-ui-form-validator";
import { getByPage, deleteItem, saveItem,getItemById} from "./OrganizationService";
import OrganizationSearchDialog from "./OrganizationSearchDialog";
import SearchOrgDialog from "./SearchOrgDialog";
import { generateRandomId } from "utils";
import { useTranslation, withTranslation, Trans } from 'react-i18next';
import { Breadcrumb, ConfirmationDialog } from "egret";
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
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
class OrganizationEditorDialog extends Component {
  state = {
    name: "",
    code: "",
    level:0,
    parent:{},
    shouldOpenSearchDialog:false,
    shouldOpenConfirmationDialog: false,
    isActive: false
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
    saveItem({
      ...this.state
    }).then(() => {
      this.props.handleOKEditClose();
    });
  };

  componentWillMount() {
    let { open, handleClose,item } = this.props;
    this.setState(item);
  }

  handleSearchDialogClose = () => {
    this.setState({
      shouldOpenSearchDialog: false
    });
  };
  handleSelect=(item)=>{
    //alert('Test');
    this.setState({parent:item});
    this.handleSearchDialogClose();
  }
  render() {
    let {
      id,
      name,
      code,
      level,
      isActive,
      parent,
      shouldOpenSearchDialog,
      shouldOpenConfirmationDialog,
    } = this.state;

    let { open, handleClose,handleOKEditClose,t, i18n} = this.props;
    return (
      <Dialog onClose={handleClose} open={open} PaperComponent={PaperComponent}>
        <div className="p-24">
        <DialogTitle style={{ cursor: 'move' }} id="draggable-dialog-title">
          <h4 className="mb-20">{t("CommonInformation.SaveUpdate")}</h4>
        </DialogTitle>
        <DialogContent>
          <ValidatorForm ref="form" onSubmit={this.handleFormSubmit}>
            <Grid className="mb-16" container spacing={4}>
              <Grid item sm={6} xs={12}>
                <TextValidator id="parent" value={parent!=null ? parent.name:''}/>
                <Button
                  className="mb-16"
                  variant="contained"
                  color="primary"
                  onClick={() => this.setState({ shouldOpenSearchDialog: true,item:{} })}
                >
                  {t('Select')}
                </Button>                   
                  {shouldOpenSearchDialog && (
                    <OrganizationSearchDialog open={this.state.shouldOpenSearchDialog} handleSelect={this.handleSelect} selectedItem={parent}
                    handleClose={this.handleSearchDialogClose} t={t} i18n={i18n}/>
                  )
                  }  
                <TextValidator
                  className="w-100 mb-16"
                  label="Name"
                  onChange={this.handleChange}
                  type="text"
                  name="name"
                  value={name}
                  validators={["required"]}
                  errorMessages={["this field is required"]}
                />
                <TextValidator
                  className="w-100 mb-16"
                  label="Code"
                  onChange={this.handleChange}
                  type="text"
                  name="code"
                  value={code}
                  validators={["required"]}
                  errorMessages={["this field is required"]}
                />
                <TextValidator
                  className="w-100 mb-16"
                  label="Level"
                  onChange={this.handleChange}
                  type="number"
                  name="level"
                  value={level}
                  validators={["required"]}
                  errorMessages={["this field is required"]}
                />
              </Grid>
              <Grid>
                <FormControlLabel
                  className="my-20"
                  control={
                    <Switch
                      checked={isActive}
                      onChange={event => this.handleChange(event, "switch")}
                    />
                  }
                  label="Active Customer"
                />
              </Grid>
            </Grid>

            <div className="flex flex-space-between flex-middle">
              <Button variant="contained" color="primary" type="submit">
                Save
              </Button>
              <Button onClick={() => handleClose()}>Cancel</Button>
            </div>
          </ValidatorForm>
          </DialogContent>

        </div>
      </Dialog>
    );
  }
}

export default OrganizationEditorDialog;
