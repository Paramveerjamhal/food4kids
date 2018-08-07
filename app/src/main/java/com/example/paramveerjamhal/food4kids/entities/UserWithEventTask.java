package com.example.paramveerjamhal.food4kids.entities;

public class UserWithEventTAsk {
    int id;
    int w_event_id;
    int event_id;
    int participate_id;
    int admin_approveStatus;
    String user_startTime;
    String user_endTime;
    String name;
    String email;
    String mobile;
    String date;



    public UserWithEventTAsk(int id,int w_event_id, int event_id, String date,int participate_id,
                             String user_startTime, String user_endTime, int admin_approveStatus,String name,
                             String email,String mobile) {
        this.id=id;
        this.w_event_id=w_event_id;
        this.event_id=event_id;
        this.date=date;
        this.participate_id=participate_id;
        this.user_startTime=user_startTime;
        this.user_endTime=user_endTime;
        this.admin_approveStatus=admin_approveStatus;
        this.name=name;
        this.mobile=mobile;
        this.email=email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getParticipate_id() {
        return participate_id;
    }

    public void setParticipate_id(int participate_id) {
        this.participate_id = participate_id;
    }

    public int getAdmin_approveStatus() {
        return admin_approveStatus;
    }

    public void setAdmin_approveStatus(int admin_approveStatus) {
        this.admin_approveStatus = admin_approveStatus;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

