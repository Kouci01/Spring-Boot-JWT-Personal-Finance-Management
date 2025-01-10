package com.finance.management.service;

import com.finance.management.controller.dto.SignUpRequestDTO;
import com.finance.management.model.User;

public interface UserService {
    void signup(SignUpRequestDTO request);
    void signUpGoogle(User user);
}
