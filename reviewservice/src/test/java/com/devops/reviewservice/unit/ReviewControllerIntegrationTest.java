package com.devops.reviewservice.unit;


import com.devops.reviewservice.model.HostReview;
import com.devops.reviewservice.repository.HostReviewRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ReviewControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private HostReviewRepository hostReviewRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Transactional
    @Rollback
    @Test
    public void testRateHost() throws Exception {
        HostReview hostReview = new HostReview(1L, 101L, 5, LocalDateTime.now());

        mockMvc.perform(post("/reviews/host")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hostReview)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value(5));
    }

    @Transactional
    @Rollback
    @Test
    public void testGetHostReviews() throws Exception {
        HostReview hostReview = new HostReview(1L, 101L, 5, LocalDateTime.now());
        hostReviewRepository.save(hostReview);

        mockMvc.perform(get("/reviews/host/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].rating").value(5));
    }
    @Transactional
    @Rollback
    @Test
    public void testUpdateHostReview() throws Exception {
        HostReview hostReview = new HostReview(1L, 101L, 4, LocalDateTime.now());
        hostReviewRepository.save(hostReview);

        hostReview.setRating(5);

        mockMvc.perform(put("/reviews/host/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(hostReview)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value(5));
    }
    @Transactional
    @Rollback
    @Test
    public void testDeleteHostReview() throws Exception {
        HostReview hostReview = new HostReview(1L, 101L, 4, LocalDateTime.now());
        hostReviewRepository.save(hostReview);

        mockMvc.perform(delete("/reviews/host/1")
                        .param("guestId", "101"))
                .andExpect(status().isOk());

        assertFalse(hostReviewRepository.findById(1L).isPresent());
    }

    // Similar tests for AccommodationReview can be added here
}

