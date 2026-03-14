package com.agms.crop_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "crops")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Crop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "zone_id")
    private Long zoneId;            // Which zone this crop belongs to

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CropStatus status;
}
