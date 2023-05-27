package com.merge.shoppingcart.dto;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User extends BaseEntity {

    @Column(name = "password")
    String password;

    @Column(name = "email")
    String email;

    @Column(name = "role")
    UserRole role;

    @Column(name = "is_active")
    boolean isActive;

    public enum UserRole{
        ADMIN, USER;
    }


}

