package edu.neu.madcourse.petspace;

public class FindFriendsObject {
    public String profile_Image, full_name, user_name;

    public FindFriendsObject() {

    }

    public FindFriendsObject(String profile_Image, String full_name, String user_name) {
        this.profile_Image = profile_Image;
        this.full_name = full_name;
        this.user_name = user_name;
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