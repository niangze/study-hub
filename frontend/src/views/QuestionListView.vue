<template>
  <div>
    <AppHeader />
    <div class="page-container">
      <el-card>
        <template #header>
          <div class="header-flex">
            <span>问题列表</span>
            <el-input v-model="keyword" placeholder="搜索问题" style="width: 300px">
              <template #append>
                <el-button @click="handleSearch"><el-icon><Search /></el-icon></el-button>
              </template>
            </el-input>
          </div>
        </template>
        <el-empty v-if="questions.length === 0" description="暂无问题" />
        <div v-for="q in questions" :key="q.id" class="question-card" @click="viewQuestion(q.id)">
          <h3>{{ q.title }}</h3>
          <p>{{ q.content }}</p>
          <div class="meta">
            <el-tag size="small">悬赏 {{ q.pointsReward }} 积分</el-tag>
            <el-tag :type="q.status === 'OPEN' ? 'success' : 'info'" size="small">{{ q.status }}</el-tag>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getQuestionList, searchQuestions } from '../api/question'
import AppHeader from '../components/AppHeader.vue'

const router = useRouter()
const questions = ref([])
const keyword = ref('')

onMounted(async () => {
  questions.value = await getQuestionList()
})

const handleSearch = async () => {
  if (keyword.value) {
    questions.value = await searchQuestions(keyword.value)
  } else {
    questions.value = await getQuestionList()
  }
}

const viewQuestion = (id) => {
  router.push(`/question/${id}`)
}
</script>

<style scoped>
.page-container {
  max-width: 1200px;
  margin: 20px auto;
  padding: 0 20px;
}
.header-flex {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.question-card {
  padding: 20px;
  border-bottom: 1px solid #eee;
  cursor: pointer;
}
.question-card:hover {
  background: #f5f7fa;
}
.question-card h3 {
  color: #409eff;
  margin-bottom: 8px;
}
.question-card p {
  color: #666;
  margin-bottom: 10px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
.meta {
  display: flex;
  gap: 10px;
}
</style>
