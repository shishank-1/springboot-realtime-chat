import { Link, useNavigate } from 'react-router-dom';
import { useState } from 'react';
import api from '../api/client.js';
import { useAuth } from '../context/AuthContext.jsx';

export default function RegisterPage() {
  const [form, setForm] = useState({ username: '', email: '', password: '' });
  const [error, setError] = useState('');
  const { saveSession } = useAuth();
  const navigate = useNavigate();

  const submit = async (event) => {
    event.preventDefault();
    setError('');
    try {
      const { data } = await api.post('/auth/register', form);
      saveSession(data);
      navigate('/');
    } catch (err) {
      setError(err.response?.data?.error || 'Registration failed');
    }
  };

  return (
    <main className="flex min-h-screen items-center justify-center px-4">
      <form onSubmit={submit} className="w-full max-w-sm rounded-lg border border-slate-200 bg-white p-6 shadow-sm">
        <h1 className="text-2xl font-semibold">Create Account</h1>
        {error && <p className="mt-4 rounded-md bg-red-50 p-3 text-sm text-red-700">{error}</p>}
        <label className="mt-5 block text-sm font-medium">Username</label>
        <input className="mt-1 w-full rounded-md border border-slate-300 px-3 py-2" value={form.username} onChange={(e) => setForm({ ...form, username: e.target.value })} required />
        <label className="mt-4 block text-sm font-medium">Email</label>
        <input className="mt-1 w-full rounded-md border border-slate-300 px-3 py-2" type="email" value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} required />
        <label className="mt-4 block text-sm font-medium">Password</label>
        <input className="mt-1 w-full rounded-md border border-slate-300 px-3 py-2" type="password" value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} required />
        <button className="mt-6 w-full rounded-md bg-emerald-600 px-4 py-2 font-medium text-white hover:bg-emerald-700">Register</button>
        <p className="mt-4 text-center text-sm text-slate-600">Already registered? <Link className="font-medium text-emerald-700" to="/login">Login</Link></p>
      </form>
    </main>
  );
}
