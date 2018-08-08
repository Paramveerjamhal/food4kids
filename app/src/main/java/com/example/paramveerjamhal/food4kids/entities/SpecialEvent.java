package com.example.paramveerjamhal.food4kids.entities;


import java.io.Serializable;

public class SpecialEvent implements Serializable {
    private int s_event_id;
    private int event_id;
    private String date;
    private String start_time;
    private String end_time;
    private int noOfVol;



    public SpecialEvent(int s_event_id, int event_id,  String date,
                        int noOfVol, String start_time, String end_time) {
        this.s_event_id=s_event_id;
        this.event_id=event_id;
        this.date=date;
        this.start_time=start_time;
        this.end_time=end_time;
        this.noOfVol=noOfVol;
    }

    public int getS_event_id() {
        return s_event_id;
    }

    public void setS_event_id(int s_event_id) {
        this.s_event_id = s_event_id;
    }

    public int getEvent_id() {
        return event_id;
    }

    public void setEvent_id(int event_id) {
        this.event_id = event_id;
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

    public int getNoOfVol() {
        return noOfVol;
    }

    public void setNoOfVol(int noOfVol) {
        this.noOfVol = noOfVol;
    }
}

