# Deployment Guide

## Neon PostgreSQL

1. Create a free Neon project.
2. Create a database named `chatapp`.
3. Copy the JDBC URL, username, and password.
4. Use these values in Render:
   - `DATABASE_URL`
   - `DATABASE_USERNAME`
   - `DATABASE_PASSWORD`

## Cloudinary

1. Create a free Cloudinary account.
2. Copy cloud name, API key, and API secret.
3. Set these backend variables:
   - `CLOUDINARY_CLOUD_NAME`
   - `CLOUDINARY_API_KEY`
   - `CLOUDINARY_API_SECRET`

## Render Backend

1. Push this project to GitHub.
2. Create a Render Web Service.
3. Root directory: `backend`
4. Build command:

```bash
mvn clean package -DskipTests
```

5. Start command:

```bash
java -jar target/chatapp-0.0.1-SNAPSHOT.jar
```

6. Add environment variables:

```text
DATABASE_URL=jdbc:postgresql://...
DATABASE_USERNAME=...
DATABASE_PASSWORD=...
JWT_SECRET=<long-random-secret>
JWT_EXPIRATION_MS=86400000
CORS_ALLOWED_ORIGINS=https://your-vercel-app.vercel.app
CLOUDINARY_CLOUD_NAME=...
CLOUDINARY_API_KEY=...
CLOUDINARY_API_SECRET=...
```

## Vercel Frontend

1. Create a Vercel project from the same GitHub repository.
2. Root directory: `frontend`
3. Build command:

```bash
npm run build
```

4. Output directory:

```text
dist
```

5. Add:

```text
VITE_API_URL=https://your-render-service.onrender.com
```

## Local Production Check

Before deploying:

```bash
cd backend
mvn clean package
```

```bash
cd frontend
npm install
npm run build
```
