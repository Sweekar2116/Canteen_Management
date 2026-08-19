import React, { createContext, useContext, useState, useEffect } from 'react';
import { Cart } from '../types';
import api from '../services/api';
import { useAuth } from './AuthContext';

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

export const CartProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const { isAuthenticated } = useAuth();
  const [cart, setCart] = useState<Cart | null>(null);
  const [loading, setLoading] = useState(false);

  const fetchCart = async () => {
    if (!isAuthenticated) {
      setCart(null);
      return;
    }
    try {
      setLoading(true);
      const res = await api.get<Cart>('/cart');
      setCart(res.data);
    } catch (err) {
      console.error('Error fetching cart:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCart();
  }, [isAuthenticated]);

  const addToCart = async (menuItemId: number, quantity = 1) => {
    if (!isAuthenticated) return;
    try {
      const res = await api.post<Cart>('/cart/items', { menuItemId, quantity });
      setCart(res.data);
    } catch (err: any) {
      console.error('Failed to add item to cart:', err);
      throw new Error(err.response?.data?.message || 'Failed to add item to cart');
    }
  };

  const updateQuantity = async (cartItemId: number, quantity: number) => {
    if (!isAuthenticated) return;
    try {
      if (quantity <= 0) {
        await removeFromCart(cartItemId);
        return;
      }
      const res = await api.put<Cart>(`/cart/items/${cartItemId}?quantity=${quantity}`);
      setCart(res.data);
    } catch (err: any) {
      console.error('Failed to update quantity:', err);
      // Refresh cart from server in case of state mismatch
      await fetchCart();
      throw new Error(err.response?.data?.message || 'Failed to update quantity');
    }
  };

  const removeFromCart = async (cartItemId: number) => {
    if (!isAuthenticated) return;
    try {
      const res = await api.delete<Cart>(`/cart/items/${cartItemId}`);
      setCart(res.data);
    } catch (err: any) {
      console.error('Failed to remove item:', err);
      await fetchCart();
      throw new Error(err.response?.data?.message || 'Failed to remove item');
    }
  };

  const clearCart = async () => {
    if (!isAuthenticated) return;
    try {
      await api.delete('/cart');
      setCart(null);
    } catch (err: any) {
      console.error('Failed to clear cart:', err);
      await fetchCart();
    }
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
