# ⚡ Connecting Backend to Supabase (PostgreSQL)

This project supports **Supabase (PostgreSQL)** for backend cloud data storage.

---

## 1. Retrieve Supabase Database Credentials

1. Go to your [Supabase Dashboard](https://supabase.com/dashboard).
2. Select your project.
3. Navigate to **Project Settings** (gear icon) ➔ **Database**.
4. Under **Connection string**, select **JDBC** (or **URI**):
   - **Direct connection (Port 5432)**:
     ```
     jdbc:postgresql://db.<YOUR-PROJECT-REF>.supabase.co:5432/postgres?sslmode=require
     ```
   - **Connection Pooler (Session / Transaction mode - Port 6543 or 5432)**:
     ```
     jdbc:postgresql://aws-0-<REGION>.pooler.supabase.com:6543/postgres?sslmode=require
     ```
5. Note down your **Database Password** (the password you created when setting up your Supabase project).

---

## 2. Run Database Schema & Seed in Supabase SQL Editor

1. In Supabase Dashboard, click on **SQL Editor** in the left sidebar.
2. Open [database/supabase_schema.sql](file:///database/supabase_schema.sql) from this repository, copy its contents, paste into the SQL Editor, and click **Run**.
3. Open [database/supabase_seed.sql](file:///database/supabase_seed.sql), paste into SQL Editor, and click **Run**.

*(Note: Spring Boot's JPA will also auto-validate and update schemas automatically on start if `ddl-auto: update` is active).*

---

## 3. Configure Backend Environment Variables

You can run the backend with your Supabase credentials by passing environment variables:

### In PowerShell:
```powershell
$env:JAVA_HOME = "C:\Program Files\Java\jdk-17"
$env:DB_URL = "jdbc:postgresql://db.<YOUR-PROJECT-REF>.supabase.co:5432/postgres?sslmode=require"
$env:DB_USERNAME = "postgres"
$env:DB_PASSWORD = "<YOUR_SUPABASE_PASSWORD>"

cd backend
.\mvnw.cmd spring-boot:run
```

### Or using standard Environment Variables / Docker `.env`:
```env
DB_URL=jdbc:postgresql://db.<YOUR-PROJECT-REF>.supabase.co:5432/postgres?sslmode=require
DB_USERNAME=postgres
DB_PASSWORD=<YOUR_SUPABASE_PASSWORD>
```

---

## 4. Default Seeded Accounts

Once connected and seeded, you can log in with:

| Role | Email | Password |
|---|---|---|
| **Admin** | `admin@canteen.com` | `admin123` |
| **Staff / Kitchen** | `staff@canteen.com` | `staff123` |
| **Customer** | `customer@canteen.com` | `customer123` |
