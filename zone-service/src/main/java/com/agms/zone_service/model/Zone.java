package com.agms.zone_service.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "zones")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(name = "device_id")
    private String deviceId;       // Stored after IoT device registration

    @Column(name = "iot_user_id")
    private String iotUserId;      // Stored after IoT user registration

    @Column(name = "min_temp")
    private Double minTemp;        // Minimum temperature threshold for automation rules

    @Column(name = "max_temp")
    private Double maxTemp;        // Maximum temperature threshold for automation rules
}
