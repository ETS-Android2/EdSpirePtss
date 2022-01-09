package com.example.edspireptss;

public class Answers {
    public String uid, pUid, answer, date, time, profileimage, username, department;

    public Answers(){

    }

    public Answers(String uid,String pUid, String answer, String date, String time, String profileimage, String username, String department) {
        this.uid = uid;
        this.pUid = pUid;
        this.answer = answer;
        this.date = date;
        this.time = time;
        this.profileimage = profileimage;
        this.username = username;
        this.department = department;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getpUid() {
        return pUid;
    }

    public void setpUid(String pUid) {
        this.pUid = pUid;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
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

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}