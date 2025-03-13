package org.library.library_app.tools;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.security.core.GrantedAuthority;

public enum LibraryUserType implements GrantedAuthority {
    ADMIN,
    EMPLOYEE,
    MEMBER;

    @JsonCreator
    public static LibraryUserType fromString(String type) {
        for (LibraryUserType libraryUserType : LibraryUserType.values()) {
            if(libraryUserType.name().equalsIgnoreCase(type)) {
                return libraryUserType;
            }
        }
        throw new IllegalArgumentException("Unknown user type: " + type);
    }

    @Override
    @JsonValue
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }

    @Override
    public String getAuthority() {
        // return "ROLE_" + this.name(); where the prefix is necessary
        return this.name();
    }
}
