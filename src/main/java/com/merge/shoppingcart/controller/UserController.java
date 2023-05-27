package com.merge.shoppingcart.controller;

import com.merge.shoppingcart.dto.AuthenticationRequest;
import com.merge.shoppingcart.dto.BaseResponse;
import com.merge.shoppingcart.model.User;
import com.merge.shoppingcart.service.UserService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

  @Autowired UserService userService;

  @PostMapping("/signup")
  public ResponseEntity<BaseResponse<String>> signup(
      @Valid @RequestBody AuthenticationRequest signupRequest) {
    String response =
        userService.signup(
            signupRequest.getEmail(), signupRequest.getPassword(), signupRequest.getRole());
    return new ResponseEntity<>(new BaseResponse<>(response), HttpStatus.CREATED);
  }

  @PostMapping("/login")
  public ResponseEntity<BaseResponse<String>> login(
      @Valid @RequestBody AuthenticationRequest loginRequest) {
    String response = userService.login(loginRequest.getEmail(), loginRequest.getPassword());
    return ResponseEntity.ok(new BaseResponse<>(response));
  }

  @PostMapping("/logout")
  public ResponseEntity<BaseResponse<String>> logout(@RequestHeader("Authorization") String token) {
    String response = userService.logout(token);
    return ResponseEntity.ok(new BaseResponse<>(response));
  }

  @PostMapping("/suspend")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<BaseResponse<String>> suspend(@Valid String email) {
    String response = userService.suspendUser(email);
    return ResponseEntity.ok(new BaseResponse<>(response));
  }

  @GetMapping
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<BaseResponse<List<User>>> getAllUsers() {
    List<User> response = userService.listUsers();
    return ResponseEntity.ok(new BaseResponse<>(response));
  }
}
