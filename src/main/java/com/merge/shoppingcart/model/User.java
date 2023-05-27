package com.merge.shoppingcart.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "user")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User extends BaseEntity {

  // Encrypted Password, Client should do encryption to avoid exposing password in Rest Calls
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
