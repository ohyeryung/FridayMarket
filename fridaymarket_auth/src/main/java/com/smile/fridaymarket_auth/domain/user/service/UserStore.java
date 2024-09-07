package com.smile.fridaymarket_auth.domain.user.service;

import com.smile.fridaymarket_auth.domain.user.entity.User;

public interface UserStore {

    void store(User initUser);

}
