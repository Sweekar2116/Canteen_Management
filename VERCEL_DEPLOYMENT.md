# 🚀 Deploying CanteenHub Frontend to Vercel

This guide explains how to deploy the **CanteenHub** frontend to **Vercel** with full client-side routing and custom backend integration.

---

## ⚡ Option 1: 1-Click Import from GitHub (Recommended)

1. Push this project repository to **GitHub**.
2. Log in to [Vercel](https://vercel.com).
3. Click **"Add New..."** ➔ **"Project"**.
4. Import your GitHub repository.
5. In the configuration screen:
   - **Framework Preset**: `Vite`
   - **Root Directory**: `./` (or click Edit and select `frontend`)
   - **Build Command**: `npm run build`
   - **Output Directory**: `dist` (or `frontend/dist` if root is `./`)
6. Under **Environment Variables**, add:
   - `VITE_API_BASE_URL` = `https://your-backend-url.railway.app/api` (or your Render / cloud backend endpoint)
7. Click **Deploy**.

---

## ⚡ Option 2: Deploy via Vercel CLI

1. Install the Vercel CLI (if not already installed):
   ```bash
   npm i -g vercel
   ```
2. Navigate to the project directory:
   ```bash
   cd "c:\Users\HP\Documents\Canteen Management final"
   ```
3. Run the deploy command:
   ```bash
   vercel
   ```
   Follow the prompts to link your project and deploy.
4. For production deployment:
   ```bash
   vercel --prod
   ```

---

## 🔧 Routing & SPA Rewrite Configuration

Both [vercel.json](file:///vercel.json) and [frontend/vercel.json](file:///frontend/vercel.json) have been pre-configured with SPA route rewriting:

```json
{
  "framework": "vite",
  "rewrites": [
    {
      "source": "/(.*)",
      "destination": "/index.html"
    }
  ]
}
```

This prevents `404 Not Found` errors when refreshing deep URLs like `/menu`, `/cart`, `/checkout`, `/orders`, and `/admin`.

---

## 🌐 Full Stack Architecture in Production

```
┌─────────────────────────────────┐
│     Vercel (React 18 + Vite)    │ ➔ https://canteenhub.vercel.app
└────────────────┬────────────────┘
                 │ (REST API with JWT)
                 ▼
┌─────────────────────────────────┐
│ Cloud Backend (Spring Boot 3)   │ ➔ Render / Railway / Fly.io / AWS
└────────────────┬────────────────┘
                 │ (JDBC / PostgreSQL)
                 ▼
┌─────────────────────────────────┐
│   Supabase Cloud Database       │ ➔ postgresql://db.supabase.co:5432
└─────────────────────────────────┘
```
