package com.example.core.repository.user;

import com.example.core.model.user.SignUp;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SignUpRepository extends JpaRepository<SignUp, Long> {

  Optional<SignUp> findByEmail(String email);

  Boolean existsByEmail(String email);

  SignUp getReferenceByEmail(String email);
}
