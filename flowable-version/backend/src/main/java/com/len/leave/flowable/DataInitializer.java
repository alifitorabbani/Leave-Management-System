package com.len.leave.flowable;

import com.len.leave.flowable.entity.LeaveRequest;
import com.len.leave.flowable.entity.LeaveStatus;
import com.len.leave.flowable.entity.LeaveType;
import com.len.leave.flowable.repository.LeaveRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Initializer for Flowable Version
 * 
 * Loads extensive sample leave requests into the database on startup.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final LeaveRequestRepository leaveRequestRepository;

    @Override
    public void run(String... args) {
        // Always initialize sample data for demo purposes
        // Clear existing data and create fresh sample data
        leaveRequestRepository.deleteAll();
        
        log.info("Initializing sample data for Flowable Version...");

        List<LeaveRequest> sampleRequests = new ArrayList<>();

        // PENDING - Waiting Manager Approval (6 requests)
        sampleRequests.add(LeaveRequest.builder()
            .employeeName("Michael Chen")
            .employeeId("EMP101")
            .department("Engineering")
            .leaveType(LeaveType.ANNUAL)
            .startDate(LocalDate.now().plusDays(7))
            .endDate(LocalDate.now().plusDays(14))
            .reason("Family vacation to Japan")
            .status(LeaveStatus.PENDING)
            .totalDays(8)
            .createdAt(LocalDateTime.now())
            .build());
            
        sampleRequests.add(LeaveRequest.builder()
            .employeeName("Sarah Johnson")
            .employeeId("EMP102")
            .department("Product")
            .leaveType(LeaveType.SICK)
            .startDate(LocalDate.now().plusDays(1))
            .endDate(LocalDate.now().plusDays(2))
            .reason("Medical checkup")
            .status(LeaveStatus.PENDING)
            .totalDays(2)
            .createdAt(LocalDateTime.now())
            .build());
            
        sampleRequests.add(LeaveRequest.builder()
            .employeeName("David Kim")
            .employeeId("EMP103")
            .department("Design")
            .leaveType(LeaveType.PERSONAL)
            .startDate(LocalDate.now().plusDays(14))
            .endDate(LocalDate.now().plusDays(16))
            .reason("Personal matters")
            .status(LeaveStatus.PENDING)
            .totalDays(3)
            .createdAt(LocalDateTime.now())
            .build());
            
        sampleRequests.add(LeaveRequest.builder()
            .employeeName("Jessica Williams")
            .employeeId("EMP109")
            .department("QA")
            .leaveType(LeaveType.ANNUAL)
            .startDate(LocalDate.now().plusDays(20))
            .endDate(LocalDate.now().plusDays(25))
            .reason("Summer holiday")
            .status(LeaveStatus.PENDING)
            .totalDays(6)
            .createdAt(LocalDateTime.now())
            .build());
            
        sampleRequests.add(LeaveRequest.builder()
            .employeeName("Thomas Anderson")
            .employeeId("EMP110")
            .department("Security")
            .leaveType(LeaveType.SICK)
            .startDate(LocalDate.now().plusDays(3))
            .endDate(LocalDate.now().plusDays(4))
            .reason("Food poisoning")
            .status(LeaveStatus.PENDING)
            .totalDays(2)
            .createdAt(LocalDateTime.now())
            .build());
            
        sampleRequests.add(LeaveRequest.builder()
            .employeeName("Amanda Garcia")
            .employeeId("EMP111")
            .department("DevOps")
            .leaveType(LeaveType.PERSONAL)
            .startDate(LocalDate.now().plusDays(8))
            .endDate(LocalDate.now().plusDays(9))
            .reason("Moving to new house")
            .status(LeaveStatus.PENDING)
            .totalDays(2)
            .createdAt(LocalDateTime.now())
            .build());

        // MANAGER_APPROVED - Waiting HR Approval (4 requests)
        sampleRequests.add(LeaveRequest.builder()
            .employeeName("Emma Wilson")
            .employeeId("EMP104")
            .department("QA")
            .leaveType(LeaveType.ANNUAL)
            .startDate(LocalDate.now().minusDays(3))
            .endDate(LocalDate.now().plusDays(1))
            .reason("Holiday break")
            .status(LeaveStatus.MANAGER_APPROVED)
            .totalDays(5)
            .managerComment("Approved! Enjoy your holiday.")
            .approvedByManager("QA Lead")
            .managerApprovalDate(LocalDateTime.now().minusDays(2))
            .createdAt(LocalDateTime.now().minusDays(5))
            .build());
            
        sampleRequests.add(LeaveRequest.builder()
            .employeeName("James Brown")
            .employeeId("EMP105")
            .department("DevOps")
            .leaveType(LeaveType.SICK)
            .startDate(LocalDate.now().minusDays(7))
            .endDate(LocalDate.now().minusDays(5))
            .reason("Flu recovery")
            .status(LeaveStatus.MANAGER_APPROVED)
            .totalDays(3)
            .managerComment("Get well soon!")
            .approvedByManager("DevOps Manager")
            .managerApprovalDate(LocalDateTime.now().minusDays(6))
            .createdAt(LocalDateTime.now().minusDays(8))
            .build());
            
        sampleRequests.add(LeaveRequest.builder()
            .employeeName("Christopher Lee")
            .employeeId("EMP112")
            .department("Engineering")
            .leaveType(LeaveType.ANNUAL)
            .startDate(LocalDate.now().minusDays(4))
            .endDate(LocalDate.now().plusDays(2))
            .reason("Year-end vacation")
            .status(LeaveStatus.MANAGER_APPROVED)
            .totalDays(7)
            .managerComment("Have a great time!")
            .approvedByManager("Engineering Manager")
            .managerApprovalDate(LocalDateTime.now().minusDays(3))
            .createdAt(LocalDateTime.now().minusDays(6))
            .build());
            
        sampleRequests.add(LeaveRequest.builder()
            .employeeName("Michelle Davis")
            .employeeId("EMP113")
            .department("Product")
            .leaveType(LeaveType.PERSONAL)
            .startDate(LocalDate.now().minusDays(8))
            .endDate(LocalDate.now().minusDays(5))
            .reason("Family emergency")
            .status(LeaveStatus.MANAGER_APPROVED)
            .totalDays(4)
            .managerComment("Approved. Take care.")
            .approvedByManager("Product Manager")
            .managerApprovalDate(LocalDateTime.now().minusDays(7))
            .createdAt(LocalDateTime.now().minusDays(9))
            .build());

        // HR_APPROVED - Final Approved (4 requests)
        sampleRequests.add(LeaveRequest.builder()
            .employeeName("Lisa Anderson")
            .employeeId("EMP106")
            .department("Engineering")
            .leaveType(LeaveType.PATERNITY)
            .startDate(LocalDate.now().minusDays(10))
            .endDate(LocalDate.now().plusDays(20))
            .reason("Paternity leave for newborn")
            .status(LeaveStatus.HR_APPROVED)
            .totalDays(31)
            .managerComment("Congratulations! Approved.")
            .approvedByManager("Engineering Manager")
            .managerApprovalDate(LocalDateTime.now().minusDays(9))
            .hrComment("Enjoy time with your baby.")
            .approvedByHR("HR Director")
            .hrApprovalDate(LocalDateTime.now().minusDays(8))
            .createdAt(LocalDateTime.now().minusDays(11))
            .build());
            
        sampleRequests.add(LeaveRequest.builder()
            .employeeName("Kevin Martinez")
            .employeeId("EMP114")
            .department("Design")
            .leaveType(LeaveType.ANNUAL)
            .startDate(LocalDate.now().minusDays(15))
            .endDate(LocalDate.now().minusDays(10))
            .reason("Annual leave")
            .status(LeaveStatus.HR_APPROVED)
            .totalDays(6)
            .managerComment("Approved!")
            .approvedByManager("Design Lead")
            .managerApprovalDate(LocalDateTime.now().minusDays(14))
            .hrComment("Approved!")
            .approvedByHR("HR Manager")
            .hrApprovalDate(LocalDateTime.now().minusDays(13))
            .createdAt(LocalDateTime.now().minusDays(16))
            .build());
            
        sampleRequests.add(LeaveRequest.builder()
            .employeeName("Rachel Green")
            .employeeId("EMP115")
            .department("Marketing")
            .leaveType(LeaveType.SICK)
            .startDate(LocalDate.now().minusDays(20))
            .endDate(LocalDate.now().minusDays(17))
            .reason("Surgery recovery")
            .status(LeaveStatus.HR_APPROVED)
            .totalDays(4)
            .managerComment("Get well soon!")
            .approvedByManager("Marketing Manager")
            .managerApprovalDate(LocalDateTime.now().minusDays(19))
            .hrComment("Medical leave approved.")
            .approvedByHR("HR Director")
            .hrApprovalDate(LocalDateTime.now().minusDays(18))
            .createdAt(LocalDateTime.now().minusDays(21))
            .build());
            
        sampleRequests.add(LeaveRequest.builder()
            .employeeName("Steven Spielberg")
            .employeeId("EMP116")
            .department("Content")
            .leaveType(LeaveType.PERSONAL)
            .startDate(LocalDate.now().minusDays(25))
            .endDate(LocalDate.now().minusDays(22))
            .reason("Personal development")
            .status(LeaveStatus.HR_APPROVED)
            .totalDays(4)
            .managerComment("Approved.")
            .approvedByManager("Content Manager")
            .managerApprovalDate(LocalDateTime.now().minusDays(24))
            .hrComment("Self-improvement is important!")
            .approvedByHR("HR Manager")
            .hrApprovalDate(LocalDateTime.now().minusDays(23))
            .createdAt(LocalDateTime.now().minusDays(26))
            .build());

        // MANAGER_REJECTED (2 requests)
        sampleRequests.add(LeaveRequest.builder()
            .employeeName("Robert Taylor")
            .employeeId("EMP107")
            .department("Security")
            .leaveType(LeaveType.ANNUAL)
            .startDate(LocalDate.now().minusDays(15))
            .endDate(LocalDate.now().minusDays(12))
            .reason("Annual leave")
            .status(LeaveStatus.MANAGER_REJECTED)
            .totalDays(4)
            .managerComment("Need coverage during that period. Please reschedule.")
            .approvedByManager("Security Manager")
            .managerApprovalDate(LocalDateTime.now().minusDays(14))
            .createdAt(LocalDateTime.now().minusDays(16))
            .build());
            
        sampleRequests.add(LeaveRequest.builder()
            .employeeName("Brian O'Connor")
            .employeeId("EMP117")
            .department("Operations")
            .leaveType(LeaveType.PERSONAL)
            .startDate(LocalDate.now().minusDays(18))
            .endDate(LocalDate.now().minusDays(15))
            .reason("Personal trip")
            .status(LeaveStatus.MANAGER_REJECTED)
            .totalDays(4)
            .managerComment("Peak season. Cannot approve.")
            .approvedByManager("Operations Lead")
            .managerApprovalDate(LocalDateTime.now().minusDays(17))
            .createdAt(LocalDateTime.now().minusDays(19))
            .build());

        // HR_REJECTED (2 requests)
        sampleRequests.add(LeaveRequest.builder()
            .employeeName("Jennifer Martinez")
            .employeeId("EMP108")
            .department("Product")
            .leaveType(LeaveType.PERSONAL)
            .startDate(LocalDate.now().minusDays(20))
            .endDate(LocalDate.now().minusDays(17))
            .reason("Personal emergency")
            .status(LeaveStatus.HR_REJECTED)
            .totalDays(4)
            .managerComment("Approved by manager pending HR review.")
            .approvedByManager("Product Manager")
            .managerApprovalDate(LocalDateTime.now().minusDays(19))
            .hrComment("Emergency coverage not arranged. Request denied.")
            .approvedByHR("HR Director")
            .hrApprovalDate(LocalDateTime.now().minusDays(18))
            .createdAt(LocalDateTime.now().minusDays(21))
            .build());
            
        sampleRequests.add(LeaveRequest.builder()
            .employeeName("Diana Prince")
            .employeeId("EMP118")
            .department("HR")
            .leaveType(LeaveType.ANNUAL)
            .startDate(LocalDate.now().minusDays(22))
            .endDate(LocalDate.now().minusDays(17))
            .reason("Vacation")
            .status(LeaveStatus.HR_REJECTED)
            .totalDays(6)
            .managerComment("Approved.")
            .approvedByManager("HR Director")
            .managerApprovalDate(LocalDateTime.now().minusDays(21))
            .hrComment("Leave quota exceeded for this year.")
            .approvedByHR("HR Manager")
            .hrApprovalDate(LocalDateTime.now().minusDays(20))
            .createdAt(LocalDateTime.now().minusDays(23))
            .build());

        leaveRequestRepository.saveAll(sampleRequests);
        log.info("Successfully initialized {} sample leave requests for Flowable Version", sampleRequests.size());
    }
}
