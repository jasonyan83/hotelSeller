import axios from 'axios'
import { ElMessage } from 'element-plus'

const request = axios.create({
  baseURL: '/api/v1/admin',
  timeout: 15000,
})

// 请求拦截
request.interceptors.request.use(
  (config) => {
    // 可在此添加token等
    return config
  },
  (error) => Promise.reject(error)
)

// 响应拦截
request.interceptors.response.use(
  (response) => {
    const res = response.data
    if (res.code !== 200) {
      ElMessage.error(res.message || '请求失败')
      return Promise.reject(new Error(res.message || '请求失败'))
    }
    return res
  },
  (error) => {
    ElMessage.error(error.message || '网络错误')
    return Promise.reject(error)
  }
)

export default request
