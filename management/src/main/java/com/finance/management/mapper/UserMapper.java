package com.finance.management.mapper;

import com.finance.management.model.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface UserMapper {
    Optional<User> findByEmail(String email);
    void addUser(User user);
}
