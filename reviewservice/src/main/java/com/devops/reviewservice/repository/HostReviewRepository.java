package com.devops.reviewservice.repository;

import com.devops.reviewservice.model.HostReview;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface HostReviewRepository extends JpaRepository<HostReview, Long> {
    List<HostReview> findByHostId(Long hostId);
    Optional<HostReview> findByHostIdAndGuestId(Long hostId, Long guestId);
}
