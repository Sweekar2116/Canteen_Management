import React, { useState, useEffect } from 'react';
import api from '../../services/api';
import { Order, OrderStatus } from '../../types';
import { OrderStatusBadge } from '../../components/ui/Badge';
import { Modal } from '../../components/ui/Modal';
import { 
  ShoppingBag, 
  Search, 
  Filter, 
  Clock, 
  CheckCircle2, 
  ChefHat, 
  PackageCheck, 
  CheckCheck, 
  XCircle, 
  Eye, 
  RefreshCw 
} from 'lucide-react';

export const AdminOrdersPage: React.FC = () => {
  const [orders, setOrders] = useState<Order[]>([]);
  const [loading, setLoading] = useState(true);
  const [selectedStatus, setSelectedStatus] = useState<string>('ALL');
  const [selectedOrder, setSelectedOrder] = useState<Order | null>(null);
  const [updatingId, setUpdatingId] = useState<number | null>(null);

  const fetchOrders = async () => {
    try {
      setLoading(true);
      const params: any = { size: 50 };
      if (selectedStatus !== 'ALL') {
        params.status = selectedStatus;
      }
      const res = await api.get<any>('/admin/orders', { params });
      if (res.data && Array.isArray(res.data.content)) {
        setOrders(res.data.content);
      } else if (Array.isArray(res.data)) {
        setOrders(res.data);
      } else {
        setOrders([]);
      }
    } catch (err) {
      console.error('Failed to load admin orders:', err);
      setOrders([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchOrders();
  }, [selectedStatus]);

  const handleUpdateStatus = async (orderId: number, newStatus: OrderStatus) => {
    setUpdatingId(orderId);
    try {
      await api.put<Order>(`/admin/orders/${orderId}/status`, { status: newStatus });
      fetchOrders();
      if (selectedOrder && selectedOrder.id === orderId) {
        setSelectedOrder((prev) => (prev ? { ...prev, status: newStatus } : null));
      }
    } catch (err: any) {
      alert(err.response?.data?.message || 'Failed to update order status');
    } finally {
      setUpdatingId(null);
    }
  };

  const getNextAction = (order: Order) => {
    switch (order.status) {
      case 'PLACED':
        return {
          label: 'Accept & Confirm',
          status: 'CONFIRMED' as OrderStatus,
          color: 'bg-brand-500 hover:bg-brand-600 text-white',
          icon: CheckCircle2,
        };
      case 'CONFIRMED':
        return {
          label: 'Start Cooking',
          status: 'PREPARING' as OrderStatus,
          color: 'bg-amber-500 hover:bg-amber-600 text-white',
          icon: ChefHat,
        };
      case 'PREPARING':
        return {
          label: 'Mark Ready',
          status: 'READY_FOR_PICKUP' as OrderStatus,
          color: 'bg-emerald-500 hover:bg-emerald-600 text-white',
          icon: PackageCheck,
        };
      case 'READY_FOR_PICKUP':
        return {
          label: 'Complete Delivery',
          status: 'COMPLETED' as OrderStatus,
          color: 'bg-slate-900 hover:bg-slate-800 text-white',
          icon: CheckCheck,
        };
      default:
        return null;
    }
  };

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl sm:text-3xl font-black text-slate-900 tracking-tight">
            Order Processing Pipeline
          </h1>
          <p className="text-sm text-slate-500 mt-0.5">
            Accept incoming orders, update kitchen preparation stages and trigger customer alerts
          </p>
        </div>

        <button
          onClick={fetchOrders}
          className="inline-flex items-center space-x-2 px-4 py-2.5 rounded-xl border border-slate-200 bg-white text-slate-700 text-xs font-bold shadow-sm hover:bg-slate-50 transition"
        >
          <RefreshCw className="h-4 w-4" />
          <span>Refresh Orders</span>
        </button>
      </div>

      {/* Filter Tabs */}
      <div className="flex flex-wrap items-center gap-2 border-b border-slate-200/80 pb-4">
        {[
          { id: 'ALL', label: 'All Orders' },
          { id: 'PLACED', label: 'New Placed' },
          { id: 'CONFIRMED', label: 'Confirmed' },
          { id: 'PREPARING', label: 'Cooking' },
          { id: 'READY_FOR_PICKUP', label: 'Ready for Counter' },
          { id: 'COMPLETED', label: 'Completed' },
          { id: 'CANCELLED', label: 'Cancelled' },
        ].map((tab) => (
          <button
            key={tab.id}
            onClick={() => setSelectedStatus(tab.id)}
            className={`px-4 py-2 rounded-xl text-xs font-bold transition ${
              selectedStatus === tab.id
                ? 'bg-slate-900 text-white shadow-sm'
                : 'bg-white border border-slate-200 text-slate-600 hover:bg-slate-50'
            }`}
          >
            {tab.label}
          </button>
        ))}
      </div>

      {/* Orders Table */}
      {loading ? (
        <div className="p-12 text-center">
          <div className="h-8 w-8 border-4 border-brand-500 border-t-transparent rounded-full animate-spin mx-auto mb-4" />
          <p className="text-sm font-semibold text-slate-500">Loading orders...</p>
        </div>
      ) : orders.length === 0 ? (
        <div className="p-16 text-center bg-white rounded-3xl border border-slate-100 space-y-2">
          <ShoppingBag className="h-10 w-10 text-slate-300 mx-auto" />
          <h3 className="text-base font-bold text-slate-700">No orders found</h3>
          <p className="text-xs text-slate-400">There are no orders matching the selected status filter.</p>
        </div>
      ) : (
        <div className="bg-white rounded-3xl border border-slate-200/80 shadow-sm overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full text-left text-sm">
              <thead className="bg-slate-50/80 text-xs font-bold text-slate-500 uppercase tracking-wider border-b border-slate-100">
                <tr>
                  <th className="px-6 py-4">Order Details</th>
                  <th className="px-6 py-4">Customer</th>
                  <th className="px-6 py-4">Dishes</th>
                  <th className="px-6 py-4">Amount & Payment</th>
                  <th className="px-6 py-4">Status</th>
                  <th className="px-6 py-4 text-right">Actions</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100">
                {orders.map((order) => {
                  const nextAction = getNextAction(order);
                  const isUpdating = updatingId === order.id;

                  return (
                    <tr key={order.id} className="hover:bg-slate-50/60 transition">
                      <td className="px-6 py-4">
                        <p className="font-extrabold text-slate-900">{order.orderNumber}</p>
                        <p className="text-xs text-slate-400">
                          {new Date(order.createdAt).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}
                        </p>
                      </td>

                      <td className="px-6 py-4">
                        <p className="font-bold text-slate-900">{order.userName}</p>
                        <p className="text-xs text-slate-400">{order.userPhone || order.userEmail}</p>
                      </td>

                      <td className="px-6 py-4">
                        <p className="font-medium text-slate-700 line-clamp-1 max-w-[200px]">
                          {order.items.map((i) => `${i.quantity}x ${i.itemName}`).join(', ')}
                        </p>
                        <span className="text-xs text-slate-400">{order.items.length} items</span>
                      </td>

                      <td className="px-6 py-4">
                        <p className="font-black text-slate-900">₹{order.finalAmount}</p>
                        <span className="text-[11px] text-slate-500 font-medium">
                          {order.paymentMethod?.replace('_', ' ')} • {order.paymentStatus}
                        </span>
                      </td>

                      <td className="px-6 py-4">
                        <OrderStatusBadge status={order.status} />
                      </td>

                      <td className="px-6 py-4 text-right space-x-2">
                        <button
                          onClick={() => setSelectedOrder(order)}
                          className="p-2 rounded-xl border border-slate-200 text-slate-600 hover:bg-slate-100 transition inline-flex items-center"
                          title="View Details"
                        >
                          <Eye className="h-4 w-4" />
                        </button>

                        {nextAction && (
                          <button
                            onClick={() => handleUpdateStatus(order.id, nextAction.status)}
                            disabled={isUpdating}
                            className={`px-3.5 py-1.5 rounded-xl text-xs font-bold shadow-sm transition disabled:opacity-50 inline-flex items-center space-x-1.5 ${nextAction.color}`}
                          >
                            <nextAction.icon className="h-3.5 w-3.5" />
                            <span>{isUpdating ? 'Updating...' : nextAction.label}</span>
                          </button>
                        )}
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        </div>
      )}

      {/* Order Details Modal */}
      <Modal
        isOpen={!!selectedOrder}
        onClose={() => setSelectedOrder(null)}
        title={`Order Details • ${selectedOrder?.orderNumber}`}
        maxWidth="lg"
      >
        {selectedOrder && (
          <div className="space-y-6">
            <div className="flex items-center justify-between p-4 rounded-2xl bg-slate-50 border border-slate-100">
              <div>
                <p className="text-xs text-slate-400 font-semibold">Customer</p>
                <h4 className="font-bold text-slate-900">{selectedOrder.userName} ({selectedOrder.userEmail})</h4>
                <p className="text-xs text-slate-500">{selectedOrder.userPhone}</p>
              </div>
              <OrderStatusBadge status={selectedOrder.status} />
            </div>

            <div className="space-y-2">
              <h5 className="text-xs font-bold text-slate-400 uppercase tracking-wider">Ordered Items</h5>
              <div className="divide-y divide-slate-100 border border-slate-100 rounded-2xl p-4">
                {selectedOrder.items.map((item) => (
                  <div key={item.id} className="py-2 flex justify-between text-sm">
                    <span className="font-medium text-slate-800">{item.quantity}x {item.itemName}</span>
                    <span className="font-bold text-slate-900">₹{item.totalPrice}</span>
                  </div>
                ))}
              </div>
            </div>

            {selectedOrder.notes && (
              <div className="p-3 rounded-xl bg-amber-50 border border-amber-200 text-xs text-amber-900 font-medium">
                <span className="font-bold">Special Note:</span> {selectedOrder.notes}
              </div>
            )}

            <div className="flex justify-between items-center text-sm font-black border-t border-slate-100 pt-4">
              <span>Final Total Paid</span>
              <span className="text-xl text-brand-600">₹{selectedOrder.finalAmount}</span>
            </div>

            {/* Quick Status Buttons in Modal */}
            <div className="flex flex-wrap gap-2 pt-2 border-t border-slate-100">
              {(['PLACED', 'CONFIRMED', 'PREPARING', 'READY_FOR_PICKUP', 'COMPLETED', 'CANCELLED'] as OrderStatus[]).map((st) => (
                <button
                  key={st}
                  onClick={() => handleUpdateStatus(selectedOrder.id, st)}
                  className={`px-3 py-1.5 rounded-xl text-xs font-bold border transition ${
                    selectedOrder.status === st
                      ? 'bg-slate-900 text-white border-slate-900'
                      : 'border-slate-200 hover:bg-slate-50 text-slate-700'
                  }`}
                >
                  Set {st.replace('_', ' ')}
                </button>
              ))}
            </div>
          </div>
        )}
      </Modal>
    </div>
  );
};
