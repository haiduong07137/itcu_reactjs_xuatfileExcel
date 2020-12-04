import axios from 'axios';
import ConstantList from "../../appConfig";
const CALENDAR_API_ENPOINT="http://localhost:8079/calendar/";
// const CALENDAR_API_ENPOINT="http://globits.net:8079/calendar/";
//const CALENDAR_API_ENPOINT="/";
export const getAllEvents = () => {
    return axios.get(CALENDAR_API_ENPOINT+"api/calendar/events/all");
}
export const getListEventByWeek = (startDate) => {
    return axios.get(CALENDAR_API_ENPOINT+"api/calendar/events/getbyweek/"+startDate);
}

export const addNewEvent = (event) => {
    return axios.post(CALENDAR_API_ENPOINT+"api/calendar/events",event);
}

export const updateEvent = (event) => {
    return axios.post(CALENDAR_API_ENPOINT+"api/calendar/events",event);
}

export const deleteEvent = (id) => {
    return axios.delete(CALENDAR_API_ENPOINT+"api/calendar/events/delete/"+id);
}

export const deleteAllEvent = (events) => {
    return axios.delete(CALENDAR_API_ENPOINT+"api/calendar/events",{data:events});
}

export const publishAllEvent = (events) => {
    return axios.post(CALENDAR_API_ENPOINT+"api/calendar/events/publishAll",events);
}

export const approveAllEvent = (events) => {
    return axios.post(CALENDAR_API_ENPOINT+"api/calendar/events/approveAll",events);
}