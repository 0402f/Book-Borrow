<template>
  <view class="wraper">
    <wd-sidebar v-model="active" @change="handleChange">
      <wd-sidebar-item
        v-for="(item, index) in categories"
        :key="index"
        :value="item.id"
        :label="item.label"
      />
    </wd-sidebar>
    <scroll-view class="content" scroll-y scroll-with-animation>
      <view v-if="bookList.length > 0" class="book-list">
        <view class="book-grid">
          <view
            v-for="(book, index) in bookList"
            :key="book.id"
            class="book-card"
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
              <text class="book-desc" v-if="book.des" v-html="book.des"></text>
            </view>
          </view>
        </view>
      </view>
      <view v-else class="empty-state"> 当前分类暂无书籍 </view>
    </scroll-view>
  </view>
</template>

<script lang="ts" setup>
import { onMounted, ref } from "vue";
import {
  getSplitInfo,
  getBookByCategoryIdService,
} from "../../api/bookCart.js";
import { getBookDetailByIdService } from "../../api/bookCart.js";

// 定义书籍类型接口
interface Book {
  id: number;
  title: string;
  author: string;
  publisher: string;
  publishDate: string;
  imageUrl: string;
  des?: string;
}

const active = ref(1);
const categories = ref<
  {
    id: number;
    label: string;
    title: string;
    items: any[];
  }[]
>([]);

// 使用定义的 Book 接口作为类型
const bookList = ref<Book[]>([]);

onMounted(async () => {
  const res = await getSplitInfo();
  categories.value = res.data.data.map((category) => ({
    id: category.id,
    label: category.name,
    title: category.name,
    items: [],
  }));

  if (categories.value.length > 0) {
    const firstCategoryId = categories.value[0].id;
    await loadBooks(firstCategoryId);
  }
});

const loadBooks = async (categoryId: number) => {
  try {
    const res = await getBookByCategoryIdService(Number(categoryId), Number(1));
    bookList.value = res.data.data || [];
    console.log("书籍列表响应:", res);
  } catch (error) {
    console.error("获取书籍列表失败:", error);
    bookList.value = [];
  }
  console.log(bookList.value);
};

const handleChange = async ({ value }: { value: number }) => {
  console.log("当前选中的分类 ID:", value);
  if (value !== undefined && value !== null) {
    await loadBooks(value);
  } else {
    console.warn("分类 ID 不存在");
    bookList.value = [];
  }
};

// 处理书籍点击事件
const handleBookClick = (book) => {
  uni.navigateTo({
    url: `/pages/book/detail?id=${book.id}`,
  });
};
</script>

<style lang="scss" scoped>
.wraper {
  display: flex;
  height: calc(100vh - var(--window-top));
  height: calc(100vh - var(--window-top) - constant(safe-area-inset-bottom));
  height: calc(100vh - var(--window-top) - env(safe-area-inset-bottom));
}
.content {
  flex: 1;
  background: #fff;
  padding: 15px;
}
.empty-state {
  padding: 20px;
  text-align: center;
  color: #999;
}
.book-list {
  padding: 0;
}
.book-grid {
  display: flex;
  flex-direction: column;
  gap: 15px;
}
.book-card {
  width: 100%;
  background-color: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  margin-bottom: 10px;
  display: flex;
  height: 160px;
  transition: transform 0.2s ease;

  &:active {
    transform: scale(0.98);
  }
}
.book-image {
  width: 120px;
  height: 160px;
  object-fit: cover;
  flex-shrink: 0;
}
.book-info {
  padding: 12px;
  display: flex;
  flex-direction: column;
  gap: 4px;
  flex: 1;
  overflow: hidden;
}
.book-title {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 2px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.book-author,
.book-publisher,
.book-date {
  font-size: 13px;
  color: #666;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.book-desc {
  font-size: 13px;
  color: #333;
  margin-top: 4px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
