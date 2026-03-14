package com.agms.sensor_service.repository;

import com.agms.sensor_service.model.SensorReading;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SensorReadingRepository extends JpaRepository<SensorReading, Long> {

    /**
     * Returns the most recent reading for a given device, ordered by recordedAt descending.
     */
    Optional<SensorReading> findTopByDeviceIdOrderByRecordedAtDesc(String deviceId);

    /**
     * Returns the most recent reading across all devices.
     */
    Optional<SensorReading> findTopByOrderByRecordedAtDesc();
}
