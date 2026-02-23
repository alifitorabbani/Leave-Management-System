import { useState } from 'react';
import { Toaster } from 'react-hot-toast';
import Dashboard from './pages/Dashboard';
import LeaveForm from './pages/LeaveForm';
import ManagerApproval from './pages/ManagerApproval';
import HRApproval from './pages/HRApproval';

type Page = 'dashboard' | 'submit' | 'manager' | 'hr';

function App() {
  const [currentPage, setCurrentPage] = useState<Page>('dashboard');

  const renderPage = () => {
    switch (currentPage) {
      case 'dashboard':
        return <Dashboard onNavigate={setCurrentPage} />;
      case 'submit':
        return <LeaveForm onSuccess={() => setCurrentPage('dashboard')} />;
      case 'manager':
        return <ManagerApproval />;
      case 'hr':
        return <HRApproval />;
      default:
        return <Dashboard onNavigate={setCurrentPage} />;
    }
  };

  return (
    <div className="min-h-screen">
      <Toaster 
        position="top-right" 
        toastOptions={{
          duration: 3000,
          style: {
            background: '#363636',
            color: '#fff',
            borderRadius: '8px',
          },
          success: {
            duration: 3000,
            iconTheme: {
              primary: '#22c55e',
              secondary: '#fff',
            },
          },
          error: {
            duration: 4000,
            iconTheme: {
              primary: '#ef4444',
              secondary: '#fff',
            },
          },
        }}
      />
      
      {/* Header */}
      <header className="bg-gradient-to-r from-emerald-900 via-emerald-800 to-emerald-700 text-white shadow-xl">
        <div className="max-w-7xl mx-auto px-4 py-5">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-4">
              <div className="w-12 h-12 bg-white/10 rounded-xl flex items-center justify-center backdrop-blur-sm">
                <svg className="w-7 h-7" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                </svg>
              </div>
              <div>
                <h1 className="text-2xl font-bold tracking-tight">Sistem Pengajuan Cuti</h1>
                <p className="text-emerald-200 text-sm">PT. Len Industri (Persero)</p>
              </div>
            </div>
            <div className="text-right hidden md:block">
              <p className="text-sm font-medium text-emerald-100">Versi: Deboot Framework</p>
              <p className="text-xs text-emerald-300">Backend: Port 8400</p>
            </div>
          </div>
        </div>
      </header>

      {/* Navigation */}
      <nav className="bg-white shadow-md border-b border-gray-200 sticky top-0 z-40">
        <div className="max-w-7xl mx-auto px-4">
          <div className="flex items-center gap-2 py-3 overflow-x-auto">
            <button
              onClick={() => setCurrentPage('dashboard')}
              className={`nav-link whitespace-nowrap flex items-center gap-2 ${
                currentPage === 'dashboard' ? 'active' : ''
              }`}
            >
              <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" />
              </svg>
              Dashboard
            </button>
            <button
              onClick={() => setCurrentPage('submit')}
              className={`nav-link whitespace-nowrap flex items-center gap-2 ${
                currentPage === 'submit' ? 'active' : ''
              }`}
            >
              <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
              </svg>
              Ajukan Cuti
            </button>
            <button
              onClick={() => setCurrentPage('manager')}
              className={`nav-link whitespace-nowrap flex items-center gap-2 ${
                currentPage === 'manager' ? 'active' : ''
              }`}
            >
              <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
              </svg>
              Approval Manager
            </button>
            <button
              onClick={() => setCurrentPage('hr')}
              className={`nav-link whitespace-nowrap flex items-center gap-2 ${
                currentPage === 'hr' ? 'active' : ''
              }`}
            >
              <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-6 9l2 2 4-4" />
              </svg>
              Approval HR
            </button>
          </div>
        </div>
      </nav>

      {/* Main Content */}
      <main className="max-w-7xl mx-auto px-4 py-6">
        <div className="animate-fadeIn">
          {renderPage()}
        </div>
      </main>

      {/* Footer */}
      <footer className="bg-gray-800 text-gray-400 py-6 mt-auto">
        <div className="max-w-7xl mx-auto px-4 text-center">
          <p className="text-sm">Sistem Pengajuan Cuti - PT. Len Industri (opersero)</p>
          <p className="text-xs mt-1">Backend Engineer Intern - Workflow Research</p>
        </div>
      </footer>
    </div>
  );
}

export default App;
