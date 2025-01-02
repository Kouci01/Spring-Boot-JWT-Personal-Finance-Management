package com.finance.management.service;

import com.finance.management.controller.dto.SignUpRequestDTO;

public interface UserService {
    void signup(SignUpRequestDTO request);
}
