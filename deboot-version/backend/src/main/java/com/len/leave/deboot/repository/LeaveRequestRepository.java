package com.len.leave.deboot.repository;

import com.len.leave.deboot.entity.LeaveRequest;
import com.len.leave.deboot.entity.LeaveStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Leave Request Repository
 * 
 * Provides database access methods for LeaveRequest entity.
 * 
 * @author Alifito Rabbani Cahyono
 * @company PT. Len Industri (opersero)
 */
@Repository
public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, UUID> {

    /**
     * Find all leave requests by status
     */
    List<LeaveRequest> findByStatus(LeaveStatus status);

    /**
     * Find all leave requests by employee name
     */
    List<LeaveRequest> findByEmployeeName(String employeeName);

    /**
     * Find all pending requests (waiting for manager approval)
     */
    @Query("SELECT lr FROM LeaveRequest lr WHERE lr.status = :status ORDER BY lr.createdAt ASC")
    List<LeaveRequest> findPendingRequests(@Param("status") LeaveStatus status);

    /**
     * Find requests waiting for manager approval
     */
    @Query("SELECT lr FROM LeaveRequest lr WHERE lr.status = 'PENDING' ORDER BY lr.createdAt ASC")
    List<LeaveRequest> findRequestsWaitingForManagerApproval();

    /**
     * Find requests waiting for HR approval
     */
    @Query("SELECT lr FROM LeaveRequest lr WHERE lr.status = 'MANAGER_APPROVED' ORDER BY lr.createdAt ASC")
    List<LeaveRequest> findRequestsWaitingForHRApproval();

    /**
     * Find all requests with pagination
     */
    Page<LeaveRequest> findAllByOrderByCreatedAtDesc(Pageable pageable);

    /**
     * Find requests by employee ID
     */
    List<LeaveRequest> findByEmployeeIdOrderByCreatedAtDesc(String employeeId);

    /**
     * Count requests by status
     */
    long countByStatus(LeaveStatus status);
    
    /**
     * Find by workflow instance ID
     */
    LeaveRequest findByWorkflowInstanceId(String workflowInstanceId);
}
