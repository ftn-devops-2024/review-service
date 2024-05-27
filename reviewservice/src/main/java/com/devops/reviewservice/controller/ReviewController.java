package com.devops.reviewservice.controller;

import com.devops.reviewservice.model.AccommodationReview;
import com.devops.reviewservice.model.HostReview;
import com.devops.reviewservice.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/host")
    public HostReview rateHost(@RequestBody HostReview hostReview) {
        return reviewService.rateHost(hostReview);
    }

    @PutMapping("/host/{id}")
    public HostReview updateHostReview(@PathVariable Long id, @RequestBody HostReview updatedReview) {
        return reviewService.updateHostReview(id, updatedReview);
    }

    @DeleteMapping("/host/{id}")
    public void deleteHostReview(@PathVariable Long id, @RequestParam Long guestId) {
        reviewService.deleteHostReview(id, guestId);
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
    public AccommodationReview rateAccommodation(@RequestBody AccommodationReview accommodationReview) {
        return reviewService.rateAccommodation(accommodationReview);
    }

    @PutMapping("/accommodation/{id}")
    public AccommodationReview updateAccommodationReview(@PathVariable Long id, @RequestBody AccommodationReview updatedReview) {
        return reviewService.updateAccommodationReview(id, updatedReview);
    }

    @DeleteMapping("/accommodation/{id}")
    public void deleteAccommodationReview(@PathVariable Long id, @RequestParam Long guestId) {
        reviewService.deleteAccommodationReview(id, guestId);
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
