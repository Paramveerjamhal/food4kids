package com.example.paramveerjamhal.food4kids.entities;

public class Participate {
    int participate_id;
    int user_id;
    int event_id;
    String user_startTime;
    String user_endTime;
    int admin_approveStatus;


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

    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
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

    public Participate(int participate_id, int user_id, int event_id, String user_startTime, String user_endTime,
                       int admin_approveStatus ) {
        this.participate_id=participate_id;
        this.user_id=user_id;

        this.event_id=event_id;
        this.user_startTime=user_startTime;
        this.user_endTime=user_endTime;
        this.admin_approveStatus=admin_approveStatus;

    }


}

