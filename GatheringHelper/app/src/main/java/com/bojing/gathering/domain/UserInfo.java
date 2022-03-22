package com.bojing.gathering.domain;

import java.util.ArrayList;

/**
 * Created by admin on 2018/10/16.
 */

public class UserInfo {
    String balance;
    String devName;
    String devCode;
    ArrayList<User> accounts = new ArrayList<>();
    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getDevCode() {
        return devCode;
    }

    public void setDevCode(String devCode) {
        this.devCode = devCode;
    }

    public ArrayList<User> getAccounts() {
        return accounts;
    }

    public void setAccounts(ArrayList<User> accounts) {
        this.accounts = accounts;
    }
}
