# 🍽️ CanteenHub — Modern Full-Stack Canteen Management & Food Ordering Platform

[![Java 17](https://img.shields.io/badge/Java-17%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot 3](https://img.shields.io/badge/Spring_Boot-3.2.5-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/Spring_Security-JWT-6DB33F?style=for-the-badge&logo=spring-security&logoColor=white)](https://spring.io/projects/spring-security)
[![MySQL 8](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![React 18](https://img.shields.io/badge/React-18-61DAFB?style=for-the-badge&logo=react&logoColor=black)](https://reactjs.org/)
[![Vite](https://img.shields.io/badge/Vite-5-646CFF?style=for-the-badge&logo=vite&logoColor=white)](https://vitejs.dev/)
[![Tailwind CSS](https://img.shields.io/badge/Tailwind_CSS-3.4-38B2AC?style=for-the-badge&logo=tailwind-css&logoColor=white)](https://tailwindcss.com/)

A modern, production-ready, full-stack campus food ordering and canteen management system. Transformed from a legacy desktop/JDBC Java Swing application into an enterprise-grade web application featuring JWT authentication, role-based access control, real-time order lifecycle tracking, dynamic coupon engines, kitchen stock inventory, and analytics dashboards.

---

## 🌟 Key Features

### 👤 Customer Experience
* **Interactive Landing Page**: Hero showcase, category carousel, and campus specials.
* **Smart Menu Browsing**: Live search, category filtering, pure vegetarian toggle, and price/rating sorting.
* **Persistent Shopping Cart**: Database-backed cart with real-time stock validation and tax calculation.
* **Multi-Step Checkout**: Promotional coupon validation (`WELCOME10`, `SAVE20`, `LUNCH15`), kitchen special instructions, and multiple payment methods (`CASH_ON_PICKUP`, `UPI`, `CARD`, `CAMPUS_CARD`).
* **Live Order Tracking**: Visual progress timeline tracking: `PLACED` ➔ `CONFIRMED` ➔ `PREPARING` ➔ `READY_FOR_PICKUP` ➔ `COMPLETED`.
* **Order History & Receipts**: Itemized past order breakdown, timestamps, and receipt totals.
* **Account Settings**: Profile updates and secure password management.

### 🛡️ Admin & Staff Management
* **Real-Time Analytics Dashboard**: Metric overview cards (Total Revenue, Today's Orders, Kitchen Queue, Low-Stock Count) with interactive **Recharts** bar & pie charts for top-selling dishes and order distributions.
* **Kitchen Order Processing Pipeline**: One-click status switcher (`Accept`, `Start Cooking`, `Mark Ready`, `Complete Delivery`, `Cancel`) with automated customer notifications.
* **Menu Item Management**: Add, update, price adjust, category assign, and toggle instant item availability.
* **Stock & Inventory Control**: Track plate/portion counts, configure minimum stock alert thresholds, and restock supplies.
* **User Directory & RBAC**: View registered users, assign roles (`CUSTOMER`, `STAFF`, `ADMIN`), and activate/deactivate accounts.
* **Coupon & Promo Engine**: Create custom discount codes, set percentage rates, max discount limits, order minimums, and expiry dates.

---

## 🏗️ Architecture & Tech Stack

```
                               ┌────────────────────────┐
                               │  React 18 + Vite (SPA) │
                               │  Tailwind CSS + UI     │
                               └───────────┬────────────┘
                                           │ REST / JSON (Axios + JWT)
                                           ▼
                               ┌────────────────────────┐
                               │   Spring Security      │
                               │   (JWT Filter + BCrypt)│
                               └───────────┬────────────┘
                                           │
                        ┌──────────────────┼──────────────────┐
                        ▼                  ▼                  ▼
               ┌────────────────┐ ┌────────────────┐ ┌────────────────┐
               │ AuthController │ │ MenuController │ │OrderController │
               └────────┬───────┘ └────────┬───────┘ └────────┬───────┘
                        │                  │                  │
                        ▼                  ▼                  ▼
               ┌────────────────┐ ┌────────────────┐ ┌────────────────┐
               │  AuthService   │ │MenuItemService │ │  OrderService  │
               └────────┬───────┘ └────────┬───────┘ └────────┬───────┘
                        │                  │                  │
                        ▼                  ▼                  ▼
               ┌──────────────────────────────────────────────┐
               │    Spring Data JPA / Hibernate Repositories   │
               └───────────────────────┬──────────────────────┘
                                       │ SQL (InnoDB)
                                       ▼
               ┌──────────────────────────────────────────────┐
               │          MySQL 8.0 Database (canteen_db)      │
               └──────────────────────────────────────────────┘
```

| Layer | Technologies Used |
|---|---|
| **Backend** | Java 17, Spring Boot 3.2.5, Spring Web, Spring Data JPA, Spring Security, JWT (JJWT 0.12.5), Hibernate, Bean Validation |
| **Frontend** | React 18, Vite 5, TypeScript, React Router v6, Tailwind CSS 3.4, Axios, Lucide Icons, Recharts |
| **Database** | MySQL 8.0 (17 relational tables, foreign key constraints, indexes, timestamps) |
| **Documentation** | SpringDoc OpenAPI 3.0 / Swagger UI |
| **Testing** | JUnit 5, Mockito |

---

## 📂 Project Structure

```
canteen-management/
├── backend/
│   ├── src/main/java/com/canteen/
│   │   ├── config/          # SecurityConfig, OpenApiConfig
│   │   ├── controller/      # REST API Controllers (Auth, Menu, Cart, Order, Admin)
│   │   ├── dto/             # Request & Response DTOs with Jakarta Validation
│   │   ├── entity/          # JPA Entities (User, Role, MenuItem, Cart, Order, etc.)
│   │   ├── exception/       # GlobalExceptionHandler & Custom Exceptions
│   │   ├── repository/      # Spring Data JPA Repositories
│   │   ├── security/        # JwtTokenProvider, JwtAuthenticationFilter, UserPrincipal
│   │   └── service/         # Business Logic Services
│   ├── src/main/resources/
│   │   └── application.yml  # Database, JWT and Server configuration
│   ├── src/test/java/       # JUnit 5 & Mockito Unit Tests
│   └── pom.xml
│
├── frontend/
│   ├── src/
│   │   ├── components/      # UI components (Navbar, Footer, AdminSidebar, Badge, Modal)
│   │   ├── context/         # AuthContext, CartContext
│   │   ├── pages/           # Landing, Menu, Cart, Checkout, Tracking, Admin views
│   │   ├── services/        # Axios API client with JWT interceptor
│   │   ├── types/           # TypeScript interfaces
│   │   ├── App.tsx
│   │   └── main.tsx
│   ├── tailwind.config.js
│   ├── vite.config.ts
│   └── package.json
│
├── database/
│   ├── schema.sql           # Complete production 17-table schema
│   └── seed.sql             # 20 menu items, default users, roles, coupons, inventory
│
└── README.md
```

---

## 🚀 Quickstart Guide

### 🐳 Production 1-Click Deployment (Docker Compose)
Deploy the full production stack (MySQL + Spring Boot Backend + Nginx/React Frontend) in a single command:
```bash
cp .env.example .env
docker compose up --build -d
```
* **Frontend Application**: `http://localhost` (Port 80)
* **Backend API & Swagger**: `http://localhost:8080/swagger-ui.html`
* **Health & Actuator Probes**: `http://localhost:8080/actuator/health`

For complete production operations, SSL/TLS, database backups, and Kubernetes manifests, see [PRODUCTION_DEPLOYMENT.md](file:///docs/PRODUCTION_DEPLOYMENT.md).

---

### 💻 Local Development Setup

#### 1. Database Setup
Ensure MySQL Server 8.0 is running on port `3306`:
```sql
mysql -u root -p < database/schema.sql
mysql -u root -p < database/seed.sql
```

#### 2. Run Backend (Port 8080)
```bash
cd backend
mvn spring-boot:run
```
* **Swagger UI API Documentation**: `http://localhost:8080/swagger-ui.html`
* **OpenAPI Specs**: `http://localhost:8080/api-docs`
* **Actuator Health**: `http://localhost:8080/actuator/health`

#### 3. Run Frontend (Port 5173)
```bash
cd frontend
npm install
npm run dev
```
* **Application URL**: `http://localhost:5173`

---

## 🔑 Demo Credentials

| Role | Email | Password | Access Level |
|---|---|---|---|
| **Admin** | `admin@canteen.com` | `admin123` | Full admin analytics, order processing, menu management, user controls, inventory & coupons |
| **Kitchen Staff** | `staff@canteen.com` | `staff123` | Order kitchen pipeline, status progression & stock monitoring |
| **Customer** | `rahul@example.com` | `customer123` | Browse menu, persistent cart, coupon checkout, order tracking |

---

## 🧪 Unit Tests

Run the test suite using Maven:
```bash
cd backend
mvn test
```
All service layer tests (CartService, CouponService, MenuItemService) validate calculations, business state transitions, stock deductions, and exception handling.

---

## 💼 Resume Talking Points (for Interviews)

1. **Architecture Migration**: Refactored legacy raw JDBC god-classes into a layered Spring Boot architecture (`Controller` ➔ `Service` ➔ `Repository`).
2. **Stateless Security**: Implemented JWT authentication and BCrypt password encryption with fine-grained role-based access control (`CUSTOMER`, `STAFF`, `ADMIN`).
3. **Optimized Queries & Relational Integrity**: Designed a 17-table relational schema with indexed search queries, preventing N+1 problems with eager category fetch and aggregate analytics queries.
4. **Resilient Cart & Order Lifecycle**: Enforced inventory-aware cart additions, transactional order placement with automated coupon calculations, and strict state-machine transitions (`PLACED` ➔ `CONFIRMED` ➔ `PREPARING` ➔ `READY` ➔ `COMPLETED`).
5. **Modern Single Page Application**: Built with React 18, TypeScript, Tailwind CSS, custom responsive design system, and real-time polling updates for order progress.
