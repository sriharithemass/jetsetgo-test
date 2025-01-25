package com.training.security.services;

import com.training.models.AppRole;
import com.training.models.Role;
import com.training.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.util.AssertionErrors.assertEquals;

class UserDetailsImplTest {

    private User user;
    private UserDetailsImpl userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Role role = new Role();
        role.setRoleName(AppRole.ROLE_USER);
        user = new User();
        user.setUserId(1L);
        user.setUserName("testUser");
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setRole(role);

        userDetails = UserDetailsImpl.build(user);
    }

    @Test
    void testBuild() {
        assertEquals(user.getUserId(), userDetails.getId());
        assertEquals(user.getUserName(), userDetails.getUsername());
        assertEquals(user.getEmail(), userDetails.getEmail());
        assertEquals(user.getPassword(), userDetails.getPassword());
        assertEquals(1, userDetails.getAuthorities().size());
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    void testGetAuthorities() {
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    void testGetId() {
        assertEquals(user.getUserId(), userDetails.getId());
    }

    @Test
    void testGetEmail() {
        assertEquals(user.getEmail(), userDetails.getEmail());
    }

    @Test
    void testGetPassword() {
        assertEquals(user.getPassword(), userDetails.getPassword());
    }

    @Test
    void testGetUsername() {
        assertEquals(user.getUserName(), userDetails.getUsername());
    }
}