<template>
  <view class="search-page">
    <!-- 搜索栏 -->
    <view class="search-container">
      <view class="search-input-container">
        <wd-search
          v-model="searchValue"
          placeholder="请输入搜索内容"
          clearable
          show-action
          cancel-txt="搜索"
          @confirm="performSearch"
          @clear="clearSearchValue"
          @cancel="performSearch"
          focus
        />
      </view>
    </view>

    <!-- 搜索历史记录 -->
    <view
      class="search-history"
      v-if="searchHistory.length > 0 && !hasSearched"
    >
      <view class="history-header">
        <text class="history-title">搜索历史</text>
        <view class="clear-history" @click="clearHistory">
          <wd-icon name="delete" size="18" color="#999"></wd-icon>
        </view>
      </view>
      <view class="history-items">
        <view
          class="history-item"
          v-for="(item, index) in searchHistory"
          :key="index"
          @click="useHistoryItem(item)"
        >
          <text>{{ item }}</text>
        </view>
      </view>
    </view>

    <!-- 搜索结果 -->
    <view class="search-results" v-if="hasSearched">
      <view v-if="searchResults.length > 0">
        <view
          class="result-item"
          v-for="(item, index) in searchResults"
          :key="index"
        >
          <text>{{ item.title }}</text>
        </view>
      </view>
      <view v-else class="no-results">
        <text>暂无搜索结果</text>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { getBookByNameService } from "../../api/bookCart.js";
import { useUserStore } from "../../store/user.js";

const searchValue = ref("");
const searchHistory = ref([]);
const searchResults = ref([]);
const hasSearched = ref(false);
const userStore = useUserStore();

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

// 从本地存储加载搜索历史
onMounted(() => {
  if (!checkLoginStatus()) return;

  try {
    const history = uni.getStorageSync("searchHistory");
    if (history) {
      searchHistory.value = JSON.parse(history);
    }
  } catch (e) {
    console.error("获取搜索历史失败", e);
  }
});

// 清空搜索框
const clearSearchValue = () => {
  searchValue.value = "";
  hasSearched.value = false;
};

// 执行搜索
const performSearch = async () => {
  if (!checkLoginStatus()) return;

  if (!searchValue.value.trim()) {
    uni.showToast({
      title: "请输入搜索内容",
      icon: "none",
    });
    return;
  }

  hasSearched.value = true;

  // 保存到搜索历史
  if (!searchHistory.value.includes(searchValue.value)) {
    searchHistory.value.unshift(searchValue.value);
    // 限制历史记录数量
    if (searchHistory.value.length > 10) {
      searchHistory.value = searchHistory.value.slice(0, 10);
    }
    saveHistory();
  }

  try {
    const res = await getBookByNameService(searchValue.value);
    if (res && res.data && res.data.code === 1) {
      // 保存搜索结果到本地存储
      uni.setStorageSync(
        "lastSearchResults",
        JSON.stringify(res.data.data || [])
      );

      // 导航到搜索结果页面
      uni.navigateTo({
        url: "/pages/search/results",
        success: function (result) {
          // 传递搜索结果数据
          result.eventChannel.emit("searchResults", res.data.data || []);
        },
        fail: function (err) {
          console.error("页面跳转失败:", err);
          uni.showToast({
            title: "页面跳转失败",
            icon: "none",
          });
        },
      });
    } else {
      uni.showToast({
        title: res?.data?.msg || "搜索失败",
        icon: "none",
      });
    }
  } catch (error) {
    console.error("搜索失败", error);
    uni.showToast({
      title: "搜索失败，请重试",
      icon: "none",
    });
  }
};

// 保存搜索历史到本地存储
const saveHistory = () => {
  try {
    uni.setStorageSync("searchHistory", JSON.stringify(searchHistory.value));
  } catch (e) {
    console.error("保存搜索历史失败", e);
  }
};

// 清空搜索历史
const clearHistory = () => {
  uni.showModal({
    title: "提示",
    content: "确定要清空搜索历史吗？",
    success: (res) => {
      if (res.confirm) {
        searchHistory.value = [];
        uni.removeStorageSync("searchHistory");
      }
    },
  });
};

// 使用历史记录项
const useHistoryItem = (item) => {
  searchValue.value = item;
  performSearch();
};
</script>

<style>
.search-page {
  background-color: #f5f5f5;
  min-height: 100vh;
}

.search-container {
  background-color: #ffffff;
  padding: 20rpx;
}

.search-input-container {
  width: 100%;
}

/* 自定义搜索按钮样式 */
:deep(.wd-search__cancel) {
  color: #007aff !important;
  font-size: 28rpx !important;
}

.search-history {
  background-color: #ffffff;
  margin-top: 20rpx;
  padding: 20rpx;
}

.history-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20rpx;
}

.history-title {
  font-size: 28rpx;
  color: #333;
  font-weight: bold;
}

.clear-history {
  font-size: 24rpx;
  color: #999;
}

.history-items {
  display: flex;
  flex-wrap: wrap;
}

.history-item {
  padding: 10rpx 20rpx;
  background-color: #f0f0f0;
  border-radius: 30rpx;
  margin-right: 20rpx;
  margin-bottom: 20rpx;
  font-size: 24rpx;
}

.search-results {
  background-color: #ffffff;
  margin-top: 20rpx;
  padding: 20rpx;
  min-height: 200rpx;
}

.result-item {
  padding: 20rpx 0;
  border-bottom: 1px solid #f0f0f0;
}

.no-results {
  text-align: center;
  padding: 40rpx 0;
  color: #999;
  font-size: 28rpx;
}
</style>
