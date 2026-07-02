# API Documentation

Base URL: `http://localhost:8080`

Authenticated endpoints require:

```http
Authorization: Bearer <token>
```

## Auth

### POST `/api/auth/register`

```json
{
  "username": "shishank",
  "email": "shishank@example.com",
  "password": "password123"
}
```

### POST `/api/auth/login`

```json
{
  "email": "shishank@example.com",
  "password": "password123"
}
```

### GET `/api/auth/me`

Returns the current logged-in user.

## Users

### GET `/api/users`

Returns all users except the current user.

### GET `/api/users/search?q=abc`

Search by username or email.

### GET `/api/users/{id}`

Returns a public user profile.

### PUT `/api/users/profile`

```json
{
  "profileImage": "https://res.cloudinary.com/..."
}
```

## Chats

### POST `/api/chats`

Creates or returns the existing private chat.

```json
{
  "userId": 2
}
```

### GET `/api/chats`

Returns all chats for the current user.

### GET `/api/chats/{id}`

Returns one chat if the current user is a participant.

## Messages

### POST `/api/messages`

```json
{
  "chatId": 1,
  "type": "TEXT",
  "text": "Hello"
}
```

Image:

```json
{
  "chatId": 1,
  "type": "IMAGE",
  "mediaUrl": "https://res.cloudinary.com/..."
}
```

Video:

```json
{
  "chatId": 1,
  "type": "VIDEO",
  "mediaUrl": "https://res.cloudinary.com/..."
}
```

### GET `/api/messages/{chatId}?page=0&size=30`

Returns paginated message history ordered by `createdAt`.

## Upload

### POST `/api/upload`

Multipart form data:

```text
file=<jpg|jpeg|png|gif|mp4>
```

Returns:

```json
{
  "url": "https://res.cloudinary.com/...",
  "resourceType": "image"
}
```

## WebSocket

Endpoint: `/ws`

STOMP connect header:

```text
Authorization: Bearer <token>
```

Send:

```text
/app/chat.send
```

Subscribe:

```text
/chat/{chatId}
```
