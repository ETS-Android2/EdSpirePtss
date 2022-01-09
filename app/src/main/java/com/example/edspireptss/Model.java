package com.example.edspireptss;

//model class is used to set and get the data from database

public class Model {
    String title, date, duedate, time;

    public Model() {
    }

    public Model(String title, String duedate, String date, String time) {
        this.title = title;
        this.date = date;
        this.duedate = duedate;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDueDate() {
        return duedate;
    }

    public void setDueDate(String duedate) {
        this.duedate = duedate;
    }
}
