package com.devops.reviewservice.repository;

import com.devops.reviewservice.model.AccommodationReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccommodationReviewRepository extends JpaRepository<AccommodationReview, Long> {
    List<AccommodationReview> findByAccommodationId(Long accommodationId);
}
