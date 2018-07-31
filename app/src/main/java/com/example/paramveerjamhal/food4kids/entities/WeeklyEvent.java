package com.example.paramveerjamhal.food4kids.entities;

public class WeeklyEvent {
    int w_event_id;
    int event_id;
    String weekly_eventTask;
    String date;
    String start_time;
    String end_time;



    public WeeklyEvent(int w_event_id, int event_id, String weekly_eventTask, String date, String start_time,String end_time) {
        this.w_event_id=w_event_id;
        this.event_id=event_id;
        this.weekly_eventTask=weekly_eventTask;
        this.date=date;
        this.start_time=start_time;
        this.end_time=end_time;


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
}

