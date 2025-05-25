<template>
  <view class="login-container">
    <view class="login-header">
      <image
        class="logo"
        src="/src/pages/login/static/logo.png"
        mode="aspectFit"
      ></image>
      <text class="title">欢迎登录</text>
      <text class="subtitle">请登录您的账号</text>
    </view>

    <view class="login-form">
      <view class="input-group">
        <text class="label">账号</text>
        <view class="input-wrapper">
          <input
            class="input"
            type="text"
            v-model="userNumber"
            placeholder="请输入账号"
          />
          <view class="input-icon">
            <text class="iconfont icon-user"></text>
          </view>
        </view>
      </view>

      <view class="input-group">
        <text class="label">密码</text>
        <view class="input-wrapper">
          <input
            class="input"
            type="password"
            v-model="password"
            placeholder="请输入密码"
          />
          <view class="input-icon">
            <text class="iconfont icon-lock"></text>
          </view>
        </view>
      </view>

      <button
        class="login-btn"
        type="primary"
        @click="handleLogin"
        :loading="isLoading"
      >
        <text class="btn-text">{{ isLoading ? "登录中..." : "登录" }}</text>
        <text class="btn-icon" v-if="!isLoading">→</text>
      </button>

      <view class="other-login">
        <text class="register" @click="handleRegister">
          <text class="iconfont icon-register"></text>
          注册账号
        </text>
        <text class="forget-pwd" @click="handleForgetPwd">
          <text class="iconfont icon-forget"></text>
          忘记密码
        </text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref } from "vue";
import { login } from "../../api/UserService";
import { useUserStore } from "../../store/user";

// 数据
const userNumber = ref("");
const password = ref("");
const isLoading = ref(false);
const userStore = useUserStore();

// 登录方法
const handleLogin = async () => {
  if (!userNumber.value || !password.value) {
    uni.showToast({ title: "请输入账号和密码", icon: "none" });
    return;
  }

  isLoading.value = true;
  try {
    const params = {
      userNumber: userNumber.value,
      password: password.value,
    };

    const res = await login(params);
    console.log(res);

    if (res.data.code === 1) {
      // 存储token
      userStore.setToken(res.data.data);

      uni.showToast({ title: "登录成功", icon: "success" });

      // 检查是否有保存的页面信息
      try {
        const savedPageInfo = uni.getStorageSync("lastVisitedPage");
        if (savedPageInfo) {
          const pageInfo = JSON.parse(savedPageInfo);
          // 清除保存的页面信息
          uni.removeStorageSync("lastVisitedPage");

          // 构建完整的URL，包含参数
          let url = `/${pageInfo.path}`;
          if (pageInfo.options) {
            const queryParams = Object.entries(pageInfo.options)
              .map(([key, value]) => `${key}=${value}`)
              .join("&");
            if (queryParams) {
              url += `?${queryParams}`;
            }
          }

          // 导航回之前的页面
          uni.reLaunch({
            url: url,
          });
          return;
        }
      } catch (e) {
        console.error("解析保存的页面信息失败:", e);
      }

      // 如果没有保存的页面信息，则跳转到首页
      uni.switchTab({ url: "/pages/home/index" });
    }
  } catch (error) {
    console.error("登录失败", error);
  } finally {
    isLoading.value = false; // 无论成功还是失败，都重置加载状态
  }
};

// 注册方法
const handleRegister = () => {
  uni.navigateTo({
    url: "/pages/register/register",
  });
};

// 忘记密码方法
const handleForgetPwd = () => {
  uni.navigateTo({
    url: "/pages/forget-pwd/forget-pwd",
  });
};
</script>

<style lang="scss">
.login-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
  padding: 40rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
  overflow: hidden;

  &::before {
    content: "";
    position: absolute;
    top: -50%;
    left: -50%;
    width: 200%;
    height: 200%;
    background: radial-gradient(
      circle,
      rgba(255, 255, 255, 0.1) 0%,
      rgba(255, 255, 255, 0) 70%
    );
    animation: rotate 20s linear infinite;
    z-index: 0;
  }

  @keyframes rotate {
    from {
      transform: rotate(0deg);
    }
    to {
      transform: rotate(360deg);
    }
  }

  .login-header {
    position: relative;
    z-index: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    margin: 60rpx 0 80rpx;
    padding: 50rpx 40rpx;
    background: rgba(255, 255, 255, 0.95);
    border-radius: 30rpx;
    box-shadow: 0 10rpx 30rpx rgba(0, 0, 0, 0.1);
    width: 100%;
    max-width: 600rpx;
    backdrop-filter: blur(10px);
    border: 1px solid rgba(255, 255, 255, 0.2);

    .logo {
      width: 140rpx;
      height: 140rpx;
      margin-bottom: 30rpx;
      filter: drop-shadow(0 4rpx 8rpx rgba(0, 0, 0, 0.1));
    }

    .title {
      font-size: 44rpx;
      font-weight: bold;
      color: #333;
      margin-bottom: 10rpx;
      background: linear-gradient(135deg, #333 0%, #666 100%);
      -webkit-background-clip: text;
      -webkit-text-fill-color: transparent;
    }

    .subtitle {
      font-size: 28rpx;
      color: #666;
      letter-spacing: 2rpx;
    }
  }

  .login-form {
    position: relative;
    z-index: 1;
    width: 100%;
    max-width: 600rpx;
    background: rgba(255, 255, 255, 0.95);
    padding: 50rpx 40rpx;
    border-radius: 30rpx;
    box-shadow: 0 10rpx 30rpx rgba(0, 0, 0, 0.1);
    backdrop-filter: blur(10px);
    border: 1px solid rgba(255, 255, 255, 0.2);

    .input-group {
      margin-bottom: 40rpx;

      .label {
        font-size: 28rpx;
        color: #333;
        margin-bottom: 16rpx;
        display: block;
        font-weight: 500;
        padding-left: 20rpx;
        border-left: 4rpx solid #007aff;
      }

      .input-wrapper {
        position: relative;
        background: rgba(255, 255, 255, 0.8);
        border-radius: 16rpx;
        padding: 0 20rpx;
        border: 2rpx solid #e9ecef;
        transition: all 0.3s ease;
        box-shadow: 0 2rpx 6rpx rgba(0, 0, 0, 0.05);

        &:focus-within {
          border-color: #007aff;
          background: #fff;
          box-shadow: 0 4rpx 10rpx rgba(0, 123, 255, 0.15);
          transform: translateY(-2rpx);
        }

        .input-icon {
          position: absolute;
          right: 20rpx;
          top: 50%;
          transform: translateY(-50%);
          color: #999;
          font-size: 32rpx;
        }
      }

      .input {
        height: 90rpx;
        font-size: 30rpx;
        color: #333;
        width: 100%;
        padding-right: 50rpx;
      }
    }

    .login-btn {
      margin-top: 60rpx;
      height: 90rpx;
      line-height: 90rpx;
      font-size: 32rpx;
      background: linear-gradient(135deg, #007aff 0%, #0056b3 100%);
      border: none;
      border-radius: 16rpx;
      color: #fff;
      box-shadow: 0 4rpx 12rpx rgba(0, 122, 255, 0.3);
      transition: all 0.3s ease;
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 10rpx;

      .btn-text {
        font-weight: 500;
      }

      .btn-icon {
        font-size: 36rpx;
        transition: transform 0.3s ease;
      }

      &:active {
        transform: translateY(2rpx);
        box-shadow: 0 2rpx 6rpx rgba(0, 122, 255, 0.3);

        .btn-icon {
          transform: translateX(4rpx);
        }
      }
    }

    .other-login {
      margin-top: 40rpx;
      display: flex;
      justify-content: space-between;
      padding: 0 20rpx;

      .register,
      .forget-pwd {
        font-size: 28rpx;
        color: #007aff;
        position: relative;
        padding: 10rpx 0;
        display: flex;
        align-items: center;
        gap: 8rpx;
        font-weight: 500;

        .iconfont {
          font-size: 32rpx;
        }

        &::after {
          content: "";
          position: absolute;
          bottom: 0;
          left: 0;
          width: 0;
          height: 2rpx;
          background: #007aff;
          transition: width 0.3s ease;
        }

        &:active::after {
          width: 100%;
        }
      }
    }
  }
}
</style>
