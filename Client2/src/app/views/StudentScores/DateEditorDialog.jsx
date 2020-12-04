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
 
import { searchByPage, getExcel, getListMajorClass } from "./StudentScoresService";
class DateEditorDialog extends Component {
  state = {
    day: "",
    month: "",
    year: "",
    loading:false,
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
    this.getFileExcel();
  };

  // componentWillMount() {
  //   getUserById(this.props.uid).then(data => this.setState({ ...data.data }));
  // }

  getFileExcel = () => {
    this.setState({ loading: true })
    var searchObject = {}
    searchObject.keyword = this.state.keyword
    searchObject.pageIndex = this.state.page + 1
    searchObject.pageSize = this.state.rowsPerPage
    searchObject.majors = this.state.majors
    searchObject.studentClass = this.state.studentClass
    searchObject.id = this.state.studentId 
    searchObject.day = this.state.day
    searchObject.month = this.state.month
    searchObject.year = this.state.year 
    getExcel(searchObject).then((result) => {
      const url = window.URL.createObjectURL(new Blob([result.data]))
      const link = document.createElement('a')
      link.href = url
      link.setAttribute('download', 'list.xls')
      document.body.appendChild(link)
      link.click();
      this.setState({ loading: false })
      this.props.handleClose();
    }
    )

  }
  render() {
    let {
      day,
      month,
      year,
      isActive,
      loading
    } = this.state;
    let { open, handleClose } = this.props;

    return (
      <Dialog onClose={handleClose} open={open} maxWidth="md" fullWidth={false}>
        <div className="p-24">
          <h4 className="mb-20">Ngày xuất</h4>
          <ValidatorForm ref="form" onSubmit={this.handleFormSubmit}> 
              <Grid item sm={12} xs={12}>
                <TextValidator
                  className="w-100 mb-16"
                  label="Ngày"
                  onChange={this.handleChange}
                  type="text"
                  name="day"
                  value={day}
                  validators={["required"]}
                  errorMessages={["this field is required"]}
                />
                <TextValidator
                  className="w-100 mb-16"
                  label="Tháng"
                  onChange={this.handleChange}
                  type="text"
                  name="month"
                  value={month}
                  validators={["required"]}
                  errorMessages={["this field is required"]}
                />

                <TextValidator
                  className="w-100 mb-16"
                  label="Năm"
                  onChange={this.handleChange}
                  type="text"
                  name="year"
                  value={year}
                  validators={["required"]}
                  errorMessages={["this field is required"]}
                />

                
              
            </Grid>

            <div className="flex flex-space-between flex-middle">
              <Button variant="contained" color="primary" type="submit" disabled={loading}>
                Export Excel
              </Button>
              <Button onClick={() => this.props.handleClose()}>Cancel</Button>
            </div>
          </ValidatorForm>
        </div>
      </Dialog>
    );
  }
}

export default DateEditorDialog;
