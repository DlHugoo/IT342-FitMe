# 💪 Fitme Admin Panel

Welcome to the **Fitme Admin Panel** — a sleek and modern web interface built with **Vite + React**, and styled using **Tailwind CSS**. This panel provides a responsive, user-friendly experience for managing users, workout sets, and exercises within the Fitme system.

---

## 🚀 Features

- ⚡ **Fast development** with Vite
- 🧩 **Component-based architecture** using React
- 🎨 **Fully customizable UI** with Tailwind CSS
- 🧭 **Page navigation** with React Router

---

## 🛠️ Tech Stack

- [Vite](https://vitejs.dev/)
- [React](https://reactjs.org/)
- [Tailwind CSS](https://tailwindcss.com/)
- [React Router DOM](https://reactrouter.com/)
- [PostCSS](https://postcss.org/)
- [Autoprefixer](https://github.com/postcss/autoprefixer)

---

## 📦 Project Setup

### 1️⃣ Create a Vite + React App

```bash
npm create vite@latest fitme-admin-panel
# Select "React" for framework
# Select "JavaScript" for variant
```

### 2️⃣ Move into the Project Directory

```bash
cd fitme-admin-panel
```

### 3️⃣ Install Dependencies

```bash
npm install
```

### 4️⃣ Run the Development Server

```bash
npm run dev
```

---

## 🎨 Setting Up Tailwind CSS

### Install Tailwind and Related Packages

```bash
npm install -D tailwindcss@3.4.17 postcss autoprefixer
npx tailwindcss init -p
```

### Configure `tailwind.config.js`

```js
/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ["./index.html", "./src/**/*.{js,jsx}"],
  theme: {
    extend: {
      colors: {
        "fitme-blue": "#1E40AF",
        "blue-header": "#0F172A",
      },
    },
  },
  plugins: [],
};
```

### Add Tailwind Directives to `src/index.css`

```css
@tailwind base;
@tailwind components;
@tailwind utilities;
```

---

## 🧭 Install React Router

```bash
npm install react-router-dom
```

---

## ✅ Requirements

- [Node.js](https://nodejs.org/) v16 or higher
- Modern web browser (Chrome, Firefox, Edge, Safari)
