<template>
  <view class="search-results-page">
    <view class="results-header">
      <text class="results-title">搜索结果</text>
      <text class="results-count" v-if="bookList.length > 0"
        >找到 {{ bookList.length }} 本相关书籍</text
      >
    </view>

    <!-- 排序和视图切换工具栏 -->
    <view class="toolbar">
      <view class="sort-options">
        <view
          class="sort-item"
          :class="{
            active: currentSort === 'default',
            loading: isLoading && currentSort === 'default',
          }"
          @click="changeSort('default')"
        >
          <text>综合</text>
          <wd-loading
            v-if="isLoading && currentSort === 'default'"
            size="14px"
            color="#ff4444"
          />
        </view>
        <view
          class="sort-item"
          :class="{
            active: currentSort === 'stock',
            loading: isLoading && currentSort === 'stock',
          }"
          @click="changeSort('stock')"
        >
          <text>库存</text>
          <wd-loading
            v-if="isLoading && currentSort === 'stock'"
            size="14px"
            color="#ff4444"
          />
        </view>
        <view
          class="sort-item"
          :class="{
            active: currentSort === 'date',
            loading: isLoading && currentSort === 'date',
          }"
          @click="changeSort('date')"
        >
          <text>出版时间</text>
          <wd-loading
            v-if="isLoading && currentSort === 'date'"
            size="14px"
            color="#ff4444"
          />
        </view>
      </view>
      <view
        class="view-toggle"
        :class="{ loading: isViewLoading }"
        @click="toggleView"
      >
        <text class="view-text">{{ isGridView ? "单列" : "双列" }}</text>
        <wd-icon :name="isGridView ? 'list' : 'grid'" size="20" />
        <wd-loading v-if="isViewLoading" size="14px" />
      </view>
    </view>

    <!-- 加载中遮罩 -->
    <view class="loading-overlay" v-if="isLoading || isViewLoading">
      <wd-loading size="30px" />
    </view>

    <scroll-view
      class="results-content"
      scroll-y
      :class="{ loading: isLoading || isViewLoading }"
    >
      <view
        v-if="sortedBooks.length > 0"
        :class="['book-list', { 'grid-view': isGridView }]"
      >
        <view
          v-for="book in sortedBooks"
          :key="book.id"
          :class="['book-card', { 'grid-card': isGridView }]"
          @click="handleBookClick(book)"
        >
          <image
            class="book-image"
            :src="book.imageUrl"
            mode="aspectFill"
          ></image>
          <view class="book-info">
            <text class="book-title">{{ book.title }}</text>
            <text class="book-author">作者: {{ book.author }}</text>
            <text class="book-publisher">出版社: {{ book.publisher }}</text>
            <text class="book-date">出版日期: {{ book.publishDate }}</text>
            <view class="stock-info">
              <text class="stock-label">库存:</text>
              <text :class="['stock-number', getStockClass(book.stock)]">{{
                book.stock
              }}</text>
            </view>
          </view>
        </view>
      </view>
      <view v-else class="no-results">
        <text>暂无相关书籍</text>
      </view>
    </scroll-view>
  </view>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from "vue";
import { getBookDetailByIdService } from "../../api/bookCart.js";
import { useUserStore } from "../../store/user.js";

const bookList = ref([]);
const currentSort = ref("default");
const isGridView = ref(false);
const isLoading = ref(false);
const isViewLoading = ref(false);
const userStore = useUserStore();

// 检查登录状态
const checkLoginStatus = () => {
  const token = userStore.token;
  if (!token) {
    uni.setStorageSync(
      "lastVisitedPage",
      JSON.stringify({
        path: "pages/search/index",
      })
    );

    uni.reLaunch({
      url: "/pages/login/login",
    });
    return false;
  }
  return true;
};

// 处理返回按钮点击
const handleBackPress = () => {
  uni.navigateBack({
    fail: () => {
      // 如果返回失败（没有上一页），则导航到搜索页
      uni.navigateTo({
        url: "/pages/search/index",
      });
    },
  });
};

// 从本地存储加载搜索结果
const loadSearchResults = () => {
  try {
    const savedResults = uni.getStorageSync("lastSearchResults");
    if (savedResults) {
      bookList.value = JSON.parse(savedResults);
    }
  } catch (e) {
    console.error("加载搜索结果失败", e);
    bookList.value = [];
  }
};

// 计算排序后的图书列表
const sortedBooks = computed(() => {
  const books = [...bookList.value];
  switch (currentSort.value) {
    case "stock":
      return books.sort((a, b) => b.stock - a.stock);
    case "date":
      return books.sort(
        (a, b) => new Date(b.publishDate) - new Date(a.publishDate)
      );
    default:
      return books;
  }
});

// 切换排序方式
const changeSort = async (sortType) => {
  if (currentSort.value === sortType || isLoading.value) return;

  isLoading.value = true;
  currentSort.value = sortType;

  // 模拟排序延迟
  await new Promise((resolve) => setTimeout(resolve, 500));
  isLoading.value = false;
};

// 切换视图模式
const toggleView = async () => {
  if (isViewLoading.value) return;

  isViewLoading.value = true;
  // 模拟视图切换延迟
  await new Promise((resolve) => setTimeout(resolve, 300));
  isGridView.value = !isGridView.value;

  // 保存用户的视图偏好
  try {
    uni.setStorageSync("preferred-view", isGridView.value ? "grid" : "list");
  } catch (e) {
    console.error("保存视图偏好失败", e);
  }

  isViewLoading.value = false;
};

// 获取库存数量的样式类
const getStockClass = (stock) => {
  if (stock <= 10) return "stock-low";
  if (stock <= 50) return "stock-medium";
  return "stock-high";
};

// 在组件挂载时读取用户的视图偏好和搜索结果
onMounted(() => {
  if (!checkLoginStatus()) return;

  // 尝试从本地存储加载搜索结果
  const savedResults = uni.getStorageSync("lastSearchResults");
  if (savedResults) {
    try {
      bookList.value = JSON.parse(savedResults);
    } catch (e) {
      console.error("解析搜索结果失败", e);
    }
  }

  // 尝试从事件通道获取新的搜索结果
  const pages = getCurrentPages();
  const currentPage = pages[pages.length - 1];
  if (currentPage.$page?.fullPath.includes("/pages/search/results")) {
    const eventChannel = currentPage.getOpenerEventChannel();
    eventChannel.on("searchResults", (data) => {
      if (data && data.length > 0) {
        bookList.value = data;
        // 保存新的搜索结果到本地存储
        try {
          uni.setStorageSync("lastSearchResults", JSON.stringify(data));
        } catch (e) {
          console.error("保存搜索结果失败", e);
        }
      }
    });
  }

  // 读取用户的视图偏好
  try {
    const preferredView = uni.getStorageSync("preferred-view");
    if (preferredView) {
      isGridView.value = preferredView === "grid";
    }
  } catch (e) {
    console.error("读取视图偏好失败", e);
  }
});

// 在组件卸载时移除拦截器
onUnmounted(() => {
  uni.removeInterceptor("navigateBack");
});

const handleBookClick = (book) => {
  // 保存当前的搜索结果到本地存储
  try {
    uni.setStorageSync("lastSearchResults", JSON.stringify(bookList.value));
  } catch (e) {
    console.error("保存搜索结果失败", e);
  }

  // 使用navigateTo而不是redirectTo，这样可以保持页面栈
  uni.navigateTo({
    url: `/pages/book/detail?id=${book.id}`,
  });
};
</script>

<style lang="scss">
.search-results-page {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding: 20rpx;
  position: relative;
}

.results-header {
  padding: 20rpx;
  background-color: #fff;
  border-radius: 12rpx;
  margin-bottom: 20rpx;
}

.results-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  display: block;
  margin-bottom: 10rpx;
}

.results-count {
  font-size: 24rpx;
  color: #666;
}

.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #fff;
  padding: 20rpx;
  border-radius: 12rpx;
  margin-bottom: 20rpx;
}

.sort-options {
  display: flex;
  gap: 30rpx;
}

.sort-item {
  font-size: 28rpx;
  color: #666;
  padding: 10rpx 20rpx;
  border-radius: 30rpx;
  transition: all 0.3s;
  display: flex;
  align-items: center;
  gap: 8rpx;

  &.active {
    color: #ff4444;
    font-weight: bold;
    background-color: #fff5f5;
  }

  &.loading {
    opacity: 0.7;
  }
}

.view-toggle {
  padding: 10rpx 20rpx;
  display: flex;
  align-items: center;
  gap: 12rpx;
  background-color: #f8f8f8;
  border-radius: 30rpx;
  transition: all 0.3s;

  &:active {
    background-color: #f0f0f0;
  }

  &.loading {
    opacity: 0.7;
  }
}

.view-text {
  font-size: 26rpx;
  color: #666;
}

.loading-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(255, 255, 255, 0.6);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 100;
  pointer-events: none;
}

.results-content {
  position: relative;
  transition: opacity 0.3s;

  &.loading {
    opacity: 0.6;
  }
}

.book-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
  animation: fadeIn 0.3s ease;

  &.grid-view {
    flex-direction: row;
    flex-wrap: wrap;
    gap: 20rpx;
    justify-content: space-between;
  }
}

.book-card {
  background-color: #fff;
  border-radius: 12rpx;
  overflow: hidden;
  box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.1);
  display: flex;
  height: 280rpx;
  transition: all 0.3s ease;
  animation: fadeIn 0.3s ease;

  &.grid-card {
    flex-direction: column;
    width: calc(50% - 10rpx);
    height: auto;

    .book-image {
      width: 100%;
      height: 300rpx;
    }

    .book-info {
      padding: 16rpx;
    }

    .book-title {
      font-size: 26rpx;
      margin-bottom: 8rpx;
      display: -webkit-box;
      -webkit-line-clamp: 2;
      -webkit-box-orient: vertical;
      overflow: hidden;
    }

    .book-author,
    .book-publisher,
    .book-date {
      font-size: 22rpx;
      white-space: nowrap;
      overflow: hidden;
      text-overflow: ellipsis;
    }
  }

  &:active {
    transform: scale(0.98);
  }
}

.book-image {
  width: 200rpx;
  height: 280rpx;
  object-fit: cover;
  flex-shrink: 0;
}

.book-info {
  flex: 1;
  padding: 20rpx;
  display: flex;
  flex-direction: column;
  gap: 8rpx;
}

.book-title {
  font-size: 28rpx;
  font-weight: bold;
  color: #333;
}

.book-author,
.book-publisher,
.book-date {
  font-size: 24rpx;
  color: #666;
}

.stock-info {
  display: flex;
  align-items: center;
  gap: 8rpx;
  margin-top: 8rpx;
}

.stock-label {
  font-size: 24rpx;
  color: #666;
}

.stock-number {
  font-size: 28rpx;
  font-weight: bold;

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

.no-results {
  padding: 100rpx 0;
  text-align: center;
  color: #999;
  font-size: 28rpx;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10rpx);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
</style>
