package com.guru.ecommerce.DAO;

import com.guru.ecommerce.Enums.AppRole;
import com.guru.ecommerce.Model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByRoleName(AppRole appRole);
}