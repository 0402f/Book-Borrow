import axios from 'axios'
import { useUserStore } from '@/stores'
// const baseURL = '/api' 上线使用
const baseURL = 'http://localhost:8080'
import { ElMessage } from 'element-plus'
import router from '@/router'
const instance = axios.create({
  // TODO 1. 基础地址，超时时间
  baseURL,
  timeout: 10000
})
// 添加请求拦截器
instance.interceptors.request.use(
  (config) => {
    // 获取用户信息token
    const userStore = useUserStore()
    //判断是否有token
    if (userStore.token) {
      config.headers.JWT = userStore.token
    }

    // 判断是否有token
    return config
  },
  function (error) {
    // 对请求错误做些什么
    return Promise.reject(error)
  }
)

// 响应拦截器
instance.interceptors.response.use(
  (res) => {
    // 处理业务成功
    if (res.data.code === 1) {
      return res
    }
    // 处理业务失败
    // 1. 统一错误提示
    ElMessage.error(res.data.msg || '服务异常')
    return Promise.reject(res.data)
  },
  (err) => {
    // TODO 5. 处理401错误
    if (err.response.status === 401) {
      // 清除token
      const userStore = useUserStore()
      userStore.removeToken()
      userStore.removeuserNumber()
      router.push('/login')
    }
    return Promise.reject(err)
  }
)

export default instance
export { baseURL }
