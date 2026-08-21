import React, { useState, useEffect } from 'react';
import api from '../../services/api';
import { Coupon } from '../../types';
import { Modal } from '../../components/ui/Modal';
import { TicketPercent, Plus, Edit3, Trash2, Calendar, RefreshCw } from 'lucide-react';

import { DEFAULT_COUPONS } from '../../services/mockData';

export const AdminCouponsPage: React.FC = () => {
  const [coupons, setCoupons] = useState<Coupon[]>(DEFAULT_COUPONS);
  const [loading, setLoading] = useState(false);

  // Modal state
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingCoupon, setEditingCoupon] = useState<Coupon | null>(null);
  const [formData, setFormData] = useState({
    code: '',
    description: '',
    discountPercent: 10,
    maxDiscount: 50,
    minOrderAmount: 100,
    expiryDate: '2027-12-31',
    usageLimit: 500,
    active: true,
  });

  const fetchCoupons = async () => {
    try {
      setLoading(true);
      const res = await api.get<any>('/admin/coupons');
      if (Array.isArray(res.data) && res.data.length > 0) {
        setCoupons(res.data);
      } else {
        setCoupons(DEFAULT_COUPONS);
      }
    } catch (err) {
      console.warn('Backend coupons offline, using default coupon data', err);
      setCoupons(DEFAULT_COUPONS);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCoupons();
  }, []);

  const handleOpenAdd = () => {
    setEditingCoupon(null);
    setFormData({
      code: '',
      description: '',
      discountPercent: 10,
      maxDiscount: 50,
      minOrderAmount: 100,
      expiryDate: '2027-12-31',
      usageLimit: 500,
      active: true,
    });
    setIsModalOpen(true);
  };

  const handleToggleStatus = async (id: number) => {
    try {
      await api.patch(`/admin/coupons/${id}/status`);
      fetchCoupons();
    } catch (err) {
      alert('Failed to toggle coupon status');
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      if (editingCoupon) {
        await api.put(`/admin/coupons/${editingCoupon.id}`, formData);
      } else {
        await api.post('/admin/coupons', formData);
      }
      setIsModalOpen(false);
      fetchCoupons();
    } catch (err: any) {
      alert(err.response?.data?.message || 'Failed to save coupon');
    }
  };

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl sm:text-3xl font-black text-slate-900 tracking-tight">
            Coupons & Campus Offers
          </h1>
          <p className="text-sm text-slate-500 mt-0.5">
            Create discount promotion codes, set usage caps and track redemptions
          </p>
        </div>

        <button
          onClick={handleOpenAdd}
          className="inline-flex items-center space-x-2 px-5 py-2.5 rounded-xl bg-gradient-to-r from-brand-500 to-amber-500 text-white font-bold text-xs shadow-md"
        >
          <Plus className="h-4 w-4" />
          <span>Create New Coupon</span>
        </button>
      </div>

      {/* Coupons Grid */}
      {loading ? (
        <div className="p-12 text-center">
          <div className="h-8 w-8 border-4 border-brand-500 border-t-transparent rounded-full animate-spin mx-auto mb-4" />
          <p className="text-sm font-semibold text-slate-500">Loading coupons...</p>
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {(Array.isArray(coupons) ? coupons : []).map((c) => (
            <div
              key={c.id}
              className="p-6 rounded-3xl bg-white border border-slate-200/80 shadow-sm hover:shadow-md transition flex flex-col justify-between space-y-4"
            >
              <div className="space-y-3">
                <div className="flex items-center justify-between">
                  <span className="px-3 py-1 rounded-xl bg-brand-50 border border-brand-200 text-brand-700 font-extrabold text-sm tracking-wider">
                    {c.code}
                  </span>
                  <span
                    className={`text-xs font-bold px-2.5 py-0.5 rounded-full ${
                      c.active
                        ? 'bg-emerald-50 text-emerald-700 border border-emerald-200'
                        : 'bg-rose-50 text-rose-700 border border-rose-200'
                    }`}
                  >
                    {c.active ? 'Active' : 'Disabled'}
                  </span>
                </div>

                <div>
                  <h3 className="font-extrabold text-slate-900 text-lg">
                    {c.discountPercent}% OFF
                    {c.maxDiscount && <span className="text-xs font-normal text-slate-400"> (Up to ₹{c.maxDiscount})</span>}
                  </h3>
                  <p className="text-xs text-slate-500 mt-1">{c.description || 'Valid on all orders'}</p>
                </div>

                <div className="pt-2 border-t border-slate-100 text-xs text-slate-400 space-y-1">
                  <p>Min. Order: ₹{c.minOrderAmount || 0}</p>
                  <p>Expires on: {c.expiryDate}</p>
                  <p>Usage: {c.usedCount || 0} / {c.usageLimit ? c.usageLimit : '∞'} redeemed</p>
                </div>
              </div>

              <div className="pt-3 border-t border-slate-100 flex justify-end gap-2">
                <button
                  onClick={() => handleToggleStatus(c.id)}
                  className={`px-3 py-1.5 rounded-xl text-xs font-bold border transition ${
                    c.active
                      ? 'border-rose-200 text-rose-600 hover:bg-rose-50'
                      : 'border-emerald-200 text-emerald-600 hover:bg-emerald-50'
                  }`}
                >
                  {c.active ? 'Disable' : 'Enable'}
                </button>
              </div>
            </div>
          ))}
        </div>
      )}

      {/* Create Coupon Modal */}
      <Modal
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        title="Create Promotional Coupon"
      >
        <form className="space-y-4" onSubmit={handleSubmit}>
          <div>
            <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1">
              Coupon Code
            </label>
            <input
              type="text"
              required
              value={formData.code}
              onChange={(e) => setFormData({ ...formData, code: e.target.value.toUpperCase() })}
              placeholder="e.g. FESTIVE25"
              className="w-full px-4 py-2.5 rounded-xl border border-slate-200 text-sm font-bold uppercase tracking-wider focus:outline-none focus:ring-2 focus:ring-brand-500/20"
            />
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1">
                Discount (%)
              </label>
              <input
                type="number"
                required
                min={1}
                max={100}
                value={formData.discountPercent}
                onChange={(e) => setFormData({ ...formData, discountPercent: Number(e.target.value) })}
                className="w-full px-4 py-2.5 rounded-xl border border-slate-200 text-sm focus:outline-none focus:ring-2 focus:ring-brand-500/20"
              />
            </div>

            <div>
              <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1">
                Max Discount (₹)
              </label>
              <input
                type="number"
                value={formData.maxDiscount}
                onChange={(e) => setFormData({ ...formData, maxDiscount: Number(e.target.value) })}
                className="w-full px-4 py-2.5 rounded-xl border border-slate-200 text-sm focus:outline-none focus:ring-2 focus:ring-brand-500/20"
              />
            </div>
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1">
                Min. Order Amount (₹)
              </label>
              <input
                type="number"
                value={formData.minOrderAmount}
                onChange={(e) => setFormData({ ...formData, minOrderAmount: Number(e.target.value) })}
                className="w-full px-4 py-2.5 rounded-xl border border-slate-200 text-sm focus:outline-none focus:ring-2 focus:ring-brand-500/20"
              />
            </div>

            <div>
              <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1">
                Expiry Date
              </label>
              <input
                type="date"
                required
                value={formData.expiryDate}
                onChange={(e) => setFormData({ ...formData, expiryDate: e.target.value })}
                className="w-full px-4 py-2.5 rounded-xl border border-slate-200 text-sm focus:outline-none focus:ring-2 focus:ring-brand-500/20"
              />
            </div>
          </div>

          <div>
            <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1">
              Description / Terms
            </label>
            <input
              type="text"
              value={formData.description}
              onChange={(e) => setFormData({ ...formData, description: e.target.value })}
              placeholder="e.g. 25% off on orders above ₹150"
              className="w-full px-4 py-2.5 rounded-xl border border-slate-200 text-sm focus:outline-none focus:ring-2 focus:ring-brand-500/20"
            />
          </div>

          <div className="pt-4 border-t border-slate-100 flex justify-end space-x-3">
            <button
              type="button"
              onClick={() => setIsModalOpen(false)}
              className="px-4 py-2 rounded-xl border border-slate-200 text-slate-600 font-bold text-xs hover:bg-slate-50"
            >
              Cancel
            </button>
            <button
              type="submit"
              className="px-6 py-2 rounded-xl bg-gradient-to-r from-brand-500 to-amber-500 text-white font-bold text-xs shadow-md"
            >
              Create Coupon
            </button>
          </div>
        </form>
      </Modal>
    </div>
  );
};
