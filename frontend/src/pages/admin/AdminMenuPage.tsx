import React, { useState, useEffect } from 'react';
import api from '../../services/api';
import { MenuItem, Category } from '../../types';
import { Modal } from '../../components/ui/Modal';
import { 
  Plus, 
  Search, 
  Edit3, 
  Trash2, 
  ToggleLeft, 
  ToggleRight, 
  UtensilsCrossed, 
  Star, 
  Clock, 
  CheckCircle2, 
  AlertCircle 
} from 'lucide-react';

export const AdminMenuPage: React.FC = () => {
  const [items, setItems] = useState<MenuItem[]>([]);
  const [categories, setCategories] = useState<Category[]>([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState('');

  // Modal State
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingItem, setEditingItem] = useState<MenuItem | null>(null);
  const [formData, setFormData] = useState({
    name: '',
    description: '',
    price: 0,
    categoryId: 1,
    vegetarian: true,
    available: true,
    preparationTime: 15,
  });

  const fetchData = async () => {
    try {
      setLoading(true);
      const [menuRes, catRes] = await Promise.all([
        api.get<any>('/admin/menu', { params: { size: 50, query: searchQuery } }),
        api.get<any>('/categories'),
      ]);
      if (menuRes.data && Array.isArray(menuRes.data.content)) {
        setItems(menuRes.data.content);
      } else if (Array.isArray(menuRes.data)) {
        setItems(menuRes.data);
      } else {
        setItems([]);
      }
      if (Array.isArray(catRes.data)) {
        setCategories(catRes.data);
      } else {
        setCategories([]);
      }
    } catch (err) {
      console.error('Failed to load menu items:', err);
      setItems([]);
      setCategories([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, [searchQuery]);

  const handleOpenAdd = () => {
    setEditingItem(null);
    setFormData({
      name: '',
      description: '',
      price: 50,
      categoryId: categories[0]?.id || 1,
      vegetarian: true,
      available: true,
      preparationTime: 15,
    });
    setIsModalOpen(true);
  };

  const handleOpenEdit = (item: MenuItem) => {
    setEditingItem(item);
    setFormData({
      name: item.name,
      description: item.description || '',
      price: item.price,
      categoryId: item.categoryId,
      vegetarian: item.vegetarian,
      available: item.available,
      preparationTime: item.preparationTime || 15,
    });
    setIsModalOpen(true);
  };

  const handleToggleAvailability = async (id: number) => {
    try {
      const res = await api.patch<MenuItem>(`/admin/menu/${id}/availability`);
      setItems((prev) => prev.map((item) => (item.id === id ? res.data : item)));
    } catch (err) {
      alert('Failed to toggle availability');
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      if (editingItem) {
        await api.put(`/admin/menu/${editingItem.id}`, formData);
      } else {
        await api.post('/admin/menu', formData);
      }
      setIsModalOpen(false);
      fetchData();
    } catch (err: any) {
      alert(err.response?.data?.message || 'Failed to save menu item');
    }
  };

  const handleDelete = async (id: number) => {
    if (!window.confirm('Are you sure you want to deactivate this item?')) return;
    try {
      await api.delete(`/admin/menu/${id}`);
      fetchData();
    } catch (err) {
      alert('Failed to delete item');
    }
  };

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl sm:text-3xl font-black text-slate-900 tracking-tight">
            Menu Management
          </h1>
          <p className="text-sm text-slate-500 mt-0.5">
            Add new food items, update pricing, assign categories and toggle stock availability
          </p>
        </div>

        <button
          onClick={handleOpenAdd}
          className="inline-flex items-center space-x-2 px-5 py-2.5 rounded-xl bg-gradient-to-r from-brand-500 to-amber-500 text-white font-bold text-xs shadow-md shadow-brand-500/20 hover:shadow-brand-500/30 transition active:scale-95"
        >
          <Plus className="h-4 w-4" />
          <span>Add New Dish</span>
        </button>
      </div>

      {/* Search Filter */}
      <div className="flex items-center gap-3">
        <div className="relative max-w-md w-full">
          <Search className="h-4 w-4 text-slate-400 absolute left-3.5 top-1/2 -translate-y-1/2" />
          <input
            type="text"
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            placeholder="Search items by name..."
            className="w-full pl-10 pr-4 py-2.5 rounded-xl border border-slate-200 text-sm focus:outline-none focus:ring-2 focus:ring-brand-500/20"
          />
        </div>
      </div>

      {/* Menu Items Table */}
      {loading ? (
        <div className="p-12 text-center">
          <div className="h-8 w-8 border-4 border-brand-500 border-t-transparent rounded-full animate-spin mx-auto mb-4" />
          <p className="text-sm font-semibold text-slate-500">Loading dishes...</p>
        </div>
      ) : (
        <div className="bg-white rounded-3xl border border-slate-200/80 shadow-sm overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full text-left text-sm">
              <thead className="bg-slate-50/80 text-xs font-bold text-slate-500 uppercase tracking-wider border-b border-slate-100">
                <tr>
                  <th className="px-6 py-4">Item Name</th>
                  <th className="px-6 py-4">Category</th>
                  <th className="px-6 py-4">Price</th>
                  <th className="px-6 py-4">Type</th>
                  <th className="px-6 py-4">Stock Units</th>
                  <th className="px-6 py-4">Available</th>
                  <th className="px-6 py-4 text-right">Actions</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100">
                {(Array.isArray(items) ? items : []).map((item) => (
                  <tr key={item.id} className="hover:bg-slate-50/60 transition">
                    <td className="px-6 py-4">
                      <div className="flex items-center space-x-3">
                        <img
                          src={item.imageUrl || 'https://images.unsplash.com/photo-1546833999-b9f581a1996d?auto=format&fit=crop&w=150&q=80'}
                          alt={item.name}
                          className="h-10 w-10 rounded-xl object-cover bg-slate-100 shrink-0 border border-slate-100"
                        />
                        <div>
                          <p className="font-extrabold text-slate-900">{item.name}</p>
                          <p className="text-xs text-slate-400 line-clamp-1 max-w-xs">{item.description}</p>
                        </div>
                      </div>
                    </td>

                    <td className="px-6 py-4">
                      <span className="px-2.5 py-1 rounded-lg bg-slate-100 text-slate-700 font-bold text-xs">
                        {item.categoryName}
                      </span>
                    </td>

                    <td className="px-6 py-4 font-black text-slate-900">
                      ₹{item.price}
                    </td>

                    <td className="px-6 py-4">
                      <span
                        className={`inline-flex items-center gap-1.5 px-2.5 py-0.5 rounded-full text-xs font-bold ${
                          item.vegetarian
                            ? 'bg-emerald-50 text-emerald-700 border border-emerald-200'
                            : 'bg-rose-50 text-rose-700 border border-rose-200'
                        }`}
                      >
                        {item.vegetarian ? 'Pure Veg' : 'Non-Veg'}
                      </span>
                    </td>

                    <td className="px-6 py-4 font-semibold text-slate-700">
                      {item.stockQuantity !== undefined ? item.stockQuantity : '-'} units
                    </td>

                    <td className="px-6 py-4">
                      <button
                        onClick={() => handleToggleAvailability(item.id)}
                        className={`inline-flex items-center gap-1 text-xs font-bold px-3 py-1 rounded-xl transition ${
                          item.available
                            ? 'bg-emerald-50 text-emerald-700 border border-emerald-200 hover:bg-emerald-100'
                            : 'bg-rose-50 text-rose-700 border border-rose-200 hover:bg-rose-100'
                        }`}
                      >
                        {item.available ? 'Active (Live)' : 'Disabled'}
                      </button>
                    </td>

                    <td className="px-6 py-4 text-right space-x-2">
                      <button
                        onClick={() => handleOpenEdit(item)}
                        className="p-2 rounded-xl border border-slate-200 text-slate-600 hover:bg-slate-100 transition"
                        title="Edit Item"
                      >
                        <Edit3 className="h-4 w-4" />
                      </button>
                      <button
                        onClick={() => handleDelete(item.id)}
                        className="p-2 rounded-xl border border-rose-200 text-rose-600 hover:bg-rose-50 transition"
                        title="Deactivate"
                      >
                        <Trash2 className="h-4 w-4" />
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}

      {/* Add/Edit Modal */}
      <Modal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        title={editingItem ? `Edit Dish • ${editingItem.name}` : 'Add New Food Item'}
      >
        <form className="space-y-4" onSubmit={handleSubmit}>
          <div>
            <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1">
              Dish Name
            </label>
            <input
              type="text"
              required
              value={formData.name}
              onChange={(e) => setFormData({ ...formData, name: e.target.value })}
              placeholder="e.g. Paneer Butter Masala"
              className="w-full px-4 py-2.5 rounded-xl border border-slate-200 text-sm focus:outline-none focus:ring-2 focus:ring-brand-500/20"
            />
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1">
                Price (₹)
              </label>
              <input
                type="number"
                required
                min={1}
                step="0.5"
                value={formData.price}
                onChange={(e) => setFormData({ ...formData, price: Number(e.target.value) })}
                className="w-full px-4 py-2.5 rounded-xl border border-slate-200 text-sm focus:outline-none focus:ring-2 focus:ring-brand-500/20"
              />
            </div>

            <div>
              <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1">
                Category
              </label>
              <select
                value={formData.categoryId}
                onChange={(e) => setFormData({ ...formData, categoryId: Number(e.target.value) })}
                className="w-full px-4 py-2.5 rounded-xl border border-slate-200 text-sm font-semibold focus:outline-none focus:ring-2 focus:ring-brand-500/20 bg-white"
              >
                {(Array.isArray(categories) ? categories : []).map((cat) => (
                  <option key={cat.id} value={cat.id}>{cat.name}</option>
                ))}
              </select>
            </div>
          </div>

          <div>
            <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1">
              Description
            </label>
            <textarea
              rows={3}
              value={formData.description}
              onChange={(e) => setFormData({ ...formData, description: e.target.value })}
              placeholder="Ingredients, preparation and serving style..."
              className="w-full px-4 py-2.5 rounded-xl border border-slate-200 text-sm focus:outline-none focus:ring-2 focus:ring-brand-500/20"
            />
          </div>

          <div className="flex items-center space-x-6 pt-2">
            <label className="inline-flex items-center space-x-2 cursor-pointer text-xs font-bold text-slate-700">
              <input
                type="checkbox"
                checked={formData.vegetarian}
                onChange={(e) => setFormData({ ...formData, vegetarian: e.target.checked })}
                className="rounded text-brand-500 h-4 w-4"
              />
              <span>Vegetarian Dish</span>
            </label>

            <label className="inline-flex items-center space-x-2 cursor-pointer text-xs font-bold text-slate-700">
              <input
                type="checkbox"
                checked={formData.available}
                onChange={(e) => setFormData({ ...formData, available: e.target.checked })}
                className="rounded text-brand-500 h-4 w-4"
              />
              <span>Available for Ordering</span>
            </label>
          </div>

          <div className="pt-4 border-t border-slate-100 flex justify-end space-x-3">
            <button
              type="button"
              onClick={() => setIsModalOpen(false)}
              className="px-4 py-2.5 rounded-xl border border-slate-200 text-slate-600 font-bold text-xs hover:bg-slate-50"
            >
              Cancel
            </button>
            <button
              type="submit"
              className="px-6 py-2.5 rounded-xl bg-gradient-to-r from-brand-500 to-amber-500 text-white font-bold text-xs shadow-md"
            >
              {editingItem ? 'Save Updates' : 'Add Item'}
            </button>
          </div>
        </form>
      </Modal>
    </div>
  );
};
