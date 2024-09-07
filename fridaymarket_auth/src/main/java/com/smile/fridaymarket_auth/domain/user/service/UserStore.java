package com.smile.fridaymarket_auth.domain.user.service;

import com.smile.fridaymarket_auth.domain.user.entity.User;

public interface UserStore {

    User store(User initUser);

}
