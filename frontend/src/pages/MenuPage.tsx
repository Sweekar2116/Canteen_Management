import React, { useState, useEffect } from 'react';
import { useSearchParams } from 'react-router-dom';
import { 
  Search, 
  Filter, 
  Star, 
  Clock, 
  Plus, 
  Check, 
  UtensilsCrossed, 
  SlidersHorizontal,
  Flame,
  AlertTriangle
} from 'lucide-react';
import { MenuItem, Category } from '../types';
import api from '../services/api';
import { useCart } from '../context/CartContext';
import { useAuth } from '../context/AuthContext';

import { DEFAULT_CATEGORIES, DEFAULT_MENU_ITEMS } from '../services/mockData';

export const MenuPage: React.FC = () => {
  const [searchParams, setSearchParams] = useSearchParams();
  const [menuItems, setMenuItems] = useState<MenuItem[]>(DEFAULT_MENU_ITEMS);
  const [categories, setCategories] = useState<Category[]>(DEFAULT_CATEGORIES);
  const [loading, setLoading] = useState(false);

  const [query, setQuery] = useState(searchParams.get('query') || '');
  const [selectedCategory, setSelectedCategory] = useState<number | null>(
    searchParams.get('category') ? Number(searchParams.get('category')) : null
  );
  const [vegOnly, setVegOnly] = useState(false);
  const [sortBy, setSortBy] = useState<'id,asc' | 'price,asc' | 'price,desc' | 'rating,desc'>('id,asc');
  const [addedItemMap, setAddedItemMap] = useState<Record<number, boolean>>({});

  const { addToCart } = useCart();
  const { isAuthenticated } = useAuth();

  useEffect(() => {
    api.get<Category[]>('/categories')
      .then(res => {
        if (Array.isArray(res.data)) {
          setCategories(res.data);
        }
      })
      .catch(err => console.warn('Could not fetch categories, using default', err));
  }, []);

  useEffect(() => {
    fetchMenu();
  }, [query, selectedCategory, vegOnly, sortBy]);

  const fetchMenu = async () => {
    try {
      setLoading(true);
      const params: any = {
        size: 30,
        sort: sortBy,
      };
      if (query.trim()) params.query = query.trim();
      if (selectedCategory) params.categoryId = selectedCategory;
      if (vegOnly) params.vegetarian = true;

      const res = await api.get<any>('/menu', { params });
      if (res.data && Array.isArray(res.data.content)) {
        setMenuItems(res.data.content);
      } else if (Array.isArray(res.data)) {
        setMenuItems(res.data);
      } else {
        // Fallback filter
        let items = [...DEFAULT_MENU_ITEMS];
        if (selectedCategory) items = items.filter(i => i.categoryId === selectedCategory);
        if (vegOnly) items = items.filter(i => i.vegetarian);
        if (query.trim()) {
          const q = query.toLowerCase();
          items = items.filter(i => i.name.toLowerCase().includes(q) || (i.description && i.description.toLowerCase().includes(q)));
        }
        setMenuItems(items);
      }
    } catch (err) {
      console.warn('Backend API unavailable, using offline fallback menu', err);
      let items = [...DEFAULT_MENU_ITEMS];
      if (selectedCategory) items = items.filter(i => i.categoryId === selectedCategory);
      if (vegOnly) items = items.filter(i => i.vegetarian);
      if (query.trim()) {
        const q = query.toLowerCase();
        items = items.filter(i => i.name.toLowerCase().includes(q) || (i.description && i.description.toLowerCase().includes(q)));
      }
      setMenuItems(items);
    } finally {
      setLoading(false);
    }
  };

  const handleAddToCart = async (item: MenuItem) => {
    if (!isAuthenticated) {
      window.location.href = '/login';
      return;
    }
    try {
      await addToCart(item.id, 1);
      setAddedItemMap(prev => ({ ...prev, [item.id]: true }));
      setTimeout(() => {
        setAddedItemMap(prev => ({ ...prev, [item.id]: false }));
      }, 1500);
    } catch (err: any) {
      alert(err.message || 'Could not add to cart');
    }
  };

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 space-y-8">
      {/* Header & Search */}
      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4 border-b border-slate-200/80 pb-6">
        <div>
          <h1 className="text-3xl font-black text-slate-900 tracking-tight">Campus Food Menu</h1>
          <p className="text-sm text-slate-500 mt-1">Fresh ingredients cooked to order daily</p>
        </div>

        <div className="flex flex-col sm:flex-row items-stretch sm:items-center gap-3">
          {/* Search bar */}
          <div className="relative min-w-[280px]">
            <Search className="h-4 w-4 text-slate-400 absolute left-3.5 top-1/2 -translate-y-1/2" />
            <input
              type="text"
              value={query}
              onChange={(e) => setQuery(e.target.value)}
              placeholder="Search dosa, biryani, coffee..."
              className="w-full pl-10 pr-4 py-2.5 rounded-xl border border-slate-200 text-sm focus:outline-none focus:ring-2 focus:ring-brand-500/20 focus:border-brand-500 transition"
            />
          </div>

          {/* Sort dropdown */}
          <select
            value={sortBy}
            onChange={(e: any) => setSortBy(e.target.value)}
            className="px-3.5 py-2.5 rounded-xl border border-slate-200 text-sm font-semibold text-slate-700 bg-white focus:outline-none focus:ring-2 focus:ring-brand-500/20"
          >
            <option value="id,asc">Default Sorting</option>
            <option value="price,asc">Price: Low to High</option>
            <option value="price,desc">Price: High to Low</option>
            <option value="rating,desc">Top Rated</option>
          </select>
        </div>
      </div>

      {/* Category Pills & Veg Filter */}
      <div className="flex flex-wrap items-center justify-between gap-4">
        {/* Categories */}
        <div className="flex flex-wrap items-center gap-2">
          <button
            onClick={() => setSelectedCategory(null)}
            className={`px-4 py-2 rounded-xl text-xs font-bold transition shadow-sm ${
              selectedCategory === null
                ? 'bg-slate-900 text-white'
                : 'bg-white border border-slate-200 text-slate-600 hover:bg-slate-50'
            }`}
          >
            All Items
          </button>
          {categories.map((category) => (
            <button
              key={category.id}
              onClick={() => setSelectedCategory(category.id)}
              className={`px-4 py-2 rounded-xl text-xs font-bold transition shadow-sm ${
                selectedCategory === category.id
                  ? 'bg-brand-500 text-white shadow-brand-500/25'
                  : 'bg-white border border-slate-200 text-slate-600 hover:bg-slate-50'
              }`}
            >
              {category.name}
            </button>
          ))}
        </div>

        {/* Veg Only Toggle */}
        <label className="inline-flex items-center space-x-2 cursor-pointer bg-white px-3.5 py-2 rounded-xl border border-slate-200 text-xs font-bold text-slate-700 shadow-sm select-none">
          <input
            type="checkbox"
            checked={vegOnly}
            onChange={(e) => setVegOnly(e.target.checked)}
            className="rounded text-brand-500 focus:ring-brand-500 h-4 w-4"
          />
          <span className="flex items-center gap-1.5">
            <span className="h-3 w-3 rounded-sm border border-emerald-600 flex items-center justify-center p-0.5">
              <span className="h-1.5 w-1.5 rounded-full bg-emerald-600" />
            </span>
            Pure Veg Only
          </span>
        </label>
      </div>

      {/* Food Grid */}
      {loading ? (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
          {[...Array(8)].map((_, i) => (
            <div key={i} className="h-64 rounded-2xl bg-slate-200 animate-pulse" />
          ))}
        </div>
      ) : menuItems.length === 0 ? (
        <div className="text-center py-16 bg-white rounded-3xl border border-slate-100 p-8 space-y-4">
          <div className="h-16 w-16 bg-slate-100 rounded-full flex items-center justify-center mx-auto text-slate-400">
            <UtensilsCrossed className="h-8 w-8" />
          </div>
          <h3 className="text-lg font-bold text-slate-900">No food items found</h3>
          <p className="text-sm text-slate-500 max-w-sm mx-auto">
            Try adjusting your search keywords, clear category filters or disable veg-only filter.
          </p>
          <button
            onClick={() => {
              setQuery('');
              setSelectedCategory(null);
              setVegOnly(false);
            }}
            className="px-4 py-2 bg-slate-900 text-white rounded-xl text-xs font-bold"
          >
            Reset Filters
          </button>
        </div>
      ) : (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
          {menuItems.map((item) => (
            <div
              key={item.id}
              className="rounded-2xl bg-white border border-slate-200/80 overflow-hidden shadow-sm hover:shadow-lg transition flex flex-col justify-between group"
            >
              {/* Dish Image */}
              <div className="h-44 w-full relative overflow-hidden bg-slate-100">
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

                <div className="absolute bottom-2 left-3">
                  <span className="text-[10px] font-black uppercase tracking-wider px-2 py-0.5 rounded-md bg-slate-900/80 text-white backdrop-blur-sm">
                    {item.categoryName}
                  </span>
                </div>
              </div>

              <div className="p-5 flex-1 flex flex-col justify-between">
                <div>
                  <h3 className="font-extrabold text-slate-900 text-base group-hover:text-brand-600 transition">
                    {item.name}
                  </h3>
                  <p className="text-xs text-slate-500 mt-1 line-clamp-2 leading-relaxed">
                    {item.description}
                  </p>
                </div>

                <div className="mt-4 flex items-center justify-between text-xs text-slate-400">
                  <span className="flex items-center gap-1"><Clock className="h-3.5 w-3.5"/> {item.preparationTime || 12} mins</span>
                  {item.stockQuantity !== undefined && item.stockQuantity <= 10 && item.stockQuantity > 0 && (
                    <span className="text-amber-600 font-bold flex items-center gap-0.5">
                      <AlertTriangle className="h-3.5 w-3.5"/> Only {item.stockQuantity} left
                    </span>
                  )}
                </div>
              </div>

              <div className="px-5 py-3.5 bg-slate-50/70 border-t border-slate-100 flex items-center justify-between">
                <div>
                  <span className="text-[10px] font-bold text-slate-400 uppercase">Price</span>
                  <p className="text-lg font-black text-slate-900">₹{item.price}</p>
                </div>

                <button
                  onClick={() => handleAddToCart(item)}
                  disabled={!item.available || (item.stockQuantity !== undefined && item.stockQuantity <= 0)}
                  className={`inline-flex items-center space-x-1.5 px-3.5 py-2 rounded-xl font-bold text-xs shadow-sm transition active:scale-95 ${
                    !item.available || (item.stockQuantity !== undefined && item.stockQuantity <= 0)
                      ? 'bg-slate-200 text-slate-400 cursor-not-allowed'
                      : addedItemMap[item.id]
                      ? 'bg-emerald-600 text-white'
                      : 'bg-brand-500 hover:bg-brand-600 text-white shadow-brand-500/20'
                  }`}
                >
                  {addedItemMap[item.id] ? (
                    <>
                      <Check className="h-3.5 w-3.5" />
                      <span>Added</span>
                    </>
                  ) : (
                    <>
                      <Plus className="h-3.5 w-3.5" />
                      <span>Add</span>
                    </>
                  )}
                </button>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};
