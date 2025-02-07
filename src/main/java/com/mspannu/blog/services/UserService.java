package com.mspannu.blog.services;

import com.mspannu.blog.domain.entities.User;

import java.util.UUID;

public interface UserService {
    User getUserById(UUID id);
}
