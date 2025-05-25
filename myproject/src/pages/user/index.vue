<template>
  <view class="user-container">
    <!-- 加载中状态 -->
    <view v-if="loading" class="loading-state">
      <text>加载中...</text>
    </view>

    <!-- 错误状态 -->
    <view v-else-if="error" class="error-state" @click="loadUserInfo">
      <text>{{ error }}</text>
      <text class="retry-text">点击屏幕重试</text>
    </view>

    <!-- 用户信息内容 -->
    <template v-else>
      <!-- 用户信息卡片 -->
      <view class="user-card">
        <view class="avatar-section">
          <image
            :src="userInfo.avtor || '/static/default-avatar.png'"
            class="avatar"
            mode="aspectFill"
          />
          <text class="nickname">{{ userInfo.nickName || "未设置昵称" }}</text>
          <text class="user-number">账号：{{ userInfo.userNumber }}</text>
        </view>
      </view>

      <!-- 信息列表 -->
      <view class="info-list">
        <view class="info-item">
          <text class="label">手机号码</text>
          <text class="value">{{ userInfo.iphone || "未绑定" }}</text>
        </view>
        <view class="info-item">
          <text class="label">用户编号</text>
          <text class="value">{{ userInfo.userNumber }}</text>
        </view>
      </view>

      <!-- 操作按钮 -->
      <view class="action-list">
        <button class="action-btn" @click="handleEditProfile">修改资料</button>
        <button class="action-btn" @click="handleChangePassword">
          修改密码
        </button>
        <button class="action-btn logout" @click="handleLogout">
          退出登录
        </button>
      </view>
    </template>
  </view>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { getCurrentUser, logout } from "../../api/UserService";
import { useUserStore } from "../../store/user";

const userStore = useUserStore();
const userInfo = ref({
  userNumber: "",
  avtor: "",
  iphone: "",
  nickName: "",
});

const loading = ref(false);
const error = ref("");

// 获取用户信息
const loadUserInfo = async () => {
  loading.value = true;
  error.value = "";

  try {
    const res = await getCurrentUser();
    userInfo.value = res.data.data;
    error.value = "";
  } catch (error) {
    console.error("获取用户信息失败:", error);
    error.value = "连接服务器超时，点击屏幕重试";
  } finally {
    loading.value = false;
  }
};

// 修改资料
const handleEditProfile = () => {
  uni.showToast({
    title: "修改资料功能开发中",
    icon: "none",
  });
};

// 修改密码
const handleChangePassword = () => {
  uni.showToast({
    title: "修改密码功能开发中",
    icon: "none",
  });
};

// 退出登录
const handleLogout = async () => {
  uni.showModal({
    title: "提示",
    content: "确定要退出登录吗？",
    success: async (res) => {
      if (res.confirm) {
        try {
          // 调用退出登录接口
          await logout();
          // 清除本地token
          userStore.removeToken();
          // 重定向到登录页
          uni.reLaunch({
            url: "/pages/login/login",
          });
        } catch (error) {
          console.error("退出登录失败:", error);
          uni.showToast({
            title: "退出登录失败",
            icon: "none",
          });
        }
      }
    },
  });
};

onMounted(() => {
  loadUserInfo();
});
</script>

<style lang="scss">
.user-container {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 40rpx;
}

// 加载状态样式
.loading-state {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 28rpx;
  color: #666;
}

// 错误状态样式
.error-state {
  height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  font-size: 28rpx;
  color: #666;

  .retry-text {
    margin-top: 20rpx;
    color: #007aff;
  }
}

.user-card {
  background: #007aff;
  padding: 40rpx 30rpx;
  color: #fff;
}

.avatar-section {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20rpx 0;

  .avatar {
    width: 150rpx;
    height: 150rpx;
    border-radius: 75rpx;
    border: 4rpx solid rgba(255, 255, 255, 0.8);
    margin-bottom: 20rpx;
  }

  .nickname {
    font-size: 32rpx;
    font-weight: bold;
    margin-bottom: 10rpx;
  }

  .user-number {
    font-size: 26rpx;
    opacity: 0.9;
  }
}

.info-list {
  margin: 30rpx;
  background: #fff;
  border-radius: 12rpx;
  padding: 0 30rpx;

  .info-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 30rpx 0;
    border-bottom: 1rpx solid #f0f0f0;

    &:last-child {
      border-bottom: none;
    }

    .label {
      color: #666;
      font-size: 28rpx;
    }

    .value {
      color: #333;
      font-size: 28rpx;
    }
  }
}

.action-list {
  margin: 30rpx;

  .action-btn {
    width: 100%;
    height: 88rpx;
    line-height: 88rpx;
    background: #fff;
    color: #333;
    font-size: 28rpx;
    border-radius: 12rpx;
    margin-bottom: 20rpx;

    &.logout {
      color: #ff4444;
    }

    &:active {
      opacity: 0.8;
    }
  }
}
</style>
