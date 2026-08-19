import React from 'react';
import { Link } from 'react-router-dom';
import { UtensilsCrossed, Heart, ShieldCheck, Github, Coffee } from 'lucide-react';

export const Footer: React.FC = () => {
  return (
    <footer className="bg-slate-900 text-slate-400 border-t border-slate-800">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
        <div className="grid grid-cols-1 md:grid-cols-4 gap-8">
          {/* Brand Info */}
          <div className="space-y-4 md:col-span-1">
            <div className="flex items-center space-x-3">
              <div className="h-9 w-9 rounded-xl bg-brand-500 flex items-center justify-center text-white">
                <UtensilsCrossed className="h-5 w-5" />
              </div>
              <span className="text-xl font-bold text-white tracking-tight">
                Canteen<span className="text-brand-400">Hub</span>
              </span>
            </div>
            <p className="text-sm text-slate-400 leading-relaxed">
              Modern digital dining platform for campus students & staff. Fast ordering, zero queues, fresh meals.
            </p>
          </div>

          {/* Quick Links */}
          <div>
            <h4 className="text-sm font-semibold text-white uppercase tracking-wider mb-4">Quick Links</h4>
            <ul className="space-y-2 text-sm">
              <li><Link to="/menu" className="hover:text-white transition">Menu & Specials</Link></li>
              <li><Link to="/orders" className="hover:text-white transition">Track Order</Link></li>
              <li><Link to="/profile" className="hover:text-white transition">User Account</Link></li>
              <li><a href="http://localhost:8080/swagger-ui.html" target="_blank" rel="noreferrer" className="hover:text-amber-400 transition flex items-center gap-1.5"><ShieldCheck className="h-3.5 w-3.5"/> Swagger API Docs</a></li>
            </ul>
          </div>

          {/* Timings */}
          <div>
            <h4 className="text-sm font-semibold text-white uppercase tracking-wider mb-4">Kitchen Hours</h4>
            <ul className="space-y-2 text-sm">
              <li>Breakfast: <span className="text-slate-200">7:30 AM - 10:30 AM</span></li>
              <li>Lunch: <span className="text-slate-200">12:00 PM - 3:30 PM</span></li>
              <li>Evening Snacks: <span className="text-slate-200">4:30 PM - 7:00 PM</span></li>
              <li>Dinner: <span className="text-slate-200">7:30 PM - 10:00 PM</span></li>
            </ul>
          </div>

          {/* System & Resume Tech */}
          <div>
            <h4 className="text-sm font-semibold text-white uppercase tracking-wider mb-4">Architecture</h4>
            <div className="flex flex-wrap gap-1.5 text-xs">
              <span className="px-2.5 py-1 rounded-md bg-slate-800 text-slate-300">Java 17</span>
              <span className="px-2.5 py-1 rounded-md bg-slate-800 text-slate-300">Spring Boot 3</span>
              <span className="px-2.5 py-1 rounded-md bg-slate-800 text-slate-300">Spring Security</span>
              <span className="px-2.5 py-1 rounded-md bg-slate-800 text-slate-300">JWT</span>
              <span className="px-2.5 py-1 rounded-md bg-slate-800 text-slate-300">MySQL</span>
              <span className="px-2.5 py-1 rounded-md bg-slate-800 text-slate-300">React + Vite</span>
              <span className="px-2.5 py-1 rounded-md bg-slate-800 text-slate-300">Tailwind CSS</span>
            </div>
          </div>
        </div>

        <div className="mt-8 pt-8 border-t border-slate-800 flex flex-col sm:flex-row justify-between items-center text-xs text-slate-500">
          <p>© {new Date().getFullYear()} CanteenHub Platform. Built for Production Portfolio.</p>
          <div className="flex items-center space-x-4 mt-4 sm:mt-0">
            <span>Powered by Spring Data JPA & React</span>
          </div>
        </div>
      </div>
    </footer>
  );
};
