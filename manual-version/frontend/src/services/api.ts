import axios from 'axios';

const API_BASE_URL = 'http://localhost:8100/api';

// Get token from localStorage
const getToken = () => localStorage.getItem('token');
const getUser = () => {
  const userStr = localStorage.getItem('user');
  return userStr ? JSON.parse(userStr) : null;
};

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add token to requests
api.interceptors.request.use((config) => {
  const token = getToken();
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Handle response errors
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// Types
export interface UserRole {
  role: 'EMPLOYEE' | 'MANAGER' | 'HR';
}

export interface User {
  id: string;
  username: string;
  email: string;
  fullName: string;
  employeeId?: string;
  department?: string;
  role: 'EMPLOYEE' | 'MANAGER' | 'HR';
  isActive: boolean;
  createdAt: string;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  password: string;
  email: string;
  fullName: string;
  employeeId?: string;
  department?: string;
  role: 'EMPLOYEE' | 'MANAGER' | 'HR';
}

export interface AuthResponse {
  id: string;
  username: string;
  email: string;
  fullName: string;
  employeeId?: string;
  department?: string;
  role: 'EMPLOYEE' | 'MANAGER' | 'HR';
  token: string;
  loginTime: string;
}

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

// Auth API Functions
export const authApi = {
  login: (data: LoginRequest) =>
    api.post<ApiResponse<AuthResponse>>('/auth/login', data),

  register: (data: RegisterRequest) =>
    api.post<ApiResponse<AuthResponse>>('/auth/register', data),

  getCurrentUser: () =>
    api.get<ApiResponse<User>>('/auth/me'),
};

// Leave API Functions
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

// Utility functions
export const setAuth = (response: AuthResponse) => {
  localStorage.setItem('token', response.token);
  localStorage.setItem('user', JSON.stringify(response));
};

export const clearAuth = () => {
  localStorage.removeItem('token');
  localStorage.removeItem('user');
};

export const isAuthenticated = () => !!getToken();
export const getCurrentUser = () => getUser();

export default api;
