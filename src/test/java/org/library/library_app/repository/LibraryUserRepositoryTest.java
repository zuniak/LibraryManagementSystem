package org.library.library_app.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.library.library_app.entity.LibraryUser;
import org.library.library_app.tools.LibraryUserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class LibraryUserRepositoryTest {
    @Autowired
    LibraryUserRepository libraryUserRepository;

    @Autowired
    TestEntityManager testEntityManager;

    @BeforeEach
    void setUp() {
        LibraryUser admin = new LibraryUser();
        admin.setAuthorities(Set.of(LibraryUserType.ADMIN, LibraryUserType.EMPLOYEE));
        admin.setFirstName("Admin");
        admin.setLastName("Admin");
        admin.setEmail("admin@admin.com");
        admin.setUsername("admin");
        admin.setPhoneNumber("123456789");
        admin.setPassword("password");

        LibraryUser employee = new LibraryUser();
        employee.setAuthorities(Set.of(LibraryUserType.EMPLOYEE));
        employee.setFirstName("Employee");
        employee.setLastName("Employee");
        employee.setEmail("employee@employee.com");
        employee.setUsername("employee");
        employee.setPhoneNumber("123456789");
        employee.setPassword("password");

        LibraryUser member = new LibraryUser();
        member.setAuthorities(Set.of(LibraryUserType.MEMBER));
        member.setFirstName("Member");
        member.setLastName("Member");
        member.setEmail("member@member.com");
        member.setUsername("member");
        member.setPhoneNumber("123456789");
        member.setPassword("password");

        testEntityManager.persist(admin);
        testEntityManager.persist(employee);
        testEntityManager.persist(member);
    }

    @Test
    void findByAuthoritiesContaining() {
        assertEquals(1, libraryUserRepository.findByAuthoritiesContaining(LibraryUserType.ADMIN).size());
        assertEquals(2, libraryUserRepository.findByAuthoritiesContaining(LibraryUserType.EMPLOYEE).size());
        assertEquals(1, libraryUserRepository.findByAuthoritiesContaining(LibraryUserType.MEMBER).size());
    }
}