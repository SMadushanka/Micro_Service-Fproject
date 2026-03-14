package com.agms.sensor_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "sensor_readings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SensorReading {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "device_id", nullable = false)
    private String deviceId;

    @Column(nullable = false)
    private Double temperature;

    private Double humidity;

    @Column(name = "soil_moisture")
    private Double soilMoisture;

    private String status;

    @Column(name = "recorded_at", nullable = false)
    private Instant recordedAt;
}
