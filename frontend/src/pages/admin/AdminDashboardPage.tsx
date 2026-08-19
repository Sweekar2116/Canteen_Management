import React, { useEffect, useState } from 'react';
import { 
  ShoppingBag, 
  DollarSign, 
  Users, 
  Utensils, 
  Clock, 
  AlertTriangle, 
  TrendingUp, 
  PackageCheck,
  RefreshCw
} from 'lucide-react';
import { 
  BarChart, 
  Bar, 
  XAxis, 
  YAxis, 
  CartesianGrid, 
  Tooltip, 
  ResponsiveContainer, 
  PieChart, 
  Pie, 
  Cell 
} from 'recharts';
import api from '../../services/api';
import { DashboardStats } from '../../types';

export const AdminDashboardPage: React.FC = () => {
  const [stats, setStats] = useState<DashboardStats | null>(null);
  const [loading, setLoading] = useState(true);

  const fetchStats = async () => {
    try {
      setLoading(true);
      const res = await api.get<DashboardStats>('/admin/dashboard');
      setStats(res.data);
    } catch (err) {
      console.error('Failed to load dashboard stats:', err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchStats();
  }, []);

  if (loading && !stats) {
    return (
      <div className="p-8 text-center">
        <div className="h-8 w-8 border-4 border-brand-500 border-t-transparent rounded-full animate-spin mx-auto mb-4" />
        <p className="text-sm font-semibold text-slate-500">Loading live canteen analytics...</p>
      </div>
    );
  }

  // Format order status data for Pie Chart
  const statusPieData = stats?.ordersByStatus
    ? Object.entries(stats.ordersByStatus).map(([name, value]) => ({
        name: name.replace('_', ' '),
        value,
      }))
    : [];

  const COLORS = ['#3b82f6', '#f59e0b', '#10b981', '#6366f1', '#ef4444'];

  return (
    <div className="space-y-8">
      {/* Top Welcome Header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl sm:text-3xl font-black text-slate-900 tracking-tight">
            Dashboard & Real-Time Analytics
          </h1>
          <p className="text-sm text-slate-500 mt-1">
            Live database-calculated metrics for today's campus canteen operations
          </p>
        </div>

        <button
          onClick={fetchStats}
          className="inline-flex items-center space-x-2 px-4 py-2.5 rounded-xl border border-slate-200 bg-white text-slate-700 text-xs font-bold shadow-sm hover:bg-slate-50 transition"
        >
          <RefreshCw className="h-4 w-4" />
          <span>Refresh Data</span>
        </button>
      </div>

      {/* Metrics Row */}
      <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-5">
        {/* Total Revenue */}
        <div className="p-6 rounded-3xl bg-white border border-slate-200/80 shadow-sm space-y-3">
          <div className="flex items-center justify-between">
            <span className="text-xs font-bold text-slate-400 uppercase tracking-wider">Total Revenue</span>
            <div className="h-10 w-10 rounded-2xl bg-emerald-50 text-emerald-600 flex items-center justify-center font-bold">
              ₹
            </div>
          </div>
          <div>
            <h3 className="text-2xl font-black text-slate-900">₹{stats?.totalRevenue || 0}</h3>
            <p className="text-xs text-emerald-600 font-bold mt-1">
              Today: ₹{stats?.todayRevenue || 0}
            </p>
          </div>
        </div>

        {/* Total Orders */}
        <div className="p-6 rounded-3xl bg-white border border-slate-200/80 shadow-sm space-y-3">
          <div className="flex items-center justify-between">
            <span className="text-xs font-bold text-slate-400 uppercase tracking-wider">Total Orders</span>
            <div className="h-10 w-10 rounded-2xl bg-brand-50 text-brand-600 flex items-center justify-center font-bold">
              <ShoppingBag className="h-5 w-5" />
            </div>
          </div>
          <div>
            <h3 className="text-2xl font-black text-slate-900">{stats?.totalOrders || 0}</h3>
            <p className="text-xs text-brand-600 font-bold mt-1">
              Today: {stats?.todayOrders || 0} orders
            </p>
          </div>
        </div>

        {/* Pending Orders */}
        <div className="p-6 rounded-3xl bg-white border border-slate-200/80 shadow-sm space-y-3">
          <div className="flex items-center justify-between">
            <span className="text-xs font-bold text-slate-400 uppercase tracking-wider">Active Kitchen</span>
            <div className="h-10 w-10 rounded-2xl bg-amber-50 text-amber-600 flex items-center justify-center font-bold">
              <Clock className="h-5 w-5" />
            </div>
          </div>
          <div>
            <h3 className="text-2xl font-black text-slate-900">{stats?.pendingOrders || 0}</h3>
            <p className="text-xs text-amber-600 font-bold mt-1">
              In Kitchen / Cooking
            </p>
          </div>
        </div>

        {/* Low Stock Alerts */}
        <div className="p-6 rounded-3xl bg-white border border-slate-200/80 shadow-sm space-y-3">
          <div className="flex items-center justify-between">
            <span className="text-xs font-bold text-slate-400 uppercase tracking-wider">Low Stock</span>
            <div className="h-10 w-10 rounded-2xl bg-rose-50 text-rose-600 flex items-center justify-center font-bold">
              <AlertTriangle className="h-5 w-5" />
            </div>
          </div>
          <div>
            <h3 className="text-2xl font-black text-slate-900">{stats?.lowStockCount || 0}</h3>
            <p className="text-xs text-rose-600 font-bold mt-1">
              Items require restocking
            </p>
          </div>
        </div>
      </div>

      {/* Analytics Charts */}
      <div className="grid grid-cols-1 lg:grid-cols-12 gap-8">
        {/* Top Selling Bar Chart */}
        <div className="lg:col-span-7 p-6 sm:p-8 rounded-3xl bg-white border border-slate-200/80 shadow-sm space-y-6">
          <div>
            <h3 className="font-black text-slate-900 text-lg">Top 5 Best Selling Dishes</h3>
            <p className="text-xs text-slate-400 mt-0.5">Ranked by total quantity sold</p>
          </div>

          <div className="h-72 w-full">
            {stats?.topSellingItems && stats.topSellingItems.length > 0 ? (
              <ResponsiveContainer width="100%" height="100%">
                <BarChart data={stats.topSellingItems}>
                  <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="#f1f5f9" />
                  <XAxis dataKey="name" tick={{ fontSize: 11 }} />
                  <YAxis tick={{ fontSize: 11 }} />
                  <Tooltip />
                  <Bar dataKey="quantitySold" fill="#e35833" radius={[8, 8, 0, 0]} name="Units Sold" />
                </BarChart>
              </ResponsiveContainer>
            ) : (
              <div className="h-full flex items-center justify-center text-xs text-slate-400">
                Place orders to generate top-selling analytics
              </div>
            )}
          </div>
        </div>

        {/* Order Status Distribution Pie Chart */}
        <div className="lg:col-span-5 p-6 sm:p-8 rounded-3xl bg-white border border-slate-200/80 shadow-sm space-y-6">
          <div>
            <h3 className="font-black text-slate-900 text-lg">Orders by Status</h3>
            <p className="text-xs text-slate-400 mt-0.5">Current distribution of orders</p>
          </div>

          <div className="h-72 w-full flex items-center justify-center">
            {statusPieData.length > 0 ? (
              <ResponsiveContainer width="100%" height="100%">
                <PieChart>
                  <Pie
                    data={statusPieData}
                    cx="50%"
                    cy="50%"
                    innerRadius={60}
                    outerRadius={90}
                    paddingAngle={4}
                    dataKey="value"
                  >
                    {statusPieData.map((entry, index) => (
                      <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                    ))}
                  </Pie>
                  <Tooltip />
                </PieChart>
              </ResponsiveContainer>
            ) : (
              <p className="text-xs text-slate-400">No order distribution data yet</p>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};
