package com.project.simple_finance_api.util;

import com.project.simple_finance_api.entities.account.Account;
import com.project.simple_finance_api.entities.user.User;

public class AccountUtil {

    public static Account createValidAccount(){
        return Account.builder()
                .id("dd421509-47a4-4395-816b-5dbb1c6b2570")
                .firstName("test")
                .lastName("test")
                .document("111111111")
                .balance(0)
                .user(new User())
                .build();
    }
}
