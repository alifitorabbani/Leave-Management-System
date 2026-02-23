package com.len.leave.manual;

import com.len.leave.manual.entity.LeaveRequest;
import com.len.leave.manual.entity.LeaveStatus;
import com.len.leave.manual.entity.LeaveType;
import com.len.leave.manual.repository.LeaveRequestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Initializer for Manual Version
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

        log.info("Initializing sample data for Manual Version...");

        List<LeaveRequest> sampleRequests = new ArrayList<>();

        // PENDING - Menunggu Approval Manager (5 requests)
        sampleRequests.add(LeaveRequest.builder()
            .employeeName("Budi Santoso")
            .employeeId("EMP001")
            .department("IT Development")
            .leaveType(LeaveType.ANNUAL)
            .startDate(LocalDate.now().plusDays(5))
            .endDate(LocalDate.now().plusDays(9))
            .reason("Liburan keluarga ke Bali")
            .status(LeaveStatus.PENDING)
            .totalDays(5)
            .createdAt(LocalDateTime.now())
            .build());
            
        sampleRequests.add(LeaveRequest.builder()
            .employeeName("Siti Rahayu")
            .employeeId("EMP002")
            .department("Human Resources")
            .leaveType(LeaveType.SICK)
            .startDate(LocalDate.now().plusDays(2))
            .endDate(LocalDate.now().plusDays(3))
            .reason("Demam dan flu berat")
            .status(LeaveStatus.PENDING)
            .totalDays(2)
            .createdAt(LocalDateTime.now())
            .build());
            
        sampleRequests.add(LeaveRequest.builder()
            .employeeName("Ahmad Wijaya")
            .employeeId("EMP003")
            .department("Finance")
            .leaveType(LeaveType.PERSONAL)
            .startDate(LocalDate.now().plusDays(10))
            .endDate(LocalDate.now().plusDays(12))
            .reason("Urusan pribadi yang mendesak")
            .status(LeaveStatus.PENDING)
            .totalDays(3)
            .createdAt(LocalDateTime.now())
            .build());
            
        sampleRequests.add(LeaveRequest.builder()
            .employeeName("Dewi Lestari")
            .employeeId("EMP004")
            .department("Marketing")
            .leaveType(LeaveType.ANNUAL)
            .startDate(LocalDate.now().plusDays(15))
            .endDate(LocalDate.now().plusDays(19))
            .reason("Liburan natal ke kampung halaman")
            .status(LeaveStatus.PENDING)
            .totalDays(5)
            .createdAt(LocalDateTime.now())
            .build());
            
        sampleRequests.add(LeaveRequest.builder()
            .employeeName("Rudi Hermawan")
            .employeeId("EMP005")
            .department("Operations")
            .leaveType(LeaveType.SICK)
            .startDate(LocalDate.now().plusDays(1))
            .endDate(LocalDate.now().plusDays(2))
            .reason("Sakit gigi")
            .status(LeaveStatus.PENDING)
            .totalDays(2)
            .createdAt(LocalDateTime.now())
            .build());

        // MANAGER_APPROVED - Menunggu HR (4 requests)
        sampleRequests.add(LeaveRequest.builder()
            .employeeName("Dewi Lestari")
            .employeeId("EMP004")
            .department("Marketing")
            .leaveType(LeaveType.ANNUAL)
            .startDate(LocalDate.now().minusDays(5))
            .endDate(LocalDate.now().minusDays(1))
            .reason("Liburan akhir tahun")
            .status(LeaveStatus.MANAGER_APPROVED)
            .totalDays(5)
            .managerComment("Disetujui, enjoy your holiday!")
            .approvedByManager("Manager Marketing")
            .managerApprovalDate(LocalDateTime.now().minusDays(4))
            .createdAt(LocalDateTime.now().minusDays(6))
            .build());
            
        sampleRequests.add(LeaveRequest.builder()
            .employeeName("Rudi Hermawan")
            .employeeId("EMP005")
            .department("Operations")
            .leaveType(LeaveType.SICK)
            .startDate(LocalDate.now().minusDays(10))
            .endDate(LocalDate.now().minusDays(8))
            .reason("Sakit kecil")
            .status(LeaveStatus.MANAGER_APPROVED)
            .totalDays(3)
            .managerComment("Get well soon!")
            .approvedByManager("Manager Operations")
            .managerApprovalDate(LocalDateTime.now().minusDays(9))
            .createdAt(LocalDateTime.now().minusDays(11))
            .build());
            
        sampleRequests.add(LeaveRequest.builder()
            .employeeName("Hendra Gunawan")
            .employeeId("EMP009")
            .department("IT Development")
            .leaveType(LeaveType.ANNUAL)
            .startDate(LocalDate.now().minusDays(3))
            .endDate(LocalDate.now().plusDays(1))
            .reason("Cuti panjang")
            .status(LeaveStatus.MANAGER_APPROVED)
            .totalDays(5)
            .managerComment("Approved!")
            .approvedByManager("IT Manager")
            .managerApprovalDate(LocalDateTime.now().minusDays(2))
            .createdAt(LocalDateTime.now().minusDays(5))
            .build());

        sampleRequests.add(LeaveRequest.builder()
            .employeeName("Maya Putri")
            .employeeId("EMP010")
            .department("Finance")
            .leaveType(LeaveType.PERSONAL)
            .startDate(LocalDate.now().minusDays(7))
            .endDate(LocalDate.now().minusDays(4))
            .reason("Urusan keluarga")
            .status(LeaveStatus.MANAGER_APPROVED)
            .totalDays(4)
            .managerComment("Disetujui.")
            .approvedByManager("Finance Manager")
            .managerApprovalDate(LocalDateTime.now().minusDays(6))
            .createdAt(LocalDateTime.now().minusDays(8))
            .build());

        // HR_APPROVED - Approved Final (3 requests)
        sampleRequests.add(LeaveRequest.builder()
            .employeeName("Fitri Handayani")
            .employeeId("EMP006")
            .department("IT Development")
            .leaveType(LeaveType.MATERNITY)
            .startDate(LocalDate.now().minusDays(15))
            .endDate(LocalDate.now().plusDays(30))
            .reason("Cuti melahirkan")
            .status(LeaveStatus.HR_APPROVED)
            .totalDays(46)
            .managerComment("Selamat! Disetujui.")
            .approvedByManager("IT Manager")
            .managerApprovalDate(LocalDateTime.now().minusDays(14))
            .hrComment("Selamat menanti kelahiran buah hati!")
            .approvedByHR("HR Manager")
            .hrApprovalDate(LocalDateTime.now().minusDays(13))
            .createdAt(LocalDateTime.now().minusDays(16))
            .build());
            
        sampleRequests.add(LeaveRequest.builder()
            .employeeName("Tono Suhartono")
            .employeeId("EMP011")
            .department("Sales")
            .leaveType(LeaveType.ANNUAL)
            .startDate(LocalDate.now().minusDays(20))
            .endDate(LocalDate.now().minusDays(15))
            .reason("Liburan tahunan")
            .status(LeaveStatus.HR_APPROVED)
            .totalDays(6)
            .managerComment("Approved!")
            .approvedByManager("Sales Manager")
            .managerApprovalDate(LocalDateTime.now().minusDays(19))
            .hrComment("approved!")
            .approvedByHR("HR Manager")
            .hrApprovalDate(LocalDateTime.now().minusDays(18))
            .createdAt(LocalDateTime.now().minusDays(22))
            .build());
            
        sampleRequests.add(LeaveRequest.builder()
            .employeeName("Wati Susilowati")
            .employeeId("EMP012")
            .department("HR")
            .leaveType(LeaveType.SICK)
            .startDate(LocalDate.now().minusDays(12))
            .endDate(LocalDate.now().minusDays(10))
            .reason("Sakit berat")
            .status(LeaveStatus.HR_APPROVED)
            .totalDays(3)
            .managerComment("Disetujui.")
            .approvedByManager("HR Director")
            .managerApprovalDate(LocalDateTime.now().minusDays(11))
            .hrComment("Get well soon!")
            .approvedByHR("HR Manager")
            .hrApprovalDate(LocalDateTime.now().minusDays(10))
            .createdAt(LocalDateTime.now().minusDays(13))
            .build());

        // MANAGER_REJECTED (2 requests)
        sampleRequests.add(LeaveRequest.builder()
            .employeeName("Joko Prasetyo")
            .employeeId("EMP007")
            .department("Sales")
            .leaveType(LeaveType.ANNUAL)
            .startDate(LocalDate.now().minusDays(20))
            .endDate(LocalDate.now().minusDays(18))
            .reason("Liburan")
            .status(LeaveStatus.MANAGER_REJECTED)
            .totalDays(3)
            .managerComment("Maaf, sales sedang tinggi. Mohon ajukan di bulan depan.")
            .approvedByManager("Sales Manager")
            .managerApprovalDate(LocalDateTime.now().minusDays(19))
            .createdAt(LocalDateTime.now().minusDays(21))
            .build());
            
        sampleRequests.add(LeaveRequest.builder()
            .employeeName("B Slamet")
            .employeeId("EMP013")
            .department("Operations")
            .leaveType(LeaveType.PERSONAL)
            .startDate(LocalDate.now().minusDays(25))
            .endDate(LocalDate.now().minusDays(22))
            .reason("Urusan pribadi")
            .status(LeaveStatus.MANAGER_REJECTED)
            .totalDays(4)
            .managerComment("Tidak bisaapprove, sedang ada project deadline.")
            .approvedByManager("Operations Manager")
            .managerApprovalDate(LocalDateTime.now().minusDays(24))
            .createdAt(LocalDateTime.now().minusDays(26))
            .build());

        // HR_REJECTED (2 requests)
        sampleRequests.add(LeaveRequest.builder()
            .employeeName("Nina Kartika")
            .employeeId("EMP008")
            .department("Finance")
            .leaveType(LeaveType.PERSONAL)
            .startDate(LocalDate.now().minusDays(25))
            .endDate(LocalDate.now().minusDays(23))
            .reason("Urusan keluarga")
            .status(LeaveStatus.HR_REJECTED)
            .totalDays(3)
            .managerComment("Tidak ada yang menggantikan tugas, disetujui sementara.")
            .approvedByManager("Finance Manager")
            .managerApprovalDate(LocalDateTime.now().minusDays(24))
            .hrComment("Mohon ajukan ulang dengan cover yang lebih baik.")
            .approvedByHR("HR Manager")
            .hrApprovalDate(LocalDateTime.now().minusDays(23))
            .createdAt(LocalDateTime.now().minusDays(26))
            .build());
            
        sampleRequests.add(LeaveRequest.builder()
            .employeeName("Doni Kusuma")
            .employeeId("EMP014")
            .department("Marketing")
            .leaveType(LeaveType.ANNUAL)
            .startDate(LocalDate.now().minusDays(30))
            .endDate(LocalDate.now().minusDays(25))
            .reason("Liburan")
            .status(LeaveStatus.HR_REJECTED)
            .totalDays(6)
            .managerComment("Approved.")
            .approvedByManager("Marketing Manager")
            .managerApprovalDate(LocalDateTime.now().minusDays(29))
            .hrComment("Annual leave sudah melebihi quota. Denied.")
            .approvedByHR("HR Director")
            .hrApprovalDate(LocalDateTime.now().minusDays(28))
            .createdAt(LocalDateTime.now().minusDays(31))
            .build());

        leaveRequestRepository.saveAll(sampleRequests);
        log.info("Successfully initialized {} sample leave requests for Manual Version", sampleRequests.size());
    }
}
