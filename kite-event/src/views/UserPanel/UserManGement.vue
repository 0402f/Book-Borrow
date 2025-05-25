<script setup>
import { getUserInfoService, deleteUserService, getUserByIdService } from '@/api/user'
import { ref } from 'vue'
import { ElMessageBox, ElMessage } from 'element-plus'
// 用户分页数据
const tableData = ref([])
// 定义请求参数
const params = ref({
  pageNum: 1,
  pageSize: 10
})
// 分页数据
const total = ref(0)
// 分页大小
const size = ref(3)
// 设置loading加载状态
const loading = ref(false)
// 设置抽屉状态
const drawer = ref(false)
// 用户信息
const userInfo = ref({})

// 基于请求参数获取用户数据
const getUserInfo = async () => {
  // 设置loading加载状态
  loading.value = true
  const res = await getUserInfoService(params.value)
  console.log('获取到的用户数据', res)
  console.log(res.data.data.items)
  // 将获取到的数据赋值给tableData
  tableData.value = res.data.data.items
  // 将获取到的总条数赋值给total
  total.value = res.data.data.total
  // 设置loading加载状态
  loading.value = false
}
// 页面加载时调用getUserInfo函数
getUserInfo()

// 编辑按钮点击事件
const onEdit = (row) => {
  console.log('编辑按钮点击', row)
  // 打开抽屉
  drawer.value = true
  // 更据当前行的id来请求服务获取当前用户信息
  getUserByIdService(row.id).then((res) => {
    console.log('获取到的用户信息', res)
    // 将获取到的用户信息赋值给userInfo
    userInfo.value = res.data.data
    console.log('获取到的用户信息', userInfo.value)
  })
}
// 删除按钮点击事件
const onDelete = async (row) => {
  console.log('删除按钮点击', row)
  // 获取到当前行数据的id
  console.log('当前行数据的id', row.id)
  // 弹窗提示
  await ElMessageBox.confirm('确认删除吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
  // 调用删除接口
  await deleteUserService(row.id)
  // 提示删除成功
  ElMessage.success('删除成功')
  // 删除成功后重新获取用户数据
  getUserInfo()
}

// 处理分页逻辑
// 1. 分页大小变化
const onSizeChange = (size) => {
  console.log('分页大小', size)
  params.value.pageNum = 1
  // 更新请求参数
  params.value.pageSize = size
  // 调用获取用户数据的函数
  getUserInfo()
}
// 2. 当前页码变化
const onCurrentChange = (page) => {
  console.log('当前页码', page)
  // 更新请求参数
  params.value.pageNum = page
  // 调用获取用户数据的函数
  getUserInfo()
}
</script>

<template>
  <PageContainer>
    <div class="user-container">
      <h4 class="title">用户管理</h4>
      <div style="width: 100%; display: flex; justify-content: space-between; align-items: center">
        <el-table :data="tableData" highlight-current-row="true" width="100%" v-loading="loading">
          <el-table-column prop="nickName" label="用户昵称" />
          <el-table-column prop="iphone" label="电话号码" />
          <el-table-column prop="userNumber" label="用户账号" />
          <el-table-column prop="password" label="用户密码" />
          <!-- 操作列 -->
          <el-table-column label="操作">
            <!-- 使用插槽可以获取到当前行数据 -->
            <template #default="{ row }">
              <el-button type="primary" size="small" @click="onEdit(row)">编辑</el-button>
              <el-button type="danger" size="small" @click="onDelete(row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <!-- 分页 -->
      <el-pagination
        v-model:current-page="params.pageNum"
        v-model:page-size="params.pageSize"
        :page-sizes="[2, 3, 5, 10]"
        :size="size"
        :background="true"
        layout="jumper,total, sizes, prev, pager, next"
        :total="total"
        @size-change="onSizeChange"
        @current-change="onCurrentChange"
        style="margin-top: 20px; justify-content: flex-end"
      />
    </div>
  </PageContainer>
  <!-- 抽屉 -->
  <el-drawer v-model="drawer" title="用户信息" :with-header="false" size="50%">
    <div class="user-info">
      <div class="avatar-container">
        <img :src="userInfo.avtor" alt="用户头像" class="avatar" />
      </div>
      <div class="info-details">
        <p><span class="label">昵称:</span> {{ userInfo.nickName }}</p>
        <p><span class="label">电话号码:</span> {{ userInfo.iphone }}</p>
        <p><span class="label">用户账号:</span> {{ userInfo.userNumber }}</p>
        <p><span class="label">注册时间:</span> 2023-01-01</p>
        <p><span class="label">用户等级:</span> VIP1</p>
      </div>
    </div>
  </el-drawer>
</template>

<style scoped>
.user-container {
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.title {
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  font-size: 20px;
  font-weight: 600;
  color: #333;
  margin-bottom: 20px;
}

.el-table {
  border-radius: 4px;
  overflow: hidden;
}

.el-table th {
  background-color: #f0f0f0;
  font-weight: 500;
}

.el-button {
  border-radius: 4px;
}

.el-pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.user-info {
  display: flex;
  align-items: flex-start;
  padding: 20px 30px;
}

.avatar-container {
  margin-right: 30px;
}

.avatar {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  object-fit: cover;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.info-details {
  flex: 1;
}

.info-details p {
  margin: 12px 0;
  font-size: 16px;
  line-height: 1.6;
}

.label {
  font-weight: 600;
  margin-right: 8px;
  color: #555;
  min-width: 80px;
  display: inline-block;
}
</style>
