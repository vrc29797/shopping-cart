package com.merge.shoppingcart.dto;

import com.merge.shoppingcart.model.User;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {

  private String username;
  private String password;
  private boolean isActive;
  private List<GrantedAuthority> authorities;

  public CustomUserDetails(User user) {
    this.username = user.getEmail();
    this.password = user.getPassword();
    this.authorities =
        new ArrayList<GrantedAuthority>() {
          {
            add(new SimpleGrantedAuthority(user.getRole()));
          }
        };
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
    return isActive; // You can implement logic for enabling/disabling user accounts if needed
  }
}
