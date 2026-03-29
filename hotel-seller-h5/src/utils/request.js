import axios from 'axios'
import { showToast } from 'vant'

const service = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  timeout: 30000,
  headers: { 'Content-Type': 'application/json' }
})

service.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) config.headers.Authorization = `Bearer ${token}`
  config.headers['X-Channel'] = 'H5'
  config.headers['X-Session-Id'] = getSessionId()
  return config
})

service.interceptors.response.use(
  response => {
    const { code, data, message } = response.data
    if (code === 200) return response.data
    showToast(message || '请求失败')
    return Promise.reject(new Error(message))
  },
  error => {
    if (error.code === 'ECONNABORTED') {
      showToast('网络请求超时，请重试')
    } else if (typeof navigator !== 'undefined' && !navigator.onLine) {
      showToast('网络不可用，请检查网络连接')
    } else {
      showToast('服务器异常，请稍后重试')
    }
    return Promise.reject(error)
  }
)

function getSessionId() {
  let sid = sessionStorage.getItem('session_id')
  if (!sid) {
    sid = crypto.randomUUID ? crypto.randomUUID() : Date.now().toString(36) + Math.random().toString(36).slice(2)
    sessionStorage.setItem('session_id', sid)
  }
  return sid
}

export default service
