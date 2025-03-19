package org.library.library_app.repository;

import org.library.library_app.entity.LibraryUser;
import org.library.library_app.tools.LibraryUserType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;


public interface LibraryUserRepository extends JpaRepository<LibraryUser, String> {
    List<LibraryUser> findByAuthoritiesContaining(LibraryUserType authority);
}