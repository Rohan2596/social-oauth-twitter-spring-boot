package com.ganitic.springoauthtwitter.controller;

import com.ganitic.springoauthtwitter.response.UserInfo;
import com.ganitic.springoauthtwitter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/***
* Author :Rohan Ravindra Kadam
* Github : Rohan2596
* Topic : Social Login With Twitter
* */
@RestController
public class UserController {

    /***
     * Step 1 : Create Twitter Developer account for app
     * Step 2 : Generates Keys and tokens from developer app in paste into application.yml
     * Step 3 : Add Callback Url into app in our case( http://localhost:8080/oauth2/callback/twitter )
     * Step 4 : Clone the application and Start using the same.
     * */

    @Autowired
    UserService userService;

    /*
    * @endpoint : http://localhost:8080/oauth2/authorize/normal/twitter
    * */
    @GetMapping("/oauth2/authorize/normal/twitter")
    public void twitterOauthLogin(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String authorizeUrl = userService.twitterOauthLogin();
        response.sendRedirect( authorizeUrl );
    }

    /*
     * @endpoint : http://localhost:8080/oauth2/callback/twitter
     * Note : It should match with the callback Url mentioned into Twitter App
     * */
    @GetMapping("/oauth2/callback/twitter")
    public UserInfo getTwitter(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        return userService.twitterUserProfile(request, response);
    }



}
