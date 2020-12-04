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
import { updateData } from './StudentInfoService'
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
toast.configure();
class MemberEditorDialog extends Component {
  state = {

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
      toast.info("Cập nhật thành công");
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
      fullName,
      birthday,
      gender,
      placeOfBirth,
      ethnicity,
      nationality,
      allCredit,
      scoreTen,
      scoreFour,
      rankGraduating,
      studentClass,
      trainingSystem,
      majors,
      dateJoin,
      typeTraining,
      dateOut,
      numberDiploma,
      note, trainingLanguage, levelTraining
    } = this.state;
    let { open, handleClose } = this.props;

    return (
      <Dialog onClose={handleClose} open={open} fullWidth={true}
        maxWidth="md">
        <div className="p-24">
          <h4 className="mb-20">Cập nhật sinh viên</h4>
          <ValidatorForm ref="form" onSubmit={this.handleFormSubmit}>
            <Grid className="mb-16" container spacing={4}>
              <Grid item sm={4} xs={12}>
                <TextValidator
                  className="w-100 mb-16"
                  label="Mã sinh viên"
                  onChange={this.handleChange}
                  type="text"
                  disabled={true}
                  name="code"
                  value={code}
                  validators={["required"]}
                  errorMessages={["this field is required"]}
                />
                <TextValidator
                  className="w-100 mb-16"
                  label="Tên sinh viên"
                  onChange={this.handleChange}
                  type="text"
                  name="fullName"
                  value={fullName}
                  validators={["required"]}
                  errorMessages={["this field is required"]}
                />

                <TextValidator
                  className="w-100 mb-16"
                  label="Ngày sinh"
                  onChange={this.handleChange}
                  type="text"
                  name="birthday"
                  value={birthday}
                  validators={["required"]}
                  errorMessages={["this field is required"]}
                />

                <TextValidator
                  className="w-100 mb-16"
                  label="Giới tính"
                  onChange={this.handleChange}
                  type="text"
                  name="gender"
                  value={gender}
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
                  label="Dân tộc"
                  onChange={this.handleChange}
                  type="text"
                  name="ethnicity"
                  value={ethnicity}
                  validators={["required"]}
                  errorMessages={["this field is required"]}
                />
                
                <TextValidator
                  className="w-100 mb-16"
                  label="Ngôn ngữ đào tạo"
                  onChange={this.handleChange}
                  type="text"
                  name="trainingLanguage"
                  value={trainingLanguage}
                  validators={["required"]}
                  errorMessages={["this field is required"]}
                />
              </Grid>
              <Grid item sm={4} xs={12}>
                <TextValidator
                  className="w-100 mb-16"
                  label="Quốc tịch"
                  onChange={this.handleChange}
                  type="text"
                  name="nationality"
                  value={nationality}
                  validators={["required"]}
                  errorMessages={["this field is required"]}
                />
                <TextValidator
                  className="w-100 mb-16"
                  label="TCTL"
                  onChange={this.handleChange}
                  type="text"
                  name="allCredit"
                  value={allCredit}
                  validators={["required"]}
                  errorMessages={["this field is required"]}
                />
                <TextValidator
                  className="w-100 mb-16"
                  label="Thang điểm 10"
                  onChange={this.handleChange}
                  type="text"
                  name="scoreTen"
                  value={scoreTen}
                  validators={["required"]}
                  errorMessages={["this field is required"]}
                />

                <TextValidator
                  className="w-100 mb-16"
                  label="Thang điểm 4"
                  onChange={this.handleChange}
                  type="text"
                  name="scoreFour"
                  value={scoreFour}
                  validators={["required"]}
                  errorMessages={["this field is required"]}
                />
                <TextValidator
                  className="w-100 mb-16"
                  label="Xếp loại tốt nghiệp"
                  onChange={this.handleChange}
                  type="text"
                  name="rankGraduating"
                  value={rankGraduating}
                  validators={["required"]}
                  errorMessages={["this field is required"]}
                />
                <TextValidator
                  className="w-100 mb-16"
                  label="Lớp học"
                  onChange={this.handleChange}
                  type="text"
                  name="studentClass"
                  value={studentClass}
                  validators={["required"]}
                  errorMessages={["this field is required"]}
                />
                 <TextValidator
                  className="w-100 mb-16"
                  label="Trình độ đào tạo"
                  onChange={this.handleChange}
                  type="text"
                  name="levelTraining"
                  value={levelTraining}
                  validators={["required"]}
                  errorMessages={["this field is required"]}
                />
              </Grid>

              <Grid item sm={4} xs={12}>
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
                <TextValidator
                  className="w-100 mb-16"
                  label="Ngành học"
                  onChange={this.handleChange}
                  type="text"
                  name="majors"
                  value={majors}
                  validators={["required"]}
                  errorMessages={["this field is required"]}
                />
                <TextValidator
                  className="w-100 mb-16"
                  label="Ngày nhập học"
                  onChange={this.handleChange}
                  type="text"
                  name="dateJoin"
                  value={dateJoin}
                  validators={["required"]}
                  errorMessages={["this field is required"]}
                />
                <TextValidator
                  className="w-100 mb-16"
                  label="Ngày ra trường"
                  onChange={this.handleChange}
                  type="text"
                  name="dateOut"
                  value={dateOut}
                  validators={["required"]}
                  errorMessages={["this field is required"]}
                />
                <TextValidator
                  className="w-100 mb-16"
                  label="Loại đào tạo"
                  onChange={this.handleChange}
                  type="text"
                  name="typeTraining"
                  value={typeTraining}
                  validators={["required"]}
                  errorMessages={["this field is required"]}
                />

                <TextValidator
                  className="w-100 mb-16"
                  label="Số hiệu bằng"
                  onChange={this.handleChange}
                  type="text"
                  name="numberDiploma"
                  value={numberDiploma}
                  validators={["required"]}
                  errorMessages={["this field is required"]}
                />

              </Grid>
 
              <Grid item sm={12} xs={12}>
                <Grid item lg={12} md={12} sm={12} xs={12}>
                  <TextValidator
                    variant="outlined"
                    className="w-100 mb-16 mr-16 ml-16"
                    label="Ghi chú"
                    variant="outlined"
                    multiline
                    rows={6}
                    rowsMax={6} onChange={this.handleChange}
                    type="text"
                    name="note"
                    value={note}
                  />
                </Grid>

              </Grid>
            </Grid>

            <div className="flex flex-space-between flex-middle">
              <Button variant="contained" color="primary" type="submit">
                Save
              </Button>
              <Button color="secondary" onClick={() => this.props.handleClose()}>Cancel</Button>
            </div>
          </ValidatorForm>
        </div>
      </Dialog >
    );
  }
}

export default MemberEditorDialog;
