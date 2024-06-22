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
        HostReview existingReview = hostReviewRepository.findByHostIdAndGuestId(hostReview.getHostId(), hostReview.getGuestId()).orElse(null);
        if (existingReview != null) {
            existingReview.setRating(hostReview.getRating());
            return hostReviewRepository.save(existingReview);
        }
        else {
            return hostReviewRepository.save(hostReview);
        }
    }

    public void deleteHostReview(Long id) {
        Optional<HostReview> existingReview = hostReviewRepository.findById(id);
        System.out.println(existingReview);
        if (existingReview.isPresent()) {
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
        AccommodationReview existingReview = accommodationReviewRepository.findByAccommodationIdAndGuestId(accommodationReview.getAccommodationId(), accommodationReview.getGuestId()).orElse(null);
        if (existingReview != null) {
            existingReview.setRating(accommodationReview.getRating());
            return accommodationReviewRepository.save(existingReview);
        } else {
            return accommodationReviewRepository.save(accommodationReview);
        }
    }

    public void deleteAccommodationReview(Long id) {
        Optional<AccommodationReview> existingReview = accommodationReviewRepository.findById(id);
        if (existingReview.isPresent()) {
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

    public HostReview getUserHostReview(String userId, String hostId) {
        return hostReviewRepository.findByHostIdAndGuestId(hostId, userId).orElse(null);
    }

    public AccommodationReview getUserAccommodationReview(String userId, Long accommodationId) {
        return accommodationReviewRepository.findByAccommodationIdAndGuestId(accommodationId, userId).orElse(null);
    }
}
