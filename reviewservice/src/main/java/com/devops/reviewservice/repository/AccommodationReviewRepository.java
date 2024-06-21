package com.devops.reviewservice.repository;

import com.devops.reviewservice.model.AccommodationReview;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AccommodationReviewRepository extends JpaRepository<AccommodationReview, Long> {
    List<AccommodationReview> findByAccommodationId(Long accommodationId);
    Optional<AccommodationReview> findByAccommodationIdAndGuestId(Long accommodationId, String guestId);
}
