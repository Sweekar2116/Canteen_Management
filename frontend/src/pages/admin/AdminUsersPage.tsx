import React, { useState, useEffect } from 'react';
import api from '../../services/api';
import { User, RoleType } from '../../types';
import { Badge } from '../../components/ui/Badge';
import { Search, Shield, UserCheck, UserX, RefreshCw } from 'lucide-react';

export const AdminUsersPage: React.FC = () => {
  const [users, setUsers] = useState<User[]>([]);
  const [loading, setLoading] = useState(true);
  const [query, setQuery] = useState('');
  const [roleFilter, setRoleFilter] = useState('');

  const fetchUsers = async () => {
    try {
      setLoading(true);
      const params: any = { size: 50 };
      if (query.trim()) params.query = query.trim();
      if (roleFilter) params.role = roleFilter;

      const res = await api.get<any>('/admin/users', { params });
      if (res.data && Array.isArray(res.data.content)) {
        setUsers(res.data.content);
      } else if (Array.isArray(res.data)) {
        setUsers(res.data);
      } else {
        setUsers([]);
      }
    } catch (err) {
      console.error('Failed to load users:', err);
      setUsers([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchUsers();
  }, [query, roleFilter]);

  const handleToggleStatus = async (userId: number) => {
    try {
      const res = await api.patch<User>(`/admin/users/${userId}/status`);
      setUsers((prev) => prev.map((u) => (u.id === userId ? res.data : u)));
    } catch (err) {
      alert('Failed to update user status');
    }
  };

  const handleRoleChange = async (userId: number, newRole: string) => {
    try {
      const res = await api.patch<User>(`/admin/users/${userId}/role?role=${newRole}`);
      setUsers((prev) => prev.map((u) => (u.id === userId ? res.data : u)));
    } catch (err: any) {
      alert(err.response?.data?.message || 'Failed to update user role');
    }
  };

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl sm:text-3xl font-black text-slate-900 tracking-tight">
            User Directory & Access Control
          </h1>
          <p className="text-sm text-slate-500 mt-0.5">
            Manage student customers, kitchen staff permissions and administrator roles
          </p>
        </div>

        <button
          onClick={fetchUsers}
          className="inline-flex items-center space-x-2 px-4 py-2.5 rounded-xl border border-slate-200 bg-white text-slate-700 text-xs font-bold shadow-sm hover:bg-slate-50 transition"
        >
          <RefreshCw className="h-4 w-4" />
          <span>Refresh Users</span>
        </button>
      </div>

      {/* Filter Row */}
      <div className="flex flex-col sm:flex-row items-center gap-3">
        <div className="relative max-w-md w-full">
          <Search className="h-4 w-4 text-slate-400 absolute left-3.5 top-1/2 -translate-y-1/2" />
          <input
            type="text"
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            placeholder="Search by name or email..."
            className="w-full pl-10 pr-4 py-2.5 rounded-xl border border-slate-200 text-sm focus:outline-none focus:ring-2 focus:ring-brand-500/20"
          />
        </div>

        <select
          value={roleFilter}
          onChange={(e) => setRoleFilter(e.target.value)}
          className="px-4 py-2.5 rounded-xl border border-slate-200 text-sm font-semibold text-slate-700 bg-white"
        >
          <option value="">All Roles</option>
          <option value="CUSTOMER">Customers</option>
          <option value="STAFF">Kitchen Staff</option>
          <option value="ADMIN">Administrators</option>
        </select>
      </div>

      {/* Users Table */}
      {loading ? (
        <div className="p-12 text-center">
          <div className="h-8 w-8 border-4 border-brand-500 border-t-transparent rounded-full animate-spin mx-auto mb-4" />
          <p className="text-sm font-semibold text-slate-500">Loading user directory...</p>
        </div>
      ) : (
        <div className="bg-white rounded-3xl border border-slate-200/80 shadow-sm overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full text-left text-sm">
              <thead className="bg-slate-50/80 text-xs font-bold text-slate-500 uppercase tracking-wider border-b border-slate-100">
                <tr>
                  <th className="px-6 py-4">User Name</th>
                  <th className="px-6 py-4">Email</th>
                  <th className="px-6 py-4">Phone</th>
                  <th className="px-6 py-4">Role</th>
                  <th className="px-6 py-4">Status</th>
                  <th className="px-6 py-4 text-right">Actions</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100">
                {(Array.isArray(users) ? users : []).map((u) => {
                  const userRole = (Array.isArray(u?.roles) && u.roles[0]) || 'CUSTOMER';
                  return (
                    <tr key={u.id} className="hover:bg-slate-50/60 transition">
                      <td className="px-6 py-4 font-extrabold text-slate-900">
                        {u.name}
                      </td>

                      <td className="px-6 py-4 text-slate-600 font-medium">
                        {u.email}
                      </td>

                      <td className="px-6 py-4 text-slate-600">
                        {u.phone || '-'}
                      </td>

                      <td className="px-6 py-4">
                        <select
                          value={userRole}
                          onChange={(e) => handleRoleChange(u.id, e.target.value)}
                          className="px-2.5 py-1 rounded-lg border border-slate-200 text-xs font-bold text-slate-800 bg-white"
                        >
                          <option value="CUSTOMER">CUSTOMER</option>
                          <option value="STAFF">STAFF</option>
                          <option value="ADMIN">ADMIN</option>
                        </select>
                      </td>

                      <td className="px-6 py-4">
                        <span
                          className={`inline-flex items-center gap-1.5 px-2.5 py-0.5 rounded-full text-xs font-bold ${
                            u.enabled
                              ? 'bg-emerald-50 text-emerald-700 border border-emerald-200'
                              : 'bg-rose-50 text-rose-700 border border-rose-200'
                          }`}
                        >
                          {u.enabled ? 'Active' : 'Deactivated'}
                        </span>
                      </td>

                      <td className="px-6 py-4 text-right">
                        <button
                          onClick={() => handleToggleStatus(u.id)}
                          className={`px-3 py-1.5 rounded-xl text-xs font-bold border transition ${
                            u.enabled
                              ? 'border-rose-200 text-rose-600 hover:bg-rose-50'
                              : 'border-emerald-200 text-emerald-600 hover:bg-emerald-50'
                          }`}
                        >
                          {u.enabled ? 'Deactivate' : 'Activate'}
                        </button>
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          </div>
        </div>
      )}
    </div>
  );
};
