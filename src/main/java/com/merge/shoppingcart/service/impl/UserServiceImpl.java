package com.merge.shoppingcart.service.impl;

import static com.merge.shoppingcart.dto.ErrorCode.*;

import com.merge.shoppingcart.dto.CustomUserDetails;
import com.merge.shoppingcart.exception.ApiException;
import com.merge.shoppingcart.model.User;
import com.merge.shoppingcart.repo.UserRepo;
import com.merge.shoppingcart.security.JwtUtils;
import com.merge.shoppingcart.service.OrderService;
import com.merge.shoppingcart.service.UserService;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserServiceImpl implements UserService {

  @Autowired UserRepo userRepo;
  @Autowired OrderService orderService;
  @Autowired AuthenticationManager authenticationManager;
  @Autowired JwtUtils jwtUtils;
  @Autowired PasswordEncoder passwordEncoder;

  @Override
  public List<User> listUsers() {
    return userRepo.findAll();
  }

  @Override
  public User getUser(String email) {
    return userRepo
        .findUserByEmail(email)
        .orElseThrow(() -> new ApiException(USER_NOT_FOUND, HttpStatus.NOT_FOUND));
  }

  @Override
  public String suspendUser(String email) {
    User user =
        userRepo
            .findUserByEmail(email)
            .orElseThrow(() -> new ApiException(USER_NOT_FOUND, HttpStatus.NOT_FOUND));

    if (user.getRole().equals("ROLE_ADMIN"))
      throw new ApiException(CANNOT_SUSPEND_ADMIN, HttpStatus.BAD_REQUEST);

    if (!user.isActive()) return "User " + email + " is already suspended";

    user.setActive(false);
    userRepo.save(user);
    return "User " + email + " is suspended";
  }

  @Override
  public String resumeUser(String email) {
    User user =
        userRepo
            .findUserByEmail(email)
            .orElseThrow(() -> new ApiException(USER_NOT_FOUND, HttpStatus.NOT_FOUND));

    if (user.isActive()) return "User " + email + " is already Active";

    user.setActive(true);
    userRepo.save(user);
    return "User " + email + " is Now Active";
  }

  @Override
  public String signup(String email, String password, String role) {
    // Check if the email is already taken
    if (userRepo.existsByEmail(email)) {
      throw new ApiException(EMAIL_ALREADY_TAKEN, HttpStatus.BAD_REQUEST);
    }
    if (role.equals("ROLE_ADMIN") || role.equals("ROLE_USER")) {

      // Create a new user entity
      User user = new User();
      user.setEmail(email);
      user.setPassword(passwordEncoder.encode(password));
      user.setRole(role);
      user.setActive(true);

      // Save the user entity in the database
      userRepo.save(user);
      return "User Created Successfully";
    } else throw new ApiException(INVALID_ROLE, HttpStatus.BAD_REQUEST);
  }

  @Override
  public String login(String email, String password) {

    Optional<User> user = userRepo.findUserByEmail(email);
    if (!user.isPresent() || !passwordEncoder.matches(password, user.get().getPassword())) {
      throw new ApiException(USER_PASS_INVALID, HttpStatus.BAD_REQUEST);
    }
    if (!user.get().isActive()) {
      throw new ApiException(USER_SUSPENDED, HttpStatus.BAD_REQUEST);
    }

    return jwtUtils.generateToken(new CustomUserDetails(user.get()));
  }
}
