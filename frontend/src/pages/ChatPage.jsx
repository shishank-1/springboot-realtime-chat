import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { Image, Send, Video } from 'lucide-react';
import { useEffect, useMemo, useRef, useState } from 'react';
import { useLocation, useParams } from 'react-router-dom';
import api, { API_URL } from '../api/client.js';
import { useAuth } from '../context/AuthContext.jsx';
import { formatTime } from '../utils/time.js';

export default function ChatPage() {
  const { chatId } = useParams();
  const { state } = useLocation();
  const { token, user } = useAuth();
  const [chat, setChat] = useState(state?.chat || null);
  const [messages, setMessages] = useState([]);
  const [text, setText] = useState('');
  const [uploading, setUploading] = useState(false);
  const bottomRef = useRef(null);
  const stompRef = useRef(null);

  const otherUser = useMemo(() => chat?.participants?.find((participant) => participant.id !== user.id), [chat, user.id]);

  useEffect(() => {
    api.get(`/chats/${chatId}`).then(({ data }) => setChat(data));
    api.get(`/messages/${chatId}?page=0&size=50`).then(({ data }) => setMessages(data.content || []));
  }, [chatId]);

  useEffect(() => {
    const client = new Client({
      webSocketFactory: () => new SockJS(`${API_URL}/ws`),
      connectHeaders: { Authorization: `Bearer ${token}` },
      reconnectDelay: 3000,
      onConnect: () => {
        client.subscribe(`/chat/${chatId}`, (frame) => {
          const incoming = JSON.parse(frame.body);
          setMessages((current) => current.some((message) => message.id === incoming.id) ? current : [...current, incoming]);
        });
      }
    });
    client.activate();
    stompRef.current = client;
    return () => client.deactivate();
  }, [chatId, token]);

  useEffect(() => {
    bottomRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages]);

  const sendMessage = async (payload) => {
    const body = { chatId: Number(chatId), ...payload };
    if (stompRef.current?.connected) {
      stompRef.current.publish({ destination: '/app/chat.send', body: JSON.stringify(body) });
    } else {
      const { data } = await api.post('/messages', body);
      setMessages((current) => [...current, data]);
    }
  };

  const submit = async (event) => {
    event.preventDefault();
    if (!text.trim()) return;
    await sendMessage({ type: 'TEXT', text: text.trim() });
    setText('');
  };

  const uploadAndSend = async (event, type) => {
    const file = event.target.files?.[0];
    if (!file) return;
    setUploading(true);
    try {
      const data = new FormData();
      data.append('file', file);
      const response = await api.post('/upload', data, { headers: { 'Content-Type': 'multipart/form-data' } });
      await sendMessage({ type, mediaUrl: response.data.url });
    } finally {
      setUploading(false);
      event.target.value = '';
    }
  };

  return (
    <main className="mx-auto flex h-[calc(100vh-56px)] max-w-5xl flex-col bg-white">
      <header className="flex h-16 items-center gap-3 border-b border-slate-200 px-4">
        <img className="h-10 w-10 rounded-full object-cover bg-slate-100" src={otherUser?.profileImage || `https://api.dicebear.com/9.x/initials/svg?seed=${otherUser?.username || 'User'}`} alt="" />
        <div className="min-w-0">
          <h1 className="truncate font-semibold">{otherUser?.username || 'Chat'}</h1>
          <p className="truncate text-sm text-slate-500">{otherUser?.email}</p>
        </div>
      </header>

      <section className="scrollbar-thin flex-1 overflow-y-auto bg-[#edf4f1] px-4 py-5">
        <div className="mx-auto flex max-w-3xl flex-col gap-3">
          {messages.map((message) => {
            const mine = message.senderId === user.id;
            return (
              <div key={message.id} className={`flex ${mine ? 'justify-end' : 'justify-start'}`}>
                <div className={`max-w-[78%] rounded-lg px-3 py-2 shadow-sm ${mine ? 'bg-emerald-600 text-white' : 'bg-white text-slate-900'}`}>
                  {message.type === 'TEXT' && <p className="whitespace-pre-wrap break-words">{message.text}</p>}
                  {message.type === 'IMAGE' && <img className="max-h-72 rounded-md object-contain" src={message.mediaUrl} alt="" />}
                  {message.type === 'VIDEO' && <video className="max-h-72 rounded-md" src={message.mediaUrl} controls />}
                  <p className={`mt-1 text-right text-xs ${mine ? 'text-emerald-50' : 'text-slate-500'}`}>{formatTime(message.createdAt)}</p>
                </div>
              </div>
            );
          })}
          <div ref={bottomRef} />
        </div>
      </section>

      <form onSubmit={submit} className="flex items-center gap-2 border-t border-slate-200 p-3">
        <label className="rounded-md p-2 hover:bg-slate-100" title="Upload image">
          <Image size={21} />
          <input className="hidden" type="file" accept="image/jpeg,image/png,image/gif" onChange={(e) => uploadAndSend(e, 'IMAGE')} />
        </label>
        <label className="rounded-md p-2 hover:bg-slate-100" title="Upload video">
          <Video size={21} />
          <input className="hidden" type="file" accept="video/mp4" onChange={(e) => uploadAndSend(e, 'VIDEO')} />
        </label>
        <input className="min-w-0 flex-1 rounded-md border border-slate-300 px-3 py-2 outline-none focus:border-emerald-500" placeholder={uploading ? 'Uploading...' : 'Type a message'} value={text} onChange={(e) => setText(e.target.value)} disabled={uploading} />
        <button className="rounded-md bg-emerald-600 p-2 text-white hover:bg-emerald-700" title="Send" disabled={uploading}>
          <Send size={21} />
        </button>
      </form>
    </main>
  );
}
