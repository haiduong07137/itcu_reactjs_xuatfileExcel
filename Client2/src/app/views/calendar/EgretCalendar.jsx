import React, { Component } from "react";
import { Button } from "@material-ui/core";
import { Calendar, Views, momentLocalizer } from "react-big-calendar";
import "react-big-calendar/lib/css/react-big-calendar.css";
import "react-big-calendar/lib/addons/dragAndDrop/styles.css";
import withDragAndDrop from "react-big-calendar/lib/addons/dragAndDrop";
import moment from "moment";
import CalendarHeader from "./CalendarHeader";
import * as ReactDOM from "react-dom";
import { Breadcrumb,ConfirmationDialog } from "egret";
import { getAllEvents, updateEvent,getListEventByWeek,deleteEvent,deleteAllEvent,publishAllEvent,approveAllEvent } from "./CalendarService";
import EventEditorDialog from "./EventEditorDialog";
import MyWeek from './MyWeek';
import { useTranslation, withTranslation, Trans } from 'react-i18next';
import EventTable from './EventTable'
import 'moment/locale/vi';
import CrudTable from '../CRUD/CrudTable';
const localizer = momentLocalizer(moment);
moment.locale('vi', {
  week: {
      dow: 1,
      doy: 1,
  },
});
const DragAndDropCalendar = withDragAndDrop(Calendar);

let viewList = Object.keys(Views).map(key => Views[key]);
const ViewEventTable = withTranslation()(EventTable);
class EgretCalendar extends Component {
  state = {
    events: [],
    shouldShowEventDialog: false,
    shouldOpenConfirmationDialog:false,
    shouldDeleteAllConfirmationDialog:false,
    newEvent: null,
    culture:"vi",
    date:new Date(),
    eventDateList:[],
    selectAllItem:false,
    askMesssage:'',
    confirmType:1,
    isAdminView:true
  };

  constructor(props) {
    super(props);
    this.headerComponentRef = React.createRef();
  }

  componentDidMount() {
    this.updateCalendar();
  }

  async updateCalendar(){
      this.getListEventDate(this.state.date);
  };

  handleDialogClose = () => {
    this.setState({selectAllItem:false, shouldShowEventDialog: false, shouldOpenConfirmationDialog:false,shouldDeleteAllConfirmationDialog:false });    
  };
  handleDelete = (id) => {
    this.setState({
      id:id,
      askMesssage:'deleteQuestion',
      shouldOpenConfirmationDialog: true      
    });
  };
  handleApprove = () => {
    this.setState({
      confirmType:2,
      askMesssage:'approveQuestion',
      shouldOpenConfirmationDialog: true      
    });
  };
  handlePublish = () => {
    this.setState({
      confirmType:3,
      askMesssage:'publishQuestion',
      shouldOpenConfirmationDialog: true      
    });
  };  
  handleOKClose = () => {
    this.updateCalendar();
    this.handleDialogClose();
  };
  handleConfirmationResponse = () => {
    if(this.state.confirmType==1){
      deleteEvent(this.state.id).then(() => {
        this.updateCalendar();
        this.handleDialogClose();
      }
      );
    }else if(this.state.confirmType==2){
        var list =[];
        for(var i=0;i<this.state.eventDateList.length;i++){
          var events =this.state.eventDateList[i].events;
          for(var j=0;j<events.length;j++){
            if(events[j].checked !=null && events[j].checked){
              list.push(events[j]);
            }
          }                
      }
      approveAllEvent(list).then(() => {
        this.updateCalendar();
        this.handleDialogClose();
      })
    }else if(this.state.confirmType==3){
      var list =[];
      for(var i=0;i<this.state.eventDateList.length;i++){
        var events =this.state.eventDateList[i].events;
        for(var j=0;j<events.length;j++){
          if(events[j].checked !=null && events[j].checked){
            list.push(events[j]);
          }
        }              
      }
      publishAllEvent(list).then(() => {
        this.updateCalendar();
        this.handleDialogClose();
      })
    }
  }

  handleDeleteAllResponse = () => {
    var numberCheckedItem =0;
    var list =[];
    for(var i=0;i<this.state.eventDateList.length;i++){
      var events =this.state.eventDateList[i].events;
      for(var j=0;j<events.length;j++){
        if(events[j].checked !=null && events[j].checked){
          numberCheckedItem++;
          list.push(events[j]);
        }
      }
    }
    deleteAllEvent(list).then(() => {
        this.updateCalendar();
        this.handleDialogClose();
      }
    );
  };


  handleEventMove = event => {
    this.handleEventResize(event);
  };

  handleEventResize = event => {
    updateEvent(event).then(() => {
      this.updateCalendar();
    });
  };

  openNewEventDialog = ({ action, ...event }) => {
    if (action === "doubleClick") {
      this.setState({
        newEvent: event,
        shouldShowEventDialog: true
      });
    }
  };

  openExistingEventDialog = event => {
    this.setState({
      newEvent: event,
      shouldShowEventDialog: true
    });
  };
  getListEventDate = (date) => {
    var strDate = moment(date).format('YYYY-MM-DD');
    getListEventByWeek(strDate).then(({data})=>{
      this.setState({eventDateList:data});
        var listEvents =[];
        for(var j=0;j<data.length;j++){
          var eventDate = data[j];
          for(var i=0;i<eventDate.events.length;i++){
            eventDate.events[i].checked=false;
            listEvents.push(eventDate.events[i]);          
          }
        }
        return listEvents;      
      }).then(listEvents=>{
        this.setState({events:listEvents}); 
      });
  };
  handleNavigate =(date, view)=>{
    this.setState({date:date},()=>{
      this.updateCalendar();
    });
  }
  handleOnViewChange  =(view)=>{
    alert('handleOnViewChange:'+this.state.date);
  }

  onAddNewClick=(date)=>{
//    alert(date);
    this.openNewEventDialog({
      action: "doubleClick",
      startTime: date,
      endTime: date
    })
  }
  handleAdminModeChange  =()=>{
    this.setState({isAdminView:!this.state.isAdminView})
  }
  handleClick = (event, item) => {
    let eventDateList =  this.state.eventDateList;
    if(item.checked==null){
      item.checked=true;
    }else {
      item.checked=!item.checked;
    }
    var selectAllItem=true;
    for(var i=0;i<eventDateList.length;i++){
      var events=eventDateList[i].events;
      for(var j=0;j<events.length;j++){
        if(events[j].checked==null || events[j].checked==false){
          selectAllItem=false;
        }
        if(events[j].id==item.id){
          events[j]=item;
        }
      }
    }
    this.setState({selectAllItem:selectAllItem, eventDateList:eventDateList});
  };

  handleSelectAllClick = (event) => {
    let eventDateList =  this.state.eventDateList;
    for(var i=0;i<eventDateList.length;i++){
      var events=eventDateList[i].events;
      for(var j=0;j<events.length;j++){
        events[j].checked=!this.state.selectAllItem;
      }
    }
    this.setState({selectAllItem:!this.state.selectAllItem, eventDateList:eventDateList});
  }; 
  render() {
    let { events, newEvent, shouldShowEventDialog,shouldOpenConfirmationDialog,culture,eventDateList,shouldDeleteAllConfirmationDialog} = this.state;
    let {t, i18n } = this.props;
    let formats = {
      timeGutterFormat: 'HH:mm',
      monthHeaderFormat: 'MM-YYYY',
      dayHeaderFormat:({start, end},culture,local)=> 'Ngày :'+ local.format(start, 'DD/MM/YYYY', culture),
      dayRangeHeaderFormat: ({
        start,
        end
      }, culture, local) =>
      local.format(start, 'DD/MM/YYYY', culture) + '-' +
      local.format(end, 'DD/MM/YYYY', culture),
      eventTimeRangeFormat: ({
          start,
          end
        }, culture, local) =>
        local.format(start, 'HH:mm', culture) + '-' +
        local.format(end, 'HH:mm', culture),
      //dayFormat: 'MM-DD' + ' ' + '星期' + 'dd',
      agendaTimeRangeFormat: ({
          start,
          end
        }, culture, local) =>
        local.format(start, 'HH:mm', culture) + '-' +
        local.format(end, 'HH:mm', culture),
        //agendaDateFormat: 'MM-DD' + ' ' + '星期' + 'dd',
  
    }
    if(eventDateList==null){
      eventDateList=[];
    }
    
    return this.state.events && (
      <div className="m-sm-30">
        <div  className="mb-sm-30">
          <Breadcrumb routeSegments={[{ name: "Calendar" }]} />
        </div>

        <Button
          className="mb-16"
          variant="contained"
          color="secondary"
          onClick={() =>
            this.openNewEventDialog({
              action: "doubleClick",
              start: new Date(),
              end: new Date()
            })
          }
        >
          Add Event
        </Button>

        <Button
          className="mb-16"
          variant="contained"
          color="secondary"
          onClick={() =>this.setState({shouldDeleteAllConfirmationDialog:true})}
        >
          {t('Delete')}
        </Button>

        <Button
          className="mb-16"
          variant="contained"
          color="secondary"
          onClick={() =>this.handleApprove()}
        >
          {t('Approve')}
        </Button> 
        <Button
          className="mb-16"
          variant="contained"
          color="secondary"
          onClick={() =>this.handlePublish()}
        >
          {t('Publish')}
        </Button>                         
        <div>
          <div ref={this.headerComponentRef} />
          {/* <CrudTable eventDateList={eventDateList}/> */}
          <DragAndDropCalendar
            selectable
            localizer={localizer}
            events={this.state.events}
            onEventDrop={this.handleEventMove}
            resizable
            onEventResize={this.handleEventResize}
            defaultView={Views.WORK_WEEK}
            defaultDate={new Date()}
            startAccessor="start"
            endAccessor="end"
            t={t}
            formats={formats}
            culture={this.state.culture}
            //views={viewList}
            views={{ work_week:EventTable,month: true,week: true, day:true,agenda: true}}
            step={60}
            showMultiDayTimes
            handleDelete = {this.handleDelete}            
            components={{
              toolbar: props => {
                
                return <CalendarHeader {...props} t={t} handleAdminModeChange={this.handleAdminModeChange} isAdminView ={this.state.isAdminView}/>;
              }
            }}
            selectAllItem={this.state.selectAllItem}
            eventDateList  ={this.state.eventDateList}
            onNavigate={this.handleNavigate}
            onView ={this.handleOnViewChange }
            handleEventMove = {this.handleEventMove}
            onSelectEvent={event => {
              this.openExistingEventDialog(event);
            }}
            onSelectSlot={slotDetails => this.openNewEventDialog(slotDetails)}
            handleOKClose ={this.handleOKClose}
            style={{minHeight: 800}}
            handleClick={this.handleClick}
            handleSelectAllClick={this.handleSelectAllClick}
            isAdminView ={this.state.isAdminView}
            onAddNewClick ={this.onAddNewClick}
          />
        </div>
        {shouldShowEventDialog && (
          <EventEditorDialog
            handleClose={this.handleDialogClose}
            open={shouldShowEventDialog}
            handleOKClose={this.handleOKClose}
            event={newEvent}
            t={t}
          />
        )}
        {shouldOpenConfirmationDialog && (
          <ConfirmationDialog
            open={shouldOpenConfirmationDialog}
            onConfirmDialogClose={this.handleDialogClose}
            onYesClick={this.handleConfirmationResponse}
            text={t(this.state.askMesssage)}
          />
        )}   
        {shouldDeleteAllConfirmationDialog && (
          <ConfirmationDialog
            open={this.state.shouldDeleteAllConfirmationDialog}
            onConfirmDialogClose={this.handleDialogClose}
            onYesClick={this.handleDeleteAllResponse}
            text={t("Are you sure to delete all?")}
          />
        )}                   
      </div>
    )
  }
}

export default EgretCalendar;
