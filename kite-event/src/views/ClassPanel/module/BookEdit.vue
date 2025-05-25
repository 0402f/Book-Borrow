<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { baseURL } from '@/utils/request'
import { QuillEditor } from '@vueup/vue-quill'
import { addBookService, uploadFileService, updateBookService } from '@/api/Book'
import { getBookCategoryList } from '@/api/BookCategory'
import '@vueup/vue-quill/dist/vue-quill.snow.css'
import moment from 'moment'

const options = ref([])

// 获取书籍分类列表
const getAllBookCategory = async () => {
  try {
    const res = await getBookCategoryList()
    //console.log('获取到的书籍分类列表', res)
    // 假设后端返回的分类列表在 res.data 中
    options.value = res.data.data.map((item) => ({
      value: item.id.toString(),
      label: item.name
    }))
  } catch (error) {
    console.error('获取书籍分类列表失败:', error)
    ElMessage.error('获取书籍分类列表失败，请重试')
  }
}
getAllBookCategory()
// 抽屉
const drawer = ref(false)
// 默认数据
const drawerModel = {
  id: '', // 书籍id
  title: '', // 书名
  author: '', // 作者
  publisher: '', // 出版社
  publish_date: '', // 出版日期
  category_id: '', // 分类id
  des: '', // 描述
  total: 0, // 总量
  image_url: '', // 图片地址
  stock: 0 // 库存
}
// 新增文章传入的参数
const formModel = ref({
  ...drawerModel
})
const imgUrl = ref('')
const editRef = ref()
const formRef = ref(null) // 用于表单校验

// 校验规则
const rules = {
  title: [{ required: true, message: '请输入书名', trigger: 'blur' }],
  author: [{ required: true, message: '请输入作者', trigger: 'blur' }],
  publisher: [{ required: true, message: '请输入出版社', trigger: 'blur' }],
  category_id: [{ required: true, message: '请选择分类', trigger: 'change' }],
  publish_date: [{ required: true, message: '请选择出版日期', trigger: 'change' }],
  total: [
    { required: true, message: '请输入书籍数量', trigger: 'blur' },
    { type: 'number', max: 1000, message: '数量不能超过1000', trigger: 'blur' }
  ],
  des: [{ required: true, message: '请输入书籍描述', trigger: 'blur' }],
  image_url: [{ required: true, message: '请上传书籍封面', trigger: 'blur' }]
}

// 向外暴露方法，决定打开方式
const open = async (row) => {
  drawer.value = true // 打开抽屉
  if (row.id) {
    // 编辑文章 将row对象回显到表单中
    formModel.value = { ...row }
    formModel.value.category_id = row.categoryId.toString() // 将分类id转换为字符串
    formModel.value.publish_date = moment(row.publish_date).format('YYYY-MM-DD')
    imgUrl.value = row.imageUrl // 将图片地址赋值给imgUrl
    formModel.value.image_url = row.imageUrl // 将图片地址赋值给表单
  } else {
    // 新增文章
    // 1.先重置表单
    formModel.value = { ...drawerModel }
    // 清空图片
    imgUrl.value = ''
    // 清空富文本编辑器
    editRef.value?.setHTML('')
  }
  await getAllBookCategory()
}

// 文件状态改变的钩子
const onSelectFile = async (file) => {
  // 1. 获取选中的文件对象
  const selectedFile = file.raw

  // 2. 创建FormData并添加文件字段（字段名需与后端接口参数一致，这里是"file"）
  const formData = new FormData()
  formData.append('file', selectedFile) // 后端接口参数是MultipartFile file，对应字段名"file"

  // 3. 调用上传接口获取URL
  try {
    const res = await uploadFileService(formData)
    //console.log('图片上传成功:', res)
    const imgUrlValue = res.data.data // 假设后端返回的url在response.data.data中（根据你的Result结构调整）
    //console.log('图片URL:', imgUrlValue)

    // 4. 更新图片预览和表单数据
    imgUrl.value = imgUrlValue
    formModel.value.image_url = imgUrlValue // 存储URL到表单，而非File对象
  } catch (error) {
    console.error('图片上传失败:', error)
    ElMessage.error('图片上传失败，请重试')
  }
}

const emit = defineEmits(['success'])

// 提交
const onPublish = async () => {
  // 表单校验
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      formModel.value.publish_date = moment(formModel.value.publish_date).format('YYYY-MM-DD')
      // 将表单数据转换为 JSON 格式
      const bookData = {
        id: formModel.value.id,
        title: formModel.value.title,
        author: formModel.value.author,
        publisher: formModel.value.publisher,
        publishDate: formModel.value.publish_date,
        categoryId: formModel.value.category_id,
        des: formModel.value.des,
        total: formModel.value.total,
        imageUrl: formModel.value.image_url
      }

      if (formModel.value.id) {
        // 修改操作
        // 这里调用修改接口，传入bookData
        await updateBookService(bookData)
        // await updateArticleService(bookData);
        ElMessage.success('修改成功!')
        emit('success', 'edit')
        drawer.value = false
      } else {
        // 新增操作
        await addBookService(bookData)
        ElMessage.success('添加成功!')
        drawer.value = false
        emit('success', 'add')
      }
    } else {
      console.log('表单校验失败')
      return false
    }
  })
}

// 向外暴露方法，决定关闭方式
defineExpose({
  open
})
</script>

<template>
  <el-drawer
    v-model="drawer"
    :title="formModel.id ? '编辑书籍' : '新增书籍'"
    direction="rtl"
    size="50%"
  >
    <!-- 发表文章表单 -->
    <el-form :model="formModel" ref="formRef" label-width="100px" :rules="rules">
      <el-form-item label="书籍名称" prop="title">
        <el-input v-model="formModel.title" placeholder="请输入书名"></el-input>
      </el-form-item>
      <el-form-item label="书籍作者" prop="author">
        <el-input v-model="formModel.author" placeholder="请输入作者"></el-input>
      </el-form-item>
      <el-form-item label="书籍出版社" prop="publisher">
        <el-input v-model="formModel.publisher" placeholder="请输入出版社"></el-input>
      </el-form-item>
      <el-form-item label="书籍分类" prop="category_id">
        <el-select
          v-model="formModel.category_id"
          placeholder="请选择分类"
          size="large"
          style="width: 240px"
        >
          <el-option
            v-for="item in options"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="出版日期" prop="publish_date">
        <el-date-picker v-model="formModel.publish_date" type="date" placeholder="选择日期">
          <template #suffix>
            <el-icon><Plus /></el-icon>
          </template>
        </el-date-picker>
      </el-form-item>

      <el-form-item label="书籍数量" prop="total">
        <el-input-number
          v-model.number="formModel.total"
          :min="0"
          :max="1000"
          :step="1"
          placeholder="请选择数量"
        />
        <!-- 灰色字体 -->
        <span v-if="formModel.id" style="margin-left: 10px; color: #999"
          >当前库存：{{ formModel.stock }},修改总量不能小于{{
            formModel.total - formModel.stock
          }}</span
        >
      </el-form-item>

      <el-form-item label="书籍封面" prop="image_url">
        <el-upload
          class="avatar-uploader"
          :show-file-list="false"
          :auto-upload="false"
          :on-change="onSelectFile"
        >
          <img v-if="imgUrl" :src="imgUrl" class="avatar" />
          <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
        </el-upload>
      </el-form-item>
      <el-form-item label="书籍描述" prop="des">
        <div class="editor">
          <div class="editor">
            <quill-editor
              ref="editRef"
              theme="snow"
              v-model:content="formModel.des"
              contentType="html"
            >
            </quill-editor>
          </div>
        </div>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onPublish">发布</el-button>
        <el-button @click="drawer = false">取消</el-button>
      </el-form-item>
    </el-form>
  </el-drawer>
</template>

<style lang="scss" scoped>
.avatar-uploader {
  :deep() {
    .avatar {
      width: 178px;
      height: 178px;
      display: block;
    }
    .el-upload {
      border: 1px dashed var(--el-border-color);
      border-radius: 6px;
      cursor: pointer;
      position: relative;
      overflow: hidden;
      transition: var(--el-transition-duration-fast);
    }
    .el-upload:hover {
      border-color: var(--el-color-primary);
    }
    .el-icon.avatar-uploader-icon {
      font-size: 28px;
      color: #8c939d;
      width: 178px;
      height: 178px;
      text-align: center;
    }
  }
}
.editor {
  width: 100%;
  :deep(.ql-editor) {
    min-height: 200px;
  }
}
</style>
