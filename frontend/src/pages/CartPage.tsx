import React from 'react';
import { Link } from 'react-router-dom';
import { useCart } from '../context/CartContext';
import { 
  ShoppingBag, 
  Trash2, 
  Plus, 
  Minus, 
  ArrowRight, 
  UtensilsCrossed, 
  ShieldCheck,
  Sparkles
} from 'lucide-react';

export const CartPage: React.FC = () => {
  const { cart, loading, updateQuantity, removeFromCart, clearCart } = useCart();

  if (loading && !cart) {
    return (
      <div className="max-w-4xl mx-auto px-4 py-16 text-center">
        <div className="h-8 w-8 border-4 border-brand-500 border-t-transparent rounded-full animate-spin mx-auto mb-4" />
        <p className="text-sm font-semibold text-slate-500">Loading your cart...</p>
      </div>
    );
  }

  if (!cart || cart.items.length === 0) {
    return (
      <div className="max-w-md mx-auto px-4 py-20 text-center space-y-6">
        <div className="h-20 w-20 bg-brand-50 rounded-full flex items-center justify-center mx-auto text-brand-500 shadow-inner">
          <ShoppingBag className="h-10 w-10" />
        </div>
        <div className="space-y-2">
          <h2 className="text-2xl font-black text-slate-900">Your Cart is Empty</h2>
          <p className="text-sm text-slate-500">
            Looks like you haven't added any dishes yet. Explore our fresh menu and pick your favorites!
          </p>
        </div>
        <Link
          to="/menu"
          className="inline-flex items-center space-x-2 px-8 py-3.5 rounded-2xl bg-gradient-to-r from-brand-500 to-amber-500 text-white font-bold text-sm shadow-xl shadow-brand-500/25 hover:shadow-brand-500/40 transition active:scale-95"
        >
          <span>Browse Fresh Menu</span>
          <ArrowRight className="h-4 w-4" />
        </Link>
      </div>
    );
  }

  return (
    <div className="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8">
      <div className="flex items-center justify-between border-b border-slate-200/80 pb-6">
        <div>
          <h1 className="text-3xl font-black text-slate-900 tracking-tight">Shopping Cart</h1>
          <p className="text-sm text-slate-500 mt-1">Review your selected dishes before checkout</p>
        </div>

        <button
          onClick={clearCart}
          className="text-xs font-bold text-rose-600 hover:text-rose-700 flex items-center gap-1 hover:bg-rose-50 px-3 py-2 rounded-xl transition"
        >
          <Trash2 className="h-4 w-4" />
          <span>Clear All</span>
        </button>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-12 gap-8 items-start">
        {/* Cart Item List */}
        <div className="lg:col-span-8 space-y-4">
          {cart.items.map((item) => (
            <div
              key={item.id}
              className="p-5 rounded-2xl bg-white border border-slate-200/80 shadow-sm flex items-center justify-between gap-4 hover:border-slate-300 transition"
            >
              <div className="flex items-center space-x-4">
                <img
                  src={item.imageUrl || 'https://images.unsplash.com/photo-1546833999-b9f581a1996d?auto=format&fit=crop&w=200&q=80'}
                  alt={item.itemName}
                  className="h-16 w-16 rounded-2xl object-cover bg-slate-100 border border-slate-100 shadow-sm shrink-0"
                  onError={(e: any) => {
                    e.target.src = 'https://images.unsplash.com/photo-1546833999-b9f581a1996d?auto=format&fit=crop&w=200&q=80';
                  }}
                />
                <div className="space-y-1">
                  <h4 className="font-extrabold text-slate-900 text-base">{item.itemName}</h4>
                  <p className="text-xs text-slate-400">Unit Price: ₹{item.unitPrice}</p>
                  <p className="text-sm font-black text-brand-600">Total: ₹{item.totalPrice}</p>
                </div>
              </div>

              {/* Quantity Controls */}
              <div className="flex items-center space-x-3">
                <div className="flex items-center border border-slate-200 rounded-xl bg-slate-50/70 p-1">
                  <button
                    onClick={() => updateQuantity(item.id, item.quantity - 1)}
                    className="h-8 w-8 rounded-lg bg-white shadow-sm flex items-center justify-center text-slate-700 hover:bg-slate-100 transition active:scale-95"
                  >
                    <Minus className="h-3.5 w-3.5" />
                  </button>
                  <span className="w-10 text-center text-sm font-bold text-slate-900">
                    {item.quantity}
                  </span>
                  <button
                    onClick={() => updateQuantity(item.id, item.quantity + 1)}
                    className="h-8 w-8 rounded-lg bg-white shadow-sm flex items-center justify-center text-slate-700 hover:bg-slate-100 transition active:scale-95"
                  >
                    <Plus className="h-3.5 w-3.5" />
                  </button>
                </div>

                <button
                  onClick={() => removeFromCart(item.id)}
                  className="p-2 text-slate-400 hover:text-rose-600 hover:bg-rose-50 rounded-xl transition"
                  title="Remove"
                >
                  <Trash2 className="h-5 w-5" />
                </button>
              </div>
            </div>
          ))}
        </div>

        {/* Order Summary Card */}
        <div className="lg:col-span-4 p-6 rounded-3xl bg-white border border-slate-200/80 shadow-lg space-y-6 sticky top-24">
          <h3 className="text-lg font-black text-slate-900 border-b border-slate-100 pb-4">
            Bill Summary
          </h3>

          <div className="space-y-3 text-sm">
            <div className="flex justify-between text-slate-600">
              <span>Item Subtotal ({cart.totalItems} items)</span>
              <span className="font-bold text-slate-900">₹{cart.subtotal}</span>
            </div>
            <div className="flex justify-between text-slate-600">
              <span>GST & Canteen Tax (5%)</span>
              <span className="font-bold text-slate-900">₹{cart.tax}</span>
            </div>
            <div className="border-t border-dashed border-slate-200 pt-3 flex justify-between text-base font-black text-slate-900">
              <span>Estimated Total</span>
              <span className="text-xl text-brand-600">₹{cart.total}</span>
            </div>
          </div>

          <div className="p-3 rounded-xl bg-amber-50 border border-amber-200 text-xs text-amber-800 flex items-start gap-2">
            <Sparkles className="h-4 w-4 text-amber-600 shrink-0 mt-0.5" />
            <span>Have a student coupon? You can apply it on the checkout screen!</span>
          </div>

          <Link
            to="/checkout"
            className="w-full py-4 px-6 rounded-2xl bg-gradient-to-r from-brand-500 to-amber-500 text-white font-extrabold text-sm shadow-xl shadow-brand-500/25 hover:shadow-brand-500/40 transition flex items-center justify-center space-x-2 active:scale-95"
          >
            <span>Proceed to Checkout</span>
            <ArrowRight className="h-4 w-4" />
          </Link>
        </div>
      </div>
    </div>
  );
};
