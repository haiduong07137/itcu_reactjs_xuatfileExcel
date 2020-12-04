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
  updateData
} from "./StudentScoresService";
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
    updateData({
        ...this.state
      }).then(() => {
        this.props.handleClose();
      }); 
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
      isActive
    } = this.state;
    let { open, handleClose } = this.props;

    return (
      <Dialog onClose={handleClose} open={open}>
        <div className="p-24">
          <h4 className="mb-20">Update Member</h4>
          <ValidatorForm ref="form" onSubmit={this.handleFormSubmit}>
            <Grid className="mb-16" container spacing={4}>
              <Grid item sm={6} xs={12}>
                <TextValidator
                  className="w-100 mb-16"
                  label="Mã sinh viên"
                  onChange={this.handleChange}
                  type="text"
                  name="code"
                  disabled={true}
                  value={code}
                  validators={["required"]}
                  errorMessages={["this field is required"]}
                />
                <TextValidator
                  className="w-100 mb-16"
                  label="Họ và tên"
                  onChange={this.handleChange}
                  type="text"
                  name="displayName"
                  value={displayName}
                  validators={["required"]}
                  errorMessages={["this field is required"]}
                />

                <TextValidator
                  className="w-100 mb-16"
                  label="Lớp"
                  onChange={this.handleChange}
                  type="text"
                  name="studentClass"
                  value={studentClass}
                  validators={["required"]}
                  errorMessages={["this field is required"]}
                />

                <TextValidator
                  className="w-100 mb-16"
                  label="Ngành"
                  onChange={this.handleChange}
                  type="text"
                  name="majors"
                  value={majors}
                  validators={["required"]}
                  errorMessages={["this field is required"]}
                />
              </Grid>

              <Grid item sm={6} xs={12}>
                <TextValidator
                  className="w-100 mb-16"
                  label="Khoá học"
                  onChange={this.handleChange}
                  type="text"
                  name="course"
                  value={course}
                  validators={["required"]}
                  errorMessages={["this field is required"]}
                />
                <TextValidator
                  className="w-100 mb-16"
                  label="Nơi sinh"
                  onChange={this.handleChange}
                  type="text"
                  name="placeOfBirth"
                  value={placeOfBirth}
                  validators={["required"]}
                  errorMessages={["this field is required"]}
                />
                <TextValidator
                  className="w-100 mb-16"
                  label="Hệ đào tạo"
                  onChange={this.handleChange}
                  type="text"
                  name="trainingSystem"
                  value={trainingSystem}
                  validators={["required"]}
                  errorMessages={["this field is required"]}
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
