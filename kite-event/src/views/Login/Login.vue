<template>
  <div class="login-page">
    <div class="login-container">
      <div class="left-image">
        <img src="https://picsum.photos/300/400" alt="登录图片" />
      </div>
      <div class="right-form">
        <div class="switch-area">
          <span @click="showRegister = false" :class="{ 'active-tab': !showRegister }">登录</span>
          <span @click="showRegister = true" :class="{ 'active-tab': showRegister }">注册</span>
        </div>

        <div v-if="!showRegister" class="form-group">
          <div class="input-group">
            <span class="icon">👤</span>
            <input type="text" v-model="userNumber" placeholder="账号" />
          </div>
          <div class="input-group">
            <span class="icon">🔒</span>
            <input type="password" v-model="password" placeholder="密码" />
          </div>
          <button @click="login" class="login-btn">登录</button>
        </div>

        <div v-if="showRegister" class="form-group">
          <div class="input-group">
            <span class="icon">👤</span>
            <input type="text" v-model="registerUsername" placeholder="注册账号" />
          </div>
          <div class="input-group">
            <span class="icon">🔒</span>
            <input type="password" v-model="registerPassword" placeholder="注册密码" />
          </div>
          <button @click="register" class="login-btn">注册</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
// 解决多单词命名问题

import { ref } from 'vue'
import { loginService, registerService } from '@/api/user'
import router from '@/router'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores'

export default {
  setup() {
    const userStore = useUserStore()

    // 登录相关
    const userNumber = ref('')
    const password = ref('')

    // 注册相关
    const showRegister = ref(false)
    const registerUsername = ref('')
    const registerPassword = ref('')

    const login = async () => {
      if (!userNumber.value || !password.value) {
        alert('请填写完整的登录信息')
        return
      }

      // 构建普通对象（避免响应式引用）
      const requestData = {
        userNumber: userNumber.value,
        password: password.value
      }
      // 调用接口
      const res = await loginService(requestData)
      userStore.setToken(res.data.data)
      ElMessage.success('登录成功')
      userStore.setuserNumber(userNumber.value)
      // 跳转到首页 延时1秒
      setTimeout(() => {
        router.push('/')
      }, 1000)
      console.log(res)
    }

    // 注册逻辑
    const register = async () => {
      if (!registerUsername.value || !registerPassword.value) {
        alert('请填写完整的注册信息')
        return
      }
      const user = {
        userNumber: registerUsername.value,
        password: registerPassword.value
      }
      // 调用接口
      await registerService(user)
      ElMessage.success('注册成功')
      showRegister.value = false
      // 清空注册信息
      registerUsername.value = ''
      registerPassword.value = ''
      // 将登录信息填充
      userNumber.value = user.userNumber
      password.value = user.password
    }

    return {
      userNumber,
      password,
      showRegister,
      registerUsername,
      registerPassword,
      login,
      register
    }
  }
}
</script>

<style scoped>
.login-page {
  margin: 0;
  padding: 0;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  overflow: hidden;
  position: relative;
}

.video-background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  z-index: -1;
}

.login-container {
  background: rgba(255, 255, 255, 0);
  padding: 2rem;
  border-radius: 1rem;
  box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(15px);
  animation: fadeIn 0.6s ease;
  display: flex;
}

.left-image {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  padding-right: 2rem;
}

.left-image img {
  max-width: 100%;
  max-height: 100%;
  border-radius: 0.5rem;
}

.right-form {
  flex: 1;
}

.switch-area {
  display: flex;
  justify-content: center;
  margin-bottom: 1.5rem;
}

.switch-area span {
  padding: 0.5rem 1.5rem;
  cursor: pointer;
  color: #666;
  transition: color 0.3s ease;
}

.switch-area .active-tab {
  color: #007bff;
  font-weight: bold;
}

.form-group {
  width: 100%;
}

.input-group {
  display: flex;
  align-items: center;
  margin-bottom: 1rem;
  border: 1px solid #e0e0e0;
  border-radius: 0.5rem;
  overflow: hidden;
}

.input-group .icon {
  background: #f5f5f5;
  padding: 0.8rem;
}

.input-group input {
  flex: 1;
  padding: 0.8rem;
  border: none;
  outline: none;
}

.login-btn {
  width: 100%;
  padding: 0.8rem;
  background: #007bff;
  color: white;
  border: none;
  border-radius: 0.5rem;
  cursor: pointer;
  transition: background 0.3s ease;
}

.login-btn:hover {
  background: #0056b3;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(-20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
