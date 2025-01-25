package com.training.controllers;

import com.training.payload.UserDTO;
import com.training.models.Role;
import com.training.models.User;
import com.training.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AdminControllerTest {

    @InjectMocks
    private AdminController adminController;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() {
        List<User> users = Collections.singletonList(new User());
        when(userService.getAllUsers()).thenReturn(users);

        ResponseEntity<List<User>> response = adminController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
    }

    @Test
    void testUpdateUserRole() {
        Long userId = 1L;
        String roleName = "ROLE_USER";

        ResponseEntity<String> response = adminController.updateUserRole(userId, roleName);

        verify(userService, times(1)).updateUserRole(userId, roleName);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User role updated", response.getBody());
    }

    @Test
    void testGetUser() {
        Long userId = 1L;
        UserDTO userDTO = new UserDTO();
        when(userService.getUserById(userId)).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = adminController.getUser(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDTO, response.getBody());
    }

    @Test
    void testGetAllRoles() {
        List<Role> roles = Collections.singletonList(new Role());
        when(userService.getAllRoles()).thenReturn(roles);

        List<Role> response = adminController.getAllRoles();

        assertEquals(roles, response);
    }

    @Test
    void testUpdatePassword() {
        Long userId = 1L;
        String password = "newPassword";

        ResponseEntity<String> response = adminController.updatePassword(userId, password);

        verify(userService, times(1)).updatePassword(userId, password);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Password updated", response.getBody());
    }

    @Test
    void testUpdatePassword_Exception() {
        Long userId = 1L;
        String password = "newPassword";
        doThrow(new RuntimeException("Error updating password")).when(userService).updatePassword(userId, password);

        ResponseEntity<String> response = adminController.updatePassword(userId, password);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error updating password", response.getBody());
    }
}