package com.merge.shoppingcart.repo;

import com.merge.shoppingcart.model.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, UUID> {

  Optional<User> findUserByEmail(String email);

  boolean existsByEmail(String email);

  @Modifying
  @Query("UPDATE User set isActive = false WHERE email = :email ")
  int suspendUser(String email);
}
