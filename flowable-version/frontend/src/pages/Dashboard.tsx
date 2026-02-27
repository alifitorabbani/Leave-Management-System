import { useEffect, useState } from 'react';
import { leaveApi, LeaveRequest } from '../services/api';
import toast from 'react-hot-toast';

interface DashboardProps {
  onNavigate: (page: 'dashboard' | 'submit' | 'manager' | 'hr') => void;
}

const statusLabels: Record<string, string> = {
  PENDING: 'Menunggu Approval Manager',
  MANAGER_APPROVED: 'Approved Manager - Menunggu HR',
  MANAGER_REJECTED: 'Ditolak Manager',
  HR_APPROVED: 'Approved (Final)',
  HR_REJECTED: 'Ditolak HR',
};

const statusClasses: Record<string, string> = {
  PENDING: 'status-pending',
  MANAGER_APPROVED: 'status-manager-approved',
  MANAGER_REJECTED: 'status-manager-rejected',
  HR_APPROVED: 'status-hr-approved',
  HR_REJECTED: 'status-hr-rejected',
};

export default function Dashboard({ onNavigate }: DashboardProps) {
  const [requests, setRequests] = useState<LeaveRequest[]>([]);
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);

  useEffect(() => {
    fetchRequests();
  }, []);

  const fetchRequests = async () => {
    try {
      setLoading(true);
      const response = await leaveApi.getAllRequests(0, 100);
      // Handle both Page format (manual) and List format (flowable/deboot)
      if (response.data.data) {
        if (response.data.data.content) {
          // Page format (manual version)
          setRequests(response.data.data.content);
        } else if (Array.isArray(response.data.data)) {
          // List format (flowable/deboot version)
          setRequests(response.data.data);
        }
      }
    } catch (error) {
      toast.error('Gagal memuat data pengajuan cuti');
      console.error(error);
    } finally {
      setLoading(false);
      setRefreshing(false);
    }
  };

  const handleRefresh = () => {
    setRefreshing(true);
    fetchRequests();
  };

  const getStatusBadge = (status: string) => (
    <span className={`status-badge ${statusClasses[status] || ''}`}>
      {statusLabels[status] || status}
    </span>
  );

  const pendingCount = requests.filter(r => r.status === 'PENDING').length;
  const managerApprovedCount = requests.filter(r => r.status === 'MANAGER_APPROVED').length;
  const approvedCount = requests.filter(r => r.status === 'HR_APPROVED').length;
  const rejectedCount = requests.filter(r => r.status === 'MANAGER_REJECTED' || r.status === 'HR_REJECTED').length;

  if (loading) {
    return (
      <div className="flex items-center justify-center h-64">
        <div className="text-center">
          <div className="spinner w-12 h-12 mx-auto mb-4"></div>
          <p className="text-gray-500">Memuat data...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="animate-fadeIn">
      {/* Header Section */}
      <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-4 mb-6">
        <div>
          <h2 className="text-2xl font-bold text-gray-800">Dashboard Pengajuan Cuti</h2>
          <p className="text-gray-500 text-sm mt-1">Overview semua pengajuan cuti karyawan</p>
        </div>
        <div className="flex gap-3">
          <button 
            onClick={handleRefresh}
            disabled={refreshing}
            className="btn btn-secondary flex items-center gap-2"
          >
            <svg 
              className={`w-4 h-4 ${refreshing ? 'animate-spin' : ''}`} 
              fill="none" 
              stroke="currentColor" 
              viewBox="0 0 24 24"
            >
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
            </svg>
            Refresh
          </button>
          <button 
            onClick={() => onNavigate('submit')} 
            className="btn btn-primary flex items-center gap-2"
          >
            <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
            </svg>
            Ajukan Cuti Baru
          </button>
        </div>
      </div>

      {/* Stats Cards */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4 mb-8">
        <div className="stats-card in-progress">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-gray-500 text-sm font-medium">Total Pengajuan</p>
              <p className="text-3xl font-bold text-gray-800 mt-1">{requests.length}</p>
            </div>
            <div className="w-12 h-12 bg-blue-100 rounded-xl flex items-center justify-center">
              <svg className="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
              </svg>
            </div>
          </div>
        </div>

        <div className="stats-card pending">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-gray-500 text-sm font-medium">Menunggu Manager</p>
              <p className="text-3xl font-bold text-yellow-600 mt-1">{pendingCount}</p>
            </div>
            <div className="w-12 h-12 bg-yellow-100 rounded-xl flex items-center justify-center">
              <svg className="w-6 h-6 text-yellow-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
          </div>
        </div>

        <div className="stats-card in-progress">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-gray-500 text-sm font-medium">Menunggu HR</p>
              <p className="text-3xl font-bold text-blue-600 mt-1">{managerApprovedCount}</p>
            </div>
            <div className="w-12 h-12 bg-blue-100 rounded-xl flex items-center justify-center">
              <svg className="w-6 h-6 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" />
              </svg>
            </div>
          </div>
        </div>

        <div className="stats-card approved">
          <div className="flex items-center justify-between">
            <div>
              <p className="text-gray-500 text-sm font-medium">Approved</p>
              <p className="text-3xl font-bold text-green-600 mt-1">{approvedCount}</p>
            </div>
            <div className="w-12 h-12 bg-green-100 rounded-xl flex items-center justify-center">
              <svg className="w-6 h-6 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
            </div>
          </div>
        </div>
      </div>

      {/* Recent Requests Table */}
      <div className="card">
        <div className="card-header">
          <h3 className="card-title flex items-center gap-2">
            <svg className="w-5 h-5 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2" />
            </svg>
            Riwayat Pengajuan Cuti
          </h3>
          <span className="badge badge-info">{requests.length} total</span>
        </div>

        {requests.length === 0 ? (
          <div className="empty-state">
            <svg className="empty-state-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
            </svg>
            <h4 className="empty-state-title">Belum Ada Pengajuan Cuti</h4>
            <p className="empty-state-description">
              Ajukan cuti pertama Anda dengan klik tombol "Ajukan Cuti Baru" di atas.
            </p>
            <button 
              onClick={() => onNavigate('submit')} 
              className="btn btn-primary mt-4"
            >
              Ajukan Cuti Sekarang
            </button>
          </div>
        ) : (
          <div className="table-container">
            <table className="table">
              <thead>
                <tr>
                  <th className="w-16">No</th>
                  <th>Karyawan</th>
                  <th>Departemen</th>
                  <th>Jenis Cuti</th>
                  <th>Tanggal</th>
                  <th>Durasi</th>
                  <th>Status</th>
                </tr>
              </thead>
              <tbody>
                {requests.slice(0, 10).map((request, index) => (
                  <tr key={request.id} className="animate-fadeIn" style={{ animationDelay: `${index * 50}ms` }}>
                    <td className="text-gray-400 font-medium">{index + 1}</td>
                    <td>
                      <div>
                        <p className="font-medium text-gray-800">{request.employeeName}</p>
                        <p className="text-xs text-gray-500">{request.employeeId || '-'}</p>
                      </div>
                    </td>
                    <td className="text-gray-600">{request.department || '-'}</td>
                    <td>
                      <span className="badge badge-info">{request.leaveType}</span>
                    </td>
                    <td className="text-sm">
                      <div>
                        <p className="font-medium">{request.startDate}</p>
                        <p className="text-gray-400 text-xs">s/d {request.endDate}</p>
                      </div>
                    </td>
                    <td>
                      <span className="font-medium">{request.totalDays || 0}</span>
                      <span className="text-gray-400 text-sm"> hari</span>
                    </td>
                    <td>{getStatusBadge(request.status)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}

        {requests.length > 10 && (
          <div className="mt-4 text-center">
            <p className="text-sm text-gray-500">
              Menampilkan 10 dari {requests.length} pengajuan
            </p>
          </div>
        )}
      </div>
    </div>
  );
}
