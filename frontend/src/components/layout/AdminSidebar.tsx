import React from 'react';
import { NavLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { 
  LayoutDashboard, 
  Utensils, 
  ShoppingBag, 
  Users, 
  Boxes, 
  TicketPercent, 
  LogOut, 
  ArrowLeft,
  UtensilsCrossed
} from 'lucide-react';

export const AdminSidebar: React.FC = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const navItems = [
    { label: 'Dashboard & Analytics', path: '/admin/dashboard', icon: LayoutDashboard },
    { label: 'Order Processing', path: '/admin/orders', icon: ShoppingBag },
    { label: 'Menu Management', path: '/admin/menu', icon: Utensils },
    { label: 'Stock & Inventory', path: '/admin/inventory', icon: Boxes },
    { label: 'User Directory', path: '/admin/users', icon: Users },
    { label: 'Coupons & Offers', path: '/admin/coupons', icon: TicketPercent },
  ];

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <aside className="w-64 bg-navy-900 text-slate-300 min-h-screen flex flex-col justify-between border-r border-navy-800">
      <div>
        {/* Header */}
        <div className="p-6 border-b border-navy-800 flex items-center justify-between">
          <div className="flex items-center space-x-3">
            <div className="h-9 w-9 rounded-xl bg-gradient-to-tr from-brand-600 to-amber-500 flex items-center justify-center text-white shadow-md">
              <UtensilsCrossed className="h-5 w-5" />
            </div>
            <div>
              <h2 className="font-extrabold text-white text-base leading-none">Canteen<span className="text-amber-400">Hub</span></h2>
              <span className="text-[11px] font-semibold text-amber-400/90 uppercase tracking-wider">Admin Console</span>
            </div>
          </div>
        </div>

        {/* Current user badge */}
        <div className="px-6 py-4 bg-navy-950/60 border-b border-navy-800/80">
          <p className="text-xs text-slate-400 font-medium">Logged in as</p>
          <p className="text-sm font-bold text-white truncate">{user?.name}</p>
          <div className="flex gap-1 mt-1">
            {(Array.isArray(user?.roles) ? user.roles : []).map(role => (
              <span key={role} className="text-[10px] font-bold px-2 py-0.5 rounded bg-brand-500/20 text-brand-300 border border-brand-500/30">
                {role}
              </span>
            ))}
          </div>
        </div>

        {/* Navigation list */}
        <nav className="p-4 space-y-1.5">
          {(Array.isArray(navItems) ? navItems : []).map((item) => {
            const Icon = item.icon;
            return (
              <NavLink
                key={item.path}
                to={item.path}
                className={({ isActive }) =>
                  `flex items-center space-x-3 px-3.5 py-2.5 rounded-xl text-sm font-semibold transition ${
                    isActive
                      ? 'bg-gradient-to-r from-brand-500 to-amber-500 text-white shadow-md shadow-brand-500/20'
                      : 'text-slate-400 hover:text-white hover:bg-navy-800/60'
                  }`
                }
              >
                <Icon className="h-4 w-4 shrink-0" />
                <span>{item.label}</span>
              </NavLink>
            );
          })}
        </nav>
      </div>

      {/* Bottom actions */}
      <div className="p-4 border-t border-navy-800 space-y-2">
        <NavLink
          to="/"
          className="flex items-center space-x-2 px-3 py-2 rounded-lg text-sm text-slate-400 hover:text-white hover:bg-navy-800/50 transition font-medium"
        >
          <ArrowLeft className="h-4 w-4" />
          <span>Customer View</span>
        </NavLink>
        <button
          onClick={handleLogout}
          className="w-full flex items-center space-x-2 px-3 py-2 rounded-lg text-sm text-rose-400 hover:bg-rose-500/10 transition font-medium"
        >
          <LogOut className="h-4 w-4" />
          <span>Sign Out</span>
        </button>
      </div>
    </aside>
  );
};
