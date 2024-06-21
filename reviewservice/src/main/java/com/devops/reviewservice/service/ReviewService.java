package com.devops.reviewservice.service;

import com.devops.reviewservice.model.AccommodationReview;
import com.devops.reviewservice.model.HostReview;
import com.devops.reviewservice.repository.AccommodationReviewRepository;
import com.devops.reviewservice.repository.HostReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private HostReviewRepository hostReviewRepository;

    @Autowired
    private AccommodationReviewRepository accommodationReviewRepository;

    public HostReview rateHost(HostReview hostReview) {
        Optional<HostReview> existingReview = hostReviewRepository.findByHostIdAndGuestId(hostReview.getHostId(), hostReview.getGuestId());
        if (existingReview.isPresent()) {
            throw new IllegalArgumentException("Guest has already reviewed this host.");
        }
        //check if the guest had a past reservation with the host which was not cancelled
        return hostReviewRepository.save(hostReview);
    }

    public HostReview updateHostReview(String id, HostReview updatedReview) {
        Optional<HostReview> existingReview = hostReviewRepository.findByHostIdAndGuestId(id, updatedReview.getGuestId());
        System.out.println(existingReview);
        if (existingReview.isPresent() && existingReview.get().getGuestId().equals(updatedReview.getGuestId())) {
            HostReview review = existingReview.get();
            review.setRating(updatedReview.getRating());
            review.setDate(updatedReview.getDate());
            return hostReviewRepository.save(review);
        }
        throw new IllegalArgumentException("Review not found or you are not authorized to update this review.");
    }

    public void deleteHostReview(Long id, String guestId) {
        Optional<HostReview> existingReview = hostReviewRepository.findById(id);
        System.out.println(existingReview);
        if (existingReview.isPresent() && existingReview.get().getGuestId().equals(guestId)) {
            hostReviewRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Review not found or you are not authorized to delete this review.");
        }
    }

    public List<HostReview> getHostReviews(String hostId) {
        return hostReviewRepository.findByHostId(hostId);
    }

    public double getAverageHostRating(String hostId) {
        List<HostReview> reviews = hostReviewRepository.findByHostId(hostId);
        return reviews.stream().mapToInt(HostReview::getRating).average().orElse(0.0);
    }

    public AccommodationReview rateAccommodation(AccommodationReview accommodationReview) {
        Optional<AccommodationReview> existingReview = accommodationReviewRepository.findByAccommodationIdAndGuestId(accommodationReview.getAccommodationId(), accommodationReview.getGuestId());
        if (existingReview.isPresent()) {
            throw new IllegalArgumentException("Guest has already reviewed this accommodation.");
        }
        //check if the guest stayed at the accommodation in the past
        return accommodationReviewRepository.save(accommodationReview);
    }

    public AccommodationReview updateAccommodationReview(Long id, AccommodationReview updatedReview) {
        Optional<AccommodationReview> existingReview = accommodationReviewRepository.findByAccommodationIdAndGuestId(id, updatedReview.getGuestId());
        if (existingReview.isPresent() && existingReview.get().getGuestId().equals(updatedReview.getGuestId())) {
            AccommodationReview review = existingReview.get();
            review.setRating(updatedReview.getRating());
            review.setDate(updatedReview.getDate());
            return accommodationReviewRepository.save(review);
        }
        throw new IllegalArgumentException("Review not found or you are not authorized to update this review.");
    }

    public void deleteAccommodationReview(Long id, String guestId) {
        Optional<AccommodationReview> existingReview = accommodationReviewRepository.findById(id);
        if (existingReview.isPresent() && existingReview.get().getGuestId().equals(guestId)) {
            accommodationReviewRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Review not found or you are not authorized to delete this review.");
        }
    }

    public List<AccommodationReview> getAccommodationReviews(Long accommodationId) {
        return accommodationReviewRepository.findByAccommodationId(accommodationId);
    }

    public double getAverageAccommodationRating(Long accommodationId) {
        List<AccommodationReview> reviews = accommodationReviewRepository.findByAccommodationId(accommodationId);
        return reviews.stream().mapToInt(AccommodationReview::getRating).average().orElse(0.0);
    }
}
