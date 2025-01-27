/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./src/**/*.{js,jsx,ts,tsx}",
  ],
  theme: {
    extend: {
      backgroundImage: {
        'orange-green': 'linear-gradient(90deg, #f2733c, #11936c)',
      },
    },
  },
  plugins: [],
}

