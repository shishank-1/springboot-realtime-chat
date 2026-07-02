import { Link, Outlet, useNavigate } from 'react-router-dom';
import { LogOut, MessageCircle, UserCircle } from 'lucide-react';
import { useAuth } from './context/AuthContext.jsx';

export default function App() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <div className="min-h-screen">
      {user && (
        <header className="border-b border-slate-200 bg-white">
          <div className="mx-auto flex h-14 max-w-6xl items-center justify-between px-4">
            <Link to="/" className="flex items-center gap-2 font-semibold">
              <MessageCircle size={22} className="text-emerald-600" />
              Private Chat
            </Link>
            <div className="flex items-center gap-2">
              <Link to="/profile" className="rounded-md p-2 hover:bg-slate-100" title="Profile">
                <UserCircle size={22} />
              </Link>
              <button onClick={handleLogout} className="rounded-md p-2 hover:bg-slate-100" title="Logout">
                <LogOut size={20} />
              </button>
            </div>
          </div>
        </header>
      )}
      <Outlet />
    </div>
  );
}
