# Spring Boot Real-Time Private Chat

Full-stack one-to-one chat app with JWT auth, PostgreSQL chat history, STOMP WebSocket live messaging, and Cloudinary image/video uploads.

## Features

- Register, login, BCrypt password hashing, JWT access token
- Protected REST APIs and current user endpoint
- Search users and open private chats
- Reuse existing chat between two users
- Text, image, and video messages
- Message history ordered by creation time with pagination
- Cloudinary upload for jpg, jpeg, png, gif, and mp4 up to 100 MB
- React UI with live updates through SockJS and STOMP

## Project Structure

```text
backend/   Spring Boot 3, Java 21, Maven, PostgreSQL, JWT, WebSocket
frontend/  React, Vite, React Router, Axios, Tailwind CSS, STOMP.js
docs/      API and deployment notes
```

## Run Locally

### 1. PostgreSQL

Create a database:

```sql
create database chatapp;
```

### 2. Backend

```bash
cd backend
copy .env.example .env
mvn spring-boot:run
```

Set environment variables from `backend/.env.example`. Spring Boot also reads the same names directly from your terminal, Render, or IDE run configuration.

### 3. Frontend

```bash
cd frontend
copy .env.example .env
npm install
npm run dev
```

Open `http://localhost:5173`.

## Free-Cost Hosting Plan

- Backend: Render free web service
- Database: Neon free PostgreSQL
- Media: Cloudinary free tier
- Frontend: Vercel free deployment

Free tiers can sleep or have monthly limits, but they are enough for learning and normal small testing.

## Important Environment Variables

Backend:

```text
DATABASE_URL
DATABASE_USERNAME
DATABASE_PASSWORD
JWT_SECRET
CORS_ALLOWED_ORIGINS
CLOUDINARY_CLOUD_NAME
CLOUDINARY_API_KEY
CLOUDINARY_API_SECRET
```

Frontend:

```text
VITE_API_URL
```

## Notes

- Media files are never stored in PostgreSQL. Only Cloudinary URLs are saved.
- `spring.jpa.hibernate.ddl-auto=update` is used for easy development. For mature production, switch to Flyway or Liquibase migrations.
- WebSocket clients connect to `/ws`, send to `/app/chat.send`, and subscribe to `/chat/{chatId}`.
