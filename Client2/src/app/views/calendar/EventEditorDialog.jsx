import React, { Component } from "react";
import { Dialog, IconButton, Button, Icon, Grid } from "@material-ui/core";
import { ValidatorForm, TextValidator } from "react-material-ui-form-validator";
import { MuiPickersUtilsProvider, DateTimePicker } from "@material-ui/pickers";
import "date-fns";
import DateFnsUtils from "@date-io/date-fns";
import { addNewEvent, updateEvent, deleteEvent } from "./CalendarService";
import EventEditor from "./EventEditor";
export default class EventEditorDialog extends Component {
  state = {
    title: "",
    startTime: "",
    endTime: "",
    location: "",
    note: ""
  };

  handleChange = event => {
    event.persist();
    this.setState({
      [event.target.name]: event.target.value
    });
  };

  handleFormSubmit = () => {
    let { id } = this.state;
    if (id) {
      updateEvent({
        ...this.state
      }).then(() => {
        this.props.handleOKClose();
      });
    } else {
      addNewEvent({
        ...this.state
      }).then(() => {
        this.props.handleClose();
        this.props.handleOKClose();
      });
    }
  };

  handleDeleteEvent = () => {
    if (this.state.id) {
      deleteEvent(this.state).then(() => {
        this.props.handleClose();
      });
    }
  };
  handleDiaglogClose = () => {
    this.props.handleClose();
  };
  handleDateChange = (date, name) => {
    this.setState({
      [name]: date
    });
  };

  generateRandomId = () => {
    let tempId = Math.random().toString();
    let id = tempId.substr(2, tempId.length - 1);
    return id;
  };

  componentWillMount() {
    this.setState({
      ...this.props.event
    });
  }

  render() {
    let { title, startTime, endTime, location, note,chairman } = this.state;
    let { open, handleClose,handleOKClose,event,t } = this.props;

    return (
      <Dialog  open={open} maxWidth="md" fullWidth={true}>
          <EventEditor t={t} event={event} handleClose={handleClose} handleOKClose={handleOKClose}/>
      </Dialog>
    );
  }
}

//export default EventEditorDialog;
