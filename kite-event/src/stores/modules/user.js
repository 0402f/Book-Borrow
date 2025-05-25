import { defineStore } from 'pinia'
import { ref } from 'vue'
// 用户模块 token setToken removeToken
export const useUserStore = defineStore(
  'user',
  () => {
    // 定义数据
    const token = ref('')
    const userNumber = ref('')
    // 定义方法
    const setToken = (newToken) => {
      token.value = newToken
    }
    const removeToken = () => {
      token.value = ''
    }
    const setuserNumber = (newuserNumber) => {
      userNumber.value = newuserNumber
    }
    const removeuserNumber = () => {
      userNumber.value = ''
    }

    // 暴露出去
    return {
      token,
      userNumber,
      setuserNumber,
      setToken,
      removeToken,
      removeuserNumber
    }
  },
  {
    persist: true // 开启数据持久化
  }
)
