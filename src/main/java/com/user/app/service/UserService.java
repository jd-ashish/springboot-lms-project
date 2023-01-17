package com.user.app.service;

import com.user.app.dto.UserDto;

public interface UserService {
    UserDto signup(UserDto userDto);
    UserDto login(UserDto userDto) throws Throwable;
}

