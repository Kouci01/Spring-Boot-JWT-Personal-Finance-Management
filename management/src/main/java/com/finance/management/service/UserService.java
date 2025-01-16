package com.finance.management.service;

import com.finance.management.controller.dto.SignUpRequestDTO;
import com.finance.management.model.User;

import java.util.Optional;

public interface UserService {
    void signup(SignUpRequestDTO request);
    void signUpGoogle(User user);
    Optional<User> findByEmail(String email);
}
