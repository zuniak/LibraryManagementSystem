package org.library.library_app.repository;

import org.library.library_app.entity.LibraryUser;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LibraryUserRepository extends JpaRepository<LibraryUser, String> {
    //List<LibraryUser> findByAuthoritiesContaining(LibraryUserType authority); failed to create a query
}