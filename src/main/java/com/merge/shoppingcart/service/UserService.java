package com.merge.shoppingcart.service;

import com.merge.shoppingcart.model.User;
import java.util.List;

public interface UserService {

  List<User> listUsers();

  User getUser(String email);

  String suspendUser(String email);

  String resumeUser(String email);

  String login(String username, String password);

  String logout(String token);

  String signup(String email, String password, String role);
}
