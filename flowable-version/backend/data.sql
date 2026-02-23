-- Dummy data for Flowable Version (leave_flowable_db)

-- PENDING - Waiting Manager Approval (6 requests)
INSERT INTO leave_requests (id, employee_name, employee_id, department, leave_type, start_date, end_date, reason, status, total_days, created_at, updated_at)
VALUES 
(gen_random_uuid(), 'Michael Chen', 'EMP101', 'Engineering', 'ANNUAL', CURRENT_DATE + 7, CURRENT_DATE + 14, 'Family vacation to Japan', 'PENDING', 8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(gen_random_uuid(), 'Sarah Johnson', 'EMP102', 'Product', 'SICK', CURRENT_DATE + 1, CURRENT_DATE + 2, 'Medical checkup', 'PENDING', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(gen_random_uuid(), 'David Kim', 'EMP103', 'Design', 'PERSONAL', CURRENT_DATE + 14, CURRENT_DATE + 16, 'Personal matters', 'PENDING', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(gen_random_uuid(), 'Jessica Williams', 'EMP109', 'QA', 'ANNUAL', CURRENT_DATE + 20, CURRENT_DATE + 25, 'Summer holiday', 'PENDING', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(gen_random_uuid(), 'Thomas Anderson', 'EMP110', 'Security', 'SICK', CURRENT_DATE + 3, CURRENT_DATE + 4, 'Food poisoning', 'PENDING', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(gen_random_uuid(), 'Amanda Garcia', 'EMP111', 'DevOps', 'PERSONAL', CURRENT_DATE + 8, CURRENT_DATE + 9, 'Moving to new house', 'PENDING', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- MANAGER_APPROVED - Waiting HR Approval (4 requests)
INSERT INTO leave_requests (id, employee_name, employee_id, department, leave_type, start_date, end_date, reason, status, total_days, manager_comment, approved_by_manager, manager_approval_date, created_at, updated_at)
VALUES 
(gen_random_uuid(), 'Emma Wilson', 'EMP104', 'QA', 'ANNUAL', CURRENT_DATE - 3, CURRENT_DATE + 1, 'Holiday break', 'MANAGER_APPROVED', 5, 'Approved! Enjoy your holiday.', 'QA Lead', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP - INTERVAL '5 days', CURRENT_TIMESTAMP),
(gen_random_uuid(), 'James Brown', 'EMP105', 'DevOps', 'SICK', CURRENT_DATE - 7, CURRENT_DATE - 5, 'Flu recovery', 'MANAGER_APPROVED', 3, 'Get well soon!', 'DevOps Manager', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '8 days', CURRENT_TIMESTAMP),
(gen_random_uuid(), 'Christopher Lee', 'EMP112', 'Engineering', 'ANNUAL', CURRENT_DATE - 4, CURRENT_DATE + 2, 'Year-end vacation', 'MANAGER_APPROVED', 7, 'Have a great time!', 'Engineering Manager', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '6 days', CURRENT_TIMESTAMP),
(gen_random_uuid(), 'Michelle Davis', 'EMP113', 'Product', 'PERSONAL', CURRENT_DATE - 8, CURRENT_DATE - 5, 'Family emergency', 'MANAGER_APPROVED', 4, 'Approved. Take care.', 'Product Manager', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '9 days', CURRENT_TIMESTAMP);

-- HR_APPROVED - Final Approved (4 requests)
INSERT INTO leave_requests (id, employee_name, employee_id, department, leave_type, start_date, end_date, reason, status, total_days, manager_comment, approved_by_manager, manager_approval_date, hr_comment, approved_by_hr, hr_approval_date, created_at, updated_at)
VALUES 
(gen_random_uuid(), 'Lisa Anderson', 'EMP106', 'Engineering', 'PATERNITY', CURRENT_DATE - 10, CURRENT_DATE + 20, 'Paternity leave for newborn', 'HR_APPROVED', 31, 'Congratulations! Approved.', 'Engineering Manager', CURRENT_TIMESTAMP - INTERVAL '2 days', 'Enjoy time with your baby.', 'HR Director', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '11 days', CURRENT_TIMESTAMP),
(gen_random_uuid(), 'Kevin Martinez', 'EMP114', 'Design', 'ANNUAL', CURRENT_DATE - 15, CURRENT_DATE - 10, 'Annual leave', 'HR_APPROVED', 6, 'Approved!', 'Design Lead', CURRENT_TIMESTAMP - INTERVAL '2 days', 'Approved!', 'HR Manager', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '16 days', CURRENT_TIMESTAMP),
(gen_random_uuid(), 'Rachel Green', 'EMP115', 'Marketing', 'SICK', CURRENT_DATE - 20, CURRENT_DATE - 17, 'Surgery recovery', 'HR_APPROVED', 4, 'Get well soon!', 'Marketing Manager', CURRENT_TIMESTAMP - INTERVAL '2 days', 'Medical leave approved.', 'HR Director', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '21 days', CURRENT_TIMESTAMP),
(gen_random_uuid(), 'Steven Spielberg', 'EMP116', 'Content', 'PERSONAL', CURRENT_DATE - 25, CURRENT_DATE - 22, 'Personal development', 'HR_APPROVED', 4, 'Approved.', 'Content Manager', CURRENT_TIMESTAMP - INTERVAL '2 days', 'Self-improvement is important!', 'HR Manager', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '26 days', CURRENT_TIMESTAMP);

-- MANAGER_REJECTED (2 requests)
INSERT INTO leave_requests (id, employee_name, employee_id, department, leave_type, start_date, end_date, reason, status, total_days, manager_comment, approved_by_manager, manager_approval_date, created_at, updated_at)
VALUES 
(gen_random_uuid(), 'Robert Taylor', 'EMP107', 'Security', 'ANNUAL', CURRENT_DATE - 15, CURRENT_DATE - 12, 'Annual leave', 'MANAGER_REJECTED', 4, 'Need coverage during that period. Please reschedule.', 'Security Manager', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '16 days', CURRENT_TIMESTAMP),
(gen_random_uuid(), 'Brian OConner', 'EMP117', 'Operations', 'PERSONAL', CURRENT_DATE - 18, CURRENT_DATE - 15, 'Personal trip', 'MANAGER_REJECTED', 4, 'Peak season. Cannot approve.', 'Operations Lead', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '19 days', CURRENT_TIMESTAMP);

-- HR_REJECTED (2 requests)
INSERT INTO leave_requests (id, employee_name, employee_id, department, leave_type, start_date, end_date, reason, status, total_days, manager_comment, approved_by_manager, manager_approval_date, hr_comment, approved_by_hr, hr_approval_date, created_at, updated_at)
VALUES 
(gen_random_uuid(), 'Jennifer Martinez', 'EMP108', 'Product', 'PERSONAL', CURRENT_DATE - 20, CURRENT_DATE - 17, 'Personal emergency', 'HR_REJECTED', 4, 'Approved by manager pending HR review.', 'Product Manager', CURRENT_TIMESTAMP - INTERVAL '2 days', 'Emergency coverage not arranged. Request denied.', 'HR Director', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '21 days', CURRENT_TIMESTAMP),
(gen_random_uuid(), 'Diana Prince', 'EMP118', 'HR', 'ANNUAL', CURRENT_DATE - 22, CURRENT_DATE - 17, 'Vacation', 'HR_REJECTED', 6, 'Approved.', 'HR Director', CURRENT_TIMESTAMP - INTERVAL '2 days', 'Leave quota exceeded for this year.', 'HR Manager', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '23 days', CURRENT_TIMESTAMP);
