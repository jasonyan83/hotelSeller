import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import Components from 'unplugin-vue-components/vite'
import { VantResolver } from '@vant/auto-import-resolver'
import postcsspxtoviewport from 'postcss-px-to-viewport-8-plugin'
import { fileURLToPath, URL } from 'node:url'

export default defineConfig({
  plugins: [
    vue(),
    Components({
      resolvers: [VantResolver()]
    })
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  css: {
    preprocessorOptions: {
      scss: {
        additionalData: `
          @use "@/assets/styles/variables.scss" as *;
          @use "@/assets/styles/mixins.scss" as *;
        `
      }
    },
    postcss: {
      plugins: [
        postcsspxtoviewport({
          viewportWidth: 375,
          unitPrecision: 5,
          viewportUnit: 'vw',
          selectorBlackList: ['.ignore'],
          minPixelValue: 1,
          mediaQuery: false,
          include: [/src/, /node_modules\/vant/]
        })
      ]
    }
  },
  server: {
    port: 3000,
    host: true
  }
})
