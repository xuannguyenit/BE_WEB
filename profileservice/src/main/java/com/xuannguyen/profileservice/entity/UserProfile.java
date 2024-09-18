package com.xuannguyen.profileservice.entity;

import java.time.LocalDate;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "userid")
    private String userid;

    @Column(name = "firsname")
    private String firstName;

    @Column(name = "lastnam")
    private String lastName;

    @Column(name = "dateofbirth")
    private LocalDate dob;

    @Column(name = "city")
    private String city;
}
