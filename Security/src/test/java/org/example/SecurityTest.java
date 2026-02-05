package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Entity.User;
import org.example.repository.UserRepository;
import org.example.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class SecurityTest {
    // ... rest of code

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @BeforeEach
    public void setup() {
        if (userRepository.findByUsername("testuser").isEmpty()) {
            User user = new User();
            user.setUsername("testuser");
            user.setPassword(passwordEncoder.encode("password123"));
            user.setRole("ROLE_USER");
            userRepository.save(user);
        }
    }

    // --- CHECKBOX 1: Security-related Unit Test ---
    @Test
    public void testAccessNotesWithoutToken_ShouldFail() throws Exception {
        // Try to go to /api/notes with NO token
        mockMvc.perform(get("/api/notes"))
                .andExpect(status().isForbidden()); // Expect 403 Forbidden
    }

    // --- CHECKBOX 2: Integration Test for Secured Endpoint ---
    @Test
    public void testAccessNotesWithValidToken_ShouldSucceed() throws Exception {
        // 1. Fetch the REAL user object from the database
        // (This object has the Roles and ID needed for the token)
        var user = userRepository.findByUsername("testuser").orElseThrow();

        // 2. Generate the token using the USER object (not just the string name)
        String token = jwtService.generateToken(user);

        // 3. Try to go to /api/notes WITH the token
        mockMvc.perform(get("/api/notes")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk()); // Expect 200 OK
    }

    @Test
    public void testLoginWithWrongPassword_ShouldFail() throws Exception {
        // Try to login with wrong password
        String badLoginJson = "{\"username\": \"testuser\", \"password\": \"wrongpass\"}";

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badLoginJson))
                .andExpect(status().isForbidden()); // Or 401/403 depending on your config
    }
}