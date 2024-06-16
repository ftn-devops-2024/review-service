package com.devops.reviewservice.service;

import com.devops.reviewservice.model.AccommodationReview;
import com.devops.reviewservice.model.HostReview;
import com.devops.reviewservice.repository.AccommodationReviewRepository;
import com.devops.reviewservice.repository.HostReviewRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger logger = LoggerFactory.getLogger(ReviewService.class);

    public HostReview rateHost(HostReview hostReview) {
        logger.info("Rating host with ID {} by guest with ID {}", hostReview.getHostId(), hostReview.getGuestId());
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
            logger.info("Host review deleted successfully with ID {}", id);
        } else {
            String errorMessage = "Review not found or you are not authorized to delete this review.";
            logger.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public List<HostReview> getHostReviews(String hostId) {
        logger.info("Fetching reviews for host with ID {}", hostId);
        return hostReviewRepository.findByHostId(hostId);
    }

    public double getAverageHostRating(String hostId) {
        logger.info("Calculating average rating for host with ID {}", hostId);
        List<HostReview> reviews = hostReviewRepository.findByHostId(hostId);
        double averageRating = reviews.stream().mapToInt(HostReview::getRating).average().orElse(0.0);
        logger.info("Average rating for host with ID {}: {}", hostId, averageRating);
        return averageRating;
    }

    public AccommodationReview rateAccommodation(AccommodationReview accommodationReview) {
        logger.info("Rating accommodation with ID {} by guest with ID {}", accommodationReview.getAccommodationId(), accommodationReview.getGuestId());
        AccommodationReview existingReview = accommodationReviewRepository.findByAccommodationIdAndGuestId(accommodationReview.getAccommodationId(), accommodationReview.getGuestId()).orElse(null);
        if (existingReview != null) {
            existingReview.setRating(accommodationReview.getRating());
            return accommodationReviewRepository.save(existingReview);
        } else {
            return accommodationReviewRepository.save(accommodationReview);
        }
    }

    public void deleteAccommodationReview(Long id) {
        logger.info("Deleting accommodation review with ID {} ", id);
        Optional<AccommodationReview> existingReview = accommodationReviewRepository.findById(id);
        if (existingReview.isPresent()) {
            accommodationReviewRepository.deleteById(id);
            logger.info("Accommodation review deleted successfully with ID {}", id);
        } else {
            String errorMessage = "Review not found or you are not authorized to delete this review.";
            logger.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public List<AccommodationReview> getAccommodationReviews(Long accommodationId) {
        logger.info("Fetching reviews for accommodation with ID {}", accommodationId);
        List<AccommodationReview> reviews = accommodationReviewRepository.findByAccommodationId(accommodationId);
        logger.info("Found {} reviews for accommodation with ID {}", reviews.size(), accommodationId);
        return reviews;
    }

    public double getAverageAccommodationRating(Long accommodationId) {
        logger.info("Calculating average rating for accommodation with ID {}", accommodationId);
        List<AccommodationReview> reviews = accommodationReviewRepository.findByAccommodationId(accommodationId);
        double averageRating = reviews.stream().mapToInt(AccommodationReview::getRating).average().orElse(0.0);
        logger.info("Average rating for accommodation with ID {}: {}", accommodationId, averageRating);
        return averageRating;
    }

    public HostReview getUserHostReview(String userId, String hostId) {
        return hostReviewRepository.findByHostIdAndGuestId(hostId, userId).orElse(null);
    }

    public AccommodationReview getUserAccommodationReview(String userId, Long accommodationId) {
        return accommodationReviewRepository.findByAccommodationIdAndGuestId(accommodationId, userId).orElse(null);
    }
}
