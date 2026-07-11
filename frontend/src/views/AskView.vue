<template>
  <div>
    <AppHeader />
    <div class="page-container">
      <el-card>
        <template #header><h3>发布问题</h3></template>
        <el-form :model="form" label-width="80px" ref="formRef" :rules="rules">
          <el-form-item label="标题" prop="title">
            <el-input v-model="form.title" placeholder="简要描述你的问题" maxlength="200" show-word-limit />
          </el-form-item>
          <el-form-item label="分类" prop="categoryId">
            <el-select v-model="form.categoryId" placeholder="选择分类" style="width: 100%">
              <el-option 
                v-for="cat in categories" 
                :key="cat.id" 
                :label="cat.name" 
                :value="cat.id" 
              />
            </el-select>
          </el-form-item>
          <el-form-item label="悬赏积分" prop="pointsReward">
            <el-input-number v-model="form.pointsReward" :min="0" :max="100" :step="5" />
          </el-form-item>
          <el-form-item label="内容" prop="content">
            <el-input v-model="form.content" type="textarea" :rows="8" placeholder="详细描述你的问题，包括：&#10;1. 问题背景&#10;2. 你已尝试的解决方案&#10;3. 报错信息（如有）" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSubmit" :loading="submitting">发布</el-button>
            <el-button @click="$router.push('/questions')">取消</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { createQuestion } from '../api/question'
import { getCategoryList } from '../api/category'
import { ElMessage } from 'element-plus'
import AppHeader from '../components/AddHeader.vue'

const router = useRouter()
const formRef = ref(null)
const submitting = ref(false)
const categories = ref([])

const form = reactive({
  title: '',
  content: '',
  categoryId: null,
  pointsReward: 0
})

const rules = {
  title: [
    { required: true, message: '请输入问题标题', trigger: 'blur' },
    { min: 5, message: '标题至少5个字符', trigger: 'blur' }
  ],
  categoryId: [
    { required: true, message: '请选择分类', trigger: 'change' }
  ],
  content: [
    { required: true, message: '请输入问题描述', trigger: 'blur' },
    { min: 10, message: '描述至少10个字符', trigger: 'blur' }
  ]
}

onMounted(async () => {
  try {
    categories.value = await getCategoryList() || []
  } catch (e) {
    console.error('获取分类失败:', e)
    // 降级：使用默认分类
    categories.value = [
      { id: 1, name: 'Java' },
      { id: 2, name: '数据库' },
      { id: 3, name: '前端' },
      { id: 4, name: '算法' },
      { id: 5, name: 'Spring' },
      { id: 6, name: '运维' },
      { id: 7, name: '其他' }
    ]
  }
})

const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    submitting.value = true
    await createQuestion(form)
    ElMessage.success('发布成功')
    router.push('/questions')
  } catch (e) {
    if (e.message) {
      ElMessage.error(e.message || '发布失败')
    }
    // 表单校验失败不显示错误
  } finally {
    submitting.value = false
  }
}
</script>

<style scoped>
.page-container {
  max-width: 800px;
  margin: 20px auto;
  padding: 0 20px;
}
</style>
