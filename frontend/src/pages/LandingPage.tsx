import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { 
  ArrowRight, 
  Sparkles, 
  Clock, 
  ShieldCheck, 
  Star, 
  Plus, 
  Check, 
  Flame, 
  Coffee, 
  Utensils 
} from 'lucide-react';
import { MenuItem, Category } from '../types';
import api from '../services/api';
import { useCart } from '../context/CartContext';
import { useAuth } from '../context/AuthContext';

import { DEFAULT_CATEGORIES, DEFAULT_MENU_ITEMS } from '../services/mockData';

export const LandingPage: React.FC = () => {
  const [featuredItems, setFeaturedItems] = useState<MenuItem[]>(DEFAULT_MENU_ITEMS.slice(0, 6));
  const [categories, setCategories] = useState<Category[]>(DEFAULT_CATEGORIES);
  const [loading, setLoading] = useState(false);
  const [addedItemMap, setAddedItemMap] = useState<Record<number, boolean>>({});
  const { addToCart } = useCart();
  const { isAuthenticated } = useAuth();

  useEffect(() => {
    Promise.all([
      api.get<Category[]>('/categories'),
      api.get<MenuItem[]>('/menu/all'),
    ])
      .then(([catRes, menuRes]) => {
        if (Array.isArray(catRes.data)) {
          setCategories(catRes.data.slice(0, 5));
        }
        if (Array.isArray(menuRes.data)) {
          setFeaturedItems(menuRes.data.slice(0, 6));
        }
      })
      .catch((err) => {
        console.warn('Backend API unavailable, using offline fallback data', err);
      })
      .finally(() => setLoading(false));
  }, []);

  const handleAddToCart = async (item: MenuItem) => {
    if (!isAuthenticated) {
      window.location.href = '/login';
      return;
    }
    try {
      await addToCart(item.id, 1);
      setAddedItemMap((prev) => ({ ...prev, [item.id]: true }));
      setTimeout(() => {
        setAddedItemMap((prev) => ({ ...prev, [item.id]: false }));
      }, 1500);
    } catch (err) {
      alert('Could not add to cart');
    }
  };

  return (
    <div className="space-y-16 pb-20">
      {/* Hero Section */}
      <section className="relative overflow-hidden pt-12 pb-20 lg:pt-20 lg:pb-28 bg-gradient-to-b from-brand-50/60 via-slate-50 to-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid grid-cols-1 lg:grid-cols-12 gap-12 items-center">
            {/* Left text */}
            <div className="lg:col-span-7 space-y-6 text-center lg:text-left">
              <div className="inline-flex items-center space-x-2 px-3.5 py-1.5 rounded-full bg-brand-100/70 text-brand-800 text-xs font-bold uppercase tracking-wider border border-brand-200 shadow-sm">
                <Sparkles className="h-3.5 w-3.5 text-brand-600 animate-pulse" />
                <span>Next-Gen Campus Dining Experience</span>
              </div>

              <h1 className="text-4xl sm:text-5xl lg:text-6xl font-extrabold text-slate-900 tracking-tight leading-[1.1]">
                Craving Hot Food? <br />
                <span className="bg-gradient-to-r from-brand-600 via-brand-500 to-amber-500 bg-clip-text text-transparent">
                  Order & Skip the Queue
                </span>
              </h1>

              <p className="text-lg text-slate-600 max-w-xl mx-auto lg:mx-0 leading-relaxed">
                Order freshly prepared campus meals directly from your phone. Track live preparation in real-time, pickup hot and fresh without waiting in line.
              </p>

              <div className="flex flex-col sm:flex-row items-center justify-center lg:justify-start gap-4 pt-2">
                <Link
                  to="/menu"
                  className="w-full sm:w-auto inline-flex items-center justify-center space-x-2 px-8 py-4 text-base font-bold text-white bg-gradient-to-r from-brand-500 to-amber-500 rounded-2xl shadow-xl shadow-brand-500/25 hover:shadow-brand-500/40 hover:-translate-y-0.5 transition active:scale-95"
                >
                  <span>Explore Menu</span>
                  <ArrowRight className="h-5 w-5" />
                </Link>
                <Link
                  to="/orders"
                  className="w-full sm:w-auto inline-flex items-center justify-center space-x-2 px-6 py-4 text-base font-semibold text-slate-700 bg-white border border-slate-200 rounded-2xl hover:bg-slate-50 transition"
                >
                  <Clock className="h-4 w-4 text-slate-500" />
                  <span>Track Current Order</span>
                </Link>
              </div>

              {/* Trust badges */}
              <div className="pt-6 grid grid-cols-3 gap-4 border-t border-slate-200/80 text-left max-w-md mx-auto lg:mx-0">
                <div>
                  <p className="text-2xl font-extrabold text-slate-900">10-15m</p>
                  <p className="text-xs text-slate-500 font-medium">Avg. Prep Time</p>
                </div>
                <div>
                  <p className="text-2xl font-extrabold text-slate-900">4.8 ★</p>
                  <p className="text-xs text-slate-500 font-medium">Campus Rating</p>
                </div>
                <div>
                  <p className="text-2xl font-extrabold text-slate-900">100%</p>
                  <p className="text-xs text-slate-500 font-medium">Hygienic Prep</p>
                </div>
              </div>
            </div>

            {/* Right Hero Visual Cards */}
            <div className="lg:col-span-5 relative">
              <div className="relative mx-auto max-w-md space-y-4">
                <div className="p-6 rounded-3xl bg-white shadow-2xl border border-slate-100 hover:scale-[1.02] transition duration-300">
                  <div className="flex items-center justify-between mb-4">
                    <div className="flex items-center space-x-3">
                      <div className="h-12 w-12 rounded-2xl bg-amber-50 flex items-center justify-center text-amber-600 font-bold text-lg">
                        🍲
                      </div>
                      <div>
                        <h4 className="font-bold text-slate-900">Chef's Masala Dosa</h4>
                        <p className="text-xs text-slate-500">Crispy rice crepe with spiced potato</p>
                      </div>
                    </div>
                    <span className="text-lg font-extrabold text-brand-600">₹80</span>
                  </div>
                  <div className="flex items-center justify-between pt-2 border-t border-slate-100 text-xs text-slate-500">
                    <span className="flex items-center gap-1"><Clock className="h-3.5 w-3.5"/> 8-10 mins</span>
                    <span className="px-2 py-0.5 rounded-full bg-emerald-50 text-emerald-700 font-bold">100% Pure Veg</span>
                  </div>
                </div>

                <div className="p-6 rounded-3xl bg-navy-900 text-white shadow-2xl border border-navy-800 translate-x-4 hover:translate-x-3 transition duration-300">
                  <div className="flex items-center justify-between">
                    <div>
                      <p className="text-xs font-semibold text-amber-400 uppercase tracking-wider">Live Order Status</p>
                      <h4 className="text-lg font-bold text-white mt-1">Order #ORD-2026-9482</h4>
                    </div>
                    <span className="px-3 py-1 rounded-full bg-emerald-500/20 text-emerald-300 text-xs font-bold border border-emerald-500/30 animate-pulse">
                      Ready for Pickup
                    </span>
                  </div>
                  <div className="mt-4 flex items-center space-x-2 text-xs text-slate-300">
                    <div className="h-2 w-2 rounded-full bg-emerald-400"></div>
                    <span>Counter #2 • Estimated wait: 0 mins</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Categories Carousel */}
      <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between mb-8">
          <div>
            <h2 className="text-2xl sm:text-3xl font-extrabold text-slate-900 tracking-tight">Popular Categories</h2>
            <p className="text-sm text-slate-500 mt-1">Choose from our daily menu selections</p>
          </div>
          <Link to="/menu" className="text-sm font-bold text-brand-600 hover:text-brand-700 flex items-center gap-1">
            View All <ArrowRight className="h-4 w-4" />
          </Link>
        </div>

        <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-5 gap-4">
          {(Array.isArray(categories) ? categories : []).map((category) => (
            <Link
              key={category.id}
              to={`/menu?category=${category.id}`}
              className="group p-5 rounded-2xl bg-white border border-slate-200/80 shadow-sm hover:shadow-md hover:border-brand-300 transition text-center flex flex-col items-center justify-center space-y-3"
            >
              <div className="h-14 w-14 rounded-2xl bg-gradient-to-br from-brand-50 to-amber-50 flex items-center justify-center text-2xl group-hover:scale-110 transition">
                {category.name === 'Breakfast' ? '🥞' :
                 category.name === 'Lunch' ? '🍛' :
                 category.name === 'Snacks' ? '🥟' :
                 category.name === 'Beverages' ? '☕' : '✨'}
              </div>
              <div>
                <h3 className="font-bold text-slate-900 group-hover:text-brand-600 transition text-sm">{category.name}</h3>
                <p className="text-[11px] text-slate-400 mt-0.5 line-clamp-1">{category.description}</p>
              </div>
            </Link>
          ))}
        </div>
      </section>

      {/* Featured Menu Items */}
      <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between mb-8">
          <div>
            <div className="inline-flex items-center space-x-1.5 text-xs font-bold text-brand-600 uppercase tracking-wider mb-1">
              <Flame className="h-4 w-4 text-brand-500" />
              <span>Campus Favorites</span>
            </div>
            <h2 className="text-2xl sm:text-3xl font-extrabold text-slate-900 tracking-tight">Today's Specials</h2>
          </div>
          <Link
            to="/menu"
            className="px-4 py-2 rounded-xl border border-slate-200 text-sm font-semibold text-slate-700 hover:bg-slate-50 transition"
          >
            Full Menu
          </Link>
        </div>

        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
          {(Array.isArray(featuredItems) ? featuredItems : []).map((item) => (
            <div
              key={item.id}
              className="rounded-2xl bg-white border border-slate-200/90 overflow-hidden shadow-sm hover:shadow-lg transition group flex flex-col justify-between"
            >
              {/* Dish Image */}
              <div className="h-48 w-full relative overflow-hidden bg-slate-100">
                <img
                  src={item.imageUrl || 'https://images.unsplash.com/photo-1546833999-b9f581a1996d?auto=format&fit=crop&w=600&q=80'}
                  alt={item.name}
                  className="w-full h-full object-cover group-hover:scale-105 transition duration-500"
                  onError={(e: any) => {
                    e.target.src = 'https://images.unsplash.com/photo-1546833999-b9f581a1996d?auto=format&fit=crop&w=600&q=80';
                  }}
                />
                <div className="absolute top-3 left-3">
                  <span
                    className={`h-5 w-5 rounded-md border-2 bg-white/90 backdrop-blur-sm flex items-center justify-center p-0.5 shadow-sm ${
                      item.vegetarian
                        ? 'border-emerald-600'
                        : 'border-rose-600'
                    }`}
                    title={item.vegetarian ? 'Vegetarian' : 'Non-Vegetarian'}
                  >
                    <span className={`h-2 w-2 rounded-full ${item.vegetarian ? 'bg-emerald-600' : 'bg-rose-600'}`} />
                  </span>
                </div>

                <div className="absolute top-3 right-3 flex items-center space-x-1 text-xs font-bold text-slate-800 bg-white/90 backdrop-blur-sm px-2 py-1 rounded-lg shadow-sm">
                  <Star className="h-3 w-3 fill-amber-400 text-amber-400" />
                  <span>{item.rating || 4.5}</span>
                </div>
              </div>

              <div className="p-6 flex-1 flex flex-col justify-between">
                <div>
                  <div className="flex items-center justify-between text-xs text-slate-400">
                    <span className="font-bold text-brand-600 uppercase tracking-wider">{item.categoryName}</span>
                    <span className="flex items-center gap-1"><Clock className="h-3 w-3"/> {item.preparationTime || 12} mins</span>
                  </div>

                  <h3 className="font-extrabold text-slate-900 text-lg mt-2 group-hover:text-brand-600 transition">
                    {item.name}
                  </h3>
                  <p className="text-xs text-slate-500 mt-1 line-clamp-2 leading-relaxed">
                    {item.description}
                  </p>
                </div>
              </div>

              <div className="px-6 py-4 bg-slate-50/70 border-t border-slate-100 flex items-center justify-between">
                <div>
                  <span className="text-xs text-slate-400 font-medium">Price</span>
                  <p className="text-xl font-black text-slate-900">₹{item.price}</p>
                </div>

                <button
                  onClick={() => handleAddToCart(item)}
                  disabled={!item.available}
                  className={`inline-flex items-center space-x-1.5 px-4 py-2.5 rounded-xl font-bold text-xs shadow-sm transition active:scale-95 ${
                    !item.available
                      ? 'bg-slate-200 text-slate-400 cursor-not-allowed'
                      : addedItemMap[item.id]
                      ? 'bg-emerald-600 text-white'
                      : 'bg-brand-500 hover:bg-brand-600 text-white shadow-brand-500/20'
                  }`}
                >
                  {addedItemMap[item.id] ? (
                    <>
                      <Check className="h-4 w-4" />
                      <span>Added!</span>
                    </>
                  ) : (
                    <>
                      <Plus className="h-4 w-4" />
                      <span>Add to Cart</span>
                    </>
                  )}
                </button>
              </div>
            </div>
          ))}
        </div>
      </section>

      {/* Feature Highlights */}
      <section className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 pt-8">
        <div className="rounded-3xl bg-gradient-to-r from-navy-900 to-slate-900 text-white p-8 sm:p-12 shadow-2xl">
          <div className="grid grid-cols-1 md:grid-cols-3 gap-8 text-center md:text-left">
            <div className="space-y-3">
              <div className="h-12 w-12 rounded-2xl bg-brand-500/20 text-brand-400 flex items-center justify-center mx-auto md:mx-0">
                <Clock className="h-6 w-6" />
              </div>
              <h3 className="font-bold text-lg text-white">Zero Wait Time</h3>
              <p className="text-sm text-slate-400">Order from your lecture hall and walk up to the counter when your food is ready.</p>
            </div>

            <div className="space-y-3">
              <div className="h-12 w-12 rounded-2xl bg-amber-500/20 text-amber-400 flex items-center justify-center mx-auto md:mx-0">
                <Sparkles className="h-6 w-6" />
              </div>
              <h3 className="font-bold text-lg text-white">Campus Discounts</h3>
              <p className="text-sm text-slate-400">Use promo codes like WELCOME10 and SAVE20 at checkout for special student pricing.</p>
            </div>

            <div className="space-y-3">
              <div className="h-12 w-12 rounded-2xl bg-emerald-500/20 text-emerald-400 flex items-center justify-center mx-auto md:mx-0">
                <ShieldCheck className="h-6 w-6" />
              </div>
              <h3 className="font-bold text-lg text-white">Cash or Digital Pay</h3>
              <p className="text-sm text-slate-400">Pay conveniently via UPI, Card, or directly at pickup counter with Cash.</p>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
};
