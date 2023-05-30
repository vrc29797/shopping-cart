package com.merge.shoppingcart.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import com.merge.shoppingcart.dto.CustomUserDetails;
import com.merge.shoppingcart.exception.ApiException;
import com.merge.shoppingcart.model.User;
import com.merge.shoppingcart.repo.UserRepo;
import com.merge.shoppingcart.security.JwtUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

  @Mock private UserRepo userRepo;

  @Mock private JwtUtils jwtUtils;

  @Mock private PasswordEncoder passwordEncoder;

  @InjectMocks private UserServiceImpl userService;

  @Test
  void testListUsers_NormalCase() {
    List<User> expectedUsers = new ArrayList<>();
    expectedUsers.add(new User());
    expectedUsers.add(new User());

    when(userRepo.findAll()).thenReturn(expectedUsers);

    List<User> users = userService.listUsers();

    assertEquals(expectedUsers, users);
  }

  @Test
  void testGetUser_NormalCase() {
    String email = "test@example.com";
    User expectedUser = new User();
    expectedUser.setEmail(email);

    when(userRepo.findUserByEmail(email)).thenReturn(Optional.of(expectedUser));

    User user = userService.getUser(email);

    assertEquals(expectedUser, user);
  }

  @Test
  void testGetUser_UserNotFound() {
    String email = "test@example.com";

    when(userRepo.findUserByEmail(email)).thenReturn(Optional.empty());

    assertThrows(ApiException.class, () -> userService.getUser(email));
  }

  @Test
  void testSuspendUser_NormalCase() {
    String email = "test@example.com";
    User user = new User();
    user.setEmail(email);
    user.setRole("ROLE_USER");
    user.setActive(true);

    when(userRepo.findUserByEmail(email)).thenReturn(Optional.of(user));

    String result = userService.suspendUser(email);

    assertEquals("User " + email + " is suspended", result);
    assertFalse(user.isActive());
  }

  @Test
  void testSuspendUser_UserNotFound() {
    String email = "test@example.com";

    when(userRepo.findUserByEmail(email)).thenReturn(Optional.empty());

    assertThrows(ApiException.class, () -> userService.suspendUser(email));
  }

  @Test
  void testSuspendUser_CannotSuspendAdmin() {
    String email = "admin@example.com";
    User user = new User();
    user.setEmail(email);
    user.setRole("ROLE_ADMIN");

    when(userRepo.findUserByEmail(email)).thenReturn(Optional.of(user));

    assertThrows(ApiException.class, () -> userService.suspendUser(email));
  }

  @Test
  void testResumeUser_NormalCase() {
    String email = "test@example.com";
    User user = new User();
    user.setEmail(email);
    user.setRole("ROLE_USER");
    user.setActive(false);

    when(userRepo.findUserByEmail(email)).thenReturn(Optional.of(user));

    String result = userService.resumeUser(email);

    assertEquals("User " + email + " is Now Active", result);
    assertTrue(user.isActive());
  }

  @Test
  void testResumeUser_UserNotFound() {
    String email = "test@example.com";

    when(userRepo.findUserByEmail(email)).thenReturn(Optional.empty());

    assertThrows(ApiException.class, () -> userService.resumeUser(email));
  }

  @Test
  void testSignup_NormalCase() {
    String email = "test@example.com";
    String password = "password";
    String role = "ROLE_USER";
    User user = new User();
    user.setEmail(email);

    when(userRepo.existsByEmail(email)).thenReturn(false);
    when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
    when(userRepo.save(any(User.class))).thenReturn(user);

    String result = userService.signup(email, password, role);

    assertEquals("User Created Successfully", result);
  }

  @Test
  void testSignup_EmailAlreadyTaken() {
    String email = "test@example.com";
    String password = "password";
    String role = "ROLE_USER";

    when(userRepo.existsByEmail(email)).thenReturn(true);

    assertThrows(ApiException.class, () -> userService.signup(email, password, role));
  }

  @Test
  void testSignup_InvalidRole() {
    String email = "test@example.com";
    String password = "password";
    String role = "ROLE_INVALID";

    assertThrows(ApiException.class, () -> userService.signup(email, password, role));
  }

  @Test
  void testLogin_NormalCase() {
    String email = "test@example.com";
    String password = "password";
    User user = new User();
    user.setEmail(email);
    user.setPassword("encodedPassword");
    user.setActive(true);
    user.setRole("ROLE_USER");

    when(userRepo.findUserByEmail(email)).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);
    when(jwtUtils.generateToken(any(CustomUserDetails.class))).thenReturn("token");

    String result = userService.login(email, password);

    assertEquals("token", result);
  }

  @Test
  void testLogin_UserNotFound() {
    String email = "test@example.com";
    String password = "password";

    when(userRepo.findUserByEmail(email)).thenReturn(Optional.empty());

    assertThrows(ApiException.class, () -> userService.login(email, password));
  }

  @Test
  void testLogin_InvalidPassword() {
    String email = "test@example.com";
    String password = "password";
    User user = new User();
    user.setEmail(email);
    user.setPassword("encodedPassword");

    when(userRepo.findUserByEmail(email)).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(password, user.getPassword())).thenReturn(false);

    assertThrows(ApiException.class, () -> userService.login(email, password));
  }

  @Test
  void testLogin_UserSuspended() {
    String email = "test@example.com";
    String password = "password";
    User user = new User();
    user.setEmail(email);
    user.setPassword("encodedPassword");
    user.setActive(false);

    when(userRepo.findUserByEmail(email)).thenReturn(Optional.of(user));

    assertThrows(ApiException.class, () -> userService.login(email, password));
  }
}
