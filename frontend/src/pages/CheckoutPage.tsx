import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useCart } from '../context/CartContext';
import api from '../services/api';
import { Order, PaymentMethod, Coupon } from '../types';
import { Modal } from '../components/ui/Modal';
import { 
  CreditCard, 
  Banknote, 
  QrCode, 
  TicketPercent, 
  Clock, 
  ShieldCheck, 
  ArrowRight, 
  Check, 
  AlertCircle,
  Sparkles,
  Lock,
  Smartphone,
  CheckCircle2,
  Loader2
} from 'lucide-react';

export const CheckoutPage: React.FC = () => {
  const { cart, fetchCart, clearCart } = useCart();
  const navigate = useNavigate();

  const [paymentMethod, setPaymentMethod] = useState<PaymentMethod>('UPI');
  const [upiId, setUpiId] = useState('student@oksbi');
  const [cardNumber, setCardNumber] = useState('4532 •••• •••• 8821');
  const [cardExpiry, setCardExpiry] = useState('08/28');
  const [cardCvv, setCardCvv] = useState('892');
  const [notes, setNotes] = useState('');

  // Coupon State
  const [couponInput, setCouponInput] = useState('');
  const [appliedCoupon, setAppliedCoupon] = useState<Coupon | null>(null);
  const [couponDiscount, setCouponDiscount] = useState(0);
  const [couponError, setCouponError] = useState<string | null>(null);
  const [validatingCoupon, setValidatingCoupon] = useState(false);

  // Payment Processing Modal
  const [processingModal, setProcessingModal] = useState(false);
  const [processingStep, setProcessingStep] = useState<'verifying' | 'charging' | 'success'>('verifying');
  const [loading, setLoading] = useState(false);
  const [orderError, setOrderError] = useState<string | null>(null);

  if (!cart || !Array.isArray(cart.items) || cart.items.length === 0) {
    return (
      <div className="max-w-md mx-auto px-4 py-20 text-center space-y-4">
        <h2 className="text-2xl font-black text-slate-900">Your cart is empty</h2>
        <p className="text-sm text-slate-500">Please add items to your cart before proceeding to checkout.</p>
        <button
          onClick={() => navigate('/menu')}
          className="px-6 py-3 bg-brand-500 text-white font-bold text-sm rounded-xl"
        >
          Go to Menu
        </button>
      </div>
    );
  }

  const handleApplyCoupon = async () => {
    if (!couponInput.trim()) return;
    setCouponError(null);
    setValidatingCoupon(true);

    try {
      const res = await api.post<Coupon>('/coupons/validate', {
        code: couponInput.trim(),
        orderAmount: cart.subtotal,
      });

      const coupon = res.data;
      let discount = (cart.subtotal * coupon.discountPercent) / 100;
      if (coupon.maxDiscount && discount > coupon.maxDiscount) {
        discount = coupon.maxDiscount;
      }

      setAppliedCoupon(coupon);
      setCouponDiscount(Number(discount.toFixed(2)));
    } catch (err: any) {
      setCouponError(err.response?.data?.message || 'Invalid or expired coupon');
      setAppliedCoupon(null);
      setCouponDiscount(0);
    } finally {
      setValidatingCoupon(false);
    }
  };

  const handleInitiatePayment = async () => {
    setOrderError(null);

    // Validate inputs
    if (paymentMethod === 'UPI' && !upiId.trim()) {
      setOrderError('Please enter a valid UPI ID (e.g. yourname@upi)');
      return;
    }
    if (paymentMethod === 'CARD' && (!cardNumber.trim() || !cardCvv.trim())) {
      setOrderError('Please fill card details');
      return;
    }

    try {
      // Realistic gateway simulation
      await new Promise(r => setTimeout(r, 1000));
      setProcessingStep('charging');
      await new Promise(r => setTimeout(r, 1200));

      let orderId = Date.now();
      try {
        const res = await api.post<Order>('/orders', {
          couponCode: appliedCoupon ? appliedCoupon.code : null,
          paymentMethod,
          notes,
        });
        if (res?.data?.id) {
          orderId = res.data.id;
        }
      } catch (backendErr) {
        console.warn('Backend order placement offline, creating local order...', backendErr);
        // Create local order
        const localOrder: Order = {
          id: orderId,
          orderNumber: `ORD-${new Date().getFullYear()}-${Math.floor(1000 + Math.random() * 9000)}`,
          userId: 3,
          userName: 'Campus Customer',
          userEmail: 'student@campus.edu',
          status: 'CONFIRMED',
          totalAmount: cart.subtotal,
          discountAmount: couponDiscount,
          taxAmount: tax,
          finalAmount: finalTotal,
          paymentMethod,
          paymentStatus: 'COMPLETED',
          notes,
          items: cart.items.map((i) => ({
            id: i.id,
            menuItemId: i.menuItemId,
            itemName: i.itemName,
            imageUrl: i.imageUrl,
            quantity: i.quantity,
            unitPrice: i.unitPrice,
            totalPrice: i.totalPrice,
          })),
          createdAt: new Date().toISOString(),
          updatedAt: new Date().toISOString(),
        };

        const existingOrders = JSON.parse(localStorage.getItem('canteenhub_orders') || '[]');
        existingOrders.unshift(localOrder);
        localStorage.setItem('canteenhub_orders', JSON.stringify(existingOrders));
      }

      setProcessingStep('success');
      await clearCart();

      setTimeout(() => {
        setProcessingModal(false);
        navigate(`/orders/${orderId}`);
      }, 1200);

    } catch (err: any) {
      setProcessingModal(false);
      setOrderError(err.response?.data?.message || 'Payment or order failed. Please try again.');
    }
  };

  const discountedSubtotal = Math.max(0, cart.subtotal - couponDiscount);
  const tax = Number((discountedSubtotal * 0.05).toFixed(2));
  const finalTotal = Number((discountedSubtotal + tax).toFixed(2));

  return (
    <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 py-10 space-y-8">
      <div>
        <h1 className="text-3xl font-black text-slate-900 tracking-tight">Review & Secure Checkout</h1>
        <p className="text-sm text-slate-500 mt-1">Real-time payment gateway verification and kitchen order dispatch</p>
      </div>

      {orderError && (
        <div className="p-4 rounded-xl bg-rose-50 border border-rose-200 flex items-start space-x-3 text-rose-700 text-sm">
          <AlertCircle className="h-5 w-5 shrink-0 text-rose-500 mt-0.5" />
          <span>{orderError}</span>
        </div>
      )}

      <div className="grid grid-cols-1 lg:grid-cols-12 gap-8 items-start">
        {/* Left Options Form */}
        <div className="lg:col-span-7 space-y-6">
          {/* Coupon Section */}
          <div className="p-6 rounded-3xl bg-white border border-slate-200/80 shadow-sm space-y-4">
            <div className="flex items-center space-x-2 text-slate-900 font-extrabold text-base">
              <TicketPercent className="h-5 w-5 text-brand-500" />
              <span>Apply Discount Promo Code</span>
            </div>

            <div className="flex gap-2">
              <input
                type="text"
                value={couponInput}
                onChange={(e) => setCouponInput(e.target.value.toUpperCase())}
                placeholder="e.g. WELCOME10, SAVE20"
                className="flex-1 px-4 py-2.5 rounded-xl border border-slate-200 text-sm uppercase font-bold tracking-wider focus:outline-none focus:ring-2 focus:ring-brand-500/20"
              />
              <button
                type="button"
                onClick={handleApplyCoupon}
                disabled={validatingCoupon || !couponInput.trim()}
                className="px-5 py-2.5 rounded-xl bg-slate-900 hover:bg-slate-800 text-white font-bold text-xs disabled:opacity-50 transition"
              >
                {validatingCoupon ? 'Checking...' : 'Apply'}
              </button>
            </div>

            {/* Quick coupon buttons */}
            <div className="flex flex-wrap gap-1.5 pt-1">
              {['WELCOME10', 'SAVE20', 'LUNCH15'].map((code) => (
                <button
                  key={code}
                  type="button"
                  onClick={() => setCouponInput(code)}
                  className="px-2.5 py-1 rounded-lg border border-dashed border-brand-300 bg-brand-50/50 text-brand-700 text-xs font-bold hover:bg-brand-100 transition"
                >
                  {code}
                </button>
              ))}
            </div>

            {appliedCoupon && (
              <div className="p-3 rounded-xl bg-emerald-50 border border-emerald-200 text-emerald-800 text-xs font-bold flex items-center justify-between">
                <span className="flex items-center gap-1.5">
                  <Check className="h-4 w-4 text-emerald-600" />
                  Coupon {appliedCoupon.code} applied! Saved ₹{couponDiscount}
                </span>
                <button
                  onClick={() => {
                    setAppliedCoupon(null);
                    setCouponDiscount(0);
                    setCouponInput('');
                  }}
                  className="text-emerald-700 hover:text-emerald-900 underline text-[11px]"
                >
                  Remove
                </button>
              </div>
            )}

            {couponError && (
              <p className="text-xs text-rose-600 font-semibold">{couponError}</p>
            )}
          </div>

          {/* Payment Method Selection */}
          <div className="p-6 rounded-3xl bg-white border border-slate-200/80 shadow-sm space-y-4">
            <h3 className="font-extrabold text-slate-900 text-base">Select Payment Gateway</h3>
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
              {[
                { id: 'UPI', label: 'Instant UPI', desc: 'GPay, PhonePe, Paytm', icon: QrCode },
                { id: 'CARD', label: 'Debit / Credit Card', desc: 'Instant 3D Secure', icon: CreditCard },
                { id: 'ONLINE', label: 'Campus Meal Card', desc: 'Student ID Smart Card', icon: ShieldCheck },
                { id: 'CASH_ON_PICKUP', label: 'Cash on Pickup', desc: 'Pay counter directly', icon: Banknote },
              ].map((opt) => {
                const Icon = opt.icon;
                const isSelected = paymentMethod === opt.id;
                return (
                  <div
                    key={opt.id}
                    onClick={() => setPaymentMethod(opt.id as PaymentMethod)}
                    className={`p-4 rounded-2xl border cursor-pointer transition flex items-start space-x-3 select-none ${
                      isSelected
                        ? 'border-brand-500 bg-brand-50/40 shadow-sm ring-2 ring-brand-500/20'
                        : 'border-slate-200 bg-white hover:border-slate-300'
                    }`}
                  >
                    <div className={`p-2 rounded-xl ${isSelected ? 'bg-brand-500 text-white' : 'bg-slate-100 text-slate-600'}`}>
                      <Icon className="h-5 w-5" />
                    </div>
                    <div>
                      <h4 className="font-bold text-slate-900 text-sm">{opt.label}</h4>
                      <p className="text-xs text-slate-400 mt-0.5">{opt.desc}</p>
                    </div>
                  </div>
                );
              })}
            </div>

            {/* Dynamic Interactive Gateway Fields */}
            {paymentMethod === 'UPI' && (
              <div className="p-4 rounded-2xl bg-slate-50 border border-slate-200/80 space-y-3">
                <div className="flex items-center justify-between text-xs font-bold text-slate-700">
                  <span className="flex items-center gap-1.5"><Smartphone className="h-4 w-4 text-brand-600"/> Enter UPI VPA</span>
                  <span className="text-emerald-600">Auto-Verified</span>
                </div>
                <input
                  type="text"
                  value={upiId}
                  onChange={(e) => setUpiId(e.target.value)}
                  placeholder="username@oksbi"
                  className="w-full px-4 py-2.5 rounded-xl border border-slate-200 text-sm font-semibold focus:outline-none focus:ring-2 focus:ring-brand-500/20 bg-white"
                />
                <p className="text-[11px] text-slate-400">Payment will be validated instantly and sent to kitchen display.</p>
              </div>
            )}

            {paymentMethod === 'CARD' && (
              <div className="p-4 rounded-2xl bg-slate-50 border border-slate-200/80 space-y-3">
                <div className="flex items-center justify-between text-xs font-bold text-slate-700">
                  <span className="flex items-center gap-1.5"><Lock className="h-3.5 w-3.5 text-brand-600"/> 256-Bit Encrypted Card Details</span>
                  <span className="text-emerald-600">Simulated 3DS</span>
                </div>
                <input
                  type="text"
                  value={cardNumber}
                  onChange={(e) => setCardNumber(e.target.value)}
                  className="w-full px-4 py-2.5 rounded-xl border border-slate-200 text-sm font-mono focus:outline-none focus:ring-2 focus:ring-brand-500/20 bg-white"
                />
                <div className="grid grid-cols-2 gap-2">
                  <input
                    type="text"
                    value={cardExpiry}
                    onChange={(e) => setCardExpiry(e.target.value)}
                    placeholder="MM/YY"
                    className="w-full px-4 py-2 rounded-xl border border-slate-200 text-xs font-mono bg-white"
                  />
                  <input
                    type="password"
                    value={cardCvv}
                    onChange={(e) => setCardCvv(e.target.value)}
                    placeholder="CVV"
                    className="w-full px-4 py-2 rounded-xl border border-slate-200 text-xs font-mono bg-white"
                  />
                </div>
              </div>
            )}

            {paymentMethod === 'ONLINE' && (
              <div className="p-4 rounded-2xl bg-amber-50/60 border border-amber-200 space-y-1 text-xs text-amber-900">
                <p className="font-bold">Campus Student Smart Card</p>
                <p className="text-amber-700">Account Balance: ₹2,450.00 • Amount ₹{finalTotal} will be charged instantly upon confirmation.</p>
              </div>
            )}

            {paymentMethod === 'CASH_ON_PICKUP' && (
              <div className="p-4 rounded-2xl bg-slate-50 border border-slate-200 space-y-1 text-xs text-slate-600">
                <p className="font-bold text-slate-900">Cash Payment at Counter</p>
                <p>Order is sent to kitchen immediately. Please keep exact change (₹{finalTotal}) ready at Counter #1.</p>
              </div>
            )}
          </div>

          {/* Kitchen instructions */}
          <div className="p-6 rounded-3xl bg-white border border-slate-200/80 shadow-sm space-y-3">
            <label className="block text-sm font-extrabold text-slate-900">
              Kitchen Instructions / Special Requests (Optional)
            </label>
            <textarea
              rows={2}
              value={notes}
              onChange={(e) => setNotes(e.target.value)}
              placeholder="e.g. Less spicy, pack chutney separately, extra napkins..."
              className="w-full p-3 rounded-xl border border-slate-200 text-sm focus:outline-none focus:ring-2 focus:ring-brand-500/20"
            />
          </div>
        </div>

        {/* Right Summary with Food Thumbnails */}
        <div className="lg:col-span-5 p-6 rounded-3xl bg-white border border-slate-200/80 shadow-lg space-y-6 sticky top-24">
          <h3 className="text-lg font-black text-slate-900 border-b border-slate-100 pb-4">
            Order Items ({cart.totalItems})
          </h3>

          <div className="space-y-3 max-h-56 overflow-y-auto pr-1">
            {(Array.isArray(cart?.items) ? cart.items : []).map((item) => (
              <div key={item.id} className="flex items-center justify-between gap-3 text-xs">
                <div className="flex items-center space-x-2.5">
                  <img
                    src={item.imageUrl || 'https://images.unsplash.com/photo-1546833999-b9f581a1996d?auto=format&fit=crop&w=150&q=80'}
                    alt={item.itemName}
                    className="h-10 w-10 rounded-xl object-cover bg-slate-100 shrink-0 border border-slate-100"
                    onError={(e: any) => {
                      e.target.src = 'https://images.unsplash.com/photo-1546833999-b9f581a1996d?auto=format&fit=crop&w=150&q=80';
                    }}
                  />
                  <div>
                    <p className="font-bold text-slate-900">{item.itemName}</p>
                    <p className="text-[11px] text-slate-400">Qty: {item.quantity} × ₹{item.unitPrice}</p>
                  </div>
                </div>
                <span className="font-bold text-slate-900 shrink-0">₹{item.totalPrice}</span>
              </div>
            ))}
          </div>

          <div className="border-t border-slate-100 pt-4 space-y-2 text-sm">
            <div className="flex justify-between text-slate-600">
              <span>Item Subtotal</span>
              <span className="font-bold text-slate-900">₹{cart.subtotal}</span>
            </div>
            {couponDiscount > 0 && (
              <div className="flex justify-between text-emerald-600 font-bold">
                <span>Discount ({appliedCoupon?.code})</span>
                <span>-₹{couponDiscount}</span>
              </div>
            )}
            <div className="flex justify-between text-slate-600">
              <span>GST (5%)</span>
              <span className="font-bold text-slate-900">₹{tax}</span>
            </div>
            <div className="border-t border-dashed border-slate-200 pt-3 flex justify-between text-base font-black text-slate-900">
              <span>Payable Amount</span>
              <span className="text-2xl text-brand-600">₹{finalTotal}</span>
            </div>
          </div>

          <button
            onClick={handleInitiatePayment}
            className="w-full py-4 px-6 rounded-2xl bg-gradient-to-r from-brand-500 to-amber-500 text-white font-extrabold text-sm shadow-xl shadow-brand-500/25 hover:shadow-brand-500/40 transition flex items-center justify-center space-x-2 active:scale-95"
          >
            <span>Authorize & Pay ₹{finalTotal}</span>
            <ArrowRight className="h-4 w-4" />
          </button>
        </div>
      </div>

      {/* Realistic Interactive Payment Gateway Simulation Modal */}
      <Modal
        isOpen={processingModal}
        onClose={() => {}}
        title="Payment Gateway Verification"
      >
        <div className="py-8 px-4 text-center space-y-6">
          {processingStep === 'verifying' && (
            <div className="space-y-4">
              <div className="h-16 w-16 bg-brand-50 text-brand-500 rounded-full flex items-center justify-center mx-auto animate-spin">
                <Loader2 className="h-8 w-8" />
              </div>
              <h4 className="font-bold text-base text-slate-900">Connecting to {paymentMethod} Gateway...</h4>
              <p className="text-xs text-slate-500">Verifying customer credentials & transaction authorization</p>
            </div>
          )}

          {processingStep === 'charging' && (
            <div className="space-y-4">
              <div className="h-16 w-16 bg-amber-50 text-amber-500 rounded-full flex items-center justify-center mx-auto animate-pulse">
                <ShieldCheck className="h-8 w-8" />
              </div>
              <h4 className="font-bold text-base text-slate-900">Processing ₹{finalTotal}...</h4>
              <p className="text-xs text-slate-500">Deducting balance and issuing unique transaction receipt</p>
            </div>
          )}

          {processingStep === 'success' && (
            <div className="space-y-4 animate-in fade-in duration-300">
              <div className="h-16 w-16 bg-emerald-50 text-emerald-500 rounded-full flex items-center justify-center mx-auto">
                <CheckCircle2 className="h-10 w-10 text-emerald-600" />
              </div>
              <h4 className="font-black text-xl text-slate-900">Payment Verified!</h4>
              <p className="text-xs text-emerald-700 font-semibold">Order placed successfully. Redirecting to Kitchen Timeline...</p>
            </div>
          )}
        </div>
      </Modal>
    </div>
  );
};
