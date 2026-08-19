import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import api from '../services/api';
import { Order, OrderStatus } from '../types';
import { OrderStatusBadge } from '../components/ui/Badge';
import { 
  CheckCircle2, 
  Clock, 
  ChefHat, 
  PackageCheck, 
  CheckCheck, 
  XCircle, 
  ArrowLeft, 
  RefreshCw,
  AlertCircle,
  ShieldCheck,
  Play,
  QrCode
} from 'lucide-react';

export const OrderTrackingPage: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const [order, setOrder] = useState<Order | null>(null);
  const [loading, setLoading] = useState(true);
  const [cancelling, setCancelling] = useState(false);
  const [advancing, setAdvancing] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchOrder = async () => {
    try {
      setLoading(true);
      const res = await api.get<Order>(`/orders/${id}`);
      setOrder(res.data);
    } catch (err: any) {
      setError(err.response?.data?.message || 'Order not found');
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchOrder();
    const interval = setInterval(fetchOrder, 6000); // Polling every 6s
    return () => clearInterval(interval);
  }, [id]);

  const handleCancel = async () => {
    if (!order || !window.confirm('Are you sure you want to cancel this order?')) return;
    setCancelling(true);
    try {
      const res = await api.post<Order>(`/orders/${order.id}/cancel`);
      setOrder(res.data);
    } catch (err: any) {
      alert(err.response?.data?.message || 'Cannot cancel order');
    } finally {
      setCancelling(false);
    }
  };

  // 1-Click Interactive Demo Stage Advancer for Interviews/Testing
  const handleSimulateNextStage = async () => {
    if (!order) return;
    let nextStatus: OrderStatus | null = null;
    if (order.status === 'PLACED') nextStatus = 'CONFIRMED';
    else if (order.status === 'CONFIRMED') nextStatus = 'PREPARING';
    else if (order.status === 'PREPARING') nextStatus = 'READY_FOR_PICKUP';
    else if (order.status === 'READY_FOR_PICKUP') nextStatus = 'COMPLETED';

    if (!nextStatus) return;

    setAdvancing(true);
    try {
      const res = await api.put<Order>(`/admin/orders/${order.id}/status`, { status: nextStatus });
      setOrder(res.data);
    } catch (err: any) {
      // Fallback if role is not admin: retry fetch
      fetchOrder();
    } finally {
      setAdvancing(false);
    }
  };

  if (loading && !order) {
    return (
      <div className="max-w-3xl mx-auto px-4 py-20 text-center">
        <div className="h-8 w-8 border-4 border-brand-500 border-t-transparent rounded-full animate-spin mx-auto mb-4" />
        <p className="text-sm font-semibold text-slate-500">Tracking live order status...</p>
      </div>
    );
  }

  if (error || !order) {
    return (
      <div className="max-w-md mx-auto px-4 py-20 text-center space-y-4">
        <AlertCircle className="h-12 w-12 text-rose-500 mx-auto" />
        <h2 className="text-xl font-bold text-slate-900">Order Not Found</h2>
        <p className="text-sm text-slate-500">{error || 'Could not locate order details.'}</p>
        <Link to="/orders" className="inline-block px-4 py-2 bg-slate-900 text-white rounded-xl text-xs font-bold">
          View All Orders
        </Link>
      </div>
    );
  }

  const steps: { status: OrderStatus; label: string; desc: string; icon: any }[] = [
    { status: 'PLACED', label: 'Order Received', desc: 'Sent to kitchen display', icon: Clock },
    { status: 'CONFIRMED', label: 'Kitchen Confirmed', desc: 'Chef accepted your order', icon: CheckCircle2 },
    { status: 'PREPARING', label: 'Cooking in Progress', desc: 'Freshly preparing your meal', icon: ChefHat },
    { status: 'READY_FOR_PICKUP', label: 'Ready at Counter', desc: 'Pickup hot at Counter #1', icon: PackageCheck },
    { status: 'COMPLETED', label: 'Order Picked Up', desc: 'Meal delivered. Enjoy!', icon: CheckCheck },
  ];

  const getStepIndex = (status: OrderStatus) => {
    switch (status) {
      case 'PLACED': return 0;
      case 'CONFIRMED': return 1;
      case 'PREPARING': return 2;
      case 'READY_FOR_PICKUP': return 3;
      case 'COMPLETED': return 4;
      case 'CANCELLED': return -1;
      default: return 0;
    }
  };

  const currentStepIndex = getStepIndex(order.status);
  const isCancelled = order.status === 'CANCELLED';

  return (
    <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8">
      {/* Top Bar */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4 border-b border-slate-200/80 pb-6">
        <div className="flex items-center space-x-3">
          <Link to="/orders" className="p-2 rounded-xl border border-slate-200 hover:bg-slate-50 text-slate-600 transition">
            <ArrowLeft className="h-4 w-4" />
          </Link>
          <div>
            <div className="flex items-center gap-2">
              <h1 className="text-2xl font-black text-slate-900 tracking-tight">Order #{order.orderNumber}</h1>
              <OrderStatusBadge status={order.status} />
            </div>
            <p className="text-xs text-slate-400 mt-0.5">
              Placed on {new Date(order.createdAt).toLocaleString()}
            </p>
          </div>
        </div>

        <div className="flex flex-wrap items-center gap-2">
          {/* Interactive Simulation Button for Testing */}
          {order.status !== 'COMPLETED' && order.status !== 'CANCELLED' && (
            <button
              onClick={handleSimulateNextStage}
              disabled={advancing}
              className="px-3.5 py-2 rounded-xl bg-amber-500 hover:bg-amber-600 text-white text-xs font-bold shadow-md shadow-amber-500/20 transition flex items-center space-x-1.5"
              title="Test real-time timeline advancement"
            >
              <Play className="h-3.5 w-3.5 fill-white" />
              <span>{advancing ? 'Simulating...' : 'Simulate Kitchen Progress ⚡'}</span>
            </button>
          )}

          <button
            onClick={fetchOrder}
            className="p-2.5 rounded-xl border border-slate-200 text-slate-600 hover:bg-slate-50 transition"
            title="Refresh Status"
          >
            <RefreshCw className="h-4 w-4" />
          </button>

          {order.status === 'PLACED' && (
            <button
              onClick={handleCancel}
              disabled={cancelling}
              className="px-4 py-2 rounded-xl bg-rose-50 text-rose-700 hover:bg-rose-100 font-bold text-xs border border-rose-200 transition"
            >
              {cancelling ? 'Cancelling...' : 'Cancel Order'}
            </button>
          )}
        </div>
      </div>

      {/* Payment Verification Status Banner */}
      <div className={`p-4 rounded-2xl border flex items-center justify-between ${
        order.paymentStatus === 'COMPLETED'
          ? 'bg-emerald-50/80 border-emerald-200 text-emerald-900'
          : 'bg-amber-50/80 border-amber-200 text-amber-900'
      }`}>
        <div className="flex items-center space-x-3">
          <div className={`h-10 w-10 rounded-xl flex items-center justify-center ${
            order.paymentStatus === 'COMPLETED' ? 'bg-emerald-600 text-white' : 'bg-amber-500 text-white'
          }`}>
            <ShieldCheck className="h-5 w-5" />
          </div>
          <div>
            <h4 className="font-extrabold text-sm">
              {order.paymentStatus === 'COMPLETED' ? 'Payment Verified & Confirmed' : 'Payment Status: Pending (Cash on Pickup)'}
            </h4>
            <p className="text-xs opacity-80">
              Gateway Method: {order.paymentMethod?.replace('_', ' ')} • Ref ID: {order.transactionRef || 'N/A'}
            </p>
          </div>
        </div>

        <span className={`px-3 py-1 rounded-full text-xs font-black uppercase tracking-wider ${
          order.paymentStatus === 'COMPLETED' ? 'bg-emerald-200/80 text-emerald-900' : 'bg-amber-200/80 text-amber-900'
        }`}>
          {order.paymentStatus === 'COMPLETED' ? 'PAID ₹' + order.finalAmount : 'DUE AT COUNTER'}
        </span>
      </div>

      {/* Visual Timeline Tracking */}
      <div className="p-8 rounded-3xl bg-white border border-slate-200/80 shadow-lg space-y-8">
        <div className="flex items-center justify-between">
          <h3 className="font-black text-slate-900 text-lg">Kitchen Live Status Timeline</h3>
          <span className="text-xs text-brand-600 font-bold bg-brand-50 px-3 py-1 rounded-full">
            Live Refreshing
          </span>
        </div>

        {isCancelled ? (
          <div className="p-6 rounded-2xl bg-rose-50 border border-rose-200 text-rose-800 flex items-center space-x-4">
            <XCircle className="h-10 w-10 text-rose-500 shrink-0" />
            <div>
              <h4 className="font-bold text-base">This Order was Cancelled</h4>
              <p className="text-xs text-rose-600 mt-1">If payment was completed, your refund is processed automatically.</p>
            </div>
          </div>
        ) : (
          <div className="relative">
            {/* Progress Bar background */}
            <div className="hidden md:block absolute top-6 left-8 right-8 h-1.5 bg-slate-100 -z-0">
              <div
                className="h-full bg-gradient-to-r from-brand-500 to-emerald-500 transition-all duration-500"
                style={{ width: `${(currentStepIndex / (steps.length - 1)) * 100}%` }}
              />
            </div>

            <div className="grid grid-cols-1 md:grid-cols-5 gap-6 relative z-10">
              {steps.map((step, idx) => {
                const Icon = step.icon;
                const isPassed = idx <= currentStepIndex;
                const isCurrent = idx === currentStepIndex;

                return (
                  <div key={step.status} className="flex md:flex-col items-center md:text-center space-x-4 md:space-x-0 space-y-0 md:space-y-2">
                    <div
                      className={`h-12 w-12 rounded-2xl flex items-center justify-center font-bold transition shadow-md ${
                        isPassed
                          ? isCurrent
                            ? 'bg-brand-500 text-white ring-4 ring-brand-100 animate-pulse'
                            : 'bg-emerald-500 text-white'
                          : 'bg-slate-100 text-slate-400'
                      }`}
                    >
                      <Icon className="h-5 w-5" />
                    </div>
                    <div>
                      <h4 className={`text-xs font-bold ${isPassed ? 'text-slate-900' : 'text-slate-400'}`}>
                        {step.label}
                      </h4>
                      <p className="text-[11px] text-slate-400">{step.desc}</p>
                    </div>
                  </div>
                );
              })}
            </div>
          </div>
        )}

        {/* Counter Alert when Ready */}
        {order.status === 'READY_FOR_PICKUP' && (
          <div className="p-5 rounded-2xl bg-gradient-to-r from-emerald-500 to-teal-600 text-white shadow-xl flex items-center space-x-4 animate-bounce">
            <PackageCheck className="h-8 w-8 shrink-0" />
            <div>
              <h4 className="font-extrabold text-base">Your Food is Hot & Ready!</h4>
              <p className="text-xs text-emerald-100">Please walk over to Counter #1 with Order #{order.orderNumber} for pickup.</p>
            </div>
          </div>
        )}
      </div>

      {/* Order Item Details and Receipt with Dish Thumbnails */}
      <div className="p-6 rounded-3xl bg-white border border-slate-200/80 shadow-sm space-y-6">
        <h3 className="font-black text-slate-900 text-lg border-b border-slate-100 pb-3">
          Order Summary & Receipt
        </h3>

        <div className="divide-y divide-slate-100">
          {order.items.map((item) => (
            <div key={item.id} className="py-3.5 flex justify-between items-center text-sm">
              <div className="flex items-center space-x-3">
                <img
                  src={item.imageUrl || 'https://images.unsplash.com/photo-1546833999-b9f581a1996d?auto=format&fit=crop&w=150&q=80'}
                  alt={item.itemName}
                  className="h-12 w-12 rounded-xl object-cover bg-slate-100 shrink-0 border border-slate-100"
                  onError={(e: any) => {
                    e.target.src = 'https://images.unsplash.com/photo-1546833999-b9f581a1996d?auto=format&fit=crop&w=150&q=80';
                  }}
                />
                <div className="space-y-0.5">
                  <p className="font-bold text-slate-900">{item.itemName}</p>
                  <p className="text-xs text-slate-400">Qty: {item.quantity} × ₹{item.unitPrice}</p>
                </div>
              </div>
              <span className="font-extrabold text-slate-900">₹{item.totalPrice}</span>
            </div>
          ))}
        </div>

        <div className="border-t border-slate-100 pt-4 space-y-2 text-sm max-w-xs ml-auto">
          <div className="flex justify-between text-slate-600">
            <span>Subtotal</span>
            <span className="font-bold text-slate-900">₹{order.totalAmount}</span>
          </div>
          {order.discountAmount > 0 && (
            <div className="flex justify-between text-emerald-600 font-bold">
              <span>Coupon Discount ({order.couponCode})</span>
              <span>-₹{order.discountAmount}</span>
            </div>
          )}
          <div className="flex justify-between text-slate-600">
            <span>GST & Tax (5%)</span>
            <span className="font-bold text-slate-900">₹{order.taxAmount}</span>
          </div>
          <div className="border-t border-dashed border-slate-200 pt-2 flex justify-between font-black text-slate-900 text-base">
            <span>Total Paid</span>
            <span className="text-xl text-brand-600">₹{order.finalAmount}</span>
          </div>
          <div className="pt-2 text-xs text-slate-500 font-medium text-right flex items-center justify-end gap-1.5">
            <ShieldCheck className="h-4 w-4 text-emerald-600"/>
            <span>Payment: {order.paymentMethod?.replace('_', ' ')} • {order.paymentStatus}</span>
          </div>
        </div>
      </div>
    </div>
  );
};
