-- Dummy data for Manual Version (leave_manual_db)
-- Run: PGPASSWORD=leave123 psql -U postgres_manual -d leave_manual_db -f data.sql

-- PENDING - Waiting Manager Approval (5 requests)
INSERT INTO leave_requests (id, employee_name, employee_id, department, leave_type, start_date, end_date, reason, status, total_days, created_at, updated_at)
VALUES 
('a1b2c3d4-e5f6-4789-a012-345678901234', 'Budi Santoso', 'EMP001', 'IT Development', 'ANNUAL', CURRENT_DATE + 5, CURRENT_DATE + 9, 'Liburan keluarga ke Bali', 'PENDING', 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('b2c3d4e5-f6a7-4890-b123-456789012345', 'Siti Rahayu', 'EMP002', 'Human Resources', 'SICK', CURRENT_DATE + 2, CURRENT_DATE + 3, 'Demam dan flu berat', 'PENDING', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('c3d4e5f6-a7b8-4901-c234-567890123456', 'Ahmad Wijaya', 'EMP003', 'Finance', 'PERSONAL', CURRENT_DATE + 10, CURRENT_DATE + 12, 'Urusan pribadi yang mendesak', 'PENDING', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('d4e5f6a7-b8c9-4012-d345-678901234567', 'Dewi Lestari', 'EMP004', 'Marketing', 'ANNUAL', CURRENT_DATE + 15, CURRENT_DATE + 19, 'Liburan natal ke kampung halaman', 'PENDING', 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('e5f6a7b8-c9d0-4123-e456-789012345678', 'Rudi Hermawan', 'EMP005', 'Operations', 'SICK', CURRENT_DATE + 1, CURRENT_DATE + 2, 'Sakit gigi', 'PENDING', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- MANAGER_APPROVED - Waiting HR Approval (4 requests)
INSERT INTO leave_requests (id, employee_name, employee_id, department, leave_type, start_date, end_date, reason, status, total_days, manager_comment, approved_by_manager, manager_approval_date, created_at, updated_at)
VALUES 
('f6a7b8c9-d0e1-4234-f567-890123456789', 'Dewi Lestari', 'EMP004', 'Marketing', 'ANNUAL', CURRENT_DATE - 5, CURRENT_DATE - 1, 'Liburan akhir tahun', 'MANAGER_APPROVED', 5, 'Disetujui, enjoy your holiday!', 'Manager Marketing', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP - INTERVAL '6 days', CURRENT_TIMESTAMP),
('a7b8c9d0-e1f2-4345-g678-901234567890', 'Rudi Hermawan', 'EMP005', 'Operations', 'SICK', CURRENT_DATE - 10, CURRENT_DATE - 8, 'Sakit kecil', 'MANAGER_APPROVED', 3, 'Get well soon!', 'Manager Operations', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP - INTERVAL '11 days', CURRENT_TIMESTAMP),
('b8c9d0e1-f2a3-4456-h789-012345678901', 'Hendra Gunawan', 'EMP009', 'IT Development', 'ANNUAL', CURRENT_DATE - 3, CURRENT_DATE + 1, 'Cuti panjang', 'MANAGER_APPROVED', 5, 'Approved!', 'IT Manager', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP - INTERVAL '5 days', CURRENT_TIMESTAMP),
('c9d0e1f2-a3b4-4567-i890-123456789012', 'Maya Putri', 'EMP010', 'Finance', 'PERSONAL', CURRENT_DATE - 7, CURRENT_DATE - 4, 'Urusan keluarga', 'MANAGER_APPROVED', 4, 'Disetujui.', 'Finance Manager', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP - INTERVAL '8 days', CURRENT_TIMESTAMP);

-- HR_APPROVED - Final Approved (3 requests)
INSERT INTO leave_requests (id, employee_name, employee_id, department, leave_type, start_date, end_date, reason, status, total_days, manager_comment, approved_by_manager, manager_approval_date, hr_comment, approved_by_hr, hr_approval_date, created_at, updated_at)
VALUES 
('d0e1f2a3-b4c5-4678-j901-234567890123', 'Fitri Handayani', 'EMP006', 'IT Development', 'MATERNITY', CURRENT_DATE - 15, CURRENT_DATE + 30, 'Cuti melahirkan', 'HR_APPROVED', 46, 'Selamat! Disetujui.', 'IT Manager', CURRENT_TIMESTAMP, 'Selamat menanti kelahiran buah hati!', 'HR Manager', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP - INTERVAL '16 days', CURRENT_TIMESTAMP),
('e1f2a3b4-c5d6-4789-k012-345678901234', 'Tono Suhartono', 'EMP011', 'Sales', 'ANNUAL', CURRENT_DATE - 20, CURRENT_DATE - 15, 'Liburan tahunan', 'HR_APPROVED', 6, 'Approved!', 'Sales Manager', CURRENT_TIMESTAMP, 'approved!', 'HR Manager', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP - INTERVAL '22 days', CURRENT_TIMESTAMP),
('f2a3b4c5-d6e7-4890-l123-456789012345', 'Wati Susilowati', 'EMP012', 'HR', 'SICK', CURRENT_DATE - 12, CURRENT_DATE - 10, 'Sakit berat', 'HR_APPROVED', 3, 'Disetujui.', 'HR Director', CURRENT_TIMESTAMP, 'Get well soon!', 'HR Manager', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP - INTERVAL '13 days', CURRENT_TIMESTAMP);

-- MANAGER_REJECTED (2 requests)
INSERT INTO leave_requests (id, employee_name, employee_id, department, leave_type, start_date, end_date, reason, status, total_days, manager_comment, approved_by_manager, manager_approval_date, created_at, updated_at)
VALUES 
('a3b4c5d6-e7f8-4901-m234-567890123456', 'Joko Prasetyo', 'EMP007', 'Sales', 'ANNUAL', CURRENT_DATE - 20, CURRENT_DATE - 18, 'Liburan', 'MANAGER_REJECTED', 3, 'Maaf, sales sedang tinggi. Mohon ajukan di bulan depan.', 'Sales Manager', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP - INTERVAL '21 days', CURRENT_TIMESTAMP),
('b4c5d6e7-f8a9-4012-n345-678901234567', 'B Slamet', 'EMP013', 'Operations', 'PERSONAL', CURRENT_DATE - 25, CURRENT_DATE - 22, 'Urusan pribadi', 'MANAGER_REJECTED', 4, 'Tidak bisaapprove, sedang ada project deadline.', 'Operations Manager', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP - INTERVAL '26 days', CURRENT_TIMESTAMP);

-- HR_REJECTED (2 requests)
INSERT INTO leave_requests (id, employee_name, employee_id, department, leave_type, start_date, end_date, reason, status, total_days, manager_comment, approved_by_manager, manager_approval_date, hr_comment, approved_by_hr, hr_approval_date, created_at, updated_at)
VALUES 
('c5d6e7f8-a9b0-4123-o456-789012345678', 'Nina Kartika', 'EMP008', 'Finance', 'PERSONAL', CURRENT_DATE - 25, CURRENT_DATE - 23, 'Urusan keluarga', 'HR_REJECTED', 3, 'Tidak ada yang menggantikan tugas, disetujui sementara.', 'Finance Manager', CURRENT_TIMESTAMP, 'Mohon ajukan ulang dengan cover yang lebih baik.', 'HR Manager', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP - INTERVAL '26 days', CURRENT_TIMESTAMP),
('d6e7f8a9-b0c1-4234-p567-890123456789', 'Doni Kusuma', 'EMP014', 'Marketing', 'ANNUAL', CURRENT_DATE - 30, CURRENT_DATE - 25, 'Liburan', 'HR_REJECTED', 6, 'Approved.', 'Marketing Manager', CURRENT_TIMESTAMP, 'Annual leave sudah melebihi quota. Denied.', 'HR Director', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP - INTERVAL '31 days', CURRENT_TIMESTAMP);
