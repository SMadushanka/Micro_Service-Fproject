package com.agms.automation_service.repository;

import com.agms.automation_service.model.AutomationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutomationLogRepository extends JpaRepository<AutomationLog, Long> {
    List<AutomationLog> findByZoneIdOrderByTimestampDesc(Long zoneId);
}
