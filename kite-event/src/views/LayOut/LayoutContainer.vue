<template>
  <el-container class="layout-container">
    <el-aside width="200px">
      <div class="el-aside__logo">
        <div class="logo-animation"></div>
      </div>
      <el-menu
        active-text-color="#FFA500"
        background-color="#2B2D42"
        :default-active="$route.path"
        text-color="#E5E5E5"
        router
      >
        <el-menu-item index="/index">
          <el-icon><House /></el-icon>
          <span>首页</span>
        </el-menu-item>
        <el-menu-item index="/UserManagement">
          <el-icon><Avatar /></el-icon>
          <span>用户管理</span>
        </el-menu-item>
        <el-menu-item index="/ClassManagement" class="menu-item">
          <el-icon class="menu-icon"><MessageBox /></el-icon>
          分类管理
        </el-menu-item>
        <!-- 将书籍管理改为下拉菜单 -->
        <el-sub-menu index="/BookManagement" class="menu-item">
          <template #title>
            <el-icon class="menu-icon"><Memo /></el-icon>
            <span>书籍管理</span>
          </template>
          <!-- 三个子主体 -->
          <el-menu-item index="/BookManagement" style="margin-left: 10px">
            <el-icon class="menu-icon"><Top /></el-icon>
            已上架书籍
          </el-menu-item>
          <el-menu-item index="/DownBookManagement" style="margin-left: 10px">
            <el-icon class="menu-icon"><Bottom /></el-icon>
            已下架书籍</el-menu-item
          >
        </el-sub-menu>
        <el-menu-item index="/BorrowManagement" class="menu-item">
          <el-icon class="menu-icon"><View /></el-icon>
          借阅数据
        </el-menu-item>
        <el-menu-item index="/CellWe" class="menu-item">
          <el-icon class="menu-icon"><Contact /></el-icon>
          联系我们
        </el-menu-item>
        <!-- 多级菜单 -->
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="beautified-header">
        <div class="header-moving-element"></div>
        <div>不懂后台终端</div>
        <!-- 头部右侧添加头像和昵称 -->
        <div class="header-profile">
          <el-dropdown class="profile-dropdown">
            <div class="profile-content">
              <el-avatar class="profile-avatar" :size="40" :src="userInfo.avtor"></el-avatar>
              <span class="profile-nickname">{{ userInfo.nickName }}</span>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item>个人中心</el-dropdown-item>
                <el-dropdown-item>设置</el-dropdown-item>
                <el-dropdown-item divided @click="onExit">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      <el-main class="beautified-main">
        <router-view></router-view>
      </el-main>
      <el-footer> ©2025 Created by 不懂</el-footer>
    </el-container>
  </el-container>
</template>

<script setup>
import { House, Avatar, Memo, MessageBox, Top, Bottom, View } from '@element-plus/icons-vue'
import { getCurrentUserInfoService } from '@/api/user'
import { ref } from 'vue'
import { useUserStore } from '@/stores'
import router from '@/router'

const userStore = useUserStore()

// 获取当前用户信息
const userInfo = ref({})
const getUserInfo = async () => {
  const res = await getCurrentUserInfoService()
  console.log('获取到的用户信息', res)
  userInfo.value = res.data.data
}
//  页面加载时调用getUserInfo函数
getUserInfo()

const onExit = () => {
  // 退出登录
  // 1.清除store中的token
  userStore.removeToken()
  // 2.跳转到登录页面
  router.push('/login')
}
</script>

<style lang="scss" scoped>
.layout-container {
  height: 100vh;
  .el-aside {
    background-color: #2b2d42;
    box-shadow: 4px 0 10px rgba(0, 0, 0, 0.3);
    &__logo {
      height: 120px;
      display: flex;
      justify-content: center;
      align-items: center;
      .logo-animation {
        width: 50px;
        height: 50px;
        border-radius: 50%;
        border: 3px solid transparent;
        border-top-color: #ffa500;
        border-bottom-color: #ffa500;
        animation: spin 2s linear infinite;
      }
    }
    .el-menu {
      border-right: none;
      .el-menu-item {
        transition: all 0.3s ease;
        padding: 12px 20px;
        &:hover {
          background-color: rgba(255, 165, 0, 0.1);
          transform: translateX(5px);
        }
        &.is-active {
          background-color: rgba(255, 165, 0, 0.2);
          box-shadow: inset 4px 0 0 #ffa500;
          font-weight: bold;
        }
        .el-icon {
          font-size: 18px;
          margin-right: 10px;
          color: #ffa500;
        }
      }
      // 确保子菜单样式与当前一致
      .el-sub-menu .el-menu-item {
        padding-left: 40px;
        transition: all 0.3s ease;
        &:hover {
          background-color: rgba(255, 165, 0, 0.1);
          transform: translateX(5px);
        }
        &.is-active {
          background-color: rgba(255, 165, 0, 0.2);
          box-shadow: inset 4px 0 0 #ffa500;
          font-weight: bold;
        }
      }
    }
  }
  .beautified-header {
    background-color: #2b2d42;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.3);
    border-radius: 10px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 20px;
    height: 60px;
    font-size: 18px;
    color: #e5e5e5;
    position: relative;
    overflow: hidden;
    .el-dropdown__box {
      display: flex;
      align-items: center;
      .el-icon {
        color: #999;
        margin-left: 10px;
      }

      &:active,
      &:focus {
        outline: none;
      }
    }
    .header-moving-element {
      position: absolute;
      top: 50%;
      left: -10%;
      transform: translateY(-50%);
      width: 20px;
      height: 20px;
      background-color: rgba(255, 165, 0, 0.5);
      border-radius: 50%;
      animation: moveAcrossHeader 10s linear infinite;
    }
  }
  .beautified-main {
    background-color: white;
    margin: 10px;
    margin-bottom: 1px;
    border-radius: 20px;
    box-shadow: 0 6px 12px rgba(0, 0, 0, 0.15);
  }
  .el-footer {
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 14px;
    color: #666;
  }

  @keyframes spin {
    0% {
      transform: rotate(0deg);
    }
    100% {
      transform: rotate(360deg);
    }
  }

  @keyframes moveAcrossHeader {
    0% {
      left: -10%;
    }
    100% {
      left: 110%;
    }
  }
}

.header-profile {
  display: flex;
  align-items: center;
  .profile-content {
    display: flex;
    align-items: center;
    cursor: pointer;
  }
  .profile-avatar {
    margin-right: 10px;
  }
  .profile-nickname {
    color: #ffa500;
    font-weight: 500;
  }
}
</style>
