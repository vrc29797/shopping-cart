package com.merge.shoppingcart.dto;

import com.merge.shoppingcart.model.User;
import java.util.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {

  private final String username;
  private final String password;
  private final boolean isActive;
  private final List<GrantedAuthority> authorities;

  public CustomUserDetails(User user) {
    this.username = user.getEmail();
    this.password = user.getPassword();
    this.authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRole()));
    this.isActive = user.isActive();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return isActive;
  }
}
