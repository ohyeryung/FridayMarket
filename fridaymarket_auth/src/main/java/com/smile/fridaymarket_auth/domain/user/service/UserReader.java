package com.smile.fridaymarket_auth.domain.user.service;

public interface UserReader {

    void checkUsernameDuplicate(String username);

}
