package com.example.core.repository.user;

import com.example.core.model.user.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);

  Boolean existsByEmail(String email);

  Optional<User> findByEmailAndRole(String email, String role);

  User getReferenceByEmail(String email);
}
