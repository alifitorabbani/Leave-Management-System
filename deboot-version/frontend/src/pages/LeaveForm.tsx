import { useState } from 'react';
import { leaveApi, LeaveRequest } from '../services/api';
import toast from 'react-hot-toast';

interface LeaveFormProps {
  onSuccess: () => void;
}

const leaveTypes = [
  { value: 'ANNUAL', label: 'Cuti Tahunan' },
  { value: 'SICK', label: 'Cuti Sakit' },
  { value: 'PERSONAL', label: 'Cuti Pribadi' },
  { value: 'MATERNITY', label: 'Cuti Melahirkan' },
  { value: 'PATERNITY', label: 'Cuti Ayah' },
  { value: 'UNPAID', label: 'Cuti Tanpa Gaji' },
];

export default function LeaveForm({ onSuccess }: LeaveFormProps) {
  const [loading, setLoading] = useState(false);
  const [formData, setFormData] = useState<Partial<LeaveRequest>>({
    employeeName: '',
    employeeId: '',
    department: '',
    leaveType: 'ANNUAL',
    startDate: '',
    endDate: '',
    reason: '',
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  const calculateDays = () => {
    if (formData.startDate && formData.endDate) {
      const start = new Date(formData.startDate);
      const end = new Date(formData.endDate);
      const diffTime = Math.abs(end.getTime() - start.getTime());
      const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)) + 1;
      return diffDays;
    }
    return 0;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    
    if (!formData.employeeName || !formData.startDate || !formData.endDate || !formData.reason) {
      toast.error('Mohon lengkapi semua field yang wajib diisi');
      return;
    }

    setLoading(true);
    try {
      await leaveApi.submitRequest(formData as any);
      toast.success('Pengajuan cuti berhasil submitted!');
      onSuccess();
    } catch (error: any) {
      console.error('Error submitting leave request:', error);
      toast.error(error?.response?.data?.message || 'Gagal mengajukan cuti. Silakan coba lagi.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="max-w-2xl mx-auto animate-fadeIn">
      <div className="mb-6">
        <h2 className="text-2xl font-bold text-gray-800">Ajukan Cuti Baru</h2>
        <p className="text-gray-500 text-sm mt-1">Isi formulir di bawah untuk mengajukan cuti</p>
      </div>

      <div className="card">
        <form onSubmit={handleSubmit}>
          {/* Employee Information */}
          <div className="mb-6">
            <h3 className="text-sm font-semibold text-gray-700 uppercase tracking-wider mb-4 flex items-center gap-2">
              <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
              </svg>
              Informasi Karyawan
            </h3>
            
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div className="form-group">
                <label className="form-label">
                  Nama Karyawan <span className="text-red-500">*</span>
                </label>
                <input
                  type="text"
                  name="employeeName"
                  value={formData.employeeName}
                  onChange={handleChange}
                  className="form-input"
                  placeholder="Masukkan nama lengkap"
                  required
                />
              </div>

              <div className="form-group">
                <label className="form-label">ID Karyawan</label>
                <input
                  type="text"
                  name="employeeId"
                  value={formData.employeeId}
                  onChange={handleChange}
                  className="form-input"
                  placeholder="Contoh: EMP001"
                />
              </div>

              <div className="form-group md:col-span-2">
                <label className="form-label">Departemen</label>
                <input
                  type="text"
                  name="department"
                  value={formData.department}
                  onChange={handleChange}
                  className="form-input"
                  placeholder="Contoh: IT, HR, Finance"
                />
              </div>
            </div>
          </div>

          {/* Leave Details */}
          <div className="mb-6">
            <h3 className="text-sm font-semibold text-gray-700 uppercase tracking-wider mb-4 flex items-center gap-2">
              <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
              </svg>
              Detail Cuti
            </h3>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div className="form-group md:col-span-2">
                <label className="form-label">
                  Jenis Cuti <span className="text-red-500">*</span>
                </label>
                <select
                  name="leaveType"
                  value={formData.leaveType}
                  onChange={handleChange}
                  className="form-select"
                  required
                >
                  {leaveTypes.map(type => (
                    <option key={type.value} value={type.value}>
                      {type.label}
                    </option>
                  ))}
                </select>
              </div>

              <div className="form-group">
                <label className="form-label">
                  Tanggal Mulai <span className="text-red-500">*</span>
                </label>
                <input
                  type="date"
                  name="startDate"
                  value={formData.startDate}
                  onChange={handleChange}
                  className="form-input"
                  required
                />
              </div>

              <div className="form-group">
                <label className="form-label">
                  Tanggal Selesai <span className="text-red-500">*</span>
                </label>
                <input
                  type="date"
                  name="endDate"
                  value={formData.endDate}
                  onChange={handleChange}
                  className="form-input"
                  min={formData.startDate}
                  required
                />
              </div>

              {formData.startDate && formData.endDate && (
                <div className="md:col-span-2 bg-blue-50 border border-blue-200 rounded-lg p-4">
                  <div className="flex items-center justify-between">
                    <div>
                      <p className="text-sm text-blue-600 font-medium">Durasi Cuti</p>
                      <p className="text-xs text-blue-400">Total hari kerja</p>
                    </div>
                    <div className="text-right">
                      <p className="text-2xl font-bold text-blue-600">{calculateDays()}</p>
                      <p className="text-xs text-blue-400">hari</p>
                    </div>
                  </div>
                </div>
              )}
            </div>
          </div>

          {/* Reason */}
          <div className="mb-6">
            <h3 className="text-sm font-semibold text-gray-700 uppercase tracking-wider mb-4 flex items-center gap-2">
              <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12h6m-6 4h6m2 5H7a2 2 0 01-2-2V5a2 2 0 012-2h5.586a1 1 0 01.707.293l5.414 5.414a1 1 0 01.293.707V19a2 2 0 01-2 2z" />
              </svg>
              Alasan Cuti
            </h3>
            
            <div className="form-group">
              <label className="form-label">
                Alasan <span className="text-red-500">*</span>
              </label>
              <textarea
                name="reason"
                value={formData.reason}
                onChange={handleChange}
                className="form-input min-h-[120px]"
                placeholder="Jelaskan alasan pengajuan cuti..."
                required
              />
            </div>
          </div>

          {/* Submit Buttons */}
          <div className="flex flex-col sm:flex-row gap-3 pt-4 border-t border-gray-200">
            <button
              type="button"
              onClick={onSuccess}
              className="btn btn-secondary flex-1"
              disabled={loading}
            >
              Batal
            </button>
            <button
              type="submit"
              className="btn btn-primary flex-1"
              disabled={loading}
            >
              {loading ? (
                <>
                  <div className="spinner w-4 h-4"></div>
                  Mengirim...
                </>
              ) : (
                <>
                  <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 13l4 4L19 7" />
                  </svg>
                  Ajukan Cuti
                </>
              )}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
