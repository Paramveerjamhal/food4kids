package com.example.paramveerjamhal.food4kids.entities;

public class Event {
    int eventId;
    int userId;
    int eventType;
    String eventTitle;
    String eventDescription;
    String eventAddress;
    String postal_code;
    String event_Organizer;
    String event_Date;


    public Event(int userId,int eventType,String eventTitle, String eventDescription, String eventAddress,
                 String postal_code, String event_Date, String event_Organizer ) {
        this.userId=userId;
        this.eventType=eventType;
        this.eventTitle=eventTitle;
        this.eventDescription=eventDescription;
        this.eventAddress=eventAddress;
        this.postal_code=postal_code;
        this.event_Date=event_Date;
        this.event_Organizer=event_Organizer;

    }


    public String getEvent_Date() {
        return event_Date;
    }

    public void setEvent_Date(String event_Date) {
        this.event_Date = event_Date;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public String getEventTitle() {
        return eventTitle;
    }


    public void setEventTitle(String eventTitle) {
        this.eventTitle = eventTitle;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventAddress() {
        return eventAddress;
    }

    public void setEventAddress(String eventAddress) {
        this.eventAddress = eventAddress;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getEvent_Organizer() {
        return event_Organizer;
    }

    public void setEvent_Organizer(String event_Organizer) {
        this.event_Organizer = event_Organizer;
    }





}

