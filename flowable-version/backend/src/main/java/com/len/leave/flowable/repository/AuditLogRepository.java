package com.len.leave.flowable.repository;

import com.len.leave.flowable.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Audit Log Repository
 * 
 * Provides database access methods for AuditLog entity.
 * 
 * @author Alifito Rabbani Cahyono
 * @company PT. Len Industri (opersero)
 */
@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {

    /**
     * Find all audit logs for a specific leave request
     */
    List<AuditLog> findByLeaveRequestIdOrderByCreatedAtAsc(UUID leaveRequestId);

    /**
     * Find audit logs by action type
     */
    List<AuditLog> findByAction(AuditLog.AuditAction action);
    
    /**
     * Find audit logs by process instance ID
     */
    List<AuditLog> findByProcessInstanceIdOrderByCreatedAtAsc(String processInstanceId);
}
