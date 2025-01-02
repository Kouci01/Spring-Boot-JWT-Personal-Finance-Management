package com.finance.management.service.impl;

import com.finance.management.mapper.UserMapper;
import com.finance.management.model.User;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;

    public UserDetailsServiceImpl(UserMapper userMapper){
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = null;
        try {
            user = userMapper.findByEmail(email).orElseThrow(() ->
                    new NotFoundException(String.format("User does not exist, email: %s", email)));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        assert user != null;
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .build();
    }
}
