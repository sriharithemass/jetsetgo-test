package com.training.services;

import com.training.payload.UserDTO;
import com.training.models.Role;
import com.training.models.User;

import java.util.List;

public interface UserService {
    void updateUserRole(Long userId, String roleName);
    List<User> getAllUsers();
    UserDTO getUserById(Long id);
    User findByUsername(String username);
    List<Role> getAllRoles();
    void updatePassword(Long userId, String password);
}
