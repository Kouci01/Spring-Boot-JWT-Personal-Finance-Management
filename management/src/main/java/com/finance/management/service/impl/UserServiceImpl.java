package com.finance.management.service.impl;

import com.finance.management.config.OAuth2LoginSuccessHandler;
import com.finance.management.controller.dto.SignUpRequestDTO;
import com.finance.management.mapper.UserMapper;
import com.finance.management.model.User;
import com.finance.management.service.UserService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private static final String SIGNON = "SingleSignOnGoogle";

    public UserServiceImpl(UserMapper userMapper, PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Transactional
    @Override
    public void signup(SignUpRequestDTO request) {
        String email = request.email();
        Optional<User> existingUser = userMapper.findByEmail(email);
        if (existingUser.isPresent()) {
            throw new DuplicateKeyException(String.format("User with the email address '%s' already exists.", email));
        }

        String hashedPassword = passwordEncoder.encode(request.password());
        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(hashedPassword);
        userMapper.addUser(user);
    }

    @Transactional
    @Override
    public void signUpGoogle(User user) {
        Optional<User> existingUser = userMapper.findByEmail(user.getEmail());
        if (!existingUser.isPresent()) {
            String hashedPassword = passwordEncoder.encode(SIGNON);
            user.setPassword(hashedPassword);
            userMapper.addUser(user);
        }
    }
}
