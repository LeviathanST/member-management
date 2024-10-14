package services;

import data.SignUpData;
import models.UserAccount;

import java.sql.Connection;
import java.util.Optional;

public class AccountService {
    public static void editAccount(Connection con, SignUpData data) {

        try {
            int round = Optional.ofNullable(Integer.parseInt(System.getenv("ROUND_HASHING"))).orElse(1);
            UserAccount.edit(con,data);

        } catch (Exception e) {

            System.out.println(e.getMessage());
        }
    }
    public static void deleteAccount(Connection con, String username) {
        try{
            UserAccount.delete(con,username);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
