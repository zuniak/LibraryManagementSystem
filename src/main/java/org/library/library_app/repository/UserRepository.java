package org.library.library_app.repository;

import jakarta.validation.constraints.NotNull;
import org.library.library_app.entity.User;
import org.library.library_app.tools.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByUserType(@NotNull UserType userType);
}