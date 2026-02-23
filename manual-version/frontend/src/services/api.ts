import axios from 'axios';

const API_BASE_URL = '/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Types
export interface LeaveRequest {
  id: string;
  employeeName: string;
  employeeId?: string;
  department?: string;
  leaveType: 'ANNUAL' | 'SICK' | 'PERSONAL' | 'MATERNITY' | 'PATERNITY';
  startDate: string;
  endDate: string;
  reason: string;
  status: 'PENDING' | 'MANAGER_APPROVED' | 'MANAGER_REJECTED' | 'HR_APPROVED' | 'HR_REJECTED';
  managerComment?: string;
  hrComment?: string;
  approvedByManager?: string;
  approvedByHR?: string;
  managerApprovalDate?: string;
  hrApprovalDate?: string;
  totalDays?: number;
  createdAt: string;
  updatedAt?: string;
}

export interface CreateLeaveRequest {
  employeeName: string;
  employeeId?: string;
  department?: string;
  leaveType: 'ANNUAL' | 'SICK' | 'PERSONAL' | 'MATERNITY' | 'PATERNITY';
  startDate: string;
  endDate: string;
  reason: string;
}

export interface ApprovalRequest {
  comment: string;
  approvedBy: string;
  role: string;
}

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
  errorCode?: string;
  timestamp: number;
}

// API Functions
export const leaveApi = {
  // Submit new leave request
  submitRequest: (data: CreateLeaveRequest) =>
    api.post<ApiResponse<LeaveRequest>>('/leave-requests', data),

  // Get all leave requests
  getAllRequests: (page = 0, size = 10) =>
    api.get<ApiResponse<any>>(`/leave-requests?page=${page}&size=${size}`),

  // Get request by ID
  getRequestById: (id: string) =>
    api.get<ApiResponse<LeaveRequest>>(`/leave-requests/${id}`),

  // Get pending manager approval
  getPendingManagerApproval: () =>
    api.get<ApiResponse<LeaveRequest[]>>('/leave-requests/pending-manager'),

  // Get pending HR approval
  getPendingHRApproval: () =>
    api.get<ApiResponse<LeaveRequest[]>>('/leave-requests/pending-hr'),

  // Manager approval
  managerApprove: (id: string, data: ApprovalRequest) =>
    api.post<ApiResponse<LeaveRequest>>(`/leave-requests/${id}/manager-approve`, data),

  // Manager reject
  managerReject: (id: string, data: ApprovalRequest) =>
    api.post<ApiResponse<LeaveRequest>>(`/leave-requests/${id}/manager-reject`, data),

  // HR approval
  hrApprove: (id: string, data: ApprovalRequest) =>
    api.post<ApiResponse<LeaveRequest>>(`/leave-requests/${id}/hr-approve`, data),

  // HR reject
  hrReject: (id: string, data: ApprovalRequest) =>
    api.post<ApiResponse<LeaveRequest>>(`/leave-requests/${id}/hr-reject`, data),

  // Get audit logs
  getAuditLogs: (id: string) =>
    api.get<ApiResponse<any>>(`/leave-requests/${id}/audit-logs`),
};

export default api;
