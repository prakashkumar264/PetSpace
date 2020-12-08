package edu.neu.madcourse.petspace.data.model;

public class ModelPost {
    String pid,uid,pcontent,pimage, ptime, pLikes;

    public ModelPost(){

    }

    public ModelPost(String pid, String uid, String pcontent, String pimage, String ptime, String pLikes) {
        this.pid = pid;
        this.uid = uid;
        this.pcontent = pcontent;
        this.pimage = pimage;
        this.ptime = ptime;
        this.pLikes = pLikes;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
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

    public String getpLikes() {
        return pLikes;
    }

    public void setpLikes(String pLikes) {
        this.pLikes = pLikes;
    }
}
