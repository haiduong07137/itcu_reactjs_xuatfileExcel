import React, { Component } from "react";
import {
  Dialog,
  Button,
  Grid,
  FormControlLabel,
  Switch
} from "@material-ui/core";
import { ValidatorForm, TextValidator } from "react-material-ui-form-validator";
import { generateRandomId } from "utils"; 

import {
  updateData,addNewData
} from "./SignatureService";
class MemberEditorDialog extends Component {
  constructor(props) {
    super(props)
    this.state = {
      isActive: false,
      code: ''
    }
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
  
    let { id } = this.state
    if (!id) {
     
      addNewData({
        ...this.state,
      }).then(() => {
        this.props.handleOKEditClose()
      })
    } else {
      updateData({
        ...this.state,
      }).then((data) => { 
        this.props.handleOKEditClose()
      },)
    }
  
  
  
   
  };




  componentWillMount() {
    this.setState({
      ...this.props.item,
    })
  }

  render() {
    let {
      code,
      displayName,
      studentClass,
      majors,
      course,
      placeOfBirth,
      trainingSystem,
      isActive,
      signPrincipal,
      departName,
      signName
    } = this.state;
    let { open, handleClose } = this.props;
    let {  handleOKEditClose, t, i18n } = this.props
    return (
      <Dialog onClose={handleClose} open={open} fullWidth={false}
      maxWidth="lg">
        <div className="p-24">
          <h4 className="mb-20">Thông tin chữ ký</h4>
          <ValidatorForm ref="form" onSubmit={this.handleFormSubmit}>
            <Grid className="mb-16" container spacing={4}>
              <Grid item sm={12} xs={12}>
                <TextValidator
                  className="w-100 mb-16"
                  label="TL Hiệu trưởng"
                  onChange={this.handleChange}
                  type="text"
                  name="signPrincipal"
                  value={signPrincipal} 
                />
                <TextValidator
                  className="w-100 mb-16"
                  label="Tên phòng ban TL"
                  onChange={this.handleChange}
                  type="text"
                  name="departName"
                  value={departName} 
                />

                <TextValidator
                  className="w-100 mb-16"
                  label="Tên người TL"
                  onChange={this.handleChange}
                  type="text"
                  name="signName"
                  value={signName} 
                />
 
                
              </Grid>
            </Grid>

            <div className="flex flex-space-between flex-middle">
              <Button variant="contained" color="primary" type="submit">
                Save
              </Button>
              <Button onClick={() => this.props.handleClose()}>Cancel</Button>
            </div>
          </ValidatorForm>
        </div>
      </Dialog>
    );
  }
}

export default MemberEditorDialog;
