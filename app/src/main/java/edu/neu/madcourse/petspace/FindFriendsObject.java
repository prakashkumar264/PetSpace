package edu.neu.madcourse.petspace;

public class FindFriendsObject {
    public String profileImage, fullname, username, city, state, country, profilebio;

    public FindFriendsObject() {

    }

    public FindFriendsObject(String profileImage, String fullname, String username) {
        this.profileImage = profileImage;
        this.fullname = fullname;
        this.username = username;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


    public String getProfilebio() {
        return profilebio;
    }

    public void setProfilebio(String profilebio) {
        this.profilebio = profilebio;
    }
}