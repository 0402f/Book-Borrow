<script lang="ts" setup>
import { ref } from 'vue'
import { useTransition } from '@vueuse/core'
import { ChatLineRound, UserFilled, Notebook } from '@element-plus/icons-vue'
import {
  getBookCategoryListService,
  addBookCategoryService,
  getBookCategoryCountService,
  deleteBookCategoryService,
  updateBookCategoryService
} from '@/api/BookCategory'
import { getUserCountService } from '@/api/user'
import { getBookStockervice } from '@/api/Book'
import { ElMessage, ElMessageBox } from 'element-plus'
import BookEdit from './module/BookEdit.vue'
// 6.分页逻辑
// 设置loading加载状态
const loading = ref(false)
// 1.定义一个变量来存储分类列表
const bookCategoryList = ref([])
// 1. 定义一个变量来存储请求参数
const params = ref({
  pageNum: 1,
  pageSize: 5
})
// 2. 定义一个变量来存储分页大小
const size = ref(3)
// 3. 定义一个变量来存储总条数
const total = ref(0)

// 2.定义一个函数来获取分类列表
const getBookCategoryList = async () => {
  // 1. 设置loading加载状态
  loading.value = true
  const res = await getBookCategoryListService(params.value)
  // 模拟延迟
  await new Promise((resolve) => setTimeout(resolve, 1000))
  console.log('获取到的分类列表', res)
  // 2.1 将获取到的分类列表赋值给bookCategoryList
  bookCategoryList.value = res.data.data.items

  // 2.2 将获取到的总条数赋值给total
  total.value = res.data.data.total
  // 3. 关闭loading加载状态
  loading.value = false
}
// 3.页面加载时调用getBookCategoryList函数
getBookCategoryList()

// 4.删除按钮点击事件
const onDelete = async (row) => {
  console.log('删除按钮点击', row)
  // 1. 弹窗提示
  await ElMessageBox.confirm('确认删除吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
  // 1.调用删除分类的接口
  const res = await deleteBookCategoryService(row.id)
  console.log('删除分类的接口', res)
  // 2.提示删除成功
  ElMessage.success('删除成功')
  // 3.重新获取分类列表
  getBookCategoryList()
  // 4.重新获取分类数量
  getBookCategoryCount()
}
// -----编辑分类----- //
const currentEditRow = ref({
  id: '',
  name: '',
  des: '',
  bookCount: ''
})
const ClassEdit = ref(false)
// 5.编辑按钮点击事件
const onEdit = (row) => {
  ClassEdit.value = true
  console.log('编辑按钮点击', row)
  currentEditRow.value = { ...row } // 深拷贝当前行数据
}

const onSave = async () => {
  try {
    // 验证表单数据
    if (!currentEditRow.value.name || !currentEditRow.value.des) {
      ElMessage.error('请填写完整的分类信息')
      return
    }
    // 构建请求参数
    const requestParams = {
      id: currentEditRow.value.id,
      name: currentEditRow.value.name,
      des: currentEditRow.value.des
    }
    // 调用更新分类的接口
    const res = await updateBookCategoryService(requestParams)
    console.log('更新分类的接口', res)

    // 提示编辑成功
    ElMessage.success('编辑成功')

    // 关闭抽屉
    ClassEdit.value = false

    // 刷新分类列表
    getBookCategoryList()
  } catch (error) {
    ElMessage.error('编辑失败，请稍后再试')
  }
}

// 4. 定义一个函数来处理分页大小变化
const onSizeChange = (size) => {
  console.log('分页大小', size)
  params.value.pageNum = 1
  // 更新请求参数
  params.value.pageSize = size
  // 调用获取分类列表的函数
  getBookCategoryList()
}

// 5. 定义一个函数来处理当前页码变化
const onCurrentChange = (page) => {
  console.log('当前页码', page)
  params.value.pageNum = page
  // 调用获取分类列表的函数
  getBookCategoryList()
}

// -----------------------------
// 2.新增分类
// 设置一个抽屉用来
// 设置抽屉状态
const drawer = ref(false)
// 1.定义一个变量来存储新增分类的表单数据
const form = ref({
  name: '',
  des: ''
})

// 2.定义一个变量来存储新增分类的表单验证规则
const rules = ref({
  name: [
    { required: true, message: '请输入分类名', trigger: 'blur' },
    { min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: 'blur' }
  ],
  des: [
    { required: true, message: '请输入分类描述', trigger: 'blur' },
    { min: 2, max: 50, message: '长度在 2 到 50 个字符', trigger: 'blur' }
  ]
})
// 3.定义一个变量来存储新增分类的表单验证结果
const formRef = ref(null)
// 4.定义一个函数来提交新增分类的表单
const onSubmit = () => {
  drawer.value = true
}
// 5.定义一个函数来提交新增分类的表单
const submit = async () => {
  // 1.验证表单
  const valid = await formRef.value.validate()
  if (!valid) {
    return
  }
  // 2.调用新增分类的接口
  const res = await addBookCategoryService(form.value)
  console.log('新增分类的接口', res)
  // 3.提示新增成功
  ElMessage.success('新增成功')
  // 4.关闭抽屉
  drawer.value = false
  // 5.清空表单数据
  form.value.name = ''
  form.value.des = ''
  // 6.重新获取分类列表
  getBookCategoryList()
  // 7.重新获取分类数量
  getBookCategoryCount()
}

// 6.定义一个函数来重置表单
const resetForm = () => {
  formRef.value.resetFields()
  form.value.name = ''
  form.value.des = ''
  drawer.value = false
}

// ------获取书籍数量-------- //
const count = ref(0)
const getBookStock = async () => {
  const res = await getBookStockervice()
  console.log('获取到的书籍数量', res)
  count.value = res.data.data
}
// 页面加载时调用getBookStock函数
getBookStock()
// ------获取用户数量-------- //
const userCount = ref(0)
const getUserCount = async () => {
  const res = await getUserCountService()
  console.log('获取到的用户数量', res)
  userCount.value = res.data.data
}
// 页面加载时调用getUserCount函数
getUserCount()
// ------获取分类数量-------- //
// 1.定义一个函数来获取分类数量
const source = ref(0)
const outputValue = useTransition(source, {
  duration: 1500
})
source.value = 17625
const getBookCategoryCount = async () => {
  const res = await getBookCategoryCountService()
  console.log('获取到的分类数量', res)
  source.value = res.data.data
}
// 页面加载时调用getBookCategoryCount函数
getBookCategoryCount()

// ---新增书籍--- //
const addBook = () => {
  console.log('新增书籍')
  // 打开抽屉
  ArticleRef.value.open({})
}

// 定义新增书籍的ref，用来调用子组件的方法
const ArticleRef = ref()
//  添加成功的回调
const onSuccess = () => {
  console.log('新增书籍成功')
  // 1.关闭抽屉
  ArticleRef.value.bookdrawer = false
  // 更新总书籍数量
  getBookStock()
  // 更新分类列表
  getBookCategoryList()
}
</script>

<template>
  <el-row>
    <el-col :span="6">
      <el-statistic :value="count">
        <template #title>
          <div style="display: inline-flex; align-items: center">
            总书籍数量
            <el-icon style="margin-left: 4px" :size="12">
              <Notebook />
            </el-icon>
          </div>
        </template>
      </el-statistic>
    </el-col>
    <el-col :span="6">
      <el-statistic :value="userCount">
        <template #title>
          <div style="display: inline-flex; align-items: center">
            用户数量
            <el-icon style="margin-left: 4px" :size="12">
              <UserFilled />
            </el-icon>
          </div>
        </template>
      </el-statistic>
    </el-col>
    <el-col :span="6">
      <el-statistic title="分类数" :value="outputValue" />
    </el-col>
    <el-col :span="6">
      <el-statistic title="Feedback number" :value="562">
        <template #suffix>
          <el-icon style="vertical-align: -0.125em">
            <ChatLineRound />
          </el-icon>
        </template>
      </el-statistic>
    </el-col>
  </el-row>

  <el-divider />
  <el-button type="primary" @click="onSubmit">新增分类</el-button>
  <el-button type="primary" style="margin-left: 10%" @click="addBook">新增书籍</el-button>
  <el-divider />
  <el-table :data="bookCategoryList" style="width: 100%" v-loading="loading">
    <el-table-column prop="name" label="分类名" width="180" />
    <el-table-column prop="des" label="描述" />
    <el-table-column prop="bookCount" label="种类数" />
    <el-table-column label="操作">
      <!-- 使用插槽可以获取到当前行数据 -->
      <template #default="{ row }">
        <el-button type="primary" size="small" @click="onEdit(row)">编辑</el-button>
        <el-button type="danger" size="small" @click="onDelete(row)">删除</el-button>
      </template>
    </el-table-column>
  </el-table>
  <!-- 抽屉方向从左侧滑入 -->
  <el-drawer v-model="ClassEdit" title="编辑分类" :with-header="true" direction="ltr" size="50%">
    <div class="drawer-content">
      <el-form :model="currentEditRow" label-width="100px" class="edit-form">
        <el-form-item label="分类ID">
          <el-input v-model="currentEditRow.id" :disabled="true" placeholder="分类ID" />
        </el-form-item>
        <el-form-item label="分类名称">
          <el-input v-model="currentEditRow.name" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="分类描述">
          <el-input v-model="currentEditRow.des" placeholder="请输入分类描述" />
        </el-form-item>
        <el-form-item label="种类数">
          <el-input v-model="currentEditRow.bookCount" :disabled="true" placeholder="种类数" />
        </el-form-item>
      </el-form>
      <div class="drawer-footer">
        <el-button @click="ClassEdit = false">取消</el-button>
        <el-button type="primary" @click="onSave">保存</el-button>
      </div>
    </div>
  </el-drawer>

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
  <!-- 新增分类 -->
  <el-dialog v-model="drawer" title="添加分类" width="40%" class="custom-dialog" @close="resetForm">
    <div class="dialog-content">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px" class="custom-form">
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="分类描述" prop="des">
          <el-input v-model="form.des" placeholder="请输入分类描述" />
        </el-form-item>
      </el-form>
    </div>
    <template #footer>
      <div class="dialog-footer">
        <el-button @click="resetForm">取消</el-button>
        <el-button type="primary" @click="submit">确认</el-button>
      </div>
    </template>
  </el-dialog>
  <!-- 新增书籍 -->
  <BookEdit ref="ArticleRef" @success="onSuccess" />
</template>

<style scoped>
.el-col {
  text-align: center;
}
/* 弹窗样式 */
.custom-dialog :deep(.el-dialog) {
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

/* 弹窗内容样式 */
.dialog-content {
  padding: 20px;
}

/* 表单样式 */
.custom-form {
  width: 100%;
}

/* 表单按钮样式 */
.dialog-footer {
  text-align: right;
  padding-right: 20px;
  padding-bottom: 20px;
}

/* 输入框样式 */
.el-input {
  width: 100%;
}

.drawer-footer {
  display: flex;
  justify-content: flex-end;
  padding: 10px 20px;
}
</style>
