# ==============================================================================
# Multi-Stage Production Dockerfile for Canteen Management Frontend
# ==============================================================================

# ------------------------------------------------------------------------------
# Stage 1: Build Frontend Assets
# ------------------------------------------------------------------------------
FROM node:20-alpine AS builder

WORKDIR /app

# Cache dependencies
COPY package*.json ./
RUN npm ci

# Copy source code and build production distribution
COPY . .
RUN npm run build

# ------------------------------------------------------------------------------
# Stage 2: Serve with Nginx Alpine
# ------------------------------------------------------------------------------
FROM nginx:1.27-alpine

LABEL maintainer="CanteenHub Team"
LABEL description="Production Nginx container serving Canteen React Frontend"

# Remove default nginx html files
RUN rm -rf /usr/share/nginx/html/*

# Copy custom Nginx reverse-proxy & routing configuration
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copy compiled SPA assets from builder stage
COPY --from=builder /app/dist /usr/share/nginx/html

EXPOSE 80

# Production Healthcheck
HEALTHCHECK --interval=30s --timeout=3s --start-period=5s --retries=3 \
    CMD wget -q --spider http://localhost/ || exit 1

CMD ["nginx", "-g", "daemon off;"]
