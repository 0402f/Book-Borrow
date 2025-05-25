import { defineStore } from 'pinia'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: uni.getStorageSync('pinan') || '',
    userInfo: {}
  }),
  actions: {
    // 设置token
    setToken(token) {
      this.token = token
      uni.setStorageSync('pinan', token)
    },
    // 移除token
    removeToken() {
      this.token = ''
      uni.removeStorageSync('pinan')
    },
    // 设置用户信息
    setUserInfo(userInfo) {
      this.userInfo = userInfo
    }
  },
  persist: {
    key: 'user-store',
    paths: ['token', 'userInfo']
  }
}) 