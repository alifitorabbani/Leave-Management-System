package com.len.leave.deboot;

import com.len.leave.deboot.entity.LeaveRequest;
import com.len.leave.deboot.entity.LeaveStatus;
import com.len.leave.deboot.entity.LeaveType;
import com.len.leave.deboot.repository.LeaveRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Initializer for Deboot Version
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
        if (leaveRequestRepository.count() > 0) {
            log.info("Database already contains data, skipping initialization");
            return;
        }

        log.info("Initializing sample data for Deboot Version...");

        List<LeaveRequest> sampleRequests = new ArrayList<>();

        // PENDING - Waiting Manager Approval (6 requests)
        sampleRequests.add(LeaveRequest.builder()
            .employeeName("Alexandra Chen")
            .employeeId("EMP201")
            .department("Frontend Team")
            .leaveType(LeaveType.ANNUAL)
            .startDate(LocalDate.now().plusDays(6))
            .endDate(LocalDate.now().plusDays(12))
            .reason("Korea trip with friends")
            .status(LeaveStatus.PENDING)
            .totalDays(7)
            .createdAt(LocalDateTime.now())
            .build());
            
        sampleRequests.add(LeaveRequest.builder()
            .employeeName("Benjamin Scott")
            .employeeId("EMP202")
            .department("Backend Team")
            .leaveType(LeaveType.SICK)
            .startDate(LocalDate.now().plusDays(2))
            .endDate(LocalDate.now().plusDays(3))
            .reason("Doctor appointment")
            .status(LeaveStatus.PENDING)
            .totalDays(2)
            .createdAt(LocalDateTime.now())
            .build());
            
        sampleRequests.add(LeaveRequest.builder()
            .employeeName("Catherine White")
            .employeeId("EMP203")
            .department("Mobile Team")
            .leaveType(LeaveType.PERSONAL)
            .startDate(LocalDate.now().plusDays(11))
            .endDate(LocalDate.now().plusDays(13))
            .reason("Wedding ceremony")
            .status(LeaveStatus.PENDING)
            .totalDays(3)
            .createdAt(LocalDateTime.now())
            .build());
            
        sampleRequests.add(LeaveRequest.builder()
            .employeeName("Daniel Roberts")
            .employeeId("EMP209")
            .department("UI/UX Team")
            .leaveType(LeaveType.ANNUAL)
            .startDate(LocalDate.now().plusDays(18))
            .endDate(LocalDate.now().plusDays(23))
            .reason("Europe tour")
            .status(LeaveStatus.PENDING)
            .totalDays(6)
            .createdAt(LocalDateTime.now())
            .build());
            
        sampleRequests.add(LeaveRequest.builder()
            .employeeName("Elizabeth Harris")
            .employeeId("EMP210")
            .department("QA Automation")
            .leaveType(LeaveType.SICK)
            .startDate(LocalDate.now().plusDays(4))
            .endDate(LocalDate.now().plusDays(5))
            .reason("Migraine")
            .status(LeaveStatus.PENDING)
            .totalDays(2)
            .createdAt(LocalDateTime.now())
            .build());
            
        sampleRequests.add(LeaveRequest.builder()
            .employeeName("Frank Miller")
            .employeeId("EMP211")
            .department("Infrastructure")
            .leaveType(LeaveType.PERSONAL)
            .startDate(LocalDate.now().plusDays(9))
            .endDate(LocalDate.now().plusDays(10))
            .reason("Car maintenance")
            .status(LeaveStatus.PENDING)
            .totalDays(2)
            .createdAt(LocalDateTime.now())
            .build());

        // MANAGER_APPROVED - Waiting HR Approval (4 requests)
        sampleRequests.add(LeaveRequest.builder()
            .employeeName("Grace Thompson")
            .employeeId("EMP204")
            .department("Frontend Team")
            .leaveType(LeaveType.ANNUAL)
            .startDate(LocalDate.now().minusDays(2))
            .endDate(LocalDate.now().plusDays(3))
            .reason("Year-end holiday")
            .status(LeaveStatus.MANAGER_APPROVED)
            .totalDays(6)
            .managerComment("Approved! Have fun!")
            .approvedByManager("Frontend Lead")
            .managerApprovalDate(LocalDateTime.now().minusDays(1))
            .createdAt(LocalDateTime.now().minusDays(4))
            .build());
            
        sampleRequests.add(LeaveRequest.builder()
            .employeeName("Henry Jackson")
            .employeeId("EMP205")
            .department("Backend Team")
            .leaveType(LeaveType.SICK)
            .startDate(LocalDate.now().minusDays(6))
            .endDate(LocalDate.now().minusDays(4))
            .reason("Recovering from surgery")
            .status(LeaveStatus.MANAGER_APPROVED)
            .totalDays(3)
            .managerComment("Take your time to recover!")
            .approvedByManager("Backend Manager")
            .managerApprovalDate(LocalDateTime.now().minusDays(5))
            .createdAt(LocalDateTime.now().minusDays(7))
            .build());
            
        sampleRequests.add(LeaveRequest.builder()
            .employeeName("Isabella Moore")
            .employeeId("EMP212")
            .department("Mobile Team")
            .leaveType(LeaveType.ANNUAL)
            .startDate(LocalDate.now().minusDays(5))
            .endDate(LocalDate.now().plusDays(1))
            .reason("Chinese New Year")
            .status(LeaveStatus.MANAGER_APPROVED)
            .totalDays(7)
            .managerComment("Happy holidays!")
            .approvedByManager("Mobile Lead")
            .managerApprovalDate(LocalDateTime.now().minusDays(4))
            .createdAt(LocalDateTime.now().minusDays(7))
            .build());
            
        sampleRequests.add(LeaveRequest.builder()
            .employeeName("Jack Williams")
            .employeeId("EMP213")
            .department("UI/UX Team")
            .leaveType(LeaveType.PERSONAL)
            .startDate(LocalDate.now().minusDays(9))
            .endDate(LocalDate.now().minusDays(6))
            .reason("Family reunion")
            .status(LeaveStatus.MANAGER_APPROVED)
            .totalDays(4)
            .managerComment("Approved. Enjoy with family!")
            .approvedByManager("UI/UX Manager")
            .managerApprovalDate(LocalDateTime.now().minusDays(8))
            .createdAt(LocalDateTime.now().minusDays(10))
            .build());

        // HR_APPROVED - Final Approved (4 requests)
        sampleRequests.add(LeaveRequest.builder()
            .employeeName("Kelly Brown")
            .employeeId("EMP206")
            .department("Frontend Team")
            .leaveType(LeaveType.MATERNITY)
            .startDate(LocalDate.now().minusDays(12))
            .endDate(LocalDate.now().plusDays(45))
            .reason("Maternity leave for newborn")
            .status(LeaveStatus.HR_APPROVED)
            .totalDays(58)
            .managerComment("Congratulations! Wishing you all the best.")
            .approvedByManager("Frontend Manager")
            .managerApprovalDate(LocalDateTime.now().minusDays(11))
            .hrComment("Congratulations on your new baby! Take care.")
            .approvedByHR("HR Director")
            .hrApprovalDate(LocalDateTime.now().minusDays(10))
            .createdAt(LocalDateTime.now().minusDays(13))
            .build());
            
        sampleRequests.add(LeaveRequest.builder()
            .employeeName("Liam Davis")
            .employeeId("EMP214")
            .department("Backend Team")
            .leaveType(LeaveType.ANNUAL)
            .startDate(LocalDate.now().minusDays(18))
            .endDate(LocalDate.now().minusDays(13))
            .reason("Annual leave")
            .status(LeaveStatus.HR_APPROVED)
            .totalDays(6)
            .managerComment("Approved!")
            .approvedByManager("Backend Lead")
            .managerApprovalDate(LocalDateTime.now().minusDays(17))
            .hrComment("Approved!")
            .approvedByHR("HR Manager")
            .hrApprovalDate(LocalDateTime.now().minusDays(16))
            .createdAt(LocalDateTime.now().minusDays(19))
            .build());
            
        sampleRequests.add(LeaveRequest.builder()
            .employeeName("Mia Garcia")
            .employeeId("EMP215")
            .department("Mobile Team")
            .leaveType(LeaveType.SICK)
            .startDate(LocalDate.now().minusDays(22))
            .endDate(LocalDate.now().minusDays(19))
            .reason("Long-term illness")
            .status(LeaveStatus.HR_APPROVED)
            .totalDays(4)
            .managerComment("Get well soon!")
            .approvedByManager("Mobile Manager")
            .managerApprovalDate(LocalDateTime.now().minusDays(21))
            .hrComment("Medical leave approved. Take care.")
            .approvedByHR("HR Director")
            .hrApprovalDate(LocalDateTime.now().minusDays(20))
            .createdAt(LocalDateTime.now().minusDays(23))
            .build());
            
        sampleRequests.add(LeaveRequest.builder()
            .employeeName("Noah Martinez")
            .employeeId("EMP216")
            .department("DevOps")
            .leaveType(LeaveType.PERSONAL)
            .startDate(LocalDate.now().minusDays(28))
            .endDate(LocalDate.now().minusDays(25))
            .reason("Moving out")
            .status(LeaveStatus.HR_APPROVED)
            .totalDays(4)
            .managerComment("Approved.")
            .approvedByManager("DevOps Lead")
            .managerApprovalDate(LocalDateTime.now().minusDays(27))
            .hrComment("Good luck with the move!")
            .approvedByHR("HR Manager")
            .hrApprovalDate(LocalDateTime.now().minusDays(26))
            .createdAt(LocalDateTime.now().minusDays(29))
            .build());

        // MANAGER_REJECTED (2 requests)
        sampleRequests.add(LeaveRequest.builder()
            .employeeName("Olivia Wilson")
            .employeeId("EMP207")
            .department("QA Team")
            .leaveType(LeaveType.ANNUAL)
            .startDate(LocalDate.now().minusDays(14))
            .endDate(LocalDate.now().minusDays(11))
            .reason("Vacation")
            .status(LeaveStatus.MANAGER_REJECTED)
            .totalDays(4)
            .managerComment("Critical release. Cannot approve at this time.")
            .approvedByManager("QA Manager")
            .managerApprovalDate(LocalDateTime.now().minusDays(13))
            .createdAt(LocalDateTime.now().minusDays(15))
            .build());
            
        sampleRequests.add(LeaveRequest.builder()
            .employeeName("Peter Parker")
            .employeeId("EMP217")
            .department("Infrastructure")
            .leaveType(LeaveType.PERSONAL)
            .startDate(LocalDate.now().minusDays(17))
            .endDate(LocalDate.now().minusDays(14))
            .reason("Trip abroad")
            .status(LeaveStatus.MANAGER_REJECTED)
            .totalDays(4)
            .managerComment("System maintenance scheduled. Need you here.")
            .approvedByManager("Infra Manager")
            .managerApprovalDate(LocalDateTime.now().minusDays(16))
            .createdAt(LocalDateTime.now().minusDays(18))
            .build());

        // HR_REJECTED (2 requests)
        sampleRequests.add(LeaveRequest.builder()
            .employeeName("Quinn Roberts")
            .employeeId("EMP208")
            .department("Mobile Team")
            .leaveType(LeaveType.PERSONAL)
            .startDate(LocalDate.now().minusDays(19))
            .endDate(LocalDate.now().minusDays(16))
            .reason("Personal issue")
            .status(LeaveStatus.HR_REJECTED)
            .totalDays(4)
            .managerComment("Approved by manager for HR decision.")
            .approvedByManager("Mobile Lead")
            .managerApprovalDate(LocalDateTime.now().minusDays(18))
            .hrComment("Insufficient documentation provided.")
            .approvedByHR("HR Director")
            .hrApprovalDate(LocalDateTime.now().minusDays(17))
            .createdAt(LocalDateTime.now().minusDays(20))
            .build());
            
        sampleRequests.add(LeaveRequest.builder()
            .employeeName("Rachel Adams")
            .employeeId("EMP218")
            .department("UI/UX Team")
            .leaveType(LeaveType.ANNUAL)
            .startDate(LocalDate.now().minusDays(24))
            .endDate(LocalDate.now().minusDays(19))
            .reason("Long vacation")
            .status(LeaveStatus.HR_REJECTED)
            .totalDays(6)
            .managerComment("Approved.")
            .approvedByManager("UI/UX Manager")
            .managerApprovalDate(LocalDateTime.now().minusDays(23))
            .hrComment("Leave balance not sufficient for this duration.")
            .approvedByHR("HR Manager")
            .hrApprovalDate(LocalDateTime.now().minusDays(22))
            .createdAt(LocalDateTime.now().minusDays(25))
            .build());

        leaveRequestRepository.saveAll(sampleRequests);
        log.info("Successfully initialized {} sample leave requests for Deboot Version", sampleRequests.size());
    }
}
