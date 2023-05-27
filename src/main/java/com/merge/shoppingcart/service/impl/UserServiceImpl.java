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
        .orElseThrow(() -> new ApiException(USER_NOT_FOUND.name()));
  }

  @Override
  public String suspendUser(String email) {
    User user =
        userRepo.findUserByEmail(email).orElseThrow(() -> new ApiException(USER_NOT_FOUND.name()));

    if (user.getRole().equals("ADMIN")) throw new ApiException(CANNOT_SUSPEND_ADMIN.name());

    if (!user.isActive()) return "User " + email + " is already suspended";

    user.setActive(false);
    userRepo.save(user);
    return "User " + email + " is suspended";
  }

  @Override
  public String signup(String email, String password, String role) {
    // Check if the email is already taken
    if (userRepo.existsByEmail(email)) {
      throw new ApiException(EMAIL_ALREADY_TAKEN.name());
    }
    if (role.equals("ADMIN") || role.equals("USER")) {

      // Create a new user entity
      User user = new User();
      user.setEmail(email);
      user.setPassword(passwordEncoder.encode(password));
      user.setRole(role);
      user.setActive(true);

      // Save the user entity in the database
      userRepo.save(user);
      return "User Created Successfully";
    } else throw new ApiException(INVALID_ROLE.name());
  }

  @Override
  public String login(String email, String password) {

    Optional<User> user = userRepo.findUserByEmail(email);
    if (!user.isPresent() || !passwordEncoder.matches(password, user.get().getPassword())) {
      throw new ApiException(USER_PASS_INVALID.name());
    }
    if (!user.get().isActive()) {
      throw new ApiException(USER_NOT_ACTIVE.name());
    }

    return jwtUtils.generateToken(new CustomUserDetails(user.get()));
  }

  @Override
  public String logout(String token) {
    // invalidate token
    return "Logout successful";
  }
}
