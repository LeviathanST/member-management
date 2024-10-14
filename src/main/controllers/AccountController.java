package controllers;

import data.SignUpData;
import services.AccountService;

import java.sql.Connection;

public class AccountController {
    public void editAccount(Connection con, SignUpData data) {
        try {
            AccountService.editAccount(con, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void deleteAccount(Connection con, String username) {
        try {
            AccountService.deleteAccount(con, username);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
