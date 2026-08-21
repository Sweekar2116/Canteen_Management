import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import api from '../services/api';
import { Order } from '../types';
import { OrderStatusBadge } from '../components/ui/Badge';
import { ShoppingBag, ArrowRight, Clock, Calendar, UtensilsCrossed } from 'lucide-react';

export const OrderHistoryPage: React.FC = () => {
  const [orders, setOrders] = useState<Order[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.get<Order[]>('/orders')
      .then(res => {
        if (Array.isArray(res.data)) {
          setOrders(res.data);
        } else {
          setOrders([]);
        }
      })
      .catch(err => {
        console.error('Failed to load orders', err);
        setOrders([]);
      })
      .finally(() => setLoading(false));
  }, []);

  return (
    <div className="max-w-5xl mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8">
      <div className="border-b border-slate-200/80 pb-6">
        <h1 className="text-3xl font-black text-slate-900 tracking-tight">My Order History</h1>
        <p className="text-sm text-slate-500 mt-1">Track current orders and view past meal receipts</p>
      </div>

      {loading ? (
        <div className="space-y-4">
          {[...Array(4)].map((_, i) => (
            <div key={i} className="h-32 rounded-2xl bg-slate-200 animate-pulse" />
          ))}
        </div>
      ) : !Array.isArray(orders) || orders.length === 0 ? (
        <div className="text-center py-20 bg-white rounded-3xl border border-slate-100 p-8 space-y-4 max-w-md mx-auto">
          <div className="h-16 w-16 bg-brand-50 rounded-full flex items-center justify-center mx-auto text-brand-500">
            <ShoppingBag className="h-8 w-8" />
          </div>
          <h3 className="text-lg font-bold text-slate-900">No Orders Yet</h3>
          <p className="text-sm text-slate-500">
            You haven't placed any food orders. Discover tasty canteen dishes and place your first order!
          </p>
          <Link
            to="/menu"
            className="inline-flex items-center space-x-2 px-6 py-3 rounded-xl bg-brand-500 text-white font-bold text-xs shadow-md"
          >
            <span>Explore Menu</span>
            <ArrowRight className="h-4 w-4" />
          </Link>
        </div>
      ) : (
        <div className="space-y-4">
          {(Array.isArray(orders) ? orders : []).map((order) => {
            const orderItems = Array.isArray(order?.items) ? order.items : [];
            return (
              <div
                key={order.id}
                className="p-6 rounded-3xl bg-white border border-slate-200/80 shadow-sm hover:shadow-md transition flex flex-col sm:flex-row sm:items-center justify-between gap-6"
              >
                <div className="space-y-2">
                  <div className="flex items-center gap-3">
                    <span className="font-extrabold text-slate-900 text-base">
                      Order #{order.orderNumber}
                    </span>
                    <OrderStatusBadge status={order.status} />
                  </div>

                  <div className="flex flex-wrap items-center gap-4 text-xs text-slate-400">
                    <span className="flex items-center gap-1">
                      <Calendar className="h-3.5 w-3.5" />
                      {new Date(order.createdAt).toLocaleDateString()} at {new Date(order.createdAt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                    </span>
                    <span>•</span>
                    <span>{orderItems.length} items ({orderItems.map(i => i.itemName).join(', ')})</span>
                  </div>
                </div>

                <div className="flex items-center justify-between sm:justify-end gap-6 border-t sm:border-t-0 pt-3 sm:pt-0 border-slate-100">
                  <div className="text-left sm:text-right">
                    <span className="text-[10px] font-bold text-slate-400 uppercase">Total Amount</span>
                    <p className="text-xl font-black text-brand-600">₹{order.finalAmount}</p>
                  </div>

                  <Link
                    to={`/orders/${order.id}`}
                    className="px-4 py-2.5 rounded-xl border border-slate-200 text-xs font-bold text-slate-700 hover:bg-slate-50 transition flex items-center space-x-1.5 shadow-sm"
                  >
                    <span>Track / View</span>
                    <ArrowRight className="h-3.5 w-3.5" />
                  </Link>
                </div>
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
};
