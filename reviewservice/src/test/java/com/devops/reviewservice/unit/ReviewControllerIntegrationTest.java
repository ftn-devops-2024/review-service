package com.devops.reviewservice.unit;


import com.devops.reviewservice.model.HostReview;
import com.devops.reviewservice.repository.HostReviewRepository;
import com.devops.reviewservice.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.Mockito.*;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class ReviewControllerIntegrationTest {

    @MockBean
    private AuthService authService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private HostReviewRepository hostReviewRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @LocalServerPort
    private Integer port;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0");

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }
    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "http://localhost:" + port;
        hostReviewRepository.deleteAll();
        MockitoAnnotations.openMocks(this);
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }


    @Transactional
    @Rollback
    @Test
    public void testRateHost() throws Exception {
        when(authService.authorizeGuest(any(), any())).thenReturn(null);
        HostReview hostReview = new HostReview("1", "101", 5, LocalDateTime.now());

        mockMvc.perform(post("/reviews/host")
                        .header("Authorization", "Bearer dummy")
                        .cookie(new Cookie("Fingerprint", "dummy"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hostReview)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value(5));
    }

    @Transactional
    @Rollback
    @Test
    public void testGetHostReviews() throws Exception {
        HostReview hostReview = new HostReview("1", "101", 5, LocalDateTime.now());
        hostReviewRepository.save(hostReview);

        mockMvc.perform(get("/reviews/host/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].rating").value(5));
    }

}

