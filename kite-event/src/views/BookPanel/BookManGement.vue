<template>
  <el-row class="tac" style="height: 100vh; display: flex">
    <!-- 左侧菜单 -->
    <el-col :span="3" style="padding: 0; position: fixed; height: 100vh; width: 100px">
      <div class="menu-box" @touchmove.prevent>
        <el-menu ref="menuRef" default-active="1" class="custom-menu" @select="handleMenuSelect">
          <template v-for="menuItem in menuList" :key="menuItem.id">
            <el-menu-item :index="menuItem.id.toString()">
              <span>{{ menuItem.name }}</span>
            </el-menu-item>
          </template>
        </el-menu>
      </div>
    </el-col>
    <!-- 右侧内容 -->
    <el-col :span="21" style="height: 100vh; padding: 0; margin-left: 100px">
      <div class="book-content" ref="bookContentRef">
        <div class="book-list">
          <el-card class="book-card" v-for="book in books" :key="book.id">
            <el-row>
              <el-col :span="4">
                <div class="book-card-left">
                  <img class="book-cover" :src="book.imageUrl" alt="书籍封面" />
                </div>
              </el-col>
              <el-col :span="13">
                <div class="book-card-middle">
                  <!-- 按顺序展示：书名、作者、出版社、出版日期、描述 -->
                  <h3 class="book-title">《{{ book.title }}》</h3>
                  <div class="book-author">作者：{{ book.author }}</div>
                  <div class="book-publisher">出版社：{{ book.publisher }}</div>
                  <div class="book-pub-date">出版日期：{{ book.publishDate }}</div>
                  <div class="book-description">描述： <span v-html="book.des"></span></div>
                </div>
              </el-col>
              <el-col :span="5">
                <div class="book-card-right">
                  <div class="book-stock">当前总量：{{ book.total }}</div>
                  <div class="book-stock">当前库存：{{ book.stock }}</div>
                  <div class="operation-buttons">
                    <el-button type="primary" size="small" @click="editBook(book)">编辑</el-button>
                    <!-- 可添加更多按钮 -->
                    <el-button type="danger" size="small" @click="downBook(book.id)"
                      >下架</el-button
                    >
                  </div>
                </div>
              </el-col>
            </el-row>
          </el-card>
        </div>
      </div>
    </el-col>
  </el-row>

  <BookEdit ref="ArticleRef" @success="onSuccess" />
</template>

<script lang="ts" setup>
import { ref, nextTick } from 'vue'
import { getBookCategoryList } from '@/api/BookCategory'
import { getBookByCategoryIdService, UpOrDownBookService } from '@/api/Book'
import BookEdit from '../ClassPanel/module/BookEdit.vue'
import { ElMessage } from 'element-plus'

const bookContentRef = ref<HTMLElement | null>(null)

// 分类菜单
const menuList = ref([])
const getBookCategorylist = async () => {
  const res = await getBookCategoryList()
  console.log('获取到的分类列表', res)
  menuList.value = res.data.data
  if (menuList.value.length > 0) {
    const firstMenuItemId = menuList.value[0].id.toString()
    handleMenuSelect(firstMenuItemId)
  }
}
getBookCategorylist()

// 获取书籍列表
const getBookByCategoryListById = async (categoryId) => {
  const res = await getBookByCategoryIdService(categoryId, 1)
  console.log('获取到的书籍列表', res)
  books.value = res.data.data
}
const bookCategoryId = ref(1)
const books = ref([])
const handleMenuSelect = async (key) => {
  bookCategoryId.value = key
  await getBookByCategoryListById(key)
}

// ------ 编辑书籍信息---------
const ArticleRef = ref() // 获取子组件的引用
// 回调函数
// 编辑成功后调用的函数
const onSuccess = () => {
  // 重新获取书籍列表
  getBookByCategoryListById(bookCategoryId.value)
}
const editBook = (book) => {
  console.log('点击编辑按钮，当前书籍信息：', book)
  ArticleRef.value.open(book)
}
// ------ 下架书籍-----------
const downBook = async (bookid) => {
  console.log('点击下架按钮，当前书籍ID：', bookid)
  await UpOrDownBookService(bookid, 0) // 下架操作
  // 重新获取书籍列表
  getBookByCategoryListById(bookCategoryId.value)
  // 提示下架成功
  ElMessage.success('下架成功')
}
</script>

<style scoped>
/* 左侧菜单样式 */
.menu-box {
  background-color: #f0f0f0;
  box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1);
}

.custom-menu {
  border-right: none;
}

.custom-menu .el-menu-item {
  transition: all 0.3s ease;
  padding: 12px 20px;
  &:hover {
    background-color: #e0e0e0;
    transform: translateX(5px);
  }
  &.is-active {
    background-color: #d0d0d0;
    box-shadow: inset 4px 0 0 #007bff;
    font-weight: bold;
  }
}

/* 书籍卡片样式 */
.book-card {
  width: 100%;
  border: 1px solid #e9ecef;
  margin-left: 10px;
  margin-top: 5px;
  border-radius: 12px; /* 加大圆角 */
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08); /* 优化阴影 */
  transition:
    box-shadow 0.3s ease,
    transform 0.2s ease;
  background-color: #ffffff;
}

.book-cover {
  width: 100%; /* 封面占满左侧列 */
  height: auto;
  border-radius: 8px; /* 封面圆角 */
}

.book-card-middle {
  padding: 24px;
  display: flex;
  flex-direction: column; /* 垂直排列内容 */
  align-items: flex-start; /* 内容左对齐 */
  border: none; /* 移除左右边框 */
}

.book-title {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 8px;
  text-align: left; /* 左对齐 */
  color: #007bff;
}

.book-author,
.book-publisher,
.book-pub-date,
.book-description {
  font-size: 14px;
  color: #495057;
  margin-bottom: 6px;
  text-align: left; /* 统一左对齐 */
  line-height: 1.6; /* 优化行高 */
}

.book-description {
  color: #6c757d;
  margin-top: 12px; /* 描述与其他信息间隔 */
  line-height: 1.8;
}

/* 右侧区域优化 */
.book-card-right {
  padding: 24px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between; /* 垂直分布内容 */
}

.book-stock {
  font-size: 14px;
  color: #6c757d;
  margin-bottom: 16px; /* 加大库存与按钮间距 */
}

.add-book-fixed-area {
  position: sticky;
  top: 0;
  background-color: white;
  padding: 10px 20px;
  border-bottom: 1px solid #e9ecef;
  z-index: 1;
}
</style>
