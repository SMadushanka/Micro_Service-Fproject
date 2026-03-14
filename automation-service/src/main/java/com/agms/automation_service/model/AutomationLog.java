package com.agms.automation_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "automation_logs")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AutomationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "zone_id", nullable = false)
    private Long zoneId;

    @Column(nullable = false)
    private Double temperature;     // The current temperature at time of evaluation

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AutomationAction action;

    @Column(nullable = false)
    private Instant timestamp;
}
