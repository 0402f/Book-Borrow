<script setup>
import { ref } from 'vue'
import { getIndexInfo } from '@/api/indexInfo'

// 进入页面时获取首页信息
// 定义一个数组来存储图片路径
const images = ref([])

// 从后台获取数据并赋值给images数组
const getInfo = async () => {
  try {
    const imag = await getIndexInfo()
    images.value = imag.data.data.map((item) => item.img)
  } catch (error) {
    console.error('获取图片信息失败:', error)
  }
}
// 页面加载时调用getInfo函数
getInfo()
</script>

<template>
  <el-main class="main-container">
    <el-carousel :interval="4000" type="card" height="400px" class="carousel">
      <el-carousel-item v-for="(item, index) in images" :key="index">
        <div class="image-wrapper">
          <img :src="item" alt="carousel image" />
        </div>
      </el-carousel-item>
    </el-carousel>
    <div class="text-area">
      <p>这里可以添加一些介绍性的文字，让用户更好地了解页面内容。</p>
    </div>
    <div class="modules-container">
      <div class="module">
        <i class="fa-solid fa-chart-line"></i>
        <p>模块一</p>
        <p class="module-description">模块一可以展示一些关键数据或重要信息。</p>
      </div>
      <div class="module">
        <i class="fa-solid fa-users"></i>
        <p>模块二</p>
        <p class="module-description">模块二可以用于展示团队成员或合作伙伴。</p>
      </div>
    </div>
  </el-main>
</template>

<style scoped>
/* 确保主内容区域充满可用空间 */
.main-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  padding-top: 0; /* 移除顶部内边距 */
  margin-top: 0px; /* 移除顶部外边距 */
}

/* 文字区域样式 */
.text-area {
  text-align: center; /* 文字居中 */
  padding: 40px; /* 增加内边距 */
  background-color: #f9fafb; /* 更浅的背景颜色 */
  color: #374151; /* 文字颜色 */
  overflow-wrap: break-word; /* 自动换行 */
  word-break: break-all; /* 强制换行 */
  font-size: 18px; /* 增大字体大小 */
  line-height: 1.6; /* 增加行高 */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05); /* 添加轻微阴影 */
}

/* 模块容器样式 */
.modules-container {
  display: flex; /* 使用 Flex 布局 */
  justify-content: space-around; /* 模块之间的间距 */
  margin: 40px 0; /* 模块容器的上下间距 */
  gap: 20px; /* 模块之间的间距 */
}

/* 模块样式 */
.module {
  flex: 1; /* 使模块均分空间 */
  text-align: center; /* 文字居中 */
  padding: 30px; /* 添加内边距 */
  background-color: #ffffff; /* 模块背景颜色 */
  color: #374151; /* 文字颜色 */
  border-radius: 8px; /* 圆角 */
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); /* 阴影 */
  transition: transform 0.3s ease; /* 过渡效果 */
}

.module:hover {
  transform: translateY(-5px); /* 鼠标悬停时向上移动 */
}

.module i {
  font-size: 36px; /* 图标大小 */
  color: #ffa500; /* 图标颜色 */
  margin-bottom: 16px; /* 图标与文字的间距 */
}

.module-description {
  font-size: 14px; /* 描述文字大小 */
  color: #6b7280; /* 描述文字颜色 */
  margin-top: 8px; /* 描述文字与标题的间距 */
}

/* 轮播图样式 */
.carousel {
  width: 100%;
  margin-top: 0; /* 移除顶部间距 */
  margin-bottom: 20px; /* 添加底部间距以分隔轮播图和文字区域 */
  height: 400px; /* 设置轮播图高度 */
}

.el-carousel__item {
  height: 400px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.el-carousel__item h3 {
  color: #475669;
  opacity: 0.75;
  line-height: 1.2;
  margin: 0;
  text-align: center;
}

.image-wrapper {
  width: 100%;
  height: 100%;
  overflow: hidden; /* 裁剪超出部分 */
}

.image-wrapper img {
  width: 100%;
  height: 100%;
  object-fit: cover; /* 裁剪图片以适应容器 */
}

.el-carousel__item:nth-child(2n) {
  background-color: rgba(153, 169, 191, 0.5); /* 设置半透明背景 */
}

.el-carousel__item:nth-child(2n + 1) {
  background-color: rgba(153, 169, 191, 0.5); /* 设置半透明背景 */
}
</style>
