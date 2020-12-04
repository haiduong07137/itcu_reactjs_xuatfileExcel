import React, { Component } from "react";
import {
  Dialog,
  DialogContent,
  Button,
  Grid,
  FormControlLabel,
  Paper,
  DialogTitle,
  Switch
} from "@material-ui/core";
import { ValidatorForm, TextValidator } from "react-material-ui-form-validator";
import { getByPage, deleteItem, saveItem,getItemById} from "./OrganizationService";
import { generateRandomId } from "utils";
import { useTranslation, withTranslation, Trans } from 'react-i18next';
import SearchOrgDialog from "./SearchOrgDialog";
import Draggable from 'react-draggable';
const ViewSearchOrgDialog = withTranslation()(SearchOrgDialog);
function PaperComponent(props) {
  return (
    <Draggable handle="#draggable-dialog-title" cancel={'[class*="MuiDialogContent-root"]'}>
      <Paper {...props} />
    </Draggable>
  );
}
class OrganizationSearchDialog extends Component {
  state = {
    name: "",
    code: "",
    level:0,
    shouldOpenSearchDialog:false,
    isActive: false
  };

  handleFormSubmit = () => {
    let { id } = this.state;
    saveItem({
      ...this.state
    }).then(() => {
      this.props.handleClose();
    });
  };

  componentWillMount() {
    let { open, handleClose,item } = this.props;
    this.setState(item);
  }

  render() {
    const { t, i18n } = this.props;
    let {
      id,
      name,
      code,
      level,
      isActive,
      shouldOpenSearchDialog
    } = this.state;

    let { open, handleClose,handleSelect,selectedItem} = this.props;
    return (
      <Dialog onClose={handleClose} open={open} PaperComponent={PaperComponent}>
        <div stype={{width:700}}>
        <DialogTitle style={{ cursor: 'move' }} id="draggable-dialog-title">
          <h4 className="mb-20">{t("CommonInformation.SaveUpdate")}</h4>
        </DialogTitle>    
        <DialogContent>    
        <div className="p-24">
          <h4 className="mb-20"></h4>
            <div>
              <ViewSearchOrgDialog handleClose={handleClose} handleSelect={handleSelect} selectedItem={selectedItem}/>
            </div>
        </div>
        </DialogContent>
        </div>
      </Dialog>
    );
  }
}

export default OrganizationSearchDialog;
