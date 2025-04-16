package org.library.library_app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.library.library_app.tools.LibraryUserType;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;

@Entity
@NoArgsConstructor
@Data
public class LibraryUser implements UserDetails {
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable()
    @Enumerated(EnumType.STRING)
    private Set<LibraryUserType> authorities;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String email;

    @Id
    @NotBlank
    private String username;

    @NotBlank // TODO: different validation
    private String phoneNumber;

    @NotBlank // TODO: security
    private String password;
}
