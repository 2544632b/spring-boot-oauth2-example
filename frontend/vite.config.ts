import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  css: {
      preprocessorOptions: {
          less: {
              modifyVars: {
                  'primary-color': '#712cf9',
                  'border-radius-base': '15px',
              },
              javascriptEnabled: true,
          }
      }
    },
    server: {
      host: 'localhost',
      port: 80
    }
})
