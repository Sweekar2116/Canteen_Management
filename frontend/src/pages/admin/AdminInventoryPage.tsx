import React, { useState, useEffect } from 'react';
import api from '../../services/api';
import { InventoryItem } from '../../types';
import { Modal } from '../../components/ui/Modal';
import { 
  Boxes, 
  AlertTriangle, 
  Plus, 
  CheckCircle2, 
  Edit3, 
  RefreshCw 
} from 'lucide-react';

export const AdminInventoryPage: React.FC = () => {
  const [inventory, setInventory] = useState<InventoryItem[]>([]);
  const [loading, setLoading] = useState(true);
  const [filterLowStock, setFilterLowStock] = useState(false);

  // Edit Modal
  const [selectedItem, setSelectedItem] = useState<InventoryItem | null>(null);
  const [quantity, setQuantity] = useState(0);
  const [minStockLevel, setMinStockLevel] = useState(10);
  const [unit, setUnit] = useState('plates');
  const [saving, setSaving] = useState(false);

  const fetchInventory = async () => {
    try {
      setLoading(true);
      const url = filterLowStock ? '/admin/inventory/low-stock' : '/admin/inventory';
      const res = await api.get<any>(url);
      if (Array.isArray(res.data)) {
        setInventory(res.data);
      } else {
        setInventory([]);
      }
    } catch (err) {
      console.error('Failed to load inventory:', err);
      setInventory([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchInventory();
  }, [filterLowStock]);

  const handleOpenEdit = (item: InventoryItem) => {
    setSelectedItem(item);
    setQuantity(item.quantity);
    setMinStockLevel(item.minStockLevel);
    setUnit(item.unit);
  };

  const handleSaveInventory = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!selectedItem) return;
    setSaving(true);

    try {
      await api.put(`/admin/inventory/${selectedItem.id}`, {
        quantity,
        minStockLevel,
        unit,
      });
      setSelectedItem(null);
      fetchInventory();
    } catch (err: any) {
      alert(err.response?.data?.message || 'Failed to update stock');
    } finally {
      setSaving(false);
    }
  };

  return (
    <div className="space-y-6">
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h1 className="text-2xl sm:text-3xl font-black text-slate-900 tracking-tight">
            Kitchen Stock & Inventory
          </h1>
          <p className="text-sm text-slate-500 mt-0.5">
            Monitor real-time food supply levels and restock items before they run out
          </p>
        </div>

        <div className="flex items-center space-x-3">
          <button
            onClick={() => setFilterLowStock(!filterLowStock)}
            className={`px-4 py-2.5 rounded-xl text-xs font-bold transition flex items-center space-x-2 ${
              filterLowStock
                ? 'bg-rose-600 text-white shadow-md shadow-rose-600/25'
                : 'bg-white border border-slate-200 text-slate-700 hover:bg-slate-50'
            }`}
          >
            <AlertTriangle className="h-4 w-4" />
            <span>{filterLowStock ? 'Showing Low Stock Only' : 'Filter Low Stock'}</span>
          </button>
          <button
            onClick={fetchInventory}
            className="p-2.5 rounded-xl border border-slate-200 bg-white text-slate-700 hover:bg-slate-50 transition"
            title="Refresh Stock"
          >
            <RefreshCw className="h-4 w-4" />
          </button>
        </div>
      </div>

      {/* Stock Table */}
      {loading ? (
        <div className="p-12 text-center">
          <div className="h-8 w-8 border-4 border-brand-500 border-t-transparent rounded-full animate-spin mx-auto mb-4" />
          <p className="text-sm font-semibold text-slate-500">Checking stock levels...</p>
        </div>
      ) : inventory.length === 0 ? (
        <div className="p-16 text-center bg-white rounded-3xl border border-slate-100 space-y-2">
          <Boxes className="h-10 w-10 text-slate-300 mx-auto" />
          <h3 className="text-base font-bold text-slate-700">No inventory records</h3>
          <p className="text-xs text-slate-400">All items are sufficiently stocked.</p>
        </div>
      ) : (
        <div className="bg-white rounded-3xl border border-slate-200/80 shadow-sm overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full text-left text-sm">
              <thead className="bg-slate-50/80 text-xs font-bold text-slate-500 uppercase tracking-wider border-b border-slate-100">
                <tr>
                  <th className="px-6 py-4">Menu Dish</th>
                  <th className="px-6 py-4">Category</th>
                  <th className="px-6 py-4">Available Quantity</th>
                  <th className="px-6 py-4">Min. Threshold</th>
                  <th className="px-6 py-4">Stock Status</th>
                  <th className="px-6 py-4 text-right">Actions</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100">
                {(Array.isArray(inventory) ? inventory : []).map((inv) => (
                  <tr key={inv.id} className="hover:bg-slate-50/60 transition">
                    <td className="px-6 py-4 font-extrabold text-slate-900">
                      {inv.itemName}
                    </td>

                    <td className="px-6 py-4 text-slate-500 text-xs font-bold">
                      {inv.categoryName || '-'}
                    </td>

                    <td className="px-6 py-4 font-black text-slate-900 text-base">
                      {inv.quantity} <span className="text-xs font-normal text-slate-400">{inv.unit}</span>
                    </td>

                    <td className="px-6 py-4 text-slate-600 font-medium">
                      {inv.minStockLevel} {inv.unit}
                    </td>

                    <td className="px-6 py-4">
                      <span
                        className={`inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-bold ${
                          inv.lowStock
                            ? 'bg-rose-50 text-rose-700 border border-rose-200 animate-pulse'
                            : 'bg-emerald-50 text-emerald-700 border border-emerald-200'
                        }`}
                      >
                        {inv.lowStock ? '⚠️ Low Stock' : '✅ Optimal Stock'}
                      </span>
                    </td>

                    <td className="px-6 py-4 text-right">
                      <button
                        onClick={() => handleOpenEdit(inv)}
                        className="px-3.5 py-1.5 rounded-xl border border-slate-200 text-xs font-bold text-slate-700 hover:bg-slate-100 transition inline-flex items-center space-x-1.5"
                      >
                        <Edit3 className="h-3.5 w-3.5" />
                        <span>Restock / Edit</span>
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      )}

      {/* Restock Modal */}
      <Modal
        isOpen={!!selectedItem}
        onClose={() => setSelectedItem(null)}
        title={`Restock Item • ${selectedItem?.itemName}`}
      >
        <form className="space-y-4" onSubmit={handleSaveInventory}>
          <div>
            <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1">
              Current Available Quantity
            </label>
            <input
              type="number"
              required
              min={0}
              value={quantity}
              onChange={(e) => setQuantity(Number(e.target.value))}
              className="w-full px-4 py-2.5 rounded-xl border border-slate-200 text-sm font-bold focus:outline-none focus:ring-2 focus:ring-brand-500/20"
            />
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1">
                Min. Stock Level
              </label>
              <input
                type="number"
                required
                min={1}
                value={minStockLevel}
                onChange={(e) => setMinStockLevel(Number(e.target.value))}
                className="w-full px-4 py-2.5 rounded-xl border border-slate-200 text-sm focus:outline-none focus:ring-2 focus:ring-brand-500/20"
              />
            </div>

            <div>
              <label className="block text-xs font-bold text-slate-700 uppercase tracking-wider mb-1">
                Measurement Unit
              </label>
              <input
                type="text"
                required
                value={unit}
                onChange={(e) => setUnit(e.target.value)}
                placeholder="plates, cups, pieces"
                className="w-full px-4 py-2.5 rounded-xl border border-slate-200 text-sm focus:outline-none focus:ring-2 focus:ring-brand-500/20"
              />
            </div>
          </div>

          <div className="pt-4 border-t border-slate-100 flex justify-end space-x-3">
            <button
              type="button"
              onClick={() => setSelectedItem(null)}
              className="px-4 py-2 rounded-xl border border-slate-200 text-slate-600 font-bold text-xs hover:bg-slate-50"
            >
              Cancel
            </button>
            <button
              type="submit"
              disabled={saving}
              className="px-6 py-2 rounded-xl bg-gradient-to-r from-brand-500 to-amber-500 text-white font-bold text-xs shadow-md"
            >
              {saving ? 'Updating...' : 'Save Stock'}
            </button>
          </div>
        </form>
      </Modal>
    </div>
  );
};
