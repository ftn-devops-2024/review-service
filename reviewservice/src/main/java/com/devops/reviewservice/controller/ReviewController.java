package com.devops.reviewservice.controller;


import com.devops.reviewservice.model.AccommodationReview;
import com.devops.reviewservice.model.HostReview;
import com.devops.reviewservice.repository.AccommodationReviewRepository;
import com.devops.reviewservice.repository.HostReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private HostReviewRepository hostReviewRepository;

    @Autowired
    private AccommodationReviewRepository accommodationReviewRepository;

    @PostMapping("/host")
    public HostReview rateHost(@RequestBody HostReview hostReview) {
        // Here you should check if the guest had a past reservation with the host which was not cancelled
        return hostReviewRepository.save(hostReview);
    }

    @PutMapping("/host/{id}")
    public HostReview updateHostReview(@PathVariable Long id, @RequestBody HostReview updatedReview) {
        Optional<HostReview> existingReview = hostReviewRepository.findById(id);
        if (existingReview.isPresent()) {
            HostReview review = existingReview.get();
            review.setRating(updatedReview.getRating());
            review.setDate(updatedReview.getDate());
            return hostReviewRepository.save(review);
        }
        return null; // Handle review not found case
    }

    @DeleteMapping("/host/{id}")
    public void deleteHostReview(@PathVariable Long id) {
        hostReviewRepository.deleteById(id);
    }

    @GetMapping("/host/{hostId}")
    public List<HostReview> getHostReviews(@PathVariable Long hostId) {
        return hostReviewRepository.findByHostId(hostId);
    }

    @GetMapping("/host/average/{hostId}")
    public double getAverageHostRating(@PathVariable Long hostId) {
        List<HostReview> reviews = hostReviewRepository.findByHostId(hostId);
        return reviews.stream().mapToInt(HostReview::getRating).average().orElse(0.0);
    }

    @PostMapping("/accommodation")
    public AccommodationReview rateAccommodation(@RequestBody AccommodationReview accommodationReview) {
        // Here you should check if the guest stayed at the accommodation in the past
        return accommodationReviewRepository.save(accommodationReview);
    }

    @PutMapping("/accommodation/{id}")
    public AccommodationReview updateAccommodationReview(@PathVariable Long id, @RequestBody AccommodationReview updatedReview) {
        Optional<AccommodationReview> existingReview = accommodationReviewRepository.findById(id);
        if (existingReview.isPresent()) {
            AccommodationReview review = existingReview.get();
            review.setRating(updatedReview.getRating());
            review.setDate(updatedReview.getDate());
            return accommodationReviewRepository.save(review);
        }
        return null; // Handle review not found case
    }

    @DeleteMapping("/accommodation/{id}")
    public void deleteAccommodationReview(@PathVariable Long id) {
        accommodationReviewRepository.deleteById(id);
    }

    @GetMapping("/accommodation/{accommodationId}")
    public List<AccommodationReview> getAccommodationReviews(@PathVariable Long accommodationId) {
        return accommodationReviewRepository.findByAccommodationId(accommodationId);
    }

    @GetMapping("/accommodation/average/{accommodationId}")
    public double getAverageAccommodationRating(@PathVariable Long accommodationId) {
        List<AccommodationReview> reviews = accommodationReviewRepository.findByAccommodationId(accommodationId);
        return reviews.stream().mapToInt(AccommodationReview::getRating).average().orElse(0.0);
    }
}
