package com.guru.ecommerce.DAO;

import com.guru.ecommerce.Model.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String username);

  Optional<User> findByUsername(String username);

  boolean existsByUsername(String user1);

}