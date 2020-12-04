import React, { Component } from "react";
import {
  Dialog,
  Button,
  Grid,
  FormControlLabel,
  Switch
} from "@material-ui/core";
import { ValidatorForm, TextValidator } from "react-material-ui-form-validator";
import { getByPage, deleteItem, saveItem,getItemById} from "./LocationService";
import { generateRandomId } from "utils";
import { useTranslation, withTranslation, Trans } from 'react-i18next';
class LocationEditorDialog extends Component {
  state = {
    name: "",
    code: "",
    level:0,
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
      isActive
    } = this.state;

    let { open, handleClose} = this.props;
    return (
      <Dialog onClose={handleClose} open={open}>
        <div className="p-24">
          <h4 className="mb-20">{t("CommonInformation.SaveUpdate")}</h4>
          <ValidatorForm ref="form" onSubmit={this.handleFormSubmit}>
            <Grid className="mb-16" container spacing={4}>
              <Grid item sm={6} xs={12}>
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
              <Button onClick={() => this.handleFormSubmit()}>Cancel</Button>
            </div>
          </ValidatorForm>
        </div>
      </Dialog>
    );
  }
}

export default LocationEditorDialog;
