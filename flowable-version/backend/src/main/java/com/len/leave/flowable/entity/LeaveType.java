package com.len.leave.flowable.entity;

/**
 * Leave Type Enum
 * 
 * Represents the types of leave available in the system.
 * 
 * @author Alifito Rabbani Cahyono
 * @company PT. Len Industri (opersero)
 */
public enum LeaveType {
    
    /**
     * Annual leave - paid leave for vacation
     */
    ANNUAL("Cuti Tahunan", 12),
    
    /**
     * Sick leave - paid leave for illness
     */
    SICK("Cuti Sakit", 14),
    
    /**
     * Personal leave - unpaid leave for personal matters
     */
    PERSONAL("Cuti Pribadi", 3),
    
    /**
     * Maternity leave
     */
    MATERNITY("Cuti Melahirkan", 90),
    
    /**
     * Paternity leave
     */
    PATERNITY("Cuti Ayah", 7);

    private final String displayName;
    private final int defaultDays;

    LeaveType(String displayName, int defaultDays) {
        this.displayName = displayName;
        this.defaultDays = defaultDays;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getDefaultDays() {
        return defaultDays;
    }
}
