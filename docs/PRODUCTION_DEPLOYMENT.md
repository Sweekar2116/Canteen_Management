# 🚀 Production Deployment & Operations Guide

This guide covers complete enterprise deployment instructions for **CanteenHub** using Docker Compose, Kubernetes, or standalone cloud instances.

---

## 📑 Table of Contents
1. [Architecture Overview](#1-architecture-overview)
2. [Prerequisites](#2-prerequisites)
3. [Environment Configuration](#3-environment-configuration)
4. [One-Click Docker Compose Deployment](#4-one-click-docker-compose-deployment)
5. [Kubernetes Cluster Deployment](#5-kubernetes-cluster-deployment)
6. [SSL / TLS & HTTPS Setup](#6-ssl--tls--https-setup)
7. [Database Backup & Disaster Recovery](#7-database-backup--disaster-recovery)
8. [Monitoring & Healthchecks](#8-monitoring--healthchecks)
9. [Production Security Checklist](#9-production-security-checklist)

---

## 1. Architecture Overview

```
 [ Internet / Client Traffic ]
               │
               ▼ (Port 80 / 443 HTTPS)
   ┌───────────────────────┐
   │ Nginx (Reverse Proxy) │  <── Serves React 18 SPA static assets with Gzip
   └───────────┬───────────┘
               │
               ├─ /api/* ─────────┐
               │                  ▼
               │       ┌───────────────────────┐
               │       │ Spring Boot 3 Backend │  <── Actuator Health & Probes
               │       └──────────┬────────────┘
               │                  │
               │                  ▼
               │       ┌───────────────────────┐
               │       │   MySQL 8.0 Database  │  <── InnoDB, Persistent Volume
               │       └───────────────────────┘
```

---

## 2. Prerequisites

Ensure your host server meets the following minimum requirements:
- **OS**: Linux (Ubuntu 22.04 LTS recommended), Debian, RHEL, or Windows Server
- **Docker Engine**: `v24.0+`
- **Docker Compose**: `v2.20+`
- **Hardware**: Minimum 2 vCPUs, 2 GB RAM (4 GB recommended)
- **Open Ports**: `80` (HTTP), `443` (HTTPS), `8080` (Optional backend API), `3306` (Internal DB)

---

## 3. Environment Configuration

1. Clone the repository on your target server:
   ```bash
   git clone https://github.com/your-username/canteen-management.git
   cd canteen-management
   ```

2. Generate a copy of the environment template:
   ```bash
   cp .env.example .env
   ```

3. Update sensitive environment values in `.env`:
   ```ini
   # Database credentials
   DB_NAME=canteen_db
   DB_USER=canteen_prod_user
   DB_PASSWORD=YOUR_STRONG_DB_PASSWORD_HERE
   DB_ROOT_PASSWORD=YOUR_STRONG_ROOT_PASSWORD_HERE

   # Generate a 256-bit+ secure random secret for JWT
   # Command: openssl rand -base64 48
   JWT_SECRET=YOUR_SECURE_GENERATED_JWT_SECRET_STRING_HERE
   JWT_EXPIRATION_MS=86400000

   # Domain and CORS configuration
   CORS_ALLOWED_ORIGINS=https://canteen.yourdomain.com,http://localhost
   FRONTEND_PORT=80
   BACKEND_PORT=8080
   ```

---

## 4. One-Click Docker Compose Deployment

To build and run all services in production mode:

```bash
# 1. Build and start containers in the background
docker compose up --build -d

# 2. View running containers and health status
docker compose ps

# 3. Stream real-time logs
docker compose logs -f

# 4. Check backend actuator health
curl http://localhost:8080/actuator/health
```

### Stopping or Restarting Services
```bash
# Gracefully stop the application
docker compose down

# Restart a specific service without downtime for others
docker compose restart canteen-backend
```

---

## 5. Kubernetes Cluster Deployment

If deploying to a managed Kubernetes cluster (EKS, GKE, AKS, or k3s):

```bash
# 1. Create Namespace
kubectl apply -f k8s/namespace.yaml

# 2. Apply ConfigMap and Secrets
kubectl apply -f k8s/configmap.yaml
kubectl apply -f k8s/secret.yaml

# 3. Deploy Database with Persistent Volume Claim
kubectl apply -f k8s/mysql.yaml

# 4. Deploy Backend and Frontend Services
kubectl apply -f k8s/backend.yaml
kubectl apply -f k8s/frontend.yaml

# 5. Apply Ingress Controller Routing
kubectl apply -f k8s/ingress.yaml

# 6. Verify rollout
kubectl get all -n canteen-hub
```

---

## 6. SSL / TLS & HTTPS Setup

### Using Certbot & Let's Encrypt on Host Nginx
```bash
sudo apt update && sudo apt install -y certbot python3-certbot-nginx
sudo certbot --nginx -d canteen.yourdomain.com
```

### Auto-Renewal Cron Job
Let's Encrypt certificates are valid for 90 days. Test auto-renewal:
```bash
sudo certbot renew --dry-run
```

---

## 7. Database Backup & Disaster Recovery

### Automated Backup Script
Create `/opt/scripts/backup_canteen_db.sh`:
```bash
#!/bin/bash
BACKUP_DIR="/var/backups/canteen_db"
TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
mkdir -p "$BACKUP_DIR"

# Execute mysqldump inside container
docker exec canteen-mysql mysqldump -u root -p"$DB_ROOT_PASSWORD" canteen_db | gzip > "$BACKUP_DIR/canteen_db_$TIMESTAMP.sql.gz"

# Retain backups for 14 days
find "$BACKUP_DIR" -type f -name "*.sql.gz" -mtime +14 -exec rm {} \;
echo "[$(date)] Backup completed: canteen_db_$TIMESTAMP.sql.gz"
```

Add to cron (`crontab -e`):
```cron
0 2 * * * /opt/scripts/backup_canteen_db.sh >> /var/log/canteen_backup.log 2>&1
```

### Database Restore Procedure
```bash
gunzip < /var/backups/canteen_db/canteen_db_YYYYMMDD_HHMMSS.sql.gz | docker exec -i canteen-mysql mysql -u root -p"$DB_ROOT_PASSWORD" canteen_db
```

---

## 8. Monitoring & Healthchecks

### Spring Boot Actuator Endpoints
* **Liveness Probe**: `GET /actuator/health/liveness`
* **Readiness Probe**: `GET /actuator/health/readiness`
* **Metrics**: `GET /actuator/metrics`
* **Application Info**: `GET /actuator/info`

### Health Check via CLI
```bash
# Backend Health
curl -s http://localhost:8080/actuator/health | jq .

# Frontend HTTP status
curl -I http://localhost:80/
```

---

## 9. Production Security Checklist

- [x] **Stateless JWT Authentication**: Passwords hashed with BCrypt.
- [x] **Secure Connection Pooling**: HikariCP with connection timeout & leak detection.
- [x] **Container Hardening**: Multi-stage minimal Alpine images running as non-root user.
- [x] **Reverse Proxy Security**: Nginx headers configured for X-Frame-Options, X-Content-Type-Options, XSS protection.
- [x] **Configurable CORS**: Whitelist only trusted frontend production domains.
- [x] **Health & Probes**: Actuator integrated for zero-downtime orchestration.
- [x] **Automated CI/CD**: Unit tests, lint checks, and container builds on every commit.
