import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  // Configuración del servidor de desarrollo
  server: {
    port: 5173, // Puerto por defecto de Vite
    host: true, // Escuchar en todas las interfaces
    // Proxy para evitar problemas de CORS durante desarrollo
    proxy: {
      '/api': {
        target: 'http://localhost:8081',
        changeOrigin: true,
        secure: false,
      },
    },
  },
  // Definir variables de entorno disponibles
  envDir: '.',
  envPrefix: 'VITE_',
})