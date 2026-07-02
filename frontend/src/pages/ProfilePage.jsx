import { useState } from 'react';
import api from '../api/client.js';
import { useAuth } from '../context/AuthContext.jsx';

export default function ProfilePage() {
  const { user, setUser } = useAuth();
  const [profileImage, setProfileImage] = useState(user?.profileImage || '');
  const [message, setMessage] = useState('');

  const save = async (event) => {
    event.preventDefault();
    const { data } = await api.put('/users/profile', { profileImage });
    localStorage.setItem('user', JSON.stringify(data));
    setUser(data);
    setMessage('Profile updated');
  };

  return (
    <main className="mx-auto max-w-lg px-4 py-8">
      <form onSubmit={save} className="rounded-lg border border-slate-200 bg-white p-6 shadow-sm">
        <h1 className="text-xl font-semibold">Profile</h1>
        <img className="mt-5 h-24 w-24 rounded-full object-cover bg-slate-100" src={profileImage || `https://api.dicebear.com/9.x/initials/svg?seed=${user.username}`} alt="" />
        <label className="mt-5 block text-sm font-medium">Profile image URL</label>
        <input className="mt-1 w-full rounded-md border border-slate-300 px-3 py-2" value={profileImage} onChange={(e) => setProfileImage(e.target.value)} />
        <button className="mt-5 rounded-md bg-emerald-600 px-4 py-2 font-medium text-white hover:bg-emerald-700">Save</button>
        {message && <p className="mt-3 text-sm text-emerald-700">{message}</p>}
      </form>
    </main>
  );
}
