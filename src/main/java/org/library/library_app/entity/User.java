package org.library.library_app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.library.library_app.tools.UserType;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private UserType userType;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String email;

    @NotBlank // TODO: different validation
    private String phoneNumber;

    @NotBlank // TODO: security
    private String password;
}
