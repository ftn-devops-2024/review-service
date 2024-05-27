package com.devops.reviewservice.unit;

import com.devops.reviewservice.model.HostReview;
import com.devops.reviewservice.repository.AccommodationReviewRepository;
import com.devops.reviewservice.repository.HostReviewRepository;
import com.devops.reviewservice.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReviewServiceUnitTest {

    @Mock
    private HostReviewRepository hostReviewRepository;

    @Mock
    private AccommodationReviewRepository accommodationReviewRepository;

    @InjectMocks
    private ReviewService reviewService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRateHost() {
        HostReview hostReview = new HostReview(1L, 101L, 5, LocalDateTime.now());
        when(hostReviewRepository.findByHostIdAndGuestId(1L, 101L)).thenReturn(Optional.empty());
        when(hostReviewRepository.save(any(HostReview.class))).thenReturn(hostReview);

        HostReview result = reviewService.rateHost(hostReview);
        assertEquals(5, result.getRating());
        verify(hostReviewRepository, times(1)).save(hostReview);
    }

    @Test
    public void testRateHost_AlreadyReviewed() {
        HostReview hostReview = new HostReview(1L, 101L, 5, LocalDateTime.now());
        when(hostReviewRepository.findByHostIdAndGuestId(1L, 101L)).thenReturn(Optional.of(hostReview));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reviewService.rateHost(hostReview);
        });

        String expectedMessage = "Guest has already reviewed this host.";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testUpdateHostReview() {
        HostReview existingReview = new HostReview(1L, 101L, 4, LocalDateTime.now());
        HostReview updatedReview = new HostReview(1L, 101L, 5, LocalDateTime.now());
        when(hostReviewRepository.findById(1L)).thenReturn(Optional.of(existingReview));
        when(hostReviewRepository.save(any(HostReview.class))).thenReturn(updatedReview);

        HostReview result = reviewService.updateHostReview(1L, updatedReview);
        assertEquals(5, result.getRating());
        verify(hostReviewRepository, times(1)).save(existingReview);
    }

    @Test
    public void testDeleteHostReview() {
        HostReview existingReview = new HostReview(1L, 101L, 4, LocalDateTime.now());
        when(hostReviewRepository.findById(1L)).thenReturn(Optional.of(existingReview));

        reviewService.deleteHostReview(1L, 101L);
        verify(hostReviewRepository, times(1)).deleteById(1L);
    }

}

