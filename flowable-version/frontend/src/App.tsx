import { useState, useEffect } from 'react';
import { Toaster } from 'react-hot-toast';
import Dashboard from './pages/Dashboard';
import LeaveForm from './pages/LeaveForm';
import ManagerApproval from './pages/ManagerApproval';
import HRApproval from './pages/HRApproval';
import Login from './pages/Login';
import Register from './pages/Register';
import { isAuthenticated, getCurrentUser, clearAuth, User } from './services/api';

type Page = 'dashboard' | 'submit' | 'manager' | 'hr' | 'login' | 'register';

function App() {
  const [currentPage, setCurrentPage] = useState<Page>('dashboard');
  const [isLoggedIn, setIsLoggedIn] = useState(isAuthenticated());
  const [user, setUser] = useState<User | null>(getCurrentUser());

  useEffect(() => {
    // Check authentication status on mount
    const checkAuth = () => {
      const authenticated = isAuthenticated();
      setIsLoggedIn(authenticated);
      setUser(getCurrentUser());
      
      if (!authenticated && currentPage !== 'login' && currentPage !== 'register') {
        setCurrentPage('login');
      }
    };
    
    checkAuth();
  }, []);

  const handleLogin = () => {
    setIsLoggedIn(true);
    setUser(getCurrentUser());
    
    // Redirect based on role
    const currentUser = getCurrentUser();
    if (currentUser?.role === 'MANAGER') {
      setCurrentPage('manager');
    } else if (currentUser?.role === 'HR') {
      setCurrentPage('hr');
    } else {
      setCurrentPage('dashboard');
    }
  };

  const handleRegister = () => {
    setCurrentPage('register');
  };

  const handleGoToLogin = () => {
    setCurrentPage('login');
  };

  const handleLogout = () => {
    clearAuth();
    setIsLoggedIn(false);
    setUser(null);
    setCurrentPage('login');
  };

  const renderPage = () => {
    // Show login/register if not authenticated
    if (!isLoggedIn) {
      if (currentPage === 'register') {
        return <Register onLogin={handleGoToLogin} />;
      }
      return <Login onLogin={handleLogin} onRegister={handleRegister} />;
    }

    switch (currentPage) {
      case 'dashboard':
        return <Dashboard onNavigate={setCurrentPage} />;
      case 'submit':
        return <LeaveForm onSuccess={() => setCurrentPage('dashboard')} />;
      case 'manager':
        return <ManagerApproval />;
      case 'hr':
        return <HRApproval />;
      case 'login':
      case 'register':
        return <Dashboard onNavigate={setCurrentPage} />;
      default:
        return <Dashboard onNavigate={setCurrentPage} />;
    }
  };

  // Show auth pages without header
  if (!isLoggedIn && (currentPage === 'login' || currentPage === 'register')) {
    return (
      <div>
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
        {renderPage()}
      </div>
    );
  }

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
      <header className="bg-gradient-to-r from-blue-900 via-blue-800 to-blue-700 text-white shadow-xl">
        <div className="max-w-7xl mx-auto px-4 py-5">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-4">
              <div className="bg-white/10 p-2 rounded-lg backdrop-blur-sm">
                <svg className="w-8 h-8" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                </svg>
              </div>
              <div>
                <h1 className="text-xl font-bold tracking-wide">Leave Management System</h1>
                <p className="text-xs text-blue-200">PT. Len Industri (Persero)</p>
              </div>
            </div>
            
            <div className="flex items-center gap-6">
              {user && (
                <div className="flex items-center gap-3 bg-white/10 px-4 py-2 rounded-lg backdrop-blur-sm">
                  <div className="text-right">
                    <p className="font-semibold text-sm">{user.fullName}</p>
                    <p className="text-xs text-blue-200">{user.role}</p>
                  </div>
                  <div className="h-8 w-8 bg-blue-500 rounded-full flex items-center justify-center">
                    <span className="font-bold">{user.fullName?.charAt(0)}</span>
                  </div>
                </div>
              )}
              <button
                onClick={handleLogout}
                className="flex items-center gap-2 bg-white/10 hover:bg-white/20 px-4 py-2 rounded-lg transition-all backdrop-blur-sm"
              >
                <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
                </svg>
                Logout
              </button>
            </div>
          </div>
        </div>
      </header>

      {/* Navigation */}
      <nav className="bg-white shadow-md">
        <div className="max-w-7xl mx-auto px-4">
          <div className="flex gap-1 py-2">
            <button
              onClick={() => setCurrentPage('dashboard')}
              className={`px-4 py-2 rounded-lg font-medium transition-all ${
                currentPage === 'dashboard' 
                  ? 'bg-blue-600 text-white' 
                  : 'text-gray-600 hover:bg-gray-100'
              }`}
            >
              Dashboard
            </button>
            
            {user?.role === 'EMPLOYEE' && (
              <button
                onClick={() => setCurrentPage('submit')}
                className={`px-4 py-2 rounded-lg font-medium transition-all ${
                  currentPage === 'submit' 
                    ? 'bg-blue-600 text-white' 
                    : 'text-gray-600 hover:bg-gray-100'
                }`}
              >
                Submit Request
              </button>
            )}
            
            {user?.role === 'MANAGER' && (
              <button
                onClick={() => setCurrentPage('manager')}
                className={`px-4 py-2 rounded-lg font-medium transition-all ${
                  currentPage === 'manager' 
                    ? 'bg-blue-600 text-white' 
                    : 'text-gray-600 hover:bg-gray-100'
                }`}
              >
                Manager Approval
              </button>
            )}
            
            {user?.role === 'HR' && (
              <button
                onClick={() => setCurrentPage('hr')}
                className={`px-4 py-2 rounded-lg font-medium transition-all ${
                  currentPage === 'hr' 
                    ? 'bg-blue-600 text-white' 
                    : 'text-gray-600 hover:bg-gray-100'
                }`}
              >
                HR Approval
              </button>
            )}
          </div>
        </div>
      </nav>

      {/* Main Content */}
      <main className="max-w-7xl mx-auto px-4 py-8">
        {renderPage()}
      </main>
    </div>
  );
}

export default App;
