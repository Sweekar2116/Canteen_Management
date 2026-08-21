# REST API Documentation

Base URL: `http://localhost:8080/api`
Interactive Swagger UI: `http://localhost:8080/swagger-ui.html`
OpenAPI Specification JSON: `http://localhost:8080/v3/api-docs`

---

## 1. Authentication Endpoints

### Register New Customer
- **Endpoint**: `POST /api/auth/register`
- **Access**: Public
- **Request Body**:
```json
{
  "name": "Rahul Sharma",
  "email": "rahul@example.com",
  "phone": "9876543210",
  "password": "customer123"
}
```
- **Response `201 Created`**:
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "id": 3,
  "name": "Rahul Sharma",
  "email": "rahul@example.com",
  "phone": "9876543210",
  "roles": ["CUSTOMER"]
}
```

### Login
- **Endpoint**: `POST /api/auth/login`
- **Access**: Public
- **Request Body**:
```json
{
  "email": "admin@canteen.com",
  "password": "admin123"
}
```
- **Response `200 OK`**:
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "id": 1,
  "name": "Admin User",
  "email": "admin@canteen.com",
  "roles": ["ADMIN", "CUSTOMER"]
}
```

---

## 2. Menu & Categories

| Method | Endpoint | Access | Description |
|---|---|---|---|
| `GET` | `/api/categories` | Public | List all active food categories |
| `POST` | `/api/categories` | `ADMIN` | Create a new food category |
| `GET` | `/api/menu` | Public | List all available dishes (supports query search) |
| `GET` | `/api/menu/{id}` | Public | Get single food item details |
| `POST` | `/api/menu` | `ADMIN` | Create a new menu item |
| `PUT` | `/api/menu/{id}` | `ADMIN` | Update dish pricing, category or details |
| `DELETE` | `/api/menu/{id}` | `ADMIN` | Remove dish from menu |
| `PATCH` | `/api/menu/{id}/toggle` | `ADMIN` | Toggle dish availability (In stock / Sold out) |

---

## 3. Cart Management

| Method | Endpoint | Access | Description |
|---|---|---|---|
| `GET` | `/api/cart` | `CUSTOMER` | Retrieve current user's active cart & calculations |
| `POST` | `/api/cart/items` | `CUSTOMER` | Add menu item to cart (`{ menuItemId, quantity }`) |
| `PUT` | `/api/cart/items/{id}` | `CUSTOMER` | Update cart item quantity (`?quantity=N`) |
| `DELETE` | `/api/cart/items/{id}` | `CUSTOMER` | Remove single item from cart |
| `DELETE` | `/api/cart` | `CUSTOMER` | Clear entire shopping cart |

---

## 4. Orders & Checkout

### Create Order
- **Endpoint**: `POST /api/orders`
- **Access**: `CUSTOMER`
- **Request Body**:
```json
{
  "couponCode": "WELCOME10",
  "paymentMethod": "UPI",
  "notes": "Less spicy please"
}
```
- **Response `201 Created`**:
```json
{
  "id": 101,
  "orderNumber": "ORD-2026-9482",
  "userId": 3,
  "userName": "Rahul Sharma",
  "status": "PLACED",
  "totalAmount": 120.00,
  "discountAmount": 12.00,
  "taxAmount": 5.40,
  "finalAmount": 113.40,
  "paymentMethod": "UPI",
  "paymentStatus": "COMPLETED",
  "items": [
    {
      "id": 201,
      "menuItemId": 1,
      "itemName": "Masala Dosa",
      "quantity": 1,
      "unitPrice": 80.00,
      "totalPrice": 80.00
    }
  ],
  "createdAt": "2026-08-21T18:45:00"
}
```

### Get Order Timeline
- **Endpoint**: `GET /api/orders/{id}`
- **Access**: `CUSTOMER` / `STAFF` / `ADMIN`
- **Response `200 OK`**: Returns live status, timestamps, items, and preparation notes.

### Update Order Status
- **Endpoint**: `PUT /api/orders/{id}/status`
- **Access**: `STAFF` / `ADMIN`
- **Request Body**:
```json
{
  "status": "PREPARING"
}
```

---

## 5. Admin & Analytics

| Method | Endpoint | Access | Description |
|---|---|---|---|
| `GET` | `/api/admin/dashboard` | `ADMIN` | Real-time KPIs: Revenue, Order volume, Active items |
| `GET` | `/api/admin/orders` | `ADMIN`, `STAFF` | Paginated kitchen orders with status filters |
| `GET` | `/api/admin/users` | `ADMIN` | User accounts, roles, and status management |
| `GET` | `/api/inventory` | `ADMIN` | Stock quantities, minimum thresholds & alerts |
| `POST` | `/api/inventory/{id}/restock` | `ADMIN` | Update raw ingredient quantities |
| `GET` | `/api/coupons` | `ADMIN` | Active discount coupons and usage limits |
| `POST` | `/api/coupons` | `ADMIN` | Create new promotional discount campaign |
