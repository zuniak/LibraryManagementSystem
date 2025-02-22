package org.library.library_app.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.library.library_app.tools.UserType;
import org.library.library_app.validationgroups.CreateUser;
import org.library.library_app.validationgroups.UpdateUser;
import org.library.library_app.validationgroups.UserWithoutPassword;

@Data
@AllArgsConstructor
public class UserDto {
    @Null(groups = {CreateUser.class}, message = "ID must be null when creating a new user.")
    @NotNull(groups = {UpdateUser.class, UserWithoutPassword.class}, message = "ID is required when updating a user.")
    private Integer id;

    @NotNull(groups = {CreateUser.class, UpdateUser.class, UserWithoutPassword.class}, message = "User type is required.")
    private UserType userType;

    @NotBlank(groups = {CreateUser.class, UpdateUser.class, UserWithoutPassword.class}, message = "First name cannot be blank.")
    private String firstName;

    @NotBlank(groups = {CreateUser.class, UpdateUser.class, UserWithoutPassword.class}, message = "Last name cannot be blank.")
    private String lastName;

    @Email(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", groups = {CreateUser.class, UpdateUser.class, UserWithoutPassword.class}, message = "Invalid email.")
    private String email;

    // TODO: different validation
    @NotBlank(groups = {CreateUser.class, UpdateUser.class, UserWithoutPassword.class}, message = "Username cannot be blank.")
    private String phoneNumber;

    @NotBlank(groups = {CreateUser.class, UpdateUser.class, UserWithoutPassword.class}, message = "Password cannot be blank.")
    private String password;
}
