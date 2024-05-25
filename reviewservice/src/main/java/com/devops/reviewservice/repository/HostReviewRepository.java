package com.devops.reviewservice.repository;

import com.devops.reviewservice.model.HostReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HostReviewRepository extends JpaRepository<HostReview, Long> {
    List<HostReview> findByHostId(Long hostId);
}
