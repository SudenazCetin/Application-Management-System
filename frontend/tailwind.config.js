/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{ts,tsx}'],
  theme: {
    extend: {
      colors: {
        app: {
          bg: '#0B0F19',
          card: '#171C28',
          primary: '#4F7CFF',
          success: '#22C55E',
          danger: '#EF4444',
          warning: '#F59E0B',
          text: '#FFFFFF',
          muted: '#A1A1AA',
          border: '#2A3245',
        },
      },
      boxShadow: {
        soft: '0 12px 36px rgba(10, 16, 32, 0.35)',
        glow: '0 0 0 1px rgba(79, 124, 255, 0.15), 0 12px 36px rgba(79, 124, 255, 0.2)',
      },
      backgroundImage: {
        haze: 'radial-gradient(900px 500px at 80% -10%, rgba(79, 124, 255, 0.2), transparent 55%), radial-gradient(700px 360px at 0% 0%, rgba(99, 102, 241, 0.16), transparent 50%)',
      },
      backdropBlur: {
        xs: '2px',
      },
      fontFamily: {
        sans: ['Manrope', 'ui-sans-serif', 'system-ui', 'sans-serif'],
        display: ['Space Grotesk', 'Manrope', 'ui-sans-serif', 'system-ui', 'sans-serif'],
      },
    },
  },
  plugins: [],
}

