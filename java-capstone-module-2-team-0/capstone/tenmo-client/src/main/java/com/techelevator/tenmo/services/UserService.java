package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.util.BasicLogger;
//import io.cucumber.java.sl.In;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

public class UserService {

    private static final String API_BASE_URL = "http://localhost:8080/";
    private final RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser currentUser;
    private String authToken = null;


    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public BigDecimal getBalance(AuthenticatedUser authenticatedUser) {
        BigDecimal balance = null;

        try {
            balance = restTemplate.exchange(
                    API_BASE_URL + "balance/" + authenticatedUser.getUser().getId(),
                    HttpMethod.GET,
                    makeAuthEntity(authenticatedUser),
                    BigDecimal.class).getBody();
        }
        catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return balance;
    }

    public User getUserById(AuthenticatedUser authenticatedUser, int id){
        User user = null;
        try {
            user = restTemplate.exchange(API_BASE_URL + "users/" + id,
                    HttpMethod.GET,
                    makeAuthEntity(authenticatedUser),
                    User.class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return user;
    }

    public String getUsernameByAccountId(AuthenticatedUser authenticatedUser, int id){
        User user = null;
        try {
            user = restTemplate.exchange(API_BASE_URL + "accounts/users/" + id,
                    HttpMethod.GET,
                    makeAuthEntity(authenticatedUser),
                    User.class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }

        if (user == null){
            return null;
        }
        else{
            String username = user.getUsername();
            return username;
        }



    }

    public User getUserByAccountId(AuthenticatedUser authenticatedUser, int id){
        User user = null;
        try {
            user = restTemplate.exchange(API_BASE_URL + "accounts/users/" + id,
                    HttpMethod.GET,
                    makeAuthEntity(authenticatedUser),
                    User.class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return user;
    }

    public Account getAccountById(AuthenticatedUser authenticatedUser, int id){
        Account account = null;
        try {
            account = restTemplate.exchange(API_BASE_URL + "users/accounts/" + id,
                    HttpMethod.GET,
                    makeAuthEntity(authenticatedUser),
                    Account.class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return account;
    }


    public User[] listUsers(AuthenticatedUser authenticatedUser) {
        User[] users = null;
        try {
            users = restTemplate.exchange(API_BASE_URL + "users/",
                    HttpMethod.GET,
                    makeAuthEntity(authenticatedUser),
                    User[].class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return users;
    }




    private HttpEntity<Void> makeAuthEntity(AuthenticatedUser authenticatedUser) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        HttpEntity entity = new HttpEntity<>(headers);
        return entity;
    }


}
