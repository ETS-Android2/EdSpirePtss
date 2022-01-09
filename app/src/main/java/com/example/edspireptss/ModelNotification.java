package com.example.edspireptss;

public class ModelNotification {
    String Postkey, timestamp, pUid, notification, sUid, sName, sEmail, sImage;

    //empty constructor is required for firebase
    public ModelNotification() {

    }
    public ModelNotification(  String Postkey,String  timestamp,String  pUid, String notification, String sUid,String  sName,String  sEmail, String sImage) {
        this.Postkey = Postkey;
        this.timestamp = timestamp;
        this.pUid = pUid;
        this.notification = notification;
        this.sUid = sUid;
        this.sName = sName;
        this.sEmail = sEmail;
        this.sImage = sImage;

    }

    public String getPostkey() {
        return Postkey;
    }

    public void setPostkey(String Postkey) {
        this.Postkey = Postkey;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getpUid() {
        return pUid;
    }

    public void setpUid(String pUid) {
        this.pUid = pUid;
    }

    public String getNotification() {
        return notification;
    }

    public void setNotification(String notification) {
        this.notification = notification;
    }

    public String getsUid() {
        return sUid;
    }

    public void setsUid(String sUid) {
        this.sUid = sUid;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getsEmail() {
        return sEmail;
    }

    public void setsEmail(String sEmail) {
        this.sEmail = sEmail;
    }

    public String getsImage() {
        return sImage;
    }

    public void setsImage(String sImage) {
        this.sImage = sImage;
    }
}