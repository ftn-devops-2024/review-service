package com.devops.reviewservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAccommodationDTO {
    private String userId;
    private Long accommodationId;
}
