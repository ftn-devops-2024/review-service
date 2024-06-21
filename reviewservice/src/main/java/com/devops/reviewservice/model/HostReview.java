package com.devops.reviewservice.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class HostReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String hostId;

    private String guestId;

    private int rating;

    private LocalDateTime date;

    public HostReview(String hostId, String guestId, int rating, LocalDateTime date) {
        this.hostId = hostId;
        this.guestId = guestId;
        this.rating = rating;
        this.date = date;

    }
}
