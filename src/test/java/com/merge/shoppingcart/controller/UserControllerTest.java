package com.merge.shoppingcart.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.merge.shoppingcart.dto.*;
import com.merge.shoppingcart.model.User;
import com.merge.shoppingcart.service.UserService;
import java.util.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

  @Mock private UserService userService;

  @InjectMocks private UserController userController;

  @Test
  void signup_WithValidRequest_ShouldReturnSuccessMessage() {
    // Prepare
    String email = "user@example.com";
    String password = "password";
    String role = "ROLE_USER";
    AuthenticationRequest request = new AuthenticationRequest();
    request.setEmail(email);
    request.setPassword(password);
    request.setRole(role);
    when(userService.signup(email, password, role)).thenReturn("User Created Successfully");

    // Execute
    ResponseEntity<BaseResponse<String>> responseEntity = userController.signup(request);

    // Verify

    assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    BaseResponse<String> response = responseEntity.getBody();
    assertNotNull(response);
    assertTrue(response.isSuccess());
    assertEquals("User Created Successfully", response.getData());
    assertNull(response.getError());
    assertNull(response.getErrorCode());
  }

  @Test
  void login_WithValidCredentials_ShouldReturnAuthToken() {
    // Prepare
    String email = "user@example.com";
    String password = "password";
    String authToken = "generatedAuthToken";
    AuthenticationRequest request = new AuthenticationRequest();
    request.setEmail(email);
    request.setPassword(password);
    when(userService.login(email, password)).thenReturn(authToken);

    // Execute
    ResponseEntity<BaseResponse<String>> responseEntity = userController.login(request);

    // Verify
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    BaseResponse<String> response = responseEntity.getBody();
    assertNotNull(response);
    assertTrue(response.isSuccess());
    assertEquals(authToken, response.getData());
    assertNull(response.getError());
    assertNull(response.getErrorCode());
  }

  @Test
  void suspendUser_WithExistingUser_ShouldSuspendUser() {
    // Prepare
    String email = "user@example.com";
    when(userService.suspendUser(email)).thenReturn("User " + email + " is suspended");

    // Execute
    ResponseEntity<BaseResponse<String>> responseEntity = userController.suspend(email);

    // Verify
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    BaseResponse<String> response = responseEntity.getBody();
    assertNotNull(response);
    assertTrue(response.isSuccess());
    assertEquals("User " + email + " is suspended", response.getData());
    assertNull(response.getError());
    assertNull(response.getErrorCode());
  }

  @Test
  void resumeUser_WithExistingUser_ShouldResumeUser() {
    // Prepare
    String email = "user@example.com";
    when(userService.resumeUser(email)).thenReturn("User " + email + " is Active");

    // Execute
    ResponseEntity<BaseResponse<String>> responseEntity = userController.resume(email);

    // Verify
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    BaseResponse<String> response = responseEntity.getBody();
    assertNotNull(response);
    assertTrue(response.isSuccess());
    assertEquals("User " + email + " is Active", response.getData());
    assertNull(response.getError());
    assertNull(response.getErrorCode());
  }

  @Test
  void testGetAllUsers_NormalCase() {

    // Create a sample list of users
    List<User> users =
        Arrays.asList(
            new User("pass", "user1@example.com", "ROLE_USER", true),
            new User("pass", "user2@example.com", "ROLE_USER", false),
            new User("pass", "admin@example.com", "ROLE_ADMIN", true));

    when(userService.listUsers()).thenReturn(users);

    ResponseEntity<BaseResponse<List<User>>> response = userController.getAllUsers();

    // Verify the response
    assertEquals(HttpStatus.OK, response.getStatusCode());

    BaseResponse<List<User>> baseResponse = response.getBody();
    assertNotNull(baseResponse);
    assertTrue(baseResponse.isSuccess());
    assertEquals(users, baseResponse.getData());
    assertNull(baseResponse.getError());
    assertNull(baseResponse.getErrorCode());
  }
}
