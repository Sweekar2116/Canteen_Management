export type RoleType = 'CUSTOMER' | 'ADMIN' | 'STAFF';

export interface User {
  id: number;
  name: string;
  email: string;
  phone?: string;
  roles: RoleType[];
  enabled: boolean;
  createdAt?: string;
}

export interface AuthResponse {
  token: string;
  type: string;
  id: number;
  name: string;
  email: string;
  phone?: string;
  roles: RoleType[];
}

export interface Category {
  id: number;
  name: string;
  description?: string;
  imageUrl?: string;
  active: boolean;
}

export interface MenuItem {
  id: number;
  name: string;
  description?: string;
  price: number;
  imageUrl?: string;
  categoryId: number;
  categoryName?: string;
  available: boolean;
  vegetarian: boolean;
  rating?: number;
  ratingCount?: number;
  preparationTime?: number;
  stockQuantity?: number;
}

export interface CartItem {
  id: number;
  menuItemId: number;
  itemName: string;
  imageUrl?: string;
  unitPrice: number;
  quantity: number;
  totalPrice: number;
  available: boolean;
  stockQuantity?: number;
}

export interface Cart {
  id: number;
  items: CartItem[];
  totalItems: number;
  subtotal: number;
  tax: number;
  total: number;
}

export type OrderStatus =
  | 'PLACED'
  | 'CONFIRMED'
  | 'PREPARING'
  | 'READY_FOR_PICKUP'
  | 'COMPLETED'
  | 'CANCELLED';

export type PaymentMethod = 'CASH_ON_PICKUP' | 'UPI' | 'CARD' | 'ONLINE';
export type PaymentStatus = 'PENDING' | 'COMPLETED' | 'FAILED' | 'REFUNDED';

export interface OrderItem {
  id: number;
  menuItemId: number;
  itemName: string;
  imageUrl?: string;
  quantity: number;
  unitPrice: number;
  totalPrice: number;
}

export interface Order {
  id: number;
  orderNumber: string;
  userId: number;
  userName: string;
  userEmail: string;
  userPhone?: string;
  status: OrderStatus;
  totalAmount: number;
  discountAmount: number;
  taxAmount: number;
  finalAmount: number;
  couponCode?: string;
  pickupTime?: string;
  notes?: string;
  paymentMethod?: PaymentMethod;
  paymentStatus?: PaymentStatus;
  transactionRef?: string;
  items: OrderItem[];
  createdAt: string;
  updatedAt: string;
}

export interface Coupon {
  id: number;
  code: string;
  description?: string;
  discountPercent: number;
  maxDiscount?: number;
  minOrderAmount?: number;
  expiryDate: string;
  usageLimit?: number;
  usedCount?: number;
  active: boolean;
}

export interface InventoryItem {
  id: number;
  menuItemId: number;
  itemName: string;
  categoryName?: string;
  quantity: number;
  unit: string;
  minStockLevel: number;
  lowStock: boolean;
  lastUpdated: string;
}

export interface DashboardStats {
  totalOrders: number;
  todayOrders: number;
  totalRevenue: number;
  todayRevenue: number;
  totalCustomers: number;
  availableMenuItems: number;
  pendingOrders: number;
  lowStockCount: number;
  ordersByStatus: Record<string, number>;
  topSellingItems: Array<{
    name: string;
    quantitySold: number;
    revenue: number;
  }>;
}

export interface NotificationItem {
  id: number;
  title: string;
  message: string;
  read: boolean;
  createdAt: string;
}
