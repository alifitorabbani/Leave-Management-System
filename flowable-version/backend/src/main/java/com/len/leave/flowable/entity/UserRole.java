package com.len.leave.flowable.entity;

/**
 * User Role Enum
 * 
 * Defines the roles available in the system:
 * - EMPLOYEE: Can submit leave requests
 * - MANAGER: Can approve/reject leave requests (level 1)
 * - HR: Can give final approval/rejection
 * 
 * @author Alifito Rabbani Cahyono
 * @company PT. Len Industri (opersero)
 */
public enum UserRole {
    EMPLOYEE("Employee", "Can submit leave requests"),
    MANAGER("Manager", "Can approve/reject leave requests"),
    HR("Human Resources", "Can give final approval/rejection");

    private final String displayName;
    private final String description;

    UserRole(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}
