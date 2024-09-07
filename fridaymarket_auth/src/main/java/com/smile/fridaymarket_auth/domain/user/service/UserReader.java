package com.smile.fridaymarket_auth.domain.user.service;

import com.smile.fridaymarket_auth.domain.user.entity.User;

public interface UserReader {

    void checkUsernameDuplicate(String username);

    User getUser(String username, String password);

}
