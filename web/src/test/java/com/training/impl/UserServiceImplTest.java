package com.training.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.training.models.Role;
import com.training.models.User;
import com.training.payload.UserDTO;
import com.training.repositories.RoleRepository;
import com.training.repositories.UserRepository;
import com.training.services.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.List;
import java.util.Collections;

class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

//    @Test
//    public void testUpdateUserRole() {
//        User user = new User();
//        user.setUserId(1L);
//        Role role = new Role();
//        role.setRoleName(AppRole.ROLE_ADMIN);
//
//        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
//        when(roleRepository.findByRoleName(AppRole.ROLE_ADMIN)).thenReturn(Optional.of(role));
//
//        userService.updateUserRole(1L, "ADMIN");
//
//        verify(userRepository).save(user);
//        assertEquals(role, user.getRole());
//    }

    @Test
    public void testGetAllUsers() {
        User user = new User();
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));

        List<User> users = userService.getAllUsers();

        assertEquals(1, users.size());
        assertEquals(user, users.get(0));
    }

    @Test
    public void testGetUserById() {
        User user = new User();
        user.setUserId(1L);
        user.setUserName("John Doe");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDTO userDTO = userService.getUserById(1L);

        assertNotNull(userDTO);
        assertEquals("John Doe", userDTO.getUserName());
    }

    @Test
    public void testFindByUsername() {
        User user = new User();
        user.setUserName("johndoe");

        when(userRepository.findByUserName("johndoe")).thenReturn(Optional.of(user));

        User result = userService.findByUsername("johndoe");

        assertNotNull(result);
        assertEquals("johndoe", result.getUserName());
    }

    @Test
    public void testGetAllRoles() {
        Role role = new Role();
        when(roleRepository.findAll()).thenReturn(Collections.singletonList(role));

        List<Role> roles = userService.getAllRoles();

        assertEquals(1, roles.size());
        assertEquals(role, roles.get(0));
    }

    @Test
    public void testUpdatePassword() {
        User user = new User();
        user.setUserId(1L);
        String encodedPassword = "encodedPassword";

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newPassword")).thenReturn(encodedPassword);

        userService.updatePassword(1L, "newPassword");

        verify(userRepository).save(user);
        assertEquals(encodedPassword, user.getPassword());
    }
}