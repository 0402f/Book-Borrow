<template>
  <view class="borrow-page">
    <!-- 加载状态 -->
    <view v-if="loading" class="loading-state">
      <text>加载中...</text>
    </view>

    <!-- 空状态 -->
    <view v-else-if="borrowList.length === 0" class="empty-state">
      <image src="/static/empty.png" mode="aspectFit" class="empty-image" />
      <text>暂无借阅记录</text>
    </view>

    <!-- 借阅列表 -->
    <view v-else class="borrow-list">
      <view
        v-for="(item, index) in borrowList"
        :key="item.borrowId"
        class="borrow-card"
      >
        <view class="book-info">
          <image :src="item.imageUrl" mode="aspectFill" class="book-cover" />
          <view class="info-content">
            <text class="book-title">{{ item.title }}</text>
            <text v-if="item.borrowIsn" class="borrow-isn"
              >借阅编号：{{ item.borrowIsn }}</text
            >
            <view class="borrow-detail">
              <view class="count-info">
                <text class="count">借阅数量：{{ item.counts }}本</text>
                <text v-if="item.returnCounts > 0" class="return-count">
                  已归还：{{ item.returnCounts }}本
                </text>
                <text
                  v-if="item.returnCounts < item.counts"
                  class="remain-count"
                >
                  待归还：{{ item.counts - item.returnCounts }}本
                </text>
              </view>
              <text class="days">借阅天数：{{ item.days }}天</text>
            </view>
            <view class="date-info">
              <text class="date-label"
                >借阅日期：{{ formatDate(item.borrowDate) }}</text
              >
              <text class="date-label"
                >应还日期：{{ formatDate(item.dueDate) }}</text
              >
              <text v-if="item.returnDate" class="date-label"
                >归还日期：{{ formatDate(item.returnDate) }}</text
              >
              <view
                v-else-if="item.status === 2 || item.status === 0"
                :class="[
                  'date-label',
                  'remaining-days',
                  calculateRemainingDays(item.dueDate) > 0
                    ? 'remaining-normal'
                    : 'remaining-critical',
                ]"
              >
                <text
                  >{{
                    calculateRemainingDays(item.dueDate) <= 0
                      ? "已逾期"
                      : "剩余天数"
                  }}：</text
                >
                <text
                  :class="
                    calculateRemainingDays(item.dueDate) <= 0
                      ? 'overdue-days'
                      : ''
                  "
                >
                  {{ Math.abs(calculateRemainingDays(item.dueDate)) }}天
                </text>
              </view>
            </view>
          </view>
        </view>
        <view class="card-footer">
          <view class="borrow-status">
            <text
              :class="['status-tag', getStatusClass(item.status, item.dueDate)]"
            >
              {{ getStatusText(item.status, item.dueDate) }}
            </text>
          </view>
          <view class="action-buttons">
            <button 
              v-if="item.status === 2"
              class="return-btn"
              @click.stop="handleReturn(item.borrowId)"
            >
              归还图书
            </button>
            <button class="delete-btn" @click.stop="handleDelete(item.borrowId)">
              删除记录
            </button>
          </view>
        </view>
      </view>
    </view>
    
    <!-- 归还弹窗遮罩层 -->
    <view class="popup-mask" v-if="showReturnDialog || showCountDialog" @click="closeAllDialogs"></view>
    
    <!-- 归还图书弹窗 -->
    <view class="return-dialog" v-if="showReturnDialog">
      <view class="dialog-header">
        <text class="dialog-title">归还图书</text>
      </view>
      <view class="dialog-content" v-if="currentBorrow">
        <view class="borrow-info">
          <text class="info-label">借阅编号：</text>
          <text class="info-value">{{ currentBorrow.borrowIsn }}</text>
        </view>
        <view class="borrow-info">
          <text class="info-label">借阅数量：</text>
          <text class="info-value">{{ currentBorrow.counts }}本</text>
        </view>
        <view class="borrow-info">
          <text class="info-label">待归还：</text>
          <text class="info-value">{{ currentBorrow.counts - currentBorrow.returnCounts }}本</text>
        </view>
        <view class="borrow-info">
          <text class="info-label">归还数量：</text>
          <text class="info-value return-count-value">{{ returnCount }}本</text>
        </view>
        
        <button 
          v-if="currentBorrow.counts - currentBorrow.returnCounts > 1"
          class="select-count-btn" 
          @click="showCountSelector"
        >
          {{ hasSelectedCount ? '重新选择归还数量' : '请选择归还数量' }}
        </button>
      </view>
      <view class="dialog-footer">
        <view class="footer-btn cancel-btn" @click="closeAllDialogs">取消</view>
        <view class="footer-btn confirm-btn" @click="onConfirmReturn">确认归还</view>
      </view>
    </view>
    
    <!-- 选择归还数量弹窗 -->
    <view class="count-dialog" v-if="showCountDialog">
      <view class="dialog-header">
        <text class="dialog-title">选择归还数量</text>
      </view>
      <view class="dialog-content">
        <view class="count-row">
          <text class="count-label">归还数量：</text>
        </view>
        <view class="number-selector">
          <view class="number-btn minus" @click="decreaseCount">-</view>
          <view class="number-value">{{ returnCount }}</view>
          <view class="number-btn plus" @click="increaseCount">+</view>
        </view>
        <button class="count-confirm-btn" @click="confirmCountSelection">确定</button>
      </view>
    </view>
  </view>
</template>

<script setup>
import { ref, onMounted } from "vue";
import { onPullDownRefresh, onShow } from "@dcloudio/uni-app";
import { getBorrowHistory, deleteBorrowHistory, getBorrowHistoryById, returnBorrowedBook } from "../../api/borrow.js";

const loading = ref(false);
const borrowList = ref([]);
const currentBorrow = ref(null);
const returnCount = ref(1);
const showReturnDialog = ref(false);
const showCountDialog = ref(false);
const hasSelectedCount = ref(false);

// 格式化日期
const formatDate = (dateStr) => {
  if (!dateStr) return "";
  const date = new Date(dateStr);
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(
    2,
    "0"
  )}-${String(date.getDate()).padStart(2, "0")}`;
};

// 计算剩余天数或逾期天数
const calculateRemainingDays = (dueDate) => {
  if (!dueDate) return 0;
  const today = new Date();
  const due = new Date(dueDate);
  const diffTime = due - today;
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
  return diffDays;
};

// 获取状态文本
const getStatusText = (status, dueDate) => {
  // 如果是借阅中状态，检查是否逾期
  if (status === 2 && calculateRemainingDays(dueDate) <= 0) {
    return "已逾期";
  }
  const statusMap = {
    0: "已逾期",
    1: "已归还",
    2: "借阅中",
  };
  return statusMap[status] || "未知状态";
};

// 获取借阅/逾期信息文本
const getBorrowTimeText = (status, dueDate) => {
  const days = calculateRemainingDays(dueDate);
  if (status === 2) {
    if (days > 0) {
      return `剩余天数：${days}天`;
    } else {
      return `已逾期：${Math.abs(days)}天`;
    }
  } else if (status === 0) {
    return `已逾期：${Math.abs(days)}天`;
  }
  return "";
};

// 获取状态样式类
const getStatusClass = (status, dueDate) => {
  // 如果是借阅中状态，检查是否逾期
  if (status === 2 && calculateRemainingDays(dueDate) <= 0) {
    return "overdue";
  }
  const statusClassMap = {
    0: "overdue",
    1: "returned",
    2: "active",
  };
  return statusClassMap[status] || "";
};

// 获取借阅记录
const loadBorrowHistory = async () => {
  loading.value = true;
  try {
    const res = await getBorrowHistory();
    // 按借阅时间降序排序
    borrowList.value = res.data.data.sort((a, b) => {
      return new Date(b.borrowDate) - new Date(a.borrowDate);
    });
  } catch (error) {
    console.error("获取借阅记录失败:", error);
    uni.showToast({
      title: "获取借阅记录失败",
      icon: "none",
    });
  } finally {
    loading.value = false;
    uni.stopPullDownRefresh();
  }
};

// 删除借阅记录
const handleDelete = async (borrowId) => {
  try {
    const modalRes = await new Promise((resolve) => {
      uni.showModal({
        title: "提示",
        content: "确定要删除这条借阅记录吗？",
        success: (res) => resolve(res),
        fail: () => resolve({ confirm: false }),
      });
    });
    
    if (!modalRes.confirm) return;
    
    // 执行删除操作
    await deleteBorrowHistory(Number(borrowId));
    
    // 删除成功后显示提示
    uni.showToast({
      title: "删除成功",
      icon: "success",
      duration: 2000,
    });
    
    // 重新加载借阅记录
    await loadBorrowHistory();
  } catch (error) {
    console.error("删除借阅记录失败:", error);
  }
};

// 下拉刷新
onPullDownRefresh(() => {
  loadBorrowHistory();
});

// 页面显示时加载数据
onShow(() => {
  loadBorrowHistory();
});

// 页面首次加载
onMounted(() => {
  loadBorrowHistory();
});

// 处理归还图书
const handleReturn = async (borrowId) => {
  try {
    console.log('归还图书，借阅ID:', borrowId);
    // 先获取借阅详情
    const res = await getBorrowHistoryById(borrowId);
    console.log('借阅详情响应:', res);
    
    if (!res.data || res.data.code !== 1) {
      uni.showToast({
        title: '获取借阅信息失败',
        icon: 'none'
      });
      return;
    }
    
    // 设置当前借阅信息
    currentBorrow.value = res.data.data;
    
    // 计算待归还数量
    const remainCount = currentBorrow.value.counts - currentBorrow.value.returnCounts;
    
    // 如果待归还数量为1，直接设置归还数量为1并显示归还确认弹窗
    if (remainCount === 1) {
      returnCount.value = 1;
      hasSelectedCount.value = true;
      showReturnDialog.value = true;
    } else {
      // 如果待归还数量大于1，直接显示数量选择器
      returnCount.value = 1;
      hasSelectedCount.value = false;
      showCountDialog.value = true;
    }
  } catch (error) {
    console.error('获取借阅信息失败:', error);
    uni.showToast({
      title: '获取借阅信息失败',
      icon: 'none'
    });
  }
};

// 显示数量选择器
const showCountSelector = () => {
  showReturnDialog.value = false;
  showCountDialog.value = true;
};

// 确认数量选择
const confirmCountSelection = () => {
  showCountDialog.value = false;
  showReturnDialog.value = true;
  hasSelectedCount.value = true;
};

// 关闭所有弹窗
const closeAllDialogs = () => {
  showReturnDialog.value = false;
  showCountDialog.value = false;
  hasSelectedCount.value = false;
};

// 确认归还按钮的点击事件处理函数
const onConfirmReturn = () => {
  console.log('确认归还按钮被点击');
  uni.showToast({
    title: '正在处理归还...',
    icon: 'loading',
    duration: 1000
  });
  setTimeout(() => {
    confirmReturn();
  }, 100);
};

// 确认归还
const confirmReturn = async () => {
  try {
    console.log('开始执行confirmReturn函数');
    // 检查是否已选择归还数量
    if (!hasSelectedCount.value && currentBorrow.value.counts - currentBorrow.value.returnCounts > 1) {
      console.log('需要选择归还数量');
      uni.showToast({
        title: '请先选择归还数量',
        icon: 'none'
      });
      return;
    }
    
    if (!currentBorrow.value || !currentBorrow.value.borrowId) {
      console.error('无效的借阅ID');
      uni.showToast({
        title: '借阅信息无效',
        icon: 'none'
      });
      return;
    }
    
    // 确保数据类型正确
    const borrowId = Number(currentBorrow.value.borrowId);
    const returnCounts = Number(returnCount.value);
    
    console.log('准备归还图书:', { borrowId, returnCounts });
    
    // 调用API进行归还 - 以对象形式传参
    console.log('调用returnBorrowedBook API, 参数:', { borrowId, returnCounts });
    const res = await returnBorrowedBook({
      borrowId: borrowId,
      returnCounts: returnCounts
    });
    
    console.log('API返回结果:', res);
    
    if (res.data && res.data.code === 1) {
      uni.showToast({
        title: '归还成功',
        icon: 'success'
      });
      closeAllDialogs();
      // 重新加载数据
      await loadBorrowHistory();
    } else {
      uni.showToast({
        title: res.data?.msg || '归还失败',
        icon: 'none'
      });
    }
  } catch (error) {
    console.error('归还图书失败:', error);
    console.error('错误详情:', JSON.stringify(error));
    uni.showToast({
      title: '归还图书失败',
      icon: 'none'
    });
  }
};

// 增加归还数量
const increaseCount = () => {
  if (currentBorrow.value && returnCount.value < (currentBorrow.value.counts - currentBorrow.value.returnCounts)) {
    returnCount.value++;
  }
};

// 减少归还数量
const decreaseCount = () => {
  if (returnCount.value > 1) {
    returnCount.value--;
  }
};
</script>

<style lang="scss">
.borrow-page {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding: 20rpx;
}

.loading-state,
.empty-state {
  height: 60vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #999;
  font-size: 28rpx;

  .empty-image {
    width: 200rpx;
    height: 200rpx;
    margin-bottom: 20rpx;
  }
}

.borrow-list {
  .borrow-card {
    background: #fff;
    border-radius: 12rpx;
    padding: 20rpx;
    margin-bottom: 20rpx;
    box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);

    .book-info {
      display: flex;
      margin-bottom: 20rpx;

      .book-cover {
        width: 160rpx;
        height: 220rpx;
        border-radius: 8rpx;
        margin-right: 20rpx;
      }

      .info-content {
        flex: 1;
        display: flex;
        flex-direction: column;

        .book-title {
          font-size: 32rpx;
          font-weight: bold;
          color: #333;
          margin-bottom: 12rpx;
        }

        .borrow-isn {
          font-size: 24rpx;
          color: #666;
          margin-bottom: 12rpx;
        }

        .borrow-detail {
          display: flex;
          flex-direction: column;
          gap: 8rpx;
          margin-bottom: 16rpx;
          font-size: 26rpx;

          .count-info {
            display: flex;
            flex-wrap: wrap;
            gap: 16rpx;
            margin-bottom: 4rpx;

            .count {
              color: #666;
            }

            .return-count {
              color: #4caf50;
            }

            .remain-count {
              color: #ff9800;
            }
          }

          .days {
            color: #666;
          }
        }

        .date-info {
          display: flex;
          flex-direction: column;
          gap: 8rpx;

          .date-label {
            font-size: 24rpx;
            color: #666;

            &.remaining-days {
              font-weight: bold;
              display: flex;
              align-items: center;

              .overdue-days {
                color: #ff4444;
              }

              &.remaining-critical {
                color: #666;
              }

              &.remaining-normal {
                color: #4caf50;
              }
            }
          }
        }
      }
    }

    .card-footer {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding-top: 20rpx;
      border-top: 1rpx solid #f0f0f0;

      .action-buttons {
        display: flex;
        gap: 16rpx;
        align-items: center;
      }

      .return-btn {
        padding: 10rpx 30rpx;
        font-size: 24rpx;
        color: #fff;
        background-color: #4caf50;
        border-radius: 30rpx;
        border: none;
        line-height: 1.5;
        margin: 0;

        &:active {
          opacity: 0.8;
        }
      }

      .delete-btn {
        padding: 10rpx 30rpx;
        font-size: 24rpx;
        color: #fff;
        background-color: #ff4444;
        border-radius: 30rpx;
        border: none;
        line-height: 1.5;
        margin: 0;

        &:active {
          opacity: 0.8;
        }
      }
    }

    .borrow-status {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding-top: 20rpx;
      border-top: 1rpx solid #f0f0f0;

      .status-tag {
        padding: 4rpx 16rpx;
        border-radius: 20rpx;
        font-size: 24rpx;
        background: #f0f0f0;
        color: #999;

        &.active {
          background: rgba(0, 122, 255, 0.1);
          color: #007aff;
        }

        &.returned {
          background: rgba(76, 175, 80, 0.1);
          color: #4caf50;
        }

        &.overdue {
          background: rgba(244, 67, 54, 0.1);
          color: #f44336;
        }
      }
    }

    .delete-btn {
      padding: 10rpx 30rpx;
      font-size: 24rpx;
      color: #fff;
      background-color: #ff4444;
      border-radius: 30rpx;
      border: none;
      line-height: 1.5;
      margin: 0;

      &:active {
        opacity: 0.8;
      }
    }
  }
}

.popup-mask {
  position: fixed;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 999;
}

.return-dialog, .count-dialog {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: #fff;
  border-radius: 16rpx;
  width: 580rpx;
  z-index: 1000;
  overflow: hidden;
  box-shadow: 0 4rpx 24rpx rgba(0, 0, 0, 0.12);
}

.dialog-header {
  padding: 36rpx 0;
  text-align: center;
  border-bottom: 1px solid #f0f0f0;
  background: linear-gradient(to right, #4caf50, #45a049);
  
  .dialog-title {
    font-size: 36rpx;
    font-weight: bold;
    color: #fff;
  }
}

.dialog-content {
  padding: 40rpx 30rpx;
  
  .borrow-info {
    display: flex;
    margin-bottom: 24rpx;
    font-size: 32rpx;
    
    .info-label {
      color: #666;
      width: 160rpx;
    }
    
    .info-value {
      color: #333;
      font-weight: 500;
      
      &.return-count-value {
        color: #4caf50;
        font-weight: bold;
      }
    }
  }
  
  .select-count-btn {
    width: 100%;
    height: 88rpx;
    line-height: 88rpx;
    text-align: center;
    background: linear-gradient(to right, #4caf50, #45a049);
    color: #fff;
    font-size: 32rpx;
    border: none;
    border-radius: 44rpx;
    margin-top: 30rpx;
    box-shadow: 0 4rpx 12rpx rgba(76, 175, 80, 0.2);
    transition: all 0.3s ease;
    
    &:active {
      transform: scale(0.98);
      box-shadow: 0 2rpx 8rpx rgba(76, 175, 80, 0.15);
    }
  }
  
  .count-row {
    margin-bottom: 36rpx;
    text-align: center;
    
    .count-label {
      font-size: 32rpx;
      color: #333;
      font-weight: 500;
    }
  }
  
  .number-selector {
    display: flex;
    align-items: center;
    justify-content: center;
    margin-bottom: 50rpx;
    background: #f8f8f8;
    border-radius: 8rpx;
    padding: 8rpx;
    
    .number-btn {
      width: 88rpx;
      height: 88rpx;
      line-height: 88rpx;
      text-align: center;
      font-size: 40rpx;
      color: #4caf50;
      background: #fff;
      border: 1px solid #e8e8e8;
      border-radius: 8rpx;
      transition: all 0.2s ease;
      
      &:active {
        background: #f0f0f0;
      }
      
      &.minus {
        border-top-right-radius: 0;
        border-bottom-right-radius: 0;
      }
      
      &.plus {
        border-top-left-radius: 0;
        border-bottom-left-radius: 0;
      }
    }
    
    .number-value {
      width: 140rpx;
      height: 88rpx;
      line-height: 88rpx;
      text-align: center;
      font-size: 36rpx;
      font-weight: bold;
      color: #333;
      background: #fff;
      border-top: 1px solid #e8e8e8;
      border-bottom: 1px solid #e8e8e8;
    }
  }
}

.dialog-footer {
  display: flex;
  border-top: 1px solid #f0f0f0;
  
  .footer-btn {
    flex: 1;
    height: 100rpx;
    line-height: 100rpx;
    text-align: center;
    font-size: 32rpx;
    transition: all 0.2s ease;
    
    &.cancel-btn {
      color: #666;
      border-right: 1px solid #f0f0f0;
      
      &:active {
        background: #f8f8f8;
      }
    }
    
    &.confirm-btn {
      color: #4caf50;
      font-weight: bold;
      
      &:active {
        background: #f8f8f8;
      }
    }
  }
  
  .count-confirm-btn {
    width: 240rpx;
    height: 88rpx;
    line-height: 88rpx;
    background: linear-gradient(to right, #4caf50, #45a049);
    color: #fff;
    font-size: 32rpx;
    font-weight: 500;
    border-radius: 44rpx;
    margin: 30rpx auto;
    box-shadow: 0 4rpx 12rpx rgba(76, 175, 80, 0.2);
    transition: all 0.3s ease;
    
    &:active {
      transform: scale(0.98);
      box-shadow: 0 2rpx 8rpx rgba(76, 175, 80, 0.15);
    }
  }
}
</style>
