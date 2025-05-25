<template>
  <view class="home-container">
    <view class="search-box" @click="navigateToSearch">
      <wd-search
        v-model="value"
        placeholder="搜索图书"
        disabled
        maxlength="10"
        :cancel-text-display="false"
      />
    </view>
    <view class="swiper-container">
      <wd-swiper
        :list="swiperList"
        autoplay
        v-model:current="current"
        :indicator="{ type: 'dots-bar' }"
        @click="handleClick"
        @change="onChange"
      ></wd-swiper>
    </view>
    <view class="notice-container">
      <wd-notice-bar
        text="这是一条消息提示信息，这是一条消息提示信息，这是一条消息提示信息"
        prefix="warn-bold"
      />
      <wd-grid :column="3">
        <wd-grid-item icon="layers" text="文字" />
        <wd-grid-item icon="heart-filled" text="文字" />
        <wd-grid-item icon="star-filled" text="文字" />
        <wd-grid-item icon="picture" text="文字" />
        <wd-grid-item icon="picture" text="文字" />
        <wd-grid-item icon="picture" text="文字" />
      </wd-grid>
      <wd-gap bg-color="#FFFFFF"></wd-gap>
      <wd-divider>精选好书</wd-divider>
      <view class="featured-books">
        <view class="book-list">
          <view
            class="book-card"
            v-for="(book, index) in featuredBooks"
            :key="index"
          >
            <image
              class="book-image"
              :src="book.imageUrl"
              mode="aspectFill"
            ></image>
            <view class="book-info">
              <view class="book-main-info">
                <text class="book-title">{{ book.title }}</text>
                <text class="book-author">作者：{{ book.author }}</text>
              </view>
              <view class="book-price-container">
                <text class="book-price">¥{{ book.price }}</text>
              </view>
            </view>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref } from "vue";
// 导入需要的组件
import WdSwiper from "wot-design-uni/components/wd-swiper/wd-swiper.vue";

const title = "欢迎使用";
const current = ref(0);
const value = ref("");

const swiperList = ref([
  "https://fjwtest.oss-cn-beijing.aliyuncs.com/6F607E20F502E6BACBEFEDFCEEEB3F8D%281%29.jpg",
  "https://fjwtest.oss-cn-beijing.aliyuncs.com/DCF80E7BD8AE85793614A7E498BC54E2.jpg",
  "https://fjwtest.oss-cn-beijing.aliyuncs.com/FE9B5309EC30931A0B11F607E03A6138.jpg",
  "https://fjwtest.oss-cn-beijing.aliyuncs.com/0822A77ED915367F3496BB6EFF5A5A60.jpg",
]);

// 精选好书数据
const featuredBooks = ref([
  {
    id: 1,
    title: "Vue.js实战指南",
    author: "张三",
    price: "59.00",
    imageUrl:
      "https://fjwtest.oss-cn-beijing.aliyuncs.com/6F607E20F502E6BACBEFEDFCEEEB3F8D%281%29.jpg",
  },
  {
    id: 2,
    title: "JavaScript高级程序设计",
    author: "李四",
    price: "99.00",
    imageUrl:
      "https://fjwtest.oss-cn-beijing.aliyuncs.com/DCF80E7BD8AE85793614A7E498BC54E2.jpg",
  },
  {
    id: 3,
    title: "Python数据分析",
    author: "王五",
    price: "79.00",
    imageUrl:
      "https://registry.npmmirror.com/wot-design-uni-assets/*/files/panda.jpg",
  },
  {
    id: 4,
    title: "深入理解计算机系统",
    author: "赵六",
    price: "129.00",
    imageUrl:
      "https://fjwtest.oss-cn-beijing.aliyuncs.com/FE9B5309EC30931A0B11F607E03A6138.jpg",
  },
  {
    id: 5,
    title: "算法导论",
    author: "孙七",
    price: "89.00",
    imageUrl:
      "https://fjwtest.oss-cn-beijing.aliyuncs.com/0822A77ED915367F3496BB6EFF5A5A60.jpg",
  },
]);

function handleClick(e) {
  console.log(e);
}

function onChange(e) {
  console.log(e);
}

function search() {
  console.log("搜索内容:", value.value);
  // 在这里添加搜索逻辑
  uni.showToast({
    title: "搜索: " + value.value,
    icon: "none",
  });
}

function focus() {
  console.log("获取焦点");
}

function blur() {
  console.log("失去焦点");
}

function clear() {
  console.log("清空内容");
}

function change(val) {
  console.log("内容变化:", val);
}

function navigateToSearch() {
  uni.navigateTo({
    url: "/pages/search/index",
  });
}
</script>

<style>
.home-container {
  padding: 20rpx;
  box-sizing: border-box;
  width: 100%;
}

.search-box {
  margin-bottom: 20rpx;
}

/* Hide cancel button in search box */
.search-box :deep(.wd-search__cancel) {
  display: none !important;
}

.swiper-container {
  margin-top: 40rpx;
  margin-bottom: 20rpx;
  margin-left: 30rpx;
  margin-right: 30rpx;
}

.notice-container {
  margin-top: 20rpx;
  margin-bottom: 20rpx;
  margin-left: 30rpx;
  margin-right: 30rpx;
}

.content {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.logo {
  height: 200rpx;
  width: 200rpx;
  margin-top: 200rpx;
  margin-left: auto;
  margin-right: auto;
  margin-bottom: 50rpx;
}

.text-area {
  display: flex;
  justify-content: center;
}

.title {
  font-size: 36rpx;
  color: #8f8f94;
}

.card-swiper {
  --wot-swiper-radius: 0;
  --wot-swiper-item-padding: 0 24rpx;
  --wot-swiper-nav-dot-color: #e7e7e7;
  --wot-swiper-nav-dot-active-color: #4d80f0;
  padding-bottom: 24rpx;
  width: 100%;
  height: 360rpx;
}
.card-swiper :deep(.custom-indicator-class) {
  bottom: -16px;
}
.card-swiper :deep(.custom-image) {
  border-radius: 12rpx;
  height: 320rpx;
}
.card-swiper :deep(.custom-image-prev) {
  height: 320rpx !important;
}

/* 精选好书样式 */
.featured-books {
  margin: 20rpx 0;
  padding: 0 30rpx;
}

.book-list {
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.book-card {
  display: flex;
  width: 100%;
  height: 200rpx;
  background: #fff;
  border-radius: 12rpx;
  box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.book-image {
  width: 140rpx;
  height: 200rpx;
  object-fit: cover;
  flex-shrink: 0;
}

.book-info {
  flex: 1;
  padding: 20rpx;
  display: flex;
  justify-content: space-between;
}

.book-main-info {
  flex: 1;
  margin-right: 20rpx;
}

.book-title {
  font-size: 28rpx;
  font-weight: bold;
  color: #333;
  display: block;
  margin-bottom: 10rpx;
}

.book-author {
  font-size: 24rpx;
  color: #666;
  display: block;
}

.book-price-container {
  display: flex;
  align-items: center;
}

.book-price {
  font-size: 32rpx;
  color: #ff6b6b;
  font-weight: bold;
}
</style>
