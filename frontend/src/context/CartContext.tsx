import React, { createContext, useContext, useState, useEffect } from 'react';
import { Cart, CartItem, MenuItem } from '../types';
import api from '../services/api';
import { useAuth } from './AuthContext';
import { DEFAULT_MENU_ITEMS } from '../services/mockData';

interface CartContextType {
  cart: Cart | null;
  loading: boolean;
  totalItems: number;
  fetchCart: () => Promise<void>;
  addToCart: (menuItemId: number, quantity?: number) => Promise<void>;
  updateQuantity: (cartItemId: number, quantity: number) => Promise<void>;
  removeFromCart: (cartItemId: number) => Promise<void>;
  clearCart: () => Promise<void>;
}

const CartContext = createContext<CartContextType | undefined>(undefined);

const calculateCartTotals = (items: CartItem[]): Cart => {
  const safeItems = Array.isArray(items) ? items : [];
  const subtotal = safeItems.reduce((sum, item) => sum + (item.totalPrice || item.unitPrice * item.quantity), 0);
  const tax = Number((subtotal * 0.05).toFixed(2));
  const total = Number((subtotal + tax).toFixed(2));
  const totalItems = safeItems.reduce((sum, item) => sum + item.quantity, 0);

  return {
    id: 1,
    items: safeItems,
    totalItems,
    subtotal: Number(subtotal.toFixed(2)),
    tax,
    total,
  };
};

export const CartProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const { isAuthenticated } = useAuth();
  const [cart, setCart] = useState<Cart | null>(() => {
    try {
      const saved = localStorage.getItem('canteenhub_cart');
      if (saved) {
        const parsed = JSON.parse(saved);
        if (parsed && Array.isArray(parsed.items)) {
          return calculateCartTotals(parsed.items);
        }
      }
    } catch (e) {
      console.warn('Could not parse local cart', e);
    }
    return null;
  });
  const [loading, setLoading] = useState(false);

  const saveLocalCart = (newCart: Cart | null) => {
    setCart(newCart);
    if (newCart) {
      localStorage.setItem('canteenhub_cart', JSON.stringify(newCart));
    } else {
      localStorage.removeItem('canteenhub_cart');
    }
  };

  const fetchCart = async () => {
    if (!isAuthenticated) {
      saveLocalCart(null);
      return;
    }
    try {
      setLoading(true);
      const res = await api.get<Cart>('/cart');
      if (res.data && Array.isArray(res.data.items)) {
        saveLocalCart(res.data);
      }
    } catch (err) {
      console.warn('Backend cart fetch failed, using local cart session', err);
      // Retain or load local cart
      try {
        const saved = localStorage.getItem('canteenhub_cart');
        if (saved) {
          const parsed = JSON.parse(saved);
          if (parsed && Array.isArray(parsed.items)) {
            setCart(calculateCartTotals(parsed.items));
          }
        }
      } catch (e) {}
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (isAuthenticated) {
      fetchCart();
    }
  }, [isAuthenticated]);

  const addToCart = async (menuItemId: number, quantity = 1) => {
    if (!isAuthenticated) return;

    // 1. Try backend API first
    try {
      const res = await api.post<Cart>('/cart/items', { menuItemId, quantity });
      if (res.data && Array.isArray(res.data.items)) {
        saveLocalCart(res.data);
        return;
      }
    } catch (err) {
      console.warn('Backend add-to-cart offline, updating client-side cart...', err);
    }

    // 2. Seamless local cart fallback
    const itemInfo: MenuItem | undefined = DEFAULT_MENU_ITEMS.find((m) => m.id === menuItemId);
    const existingItems = Array.isArray(cart?.items) ? [...cart.items] : [];
    const existingIndex = existingItems.findIndex((i) => i.menuItemId === menuItemId);

    if (existingIndex > -1) {
      const current = existingItems[existingIndex];
      const newQty = current.quantity + quantity;
      existingItems[existingIndex] = {
        ...current,
        quantity: newQty,
        totalPrice: Number((newQty * current.unitPrice).toFixed(2)),
      };
    } else {
      const unitPrice = itemInfo ? itemInfo.price : 50;
      existingItems.push({
        id: Date.now(),
        menuItemId,
        itemName: itemInfo ? itemInfo.name : `Dish #${menuItemId}`,
        imageUrl: itemInfo?.imageUrl,
        unitPrice,
        quantity,
        totalPrice: Number((unitPrice * quantity).toFixed(2)),
        available: true,
        stockQuantity: 100,
      });
    }

    const updatedCart = calculateCartTotals(existingItems);
    saveLocalCart(updatedCart);
  };

  const updateQuantity = async (cartItemId: number, quantity: number) => {
    if (!isAuthenticated) return;

    if (quantity <= 0) {
      await removeFromCart(cartItemId);
      return;
    }

    // Try backend
    try {
      const res = await api.put<Cart>(`/cart/items/${cartItemId}?quantity=${quantity}`);
      if (res.data && Array.isArray(res.data.items)) {
        saveLocalCart(res.data);
        return;
      }
    } catch (err) {
      console.warn('Backend update quantity offline, updating local cart...', err);
    }

    // Local fallback
    const existingItems = Array.isArray(cart?.items) ? [...cart.items] : [];
    const target = existingItems.find((i) => i.id === cartItemId || i.menuItemId === cartItemId);
    if (target) {
      target.quantity = quantity;
      target.totalPrice = Number((quantity * target.unitPrice).toFixed(2));
    }
    saveLocalCart(calculateCartTotals(existingItems));
  };

  const removeFromCart = async (cartItemId: number) => {
    if (!isAuthenticated) return;

    // Try backend
    try {
      const res = await api.delete<Cart>(`/cart/items/${cartItemId}`);
      if (res.data && Array.isArray(res.data.items)) {
        saveLocalCart(res.data);
        return;
      }
    } catch (err) {
      console.warn('Backend remove item offline, updating local cart...', err);
    }

    // Local fallback
    const existingItems = Array.isArray(cart?.items) ? [...cart.items] : [];
    const filtered = existingItems.filter((i) => i.id !== cartItemId && i.menuItemId !== cartItemId);
    saveLocalCart(filtered.length > 0 ? calculateCartTotals(filtered) : null);
  };

  const clearCart = async () => {
    if (!isAuthenticated) return;
    try {
      await api.delete('/cart');
    } catch (err) {
      console.warn('Backend clear cart offline', err);
    }
    saveLocalCart(null);
  };

  const totalItems = cart?.totalItems || 0;

  return (
    <CartContext.Provider
      value={{
        cart,
        loading,
        totalItems,
        fetchCart,
        addToCart,
        updateQuantity,
        removeFromCart,
        clearCart,
      }}
    >
      {children}
    </CartContext.Provider>
  );
};

export const useCart = () => {
  const context = useContext(CartContext);
  if (!context) {
    throw new Error('useCart must be used within a CartProvider');
  }
  return context;
};
