package com.finance.management.service.impl;

import com.finance.management.mapper.UserMapper;
import com.finance.management.model.User;
import com.finance.management.utils.EmailUtils;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserMapper userMapper;

    public UserDetailsServiceImpl(UserMapper userMapper){
        this.userMapper = userMapper;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = null;
        String finalEmail = "";
        try {
            if(EmailUtils.isEmailDots(email)){
                finalEmail = EmailUtils.revertDotsBeforeAt(email, '.');
            }else {
                finalEmail = email;
            }
            user = userMapper.findByEmail(finalEmail).orElseThrow(() ->
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
