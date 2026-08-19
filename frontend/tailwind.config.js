/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        brand: {
          50: '#fdf4f0',
          100: '#fbe8de',
          200: '#f7d0bd',
          300: '#f2ad91',
          400: '#eb805f',
          500: '#e35833',
          600: '#d43e1d',
          700: '#b23016',
          800: '#8e2916',
          900: '#732517',
          950: '#3e100a',
        },
        navy: {
          800: '#1e204a',
          900: '#141636',
          950: '#0c0d24',
        }
      },
      fontFamily: {
        sans: ['Plus Jakarta Sans', 'Inter', 'system-ui', 'sans-serif'],
      }
    },
  },
  plugins: [],
}
