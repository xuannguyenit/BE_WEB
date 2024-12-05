package com.xuannguyen.product_service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="tbl_brand")

public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;
    private boolean enable;
    private boolean isDelete ;
    private LocalDate createDate = LocalDate.now();
    @ManyToMany
    @JoinTable(name = "brand_image",joinColumns = @JoinColumn(name="brand_id"),inverseJoinColumns = @JoinColumn(name="image_id"))
    private Set<Image> images = new HashSet<>();
}
