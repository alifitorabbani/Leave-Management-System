package com.len.leave.deboot.entity;

/**
 * Leave Status Enum
 * 
 * Represents the possible statuses in the leave request workflow.
 * 
 * @author Alifito Rabbani Cahyono
 * @company PT. Len Industri (opersero)
 */
public enum LeaveStatus {
    
    /**
     * Initial state - waiting for manager approval
     */
    PENDING("Menunggu Approval Manager", "leave.pending"),
    
    /**
     * Manager has approved - waiting for HR approval
     */
    MANAGER_APPROVED("Approved oleh Manager", "leave.manager.approved"),
    
    /**
     * Manager has rejected - final state
     */
    MANAGER_REJECTED("Ditolak oleh Manager", "leave.manager.rejected"),
    
    /**
     * HR has approved - final approved state
     */
    HR_APPROVED("Approved (Final)", "leave.hr.approved"),
    
    /**
     * HR has rejected - final rejected state
     */
    HR_REJECTED("Ditolak oleh HR", "leave.hr.rejected");

    private final String displayName;
    private final String messageCode;

    LeaveStatus(String displayName, String messageCode) {
        this.displayName = displayName;
        this.messageCode = messageCode;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getMessageCode() {
        return messageCode;
    }

    /**
     * Check if this status is a final state (no more transitions possible)
     */
    public boolean isFinalState() {
        return this == MANAGER_REJECTED || this == HR_APPROVED || this == HR_REJECTED;
    }

    /**
     * Check if this status requires manager approval
     */
    public boolean requiresManagerApproval() {
        return this == PENDING;
    }

    /**
     * Check if this status requires HR approval
     */
    public boolean requiresHRApproval() {
        return this == MANAGER_APPROVED;
    }
}
