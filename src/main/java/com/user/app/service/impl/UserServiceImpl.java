package com.user.app.service.impl;

import com.user.app.dto.UserDto;
import com.user.app.entity.User;
import com.user.app.exceptions.ResourceNotFoundException;
import com.user.app.repositories.UserRepo;
import com.user.app.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired private UserRepo userRepo;
    @Autowired private ModelMapper userMapper;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public UserDto signup(UserDto userDto) {
        Optional<User> userObj = userRepo.findByEmail(userDto.getEmail());

        if(userObj.isEmpty()) {
            userDto.setCreatedAt(new Date());
            userDto.setRole("ROLE_"+userDto.getRole().toUpperCase());
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
            userDto.setEnable(true);
           userRepo.save(userMapper.map(userDto,User.class));
           return userMapper.map(userRepo.findByEmail(userDto.getEmail()).get(), UserDto.class);
        }
        return null;
    }

    @Override
    public UserDto login(UserDto userDto) throws Throwable {
        User userRepos = userRepo.findByEmail(userDto.getEmail())
                .orElseThrow(() ->
                        new ResourceNotFoundException
                                ("User not found for email " + userDto.getEmail()));

        return userMapper.map(userRepos,UserDto.class);

    }
}
