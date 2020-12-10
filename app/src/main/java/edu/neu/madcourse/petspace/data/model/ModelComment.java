package edu.neu.madcourse.petspace.data.model;

import android.view.Display;

public class ModelComment {
    String cId, uid, comment, time;

    public ModelComment(){

    }

    public ModelComment(String cId, String uid, String comment, String time) {
        this.cId = cId;
        this.uid = uid;
        this.comment = comment;
        this.time = time;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
