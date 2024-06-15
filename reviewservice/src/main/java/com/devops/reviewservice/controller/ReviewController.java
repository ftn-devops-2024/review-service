package com.devops.reviewservice.controller;

import com.devops.reviewservice.exceptions.UnauthorizedException;
import com.devops.reviewservice.model.AccommodationReview;
import com.devops.reviewservice.model.HostReview;
import com.devops.reviewservice.service.AuthService;
import com.devops.reviewservice.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @PostMapping("/host")
    public HostReview rateHost(@RequestBody HostReview hostReview,
                               @RequestHeader("Authorization") String authToken,
                               @CookieValue("Fingerprint") String fingerprint) {
        try {
            authService.authorizeGuest(authToken, fingerprint);
            return reviewService.rateHost(hostReview);
        } catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized", e);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e);
        }
    }

    @PutMapping("/host/{id}")
    public HostReview updateHostReview(@PathVariable Long id, @RequestBody HostReview updatedReview,
                                       @RequestHeader("Authorization") String authToken,
                                       @CookieValue("Fingerprint") String fingerprint) {
        try {
            authService.authorizeGuest(authToken, fingerprint);
            return reviewService.updateHostReview(id, updatedReview);
        } catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized", e);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e);
        }
    }

    @DeleteMapping("/host/{id}")
    public void deleteHostReview(@PathVariable Long id, @RequestParam Long guestId,
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
    public List<HostReview> getHostReviews(@PathVariable Long hostId) {
        return reviewService.getHostReviews(hostId);
    }

    @GetMapping("/host/average/{hostId}")
    public double getAverageHostRating(@PathVariable Long hostId) {
        return reviewService.getAverageHostRating(hostId);
    }

    @PostMapping("/accommodation")
    public AccommodationReview rateAccommodation(@RequestBody AccommodationReview accommodationReview,
                                                 @RequestHeader("Authorization") String authToken,
                                                 @CookieValue("Fingerprint") String fingerprint) {
        try {
            authService.authorizeGuest(authToken, fingerprint);
            return reviewService.rateAccommodation(accommodationReview);
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
            return reviewService.updateAccommodationReview(id, updatedReview);
        } catch (UnauthorizedException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized", e);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e);
        }
    }

    @DeleteMapping("/accommodation/{id}")
    public void deleteAccommodationReview(@PathVariable Long id, @RequestParam Long guestId,
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
