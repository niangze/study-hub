<template>
  <div>
    <AppHeader />
    <div class="page-container">
      <el-card>
        <template #header><h3>发布问题</h3></template>
        <el-form :model="form" label-width="80px">
          <el-form-item label="标题">
            <el-input v-model="form.title" placeholder="简要描述你的问题" />
          </el-form-item>
          <el-form-item label="分类">
            <el-select v-model="form.categoryId" placeholder="选择分类">
              <el-option label="Java" :value="1" />
              <el-option label="数据库" :value="2" />
              <el-option label="前端" :value="3" />
              <el-option label="其他" :value="4" />
            </el-select>
          </el-form-item>
          <el-form-item label="悬赏积分">
            <el-input-number v-model="form.pointsReward" :min="0" :max="100" />
          </el-form-item>
          <el-form-item label="内容">
            <el-input v-model="form.content" type="textarea" :rows="8" placeholder="详细描述你的问题..." />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSubmit">发布</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { useRouter } from 'vue-router'
import { createQuestion } from '../api/question'
import { ElMessage } from 'element-plus'
import AppHeader from '../components/AppHeader.vue'

const router = useRouter()

const form = reactive({
  title: '',
  content: '',
  categoryId: null,
  pointsReward: 0,
  status: 'OPEN'
})

const handleSubmit = async () => {
  await createQuestion(form)
  ElMessage.success('发布成功')
  router.push('/questions')
}
</script>

<style scoped>
.page-container {
  max-width: 800px;
  margin: 20px auto;
  padding: 0 20px;
}
</style>
