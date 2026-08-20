import axios from 'axios';

const API_BASE_URL = (import.meta as any).env?.VITE_API_BASE_URL || '/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Attach JWT token to all outgoing requests
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Global response error handler
api.interceptors.response.use(
  (response) => {
    // If we received an HTML document instead of JSON (happens on SPA rewrites when backend is offline/unproxied)
    if (typeof response.data === 'string' && (response.data.trim().startsWith('<!doctype') || response.data.trim().startsWith('<html'))) {
      const error: any = new Error('API server returned HTML instead of JSON. Backend might be unreachable.');
      error.response = { status: 404, data: { message: 'Backend endpoint not found' } };
      return Promise.reject(error);
    }
    return response;
  },
  (error) => {
    if ((error.response?.status === 401 || error.response?.status === 403) && !window.location.pathname.includes('/login')) {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default api;
