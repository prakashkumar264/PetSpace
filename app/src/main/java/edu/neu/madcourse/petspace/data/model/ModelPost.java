package edu.neu.madcourse.petspace.data.model;

public class ModelPost {
    String uid,pcontent,pimage, ptime;

    public ModelPost(){

    }

    public ModelPost(String uid, String pcontent, String pimage, String ptime) {
        this.uid = uid;
        this.pcontent = pcontent;
        this.pimage = pimage;
        this.ptime = ptime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPcontent() {
        return pcontent;
    }

    public void setPcontent(String pcontent) {
        this.pcontent = pcontent;
    }

    public String getPimage() {
        return pimage;
    }

    public void setPimage(String pimage) {
        this.pimage = pimage;
    }

    public String getPtime() {
        return ptime;
    }

    public void setPtime(String ptime) {
        this.ptime = ptime;
    }
}
