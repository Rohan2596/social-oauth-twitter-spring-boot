package com.ganitic.springoauthtwitter.response;
/***
 * Author :Rohan Ravindra Kadam
 * Github : Rohan2596
 * Topic : Social Login With Twitter
 * */

public class UserInfo {

    public String name;
    public String email;
    public String description;
    public String location;

    public UserInfo(String name, String email, String description, String location) {
        this.name = name;
        this.email = email;
        this.description = description;
        this.location = location;
    }
}
