import { useEffect, useState } from 'react';
import { leaveApi, LeaveRequest, ApprovalRequest } from '../services/api';
import toast from 'react-hot-toast';

export default function ManagerApproval() {
  const [requests, setRequests] = useState<LeaveRequest[]>([]);
  const [loading, setLoading] = useState(true);
  const [refreshing, setRefreshing] = useState(false);
  const [selectedRequest, setSelectedRequest] = useState<LeaveRequest | null>(null);
  const [comment, setComment] = useState('');
  const [approverName, setApproverName] = useState('');
  const [processing, setProcessing] = useState(false);

  useEffect(() => {
    fetchPendingRequests();
  }, []);

  const fetchPendingRequests = async () => {
    try {
      setLoading(true);
      const response = await leaveApi.getPendingManagerApproval();
      setRequests(response.data.data || []);
    } catch (error) {
      toast.error('Gagal memuat data pengajuan');
      console.error(error);
    } finally {
      setLoading(false);
      setRefreshing(false);
    }
  };

  const handleRefresh = () => {
    setRefreshing(true);
    fetchPendingRequests();
  };

  const handleApprove = async (id: string) => {
    if (!comment || !approverName) {
      toast.error('Mohon isi nama dan komentar persetujuan');
      return;
    }

    setProcessing(true);
    try {
      const approvalData: ApprovalRequest = {
        comment,
        approvedBy: approverName,
        role: 'MANAGER',
      };
      await leaveApi.managerApprove(id, approvalData);
      toast.success('Pengajuan cuti disetujui!');
      setSelectedRequest(null);
      setComment('');
      fetchPendingRequests();
    } catch (error: any) {
      toast.error(error.response?.data?.message || 'Gagal menyetujui pengajuan');
    } finally {
      setProcessing(false);
    }
  };

  const handleReject = async (id: string) => {
    if (!comment || !approverName) {
      toast.error('Mohon isi nama dan komentar penolakan');
      return;
    }

    setProcessing(true);
    try {
      const rejectionData: ApprovalRequest = {
        comment,
        approvedBy: approverName,
        role: 'MANAGER',
      };
      await leaveApi.managerReject(id, rejectionData);
      toast.success('Pengajuan cuti ditolak!');
      setSelectedRequest(null);
      setComment('');
      fetchPendingRequests();
    } catch (error: any) {
      toast.error(error.response?.data?.message || 'Gagal menolak pengajuan');
    } finally {
      setProcessing(false);
    }
  };

  const leaveTypeLabels: Record<string, string> = {
    ANNUAL: 'Cuti Tahunan',
    SICK: 'Cuti Sakit',
    PERSONAL: 'Cuti Pribadi',
    MATERNITY: 'Cuti Melahirkan',
    PATERNITY: 'Cuti Ayah',
    UNPAID: 'Cuti Tanpa Gaji',
  };

  return (
    <div className="animate-fadeIn">
      {/* Header */}
      <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-4 mb-6">
        <div>
          <h2 className="text-2xl font-bold text-gray-800">Approval Manager</h2>
          <p className="text-gray-500 text-sm mt-1">Kelola pengajuan cuti yang menunggu persetujuan Anda</p>
        </div>
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
      </div>

      {/* Approver Info */}
      <div className="card mb-6">
        <div className="flex items-center gap-3 mb-4">
          <div className="w-10 h-10 bg-blue-100 rounded-lg flex items-center justify-center">
            <svg className="w-5 h-5 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
            </svg>
          </div>
          <div>
            <h3 className="font-semibold text-gray-800">Identitas Approver</h3>
            <p className="text-sm text-gray-500">Masukkan nama Anda untuk memproses approval</p>
          </div>
        </div>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <label className="form-label">
              Nama Manager <span className="text-red-500">*</span>
            </label>
            <input
              type="text"
              value={approverName}
              onChange={(e) => setApproverName(e.target.value)}
              className="form-input"
              placeholder="Contoh: John Doe"
            />
          </div>
        </div>
      </div>

      {loading ? (
        <div className="flex items-center justify-center h-64">
          <div className="text-center">
            <div className="spinner w-12 h-12 mx-auto mb-4"></div>
            <p className="text-gray-500">Memuat data...</p>
          </div>
        </div>
      ) : requests.length === 0 ? (
        <div className="card">
          <div className="empty-state">
            <svg className="empty-state-icon" fill="none" stroke="currentColor" viewBox="0 0 24 24">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1.5} d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
            </svg>
            <h4 className="empty-state-title">Tidak Ada Pengajuan Menunggu</h4>
            <p className="empty-state-description">
              Semua pengajuan cuti telah diproses. Silakan refresh untuk memperbarui data.
            </p>
            <button onClick={handleRefresh} className="btn btn-primary mt-4">
              Refresh Data
            </button>
          </div>
        </div>
      ) : (
        <div className="space-y-4">
          <div className="flex items-center justify-between mb-4">
            <h3 className="font-semibold text-gray-700">
              Pengajuan Menunggu ({requests.length})
            </h3>
          </div>
          
          {requests.map((request, index) => (
            <div 
              key={request.id} 
              className="card animate-fadeIn"
              style={{ animationDelay: `${index * 100}ms` }}
            >
              {/* Header */}
              <div className="flex justify-between items-start mb-4">
                <div className="flex items-center gap-3">
                  <div className="w-12 h-12 bg-gradient-to-br from-blue-500 to-blue-600 rounded-xl flex items-center justify-center text-white font-bold">
                    {request.employeeName?.charAt(0).toUpperCase()}
                  </div>
                  <div>
                    <h3 className="text-lg font-semibold text-gray-800">{request.employeeName}</h3>
                    <p className="text-sm text-gray-500">{request.department || 'Tidak ada departemen'}</p>
                  </div>
                </div>
                <span className="status-badge status-pending">
                  <svg className="w-3 h-3" fill="currentColor" viewBox="0 0 20 20">
                    <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zm1-12a1 1 0 10-2 0v4a1 1 0 00.293.707l2.828 2.829a1 1 0 101.415-1.415L11 9.586V6z" clipRule="evenodd" />
                  </svg>
                  Menunggu
                </span>
              </div>
              
              {/* Details Grid */}
              <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-4 text-sm">
                <div className="bg-gray-50 rounded-lg p-3">
                  <p className="text-gray-400 text-xs mb-1">Jenis Cuti</p>
                  <p className="font-medium text-gray-700">{leaveTypeLabels[request.leaveType] || request.leaveType}</p>
                </div>
                <div className="bg-gray-50 rounded-lg p-3">
                  <p className="text-gray-400 text-xs mb-1">Durasi</p>
                  <p className="font-medium text-gray-700">{request.totalDays || 0} hari</p>
                </div>
                <div className="bg-gray-50 rounded-lg p-3 col-span-2">
                  <p className="text-gray-400 text-xs mb-1">Periode Cuti</p>
                  <p className="font-medium text-gray-700">{request.startDate} - {request.endDate}</p>
                </div>
              </div>

              {/* Reason */}
              <div className="mb-4">
                <p className="text-gray-400 text-xs mb-1 uppercase tracking-wider">Alasan Pengajuan</p>
                <p className="text-gray-700 bg-gray-50 rounded-lg p-3">{request.reason}</p>
              </div>

              {/* Action Buttons */}
              {selectedRequest?.id === request.id ? (
                <div className="border-t border-gray-200 pt-4 mt-4 animate-fadeIn">
                  <label className="form-label">
                    Komentar {selectedRequest?.id === request.id ? (
                      <span className="text-red-500">*</span>
                    ) : null}
                  </label>
                  <textarea
                    value={comment}
                    onChange={(e) => setComment(e.target.value)}
                    className="form-input mb-3"
                    placeholder="Masukkan komentar persetujuan atau penolakan..."
                    rows={3}
                  />
                  <div className="flex flex-col sm:flex-row gap-3">
                    <button
                      onClick={() => handleApprove(request.id)}
                      disabled={processing || !comment || !approverName}
                      className="btn btn-success flex-1 flex items-center justify-center gap-2"
                    >
                      <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 13l4 4L19 7" />
                      </svg>
                      {processing ? 'Memproses...' : 'Setuju'}
                    </button>
                    <button
                      onClick={() => handleReject(request.id)}
                      disabled={processing || !comment || !approverName}
                      className="btn btn-danger flex-1 flex items-center justify-center gap-2"
                    >
                      <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                      </svg>
                      {processing ? 'Memproses...' : 'Tolak'}
                    </button>
                    <button
                      onClick={() => {
                        setSelectedRequest(null);
                        setComment('');
                      }}
                      className="btn btn-secondary flex-1"
                    >
                      Batal
                    </button>
                  </div>
                </div>
              ) : (
                <div className="flex gap-3 pt-2">
                  <button
                    onClick={() => setSelectedRequest(request)}
                    className="btn btn-primary flex-1"
                  >
                    Proses Approval
                  </button>
                </div>
              )}
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
