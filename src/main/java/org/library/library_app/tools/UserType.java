package org.library.library_app.tools;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum UserType {
    ADMIN,
    EMPLOYEE,
    MEMBER;

    @JsonCreator
    public static UserType fromString(String type) {
        for (UserType userType : UserType.values()) {
            if(userType.name().equalsIgnoreCase(type)) {
                return userType;
            }
        }
        throw new IllegalArgumentException("Unknown user type: " + type);
    }

    @Override
    @JsonValue
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}
