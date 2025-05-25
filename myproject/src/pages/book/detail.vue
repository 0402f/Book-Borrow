<template>
  <view class="book-detail">
    <!-- 顶部图片区域 -->
    <view class="book-cover-section">
      <image :src="bookDetail.imageUrl" mode="aspectFill" class="book-cover" />
      <view class="cover-overlay"></view>
    </view>

    <!-- 基本信息卡片 -->
    <view class="info-card">
      <image
        :src="bookDetail.imageUrl"
        mode="aspectFill"
        class="book-thumbnail"
      />
      <view class="basic-info">
        <text class="book-title">{{ bookDetail.title }}</text>
        <view class="info-row">
          <text class="label">作者：</text>
          <text class="value">{{ bookDetail.author }}</text>
        </view>
        <view class="info-row">
          <text class="label">出版社：</text>
          <text class="value">{{ bookDetail.publisher }}</text>
        </view>
        <view class="info-row">
          <text class="label">出版日期：</text>
          <text class="value">{{ bookDetail.publishDate }}</text>
        </view>
        <view class="stock-info">
          <text class="label">库存：</text>
          <text :class="['stock-value', getStockClass(bookDetail.stock)]">
            {{ bookDetail.stock }}/{{ bookDetail.total }}
          </text>
        </view>
      </view>
    </view>

    <!-- 详细信息区域 -->
    <view class="detail-section">
      <view class="section-title">
        <text class="title-text">图书简介</text>
      </view>
      <view class="description" v-html="bookDetail.des"></view>
    </view>

    <!-- 底部抽屉 -->
    <view class="drawer-mask" v-if="showDrawer" @click="closeDrawer"></view>
    <view class="drawer-container" :class="{ 'drawer-show': showDrawer }">
      <view class="drawer-header">
        <text class="drawer-title">借阅信息</text>
        <text class="drawer-close" @click="closeDrawer">×</text>
      </view>

      <view class="drawer-content">
        <!-- 借阅天数 -->
        <view class="borrow-section">
          <text class="section-title">借阅天数</text>
          <view class="days-options">
            <view
              v-for="day in [30, 60, 90]"
              :key="day"
              :class="['day-option', borrowInfo.days === day ? 'active' : '']"
              @click="borrowInfo.days = day"
            >
              {{ day }}天
            </view>
          </view>
        </view>

        <!-- 借阅数量 -->
        <view class="borrow-section">
          <text class="section-title">借阅数量</text>
          <view class="count-selector">
            <view
              class="count-btn"
              :class="{ disabled: borrowInfo.counts <= 1 }"
              @click="decreaseCount"
              >-</view
            >
            <text class="count-number">{{ borrowInfo.counts }}</text>
            <view
              class="count-btn"
              :class="{
                disabled:
                  borrowInfo.counts >= 5 ||
                  borrowInfo.counts >= bookDetail.stock,
              }"
              @click="increaseCount"
              >+</view
            >
          </view>
          <text class="stock-info">当前库存：{{ bookDetail.stock }}本</text>
        </view>
      </view>

      <view class="drawer-footer">
        <button class="confirm-btn" @click="handleBorrow">确认借阅</button>
      </view>
    </view>

    <!-- 底部操作栏 -->
    <view class="bottom-bar">
      <button
        class="action-button borrow"
        :disabled="!canBorrow"
        @click="showBorrowPopup"
      >
        {{ canBorrow ? "借阅" : "暂无库存" }}
      </button>
    </view>
  </view>
</template>

<script setup>
import { ref, computed } from "vue";
import { onLoad } from "@dcloudio/uni-app";
import { getBookDetailByIdService } from "../../api/bookCart.js";
import { borrowBook } from "../../api/borrow.js";
import { useUserStore } from "../../store/user.js";

const bookDetail = ref({
  id: 0,
  title: "",
  author: "",
  publisher: "",
  publishDate: "",
  categoryId: 0,
  stock: 0,
  imageUrl: "",
  des: "",
  total: 0,
  status: 1,
});

const userStore = useUserStore();

// 借阅信息
const borrowInfo = ref({
  days: 30,
  counts: 1,
});

// 控制抽屉显示
const showDrawer = ref(false);

// 检查登录状态
const checkLoginStatus = () => {
  const token = userStore.token;
  if (!token) {
    uni.reLaunch({
      url: "/pages/login/login",
    });
    return false;
  }
  return true;
};

// 加载书籍详情
const loadBookDetail = async (bookId) => {
  if (!bookId) return;

  try {
    const res = await getBookDetailByIdService(bookId);
    console.log("书籍详情响应:", res);
    if (res.data.code === 1) {
      bookDetail.value = res.data.data;
    } else {
      uni.showToast({
        title: res.data.msg || "获取图书详情失败",
        icon: "none",
      });
      setTimeout(() => {
        uni.navigateBack();
      }, 1500);
    }
  } catch (error) {
    console.error("获取图书详情失败:", error);
    uni.showToast({
      title: "获取图书详情失败",
      icon: "none",
    });
    setTimeout(() => {
      uni.navigateBack();
    }, 1500);
  }
};

const canBorrow = computed(() => {
  return bookDetail.value.stock > 0 && bookDetail.value.status === 1;
});

// 获取库存状态样式
const getStockClass = (stock) => {
  if (stock <= 10) return "stock-low";
  if (stock <= 50) return "stock-medium";
  return "stock-high";
};

// 显示抽屉
const showBorrowPopup = () => {
  if (!canBorrow.value) {
    uni.showToast({
      title: "暂无库存",
      icon: "none",
    });
    return;
  }
  // 重置借阅信息
  borrowInfo.value = {
    days: 30,
    counts: 1,
  };
  showDrawer.value = true;
};

// 关闭抽屉
const closeDrawer = () => {
  showDrawer.value = false;
};

// 增加借阅数量
const increaseCount = () => {
  if (
    borrowInfo.value.counts < 5 &&
    borrowInfo.value.counts < bookDetail.value.stock
  ) {
    borrowInfo.value.counts++;
  }
};

// 减少借阅数量
const decreaseCount = () => {
  if (borrowInfo.value.counts > 1) {
    borrowInfo.value.counts--;
  }
};

// 处理借阅
const handleBorrow = async () => {
  try {
    const data = {
      readerId: userStore.userInfo.id,
      bookId: bookDetail.value.id,
      days: borrowInfo.value.days,
      counts: borrowInfo.value.counts,
    };

    await borrowBook(data);

    uni.showToast({
      title: "借阅成功",
      icon: "success",
    });

    // 关闭抽屉
    closeDrawer();

    // 跳转到借阅页面
    setTimeout(() => {
      uni.switchTab({
        url: "/pages/borrow/index",
      });
    }, 1500);
  } catch (error) {
    console.error("借阅失败:", error);
  }
};

// 定义页面生命周期钩子
onLoad((options) => {
  console.log("页面参数:", options);
  if (!checkLoginStatus()) return;

  const bookId = options.id;
  console.log("书籍ID:", bookId);

  if (bookId) {
    loadBookDetail(bookId);
  } else {
    uni.showToast({
      title: "图书ID不存在",
      icon: "none",
    });
    setTimeout(() => {
      uni.navigateBack();
    }, 1500);
  }
});
</script>

<style lang="scss">
.book-detail {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 120rpx;
}

.book-cover-section {
  position: relative;
  height: 400rpx;
  overflow: hidden;

  .book-cover {
    width: 100%;
    height: 100%;
    filter: blur(10px);
    transform: scale(1.1);
  }

  .cover-overlay {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0.3);
  }
}

.info-card {
  position: relative;
  margin: -100rpx 30rpx 30rpx;
  padding: 30rpx;
  background: #fff;
  border-radius: 20rpx;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.1);
  display: flex;
  z-index: 1;

  .book-thumbnail {
    width: 200rpx;
    height: 280rpx;
    border-radius: 12rpx;
    box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.2);
  }

  .basic-info {
    flex: 1;
    margin-left: 30rpx;
    display: flex;
    flex-direction: column;
    gap: 12rpx;
  }

  .book-title {
    font-size: 32rpx;
    font-weight: bold;
    color: #333;
    margin-bottom: 16rpx;
  }

  .info-row {
    display: flex;
    align-items: center;
    font-size: 26rpx;

    .label {
      color: #666;
      width: 140rpx;
    }

    .value {
      color: #333;
      flex: 1;
    }
  }

  .stock-info {
    margin-top: 16rpx;
    display: flex;
    align-items: center;
    font-size: 26rpx;

    .label {
      color: #666;
    }

    .stock-value {
      font-weight: bold;
      font-size: 28rpx;

      &.stock-low {
        color: #ff4444;
      }

      &.stock-medium {
        color: #ff9900;
      }

      &.stock-high {
        color: #22ac38;
      }
    }
  }
}

.detail-section {
  margin: 30rpx;
  padding: 30rpx;
  background: #fff;
  border-radius: 20rpx;
  box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.1);

  .section-title {
    margin-bottom: 20rpx;
    display: flex;
    align-items: center;

    .title-text {
      font-size: 30rpx;
      font-weight: bold;
      color: #333;
      position: relative;
      padding-left: 20rpx;

      &::before {
        content: "";
        position: absolute;
        left: 0;
        top: 50%;
        transform: translateY(-50%);
        width: 6rpx;
        height: 28rpx;
        background: #007aff;
        border-radius: 3rpx;
      }
    }
  }

  .description {
    font-size: 28rpx;
    color: #666;
    line-height: 1.6;
  }
}

.bottom-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 20rpx 30rpx;
  background: #fff;
  box-shadow: 0 -2rpx 10rpx rgba(0, 0, 0, 0.05);
  display: flex;
  justify-content: space-between;
  z-index: 100;

  .action-button {
    flex: 1;
    height: 80rpx;
    border-radius: 40rpx;
    font-size: 28rpx;
    font-weight: bold;
    display: flex;
    align-items: center;
    justify-content: center;

    &.borrow {
      background: #007aff;
      color: #fff;

      &:active {
        opacity: 0.9;
      }

      &[disabled] {
        background: #ccc;
        opacity: 1;
      }
    }
  }
}

.drawer-mask {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.4);
  z-index: 998;
}

.drawer-container {
  position: fixed;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: #fff;
  border-radius: 24rpx 24rpx 0 0;
  transform: translateY(100%);
  transition: transform 0.3s ease-out;
  z-index: 999;

  &.drawer-show {
    transform: translateY(0);
  }
}

.drawer-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 30rpx;
  border-bottom: 1rpx solid #f5f5f5;

  .drawer-title {
    font-size: 32rpx;
    font-weight: bold;
    color: #333;
  }

  .drawer-close {
    font-size: 40rpx;
    color: #999;
    padding: 10rpx;
  }
}

.drawer-content {
  padding: 30rpx;
}

.borrow-section {
  margin-bottom: 40rpx;

  .section-title {
    font-size: 28rpx;
    color: #666;
    margin-bottom: 20rpx;
  }

  .days-options {
    display: flex;
    gap: 20rpx;

    .day-option {
      flex: 1;
      height: 80rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      border: 2rpx solid #eee;
      border-radius: 8rpx;
      font-size: 28rpx;
      color: #333;

      &.active {
        background: #007aff;
        color: #fff;
        border-color: #007aff;
      }
    }
  }

  .count-selector {
    display: flex;
    align-items: center;
    justify-content: center;
    margin-bottom: 20rpx;

    .count-btn {
      width: 60rpx;
      height: 60rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      border: 2rpx solid #eee;
      border-radius: 30rpx;
      font-size: 36rpx;
      color: #333;

      &.disabled {
        color: #ccc;
        border-color: #f5f5f5;
      }
    }

    .count-number {
      margin: 0 40rpx;
      font-size: 32rpx;
      color: #333;
      min-width: 60rpx;
      text-align: center;
    }
  }

  .stock-info {
    text-align: center;
    font-size: 24rpx;
    color: #999;
  }
}

.drawer-footer {
  padding: 20rpx 30rpx 40rpx;

  .confirm-btn {
    width: 100%;
    height: 88rpx;
    background: #007aff;
    color: #fff;
    border-radius: 44rpx;
    font-size: 30rpx;
    font-weight: bold;
    display: flex;
    align-items: center;
    justify-content: center;

    &:active {
      opacity: 0.9;
    }
  }
}
</style>
