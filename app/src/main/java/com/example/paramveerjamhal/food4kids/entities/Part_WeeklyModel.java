package com.example.paramveerjamhal.food4kids.entities;

public class Part_WeeklyModel {
    int w_event_id;
    int event_id;
    String weekly_eventTask;
    String date;
    String start_time;
    String end_time;
    int participate_id;
    int user_id;
    String user_startTime;
    String user_endTime;
    int admin_approveStatus;
    int noOfVol;



    public Part_WeeklyModel(int w_event_id, int event_id, String weekly_eventTask,int noOfVol, String date, String start_time,String end_time,
                            int participate_id,int user_id,String user_startTime,String user_endTime,int admin_approveStatus) {
        this.w_event_id=w_event_id;
        this.event_id=event_id;
        this.weekly_eventTask=weekly_eventTask;
        this.date=date;
        this.start_time=start_time;
        this.end_time=end_time;
        this.participate_id=participate_id;
        this.user_id=user_id;
        this.user_startTime=user_startTime;
        this.user_endTime=user_endTime;
        this.admin_approveStatus=admin_approveStatus;
        this.noOfVol=noOfVol;


    }

    public int getNoOfVol() {
        return noOfVol;
    }

    public void setNoOfVol(int noOfVol) {
        this.noOfVol = noOfVol;
    }

    public int getW_event_id() {
        return w_event_id;
    }

    public void setW_event_id(int w_event_id) {
        this.w_event_id = w_event_id;
    }

    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public String getWeekly_eventTask() {
        return weekly_eventTask;
    }

    public void setWeekly_eventTask(String weekly_eventTask) {
        this.weekly_eventTask = weekly_eventTask;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public int getParticipate_id() {
        return participate_id;
    }

    public void setParticipate_id(int participate_id) {
        this.participate_id = participate_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_startTime() {
        return user_startTime;
    }

    public void setUser_startTime(String user_startTime) {
        this.user_startTime = user_startTime;
    }

    public String getUser_endTime() {
        return user_endTime;
    }

    public void setUser_endTime(String user_endTime) {
        this.user_endTime = user_endTime;
    }

    public int getAdmin_approveStatus() {
        return admin_approveStatus;
    }

    public void setAdmin_approveStatus(int admin_approveStatus) {
        this.admin_approveStatus = admin_approveStatus;
    }
}

