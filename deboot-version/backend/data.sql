-- Dummy data for Deboot Version (leave_deboot_db)

-- PENDING - Waiting Manager Approval (6 requests)
INSERT INTO leave_requests (id, employee_name, employee_id, department, leave_type, start_date, end_date, reason, status, total_days, created_at, updated_at)
VALUES 
(gen_random_uuid(), 'Alexandra Chen', 'EMP201', 'Frontend Team', 'ANNUAL', CURRENT_DATE + 6, CURRENT_DATE + 12, 'Korea trip with friends', 'PENDING', 7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(gen_random_uuid(), 'Benjamin Scott', 'EMP202', 'Backend Team', 'SICK', CURRENT_DATE + 2, CURRENT_DATE + 3, 'Doctor appointment', 'PENDING', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(gen_random_uuid(), 'Catherine White', 'EMP203', 'Mobile Team', 'PERSONAL', CURRENT_DATE + 11, CURRENT_DATE + 13, 'Wedding ceremony', 'PENDING', 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(gen_random_uuid(), 'Daniel Roberts', 'EMP209', 'UI/UX Team', 'ANNUAL', CURRENT_DATE + 18, CURRENT_DATE + 23, 'Europe tour', 'PENDING', 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(gen_random_uuid(), 'Elizabeth Harris', 'EMP210', 'QA Automation', 'SICK', CURRENT_DATE + 4, CURRENT_DATE + 5, 'Migraine', 'PENDING', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(gen_random_uuid(), 'Frank Miller', 'EMP211', 'Infrastructure', 'PERSONAL', CURRENT_DATE + 9, CURRENT_DATE + 10, 'Car maintenance', 'PENDING', 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- MANAGER_APPROVED - Waiting HR Approval (4 requests)
INSERT INTO leave_requests (id, employee_name, employee_id, department, leave_type, start_date, end_date, reason, status, total_days, manager_comment, approved_by_manager, manager_approval_date, created_at, updated_at)
VALUES 
(gen_random_uuid(), 'Grace Thompson', 'EMP204', 'Frontend Team', 'ANNUAL', CURRENT_DATE - 2, CURRENT_DATE + 3, 'Year-end holiday', 'MANAGER_APPROVED', 6, 'Approved! Have fun!', 'Frontend Lead', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP - INTERVAL '4 days', CURRENT_TIMESTAMP),
(gen_random_uuid(), 'Henry Jackson', 'EMP205', 'Backend Team', 'SICK', CURRENT_DATE - 6, CURRENT_DATE - 4, 'Recovering from surgery', 'MANAGER_APPROVED', 3, 'Take your time to recover!', 'Backend Manager', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '7 days', CURRENT_TIMESTAMP),
(gen_random_uuid(), 'Isabella Moore', 'EMP212', 'Mobile Team', 'ANNUAL', CURRENT_DATE - 5, CURRENT_DATE + 1, 'Chinese New Year', 'MANAGER_APPROVED', 7, 'Happy holidays!', 'Mobile Lead', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '7 days', CURRENT_TIMESTAMP),
(gen_random_uuid(), 'Jack Williams', 'EMP213', 'UI/UX Team', 'PERSONAL', CURRENT_DATE - 9, CURRENT_DATE - 6, 'Family reunion', 'MANAGER_APPROVED', 4, 'Approved. Enjoy with family!', 'UI/UX Manager', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '10 days', CURRENT_TIMESTAMP);

-- HR_APPROVED - Final Approved (4 requests)
INSERT INTO leave_requests (id, employee_name, employee_id, department, leave_type, start_date, end_date, reason, status, total_days, manager_comment, approved_by_manager, manager_approval_date, hr_comment, approved_by_hr, hr_approval_date, created_at, updated_at)
VALUES 
(gen_random_uuid(), 'Kelly Brown', 'EMP206', 'Frontend Team', 'MATERNITY', CURRENT_DATE - 12, CURRENT_DATE + 45, 'Maternity leave for newborn', 'HR_APPROVED', 58, 'Congratulations! Wishing you all the best.', 'Frontend Manager', CURRENT_TIMESTAMP - INTERVAL '2 days', 'Congratulations on your new baby! Take care.', 'HR Director', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '13 days', CURRENT_TIMESTAMP),
(gen_random_uuid(), 'Liam Davis', 'EMP214', 'Backend Team', 'ANNUAL', CURRENT_DATE - 18, CURRENT_DATE - 13, 'Annual leave', 'HR_APPROVED', 6, 'Approved!', 'Backend Lead', CURRENT_TIMESTAMP - INTERVAL '2 days', 'Approved!', 'HR Manager', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '19 days', CURRENT_TIMESTAMP),
(gen_random_uuid(), 'Mia Garcia', 'EMP215', 'Mobile Team', 'SICK', CURRENT_DATE - 22, CURRENT_DATE - 19, 'Long-term illness', 'HR_APPROVED', 4, 'Get well soon!', 'Mobile Manager', CURRENT_TIMESTAMP - INTERVAL '2 days', 'Medical leave approved. Take care.', 'HR Director', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '23 days', CURRENT_TIMESTAMP),
(gen_random_uuid(), 'Noah Martinez', 'EMP216', 'DevOps', 'PERSONAL', CURRENT_DATE - 28, CURRENT_DATE - 25, 'Moving out', 'HR_APPROVED', 4, 'Approved.', 'DevOps Lead', CURRENT_TIMESTAMP - INTERVAL '2 days', 'Good luck with the move!', 'HR Manager', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '29 days', CURRENT_TIMESTAMP);

-- MANAGER_REJECTED (2 requests)
INSERT INTO leave_requests (id, employee_name, employee_id, department, leave_type, start_date, end_date, reason, status, total_days, manager_comment, approved_by_manager, manager_approval_date, created_at, updated_at)
VALUES 
(gen_random_uuid(), 'Olivia Wilson', 'EMP207', 'QA Team', 'ANNUAL', CURRENT_DATE - 14, CURRENT_DATE - 11, 'Vacation', 'MANAGER_REJECTED', 4, 'Critical release. Cannot approve at this time.', 'QA Manager', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '15 days', CURRENT_TIMESTAMP),
(gen_random_uuid(), 'Peter Parker', 'EMP217', 'Infrastructure', 'PERSONAL', CURRENT_DATE - 17, CURRENT_DATE - 14, 'Trip abroad', 'MANAGER_REJECTED', 4, 'System maintenance scheduled. Need you here.', 'Infra Manager', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '18 days', CURRENT_TIMESTAMP);

-- HR_REJECTED (2 requests)
INSERT INTO leave_requests (id, employee_name, employee_id, department, leave_type, start_date, end_date, reason, status, total_days, manager_comment, approved_by_manager, manager_approval_date, hr_comment, approved_by_hr, hr_approval_date, created_at, updated_at)
VALUES 
(gen_random_uuid(), 'Quinn Roberts', 'EMP208', 'Mobile Team', 'PERSONAL', CURRENT_DATE - 19, CURRENT_DATE - 16, 'Personal issue', 'HR_REJECTED', 4, 'Approved by manager for HR decision.', 'Mobile Lead', CURRENT_TIMESTAMP - INTERVAL '2 days', 'Insufficient documentation provided.', 'HR Director', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '20 days', CURRENT_TIMESTAMP),
(gen_random_uuid(), 'Rachel Adams', 'EMP218', 'UI/UX Team', 'ANNUAL', CURRENT_DATE - 24, CURRENT_DATE - 19, 'Long vacation', 'HR_REJECTED', 6, 'Approved.', 'UI/UX Manager', CURRENT_TIMESTAMP - INTERVAL '2 days', 'Leave balance not sufficient for this duration.', 'HR Manager', CURRENT_TIMESTAMP - INTERVAL '1 day', CURRENT_TIMESTAMP - INTERVAL '25 days', CURRENT_TIMESTAMP);
