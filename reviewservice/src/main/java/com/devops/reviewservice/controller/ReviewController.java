package com.devops.reviewservice.controller;

import com.devops.reviewservice.exceptions.UnauthorizedException;
import com.devops.reviewservice.model.AccommodationReview;
import com.devops.reviewservice.model.HostReview;
import com.devops.reviewservice.service.AuthService;
import com.devops.reviewservice.service.ReviewService;
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
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized", e);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e);
        }
    }

    @PutMapping("/host/{id}")
    public ResponseEntity<HostReview> updateHostReview(@PathVariable String id, @RequestBody HostReview updatedReview,
                                       @RequestHeader("Authorization") String authToken,
                                       @CookieValue("Fingerprint") String fingerprint) {
        try {
            authService.authorizeGuest(authToken, fingerprint);
            HostReview review = reviewService.updateHostReview(id, updatedReview);
            simpMessagingTemplate.convertAndSend("/notification/host-review", review);
            return ResponseEntity.ok(review);
        } catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized", e);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e);
        }
    }

    @DeleteMapping("/host/{id}")
    public void deleteHostReview(@PathVariable Long id, @RequestParam String guestId,
                                 @RequestHeader("Authorization") String authToken,
                                 @CookieValue("Fingerprint") String fingerprint) {
        try {
            authService.authorizeGuest(authToken, fingerprint);
            reviewService.deleteHostReview(id, guestId);
        } catch (UnauthorizedException e) {
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
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized", e);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e);
        }
    }

    @PutMapping("/accommodation/{id}")
    public AccommodationReview updateAccommodationReview(@PathVariable Long id,
                                                         @RequestBody AccommodationReview updatedReview,
                                                         @RequestHeader("Authorization") String authToken,
                                                         @CookieValue("Fingerprint") String fingerprint) {
        try {
            authService.authorizeGuest(authToken, fingerprint);
            AccommodationReview review = reviewService.updateAccommodationReview(id, updatedReview);
            simpMessagingTemplate.convertAndSend("/notification/accommodation-review", review);
            return review;
        } catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized", e);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e);
        }
    }

    @DeleteMapping("/accommodation/{id}")
    public void deleteAccommodationReview(@PathVariable Long id, @RequestParam String guestId,
                                          @RequestHeader("Authorization") String authToken,
                                          @CookieValue("Fingerprint") String fingerprint) {
        try {
            authService.authorizeGuest(authToken, fingerprint);
            reviewService.deleteAccommodationReview(id, guestId);
        } catch (UnauthorizedException e) {
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
}
