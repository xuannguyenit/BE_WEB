package com.xuannguyen.product_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name= "image")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    private String type;

    private long size;
    private LocalDate createDate = LocalDate.now();

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] data;

}

