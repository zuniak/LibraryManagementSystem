package org.library.library_app.tools;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LibraryUserTypeTest {

    @Test
    void fromString() {
        assertEquals(LibraryUserType.MEMBER, LibraryUserType.fromString("member"));
        assertEquals(LibraryUserType.EMPLOYEE, LibraryUserType.fromString("employee"));
        assertEquals(LibraryUserType.ADMIN, LibraryUserType.fromString("admin"));
    }

    @Test
    void toStringTest() {
        assertEquals("Member", LibraryUserType.MEMBER.toString());
        assertEquals("Employee", LibraryUserType.EMPLOYEE.toString());
        assertEquals("Admin", LibraryUserType.ADMIN.toString());
    }

    @Test
    void getAuthority() {
        assertEquals("MEMBER", LibraryUserType.MEMBER.getAuthority());
        assertEquals("EMPLOYEE", LibraryUserType.EMPLOYEE.getAuthority());
        assertEquals("ADMIN", LibraryUserType.ADMIN.getAuthority());
    }
}