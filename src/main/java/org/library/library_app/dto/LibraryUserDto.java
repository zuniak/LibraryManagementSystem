package org.library.library_app.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.library.library_app.validationgroups.CreateUser;
import org.library.library_app.validationgroups.UpdateUser;


@Data
@NoArgsConstructor
public class LibraryUserDto {
    @NotBlank(groups = {CreateUser.class, UpdateUser.class}, message = "First name cannot be blank.")
    private String firstName;

    @NotBlank(groups = {CreateUser.class, UpdateUser.class}, message = "Last name cannot be blank.")
    private String lastName;

    @Email(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", groups = {CreateUser.class, UpdateUser.class}, message = "Invalid email.")
    private String email;

    @NotBlank(groups = {CreateUser.class, UpdateUser.class}, message = "Username cannot be blank.")
    private String username;

    // TODO: different validation
    @NotBlank(groups = {CreateUser.class, UpdateUser.class}, message = "Username cannot be blank.")
    private String phoneNumber;

    @NotBlank(groups = {CreateUser.class, UpdateUser.class}, message = "Password cannot be blank.")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
}
