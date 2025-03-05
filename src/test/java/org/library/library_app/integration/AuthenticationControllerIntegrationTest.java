package org.library.library_app.integration;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.library.library_app.entity.LibraryUser;
import org.library.library_app.repository.LibraryUserRepository;
import org.library.library_app.tools.LibraryUserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
public class AuthenticationControllerIntegrationTest {
    @Autowired
    private WebApplicationContext context;

    static MockMvc mvc;

    @Autowired
    LibraryUserRepository repository;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @BeforeEach()
    public void restartDatabase() {
        repository.deleteAll();

        LibraryUser user = new LibraryUser();
        user.setAuthorities(Set.of(LibraryUserType.MEMBER));
        user.setFirstName("Name");
        user.setLastName("Lastname");
        user.setEmail("email@email.com");
        user.setUsername("username");
        user.setPhoneNumber("123456789");
        user.setPassword("password"); // TODO: hash password

        repository.save(user);
    }

    @Test
    public void register_whenValidInput_shouldReturnCreated() throws Exception {
        Map<String, String> input = Map.of(
                "firstName", "Name",
                "lastName", "Lastname",
                "email", "email@email.com",
                "username", "username1",
                "phoneNumber", "123456789",
                "password", "password");
        String jsonInput = new JSONObject(input).toString();

        String expectedMessage = "Library member username1 registered successfully.";

        mvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInput))
                .andExpect(status().isCreated())
                .andExpect(content().string(expectedMessage));

        assertTrue(repository.existsById("username1"));
    }

    @Test
    public void register_whenUsernameIsTaken_shouldReturnConflict() throws Exception {
        Map<String, String> input = Map.of(
                "firstName", "Name",
                "lastName", "Lastname",
                "email", "email@email.com",
                "username", "username",
                "phoneNumber", "123456789",
                "password", "password");
        String jsonInput = new JSONObject(input).toString();

        String expectedMessage = "Library user username already exists";

        mvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInput))
                .andExpect(status().isConflict())
                .andExpect(content().string(expectedMessage));

        assertTrue(repository.existsById("username"));
    }

    @Test
    public void register_whenInvalidInput_shouldReturnBadRequest() throws Exception {
        Map<String, String> input = Map.of(
                "firstName", "Name",
                "lastName", "Lastname",
                "email", "email",
                "username", "username1",
                "phoneNumber", "123456789",
                "password", "password");
        String jsonInput = new JSONObject(input).toString();

        String expectedMessage = "Library member registration failed:\nInvalid email.";

        mvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonInput))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(expectedMessage));
        assertFalse(repository.existsById("username1"));
    }
}
