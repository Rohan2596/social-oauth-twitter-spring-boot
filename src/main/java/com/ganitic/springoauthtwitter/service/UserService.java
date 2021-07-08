package com.ganitic.springoauthtwitter.service;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ganitic.springoauthtwitter.response.UserInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.social.oauth1.AuthorizedRequestToken;
import org.springframework.social.oauth1.OAuth1Operations;
import org.springframework.social.oauth1.OAuth1Parameters;
import org.springframework.social.oauth1.OAuthToken;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/***
 * Author :Rohan Ravindra Kadam
 * Github : Rohan2596
 * Topic : Social Login With Twitter
 * */

@Service
public class UserService {

    @Value("${twitter.consumerKey}")
    String consumerKey;

    @Value("${twitter.consumerSecret}")
    String consumerSecret;

    @Value("${twitter.callbackUrl}")
    String callbackUrl;

    @Value("${twitter.userInfoUrl}")
    String userInfoUrl;


    // Connection Factory
    public TwitterConnectionFactory twitterConnectionFactory(){
       return new TwitterConnectionFactory( consumerKey,consumerSecret );
    }

    //Since Twitter Supports Oauth 1.0 we used "oauth1Operations"
    public OAuth1Operations oAuth1Operations(TwitterConnectionFactory connectionFactory){
        return connectionFactory.getOAuthOperations();
    }

    //Create Twitter Template for fetching user email etc.
    public TwitterTemplate twitterTemplate( OAuthToken accessToken){
        return  new TwitterTemplate( consumerKey, consumerSecret, accessToken.getValue(), accessToken.getSecret() );
    }


    /*
    * @method : TitterOauthLogin
    * @return : authorizedUrl
    * @purpose : To establish connection with Twitter Application and on successfully authorization it will return
    *           authorizedUrl
    * */
    public String twitterOauthLogin(){
        TwitterConnectionFactory connectionFactory = twitterConnectionFactory();
        OAuth1Operations oauthOperations = oAuth1Operations(connectionFactory);
        OAuthToken requestToken = oauthOperations.fetchRequestToken( callbackUrl, null );
        System.out.println(requestToken.getSecret()+" -----  "+ requestToken.getValue());
        String authorizeUrl = oauthOperations.buildAuthorizeUrl(requestToken.getValue(), OAuth1Parameters.NONE);
        return authorizeUrl;
    }


    /*
     * @method : TwitterUserProfile
     * @return : userinfo
     * @purpose : On Successfully redirections oauth_token and oauth_verifier is added to callback url.
     *            In return it we used  fetch user details from Twitter
     * */
    public UserInfo twitterUserProfile(HttpServletRequest request, HttpServletResponse response){

        TwitterConnectionFactory connectionFactory = twitterConnectionFactory();
        OAuth1Operations oauthOperations =oAuth1Operations(connectionFactory);
        OAuthToken oAuthToken=new OAuthToken(request.getParameter("oauth_token"),request.getParameter("oauth_verifier"));

        OAuthToken accessToken = oauthOperations.exchangeForAccessToken(new AuthorizedRequestToken(oAuthToken,request.getParameter("oauth_verifier")), null);

        // Using In built twitter Profile
//        Twitter twitter = twitterTemplate(accessToken);
//        TwitterProfile profile = twitter.userOperations().getUserProfile();

        TwitterTemplate twitterTemplate =twitterTemplate(accessToken);


        RestTemplate restTemplate = twitterTemplate.getRestTemplate();
        ObjectNode objectNode = restTemplate.getForObject(userInfoUrl, ObjectNode.class);
        String email=objectNode.get("email").asText();

        return new UserInfo(objectNode.get("name").asText(),
                objectNode.get("email").asText(),
                objectNode.get("description").asText(),
                objectNode.get("location").asText());
    }




}
