package com.devops.reviewservice.controller;

import com.devops.reviewservice.dto.UserAccommodationDTO;
import com.devops.reviewservice.dto.UserHostDTO;
import com.devops.reviewservice.exceptions.UnauthorizedException;
import com.devops.reviewservice.model.AccommodationReview;
import com.devops.reviewservice.model.HostReview;
import com.devops.reviewservice.service.AuthService;
import com.devops.reviewservice.service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private AuthService authService;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    Logger logger = LoggerFactory.getLogger(ReviewService.class);

    @PostMapping("/host")
    public ResponseEntity<HostReview> rateHost(@RequestBody HostReview hostReview,
                                              @RequestHeader("Authorization") String authToken,
                                              @CookieValue("Fingerprint") String fingerprint) {
        try {
            authService.authorizeGuest(authToken, fingerprint);
            HostReview review = reviewService.rateHost(hostReview);
            simpMessagingTemplate.convertAndSend("/notification/host-review", review);
            return ResponseEntity.ok(review);
        } catch (UnauthorizedException e) {
            logger.error("User is not authorized.");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized", e);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e);
        }
    }

    @DeleteMapping("/host/{id}")
    public void deleteHostReview(@PathVariable Long id,
                                 @RequestHeader("Authorization") String authToken,
                                 @CookieValue("Fingerprint") String fingerprint) {
        try {
            authService.authorizeGuest(authToken, fingerprint);
            reviewService.deleteHostReview(id);
        } catch (UnauthorizedException e) {
            logger.error("User is not authorized.");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized", e);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e);
        }
    }

    @GetMapping("/host/{hostId}")
    public ResponseEntity<List<HostReview>> getHostReviews(@PathVariable String hostId) {
        return ResponseEntity.ok(reviewService.getHostReviews(hostId));
    }

    @GetMapping("/host/average/{hostId}")
    public double getAverageHostRating(@PathVariable String hostId) {
        return reviewService.getAverageHostRating(hostId);
    }

    @PostMapping("/accommodation")
    public AccommodationReview rateAccommodation(@RequestBody AccommodationReview accommodationReview,
                                                 @RequestHeader("Authorization") String authToken,
                                                 @CookieValue("Fingerprint") String fingerprint) {
        try {
            authService.authorizeGuest(authToken, fingerprint);
            AccommodationReview review = reviewService.rateAccommodation(accommodationReview);
            simpMessagingTemplate.convertAndSend("/notification/accommodation-review", review);
            return review;
        } catch (UnauthorizedException e) {
            logger.error("User is not authorized.");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized", e);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e);
        }
    }

    @DeleteMapping("/accommodation/{id}")
    public void deleteAccommodationReview(@PathVariable Long id,
                                          @RequestHeader("Authorization") String authToken,
                                          @CookieValue("Fingerprint") String fingerprint) {
        try {
            authService.authorizeGuest(authToken, fingerprint);
            reviewService.deleteAccommodationReview(id);
        } catch (UnauthorizedException e) {
            logger.error("User is not authorized.");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized", e);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e);
        }
    }

    @GetMapping("/accommodation/{accommodationId}")
    public List<AccommodationReview> getAccommodationReviews(@PathVariable Long accommodationId) {
        return reviewService.getAccommodationReviews(accommodationId);
    }

    @GetMapping("/accommodation/average/{accommodationId}")
    public double getAverageAccommodationRating(@PathVariable Long accommodationId) {
        return reviewService.getAverageAccommodationRating(accommodationId);
    }

    @PostMapping("/host/user")
    public ResponseEntity<HostReview> getUserHostReview(@RequestBody UserHostDTO dto) {
        return ResponseEntity.ok(reviewService.getUserHostReview(dto.getUserId(), dto.getHostId()));
    }

    @PostMapping("/accommodation/user")
    public ResponseEntity<AccommodationReview> getUserAccommodationReview(@RequestBody UserAccommodationDTO dto) {
        return ResponseEntity.ok(reviewService.getUserAccommodationReview(dto.getUserId(), dto.getAccommodationId()));
    }
}
