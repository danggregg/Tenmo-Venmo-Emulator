package com.techelevator.tenmo.controller;


import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")

public class AccountController {
private UserDao userDao;
private JdbcAccountDao accountDao;

    public AccountController(JdbcAccountDao account, JdbcUserDao user) {
        this.accountDao = account;
        this.userDao = user;
    }



    @RequestMapping(path = "balance/{userid}", method = RequestMethod.GET)
    public BigDecimal getBalance(@Valid @PathVariable int userid, Principal principal) {
        if (principal.getName().equals(userDao.findByUsername(principal.getName()).getUsername())) {
            BigDecimal balance = accountDao.getBalance(userid);
            return balance;
        }
        return null;
    }

    @RequestMapping(path = "users/", method = RequestMethod.GET)
    public List<User> getUsers(){
        return userDao.findAll();
    }

    @RequestMapping(path = "users/{userid}", method = RequestMethod.GET)
    public User findUserById(@PathVariable int userid){
        return userDao.findUserById(userid);
    }

    @RequestMapping(path = "accounts/users/{userid}", method = RequestMethod.GET)
    public User findUserByAccountId(@PathVariable int userid){
        return userDao.findUserByAccountId(userid);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "users/accounts/{userid}", method = RequestMethod.GET)
    public Account findAccountByUserId (@PathVariable int userid){
        return accountDao.findAccountByUserId(userid);
    }




}
