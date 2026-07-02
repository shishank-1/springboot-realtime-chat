import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Search } from 'lucide-react';
import api from '../api/client.js';

export default function HomePage() {
  const [users, setUsers] = useState([]);
  const [query, setQuery] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    const path = query.trim() ? `/users/search?q=${encodeURIComponent(query)}` : '/users';
    api.get(path).then(({ data }) => setUsers(data));
  }, [query]);

  const openChat = async (userId) => {
    const { data } = await api.post('/chats', { userId });
    navigate(`/chats/${data.id}`, { state: { chat: data } });
  };

  return (
    <main className="mx-auto max-w-5xl px-4 py-6">
      <div className="mb-5 flex items-center gap-3 rounded-lg border border-slate-200 bg-white px-3 py-2">
        <Search size={20} className="text-slate-500" />
        <input className="w-full outline-none" placeholder="Search users" value={query} onChange={(e) => setQuery(e.target.value)} />
      </div>
      <div className="grid gap-3 sm:grid-cols-2 lg:grid-cols-3">
        {users.map((user) => (
          <button key={user.id} onClick={() => openChat(user.id)} className="flex items-center gap-3 rounded-lg border border-slate-200 bg-white p-4 text-left shadow-sm hover:border-emerald-400">
            <img className="h-12 w-12 rounded-full object-cover bg-slate-100" src={user.profileImage || `https://api.dicebear.com/9.x/initials/svg?seed=${user.username}`} alt="" />
            <div className="min-w-0">
              <p className="truncate font-semibold">{user.username}</p>
              <p className="truncate text-sm text-slate-500">{user.email}</p>
            </div>
          </button>
        ))}
      </div>
    </main>
  );
}
