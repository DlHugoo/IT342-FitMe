/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        'fitme-blue': '#39B7FF',
        'fitme-blue-hover': '#4DBEFF',
      }
    },
  },
  plugins: [],
}
