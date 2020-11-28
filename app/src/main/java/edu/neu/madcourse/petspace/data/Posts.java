package edu.neu.madcourse.petspace.data;

public class Posts {
    public String user_id;
    public String time;
    public String date;
    public String post_image;
    public String description;
    public String profile_Image;
    public String full_name;
    public String user_name;

    public Posts()
    {

    }

    public Posts(String user_id, String time, String date, String post_image, String description, String profile_Image, String full_name, String user_name) {
        this.user_id = user_id;
        this.time = time;
        this.date = date;
        this.post_image = post_image;
        this.description = description;
        this.profile_Image = profile_Image;
        this.full_name = full_name;
        this.user_name = user_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPost_image() {
        return post_image;
    }

    public void setPost_image(String post_image) {
        this.post_image = post_image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProfile_Image() {
        return profile_Image;
    }

    public void setProfile_Image(String profile_Image) {
        this.profile_Image = profile_Image;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
}
