package edu.neu.madcourse.petspace.data;

public class Comments {

    String comment_text, date, time, time_stamp, uid, user_name, profile_Image;

    public Comments() {

    }

    public Comments(String comment_text, String date, String time, String time_stamp, String uid, String user_name, String profile_Image) {
        this.comment_text = comment_text;
        this.date = date;
        this.time = time;
        this.time_stamp = time_stamp;
        this.uid = uid;
        this.user_name = user_name;
        this.profile_Image = profile_Image;
    }

    public String getComment_text() {
        return comment_text;
    }

    public void setComment_text(String comment_text) {
        this.comment_text = comment_text;
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

    public String getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(String time_stamp) {
        this.time_stamp = time_stamp;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getProfile_Image() {
        return profile_Image;
    }

    public void setProfile_Image(String profile_Image) {
        this.profile_Image = profile_Image;
    }
}