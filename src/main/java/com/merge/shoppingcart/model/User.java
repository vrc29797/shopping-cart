package com.merge.shoppingcart.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User extends BaseEntity {

  @Column(name = "password")
  @JsonIgnore
  String password;

  @Column(name = "email", unique = true)
  String email;

  @Column(name = "role")
  String role;

  @Column(name = "is_active")
  boolean isActive;
}
