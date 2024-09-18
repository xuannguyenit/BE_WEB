package com.xuannguyen.identityservice.dto.response;

import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileResponse {
    String userId;
    String firstName;
    String lastName;
    LocalDate dob;
    String city;
}
