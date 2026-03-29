import { createApp } from 'vue'
import { createPinia } from 'pinia'
import router from './router'
import App from './App.vue'

// Vant 组件
import {
  Icon, Loading, Calendar, Switch, Lazyload,
  Toast, Popup, Overlay
} from 'vant'
import 'vant/lib/index.css'

// 全局样式
import './assets/styles/global.scss'

const app = createApp(App)
const pinia = createPinia()

app.use(pinia)
app.use(router)

// 注册 Vant 组件
app.use(Icon)
app.use(Loading)
app.use(Calendar)
app.use(Switch)
app.use(Lazyload)
app.use(Toast)
app.use(Popup)
app.use(Overlay)

app.mount('#app')
