package com.xuannguyen.product_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] data;

}

