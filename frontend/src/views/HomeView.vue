<template>
  <div>
    <AppHeader />
    <div class="home-container">
      <el-row :gutter="20">
        <el-col :span="16">
          <el-card>
            <template #header>
              <span>最新问题</span>
              <el-button link type="primary" @click="$router.push('/questions')">查看更多</el-button>
            </template>
            <div v-for="q in questions" :key="q.id" class="question-item" @click="viewQuestion(q.id)">
              <h4>{{ q.title }}</h4>
              <p class="meta">悬赏: {{ q.pointsReward }} 积分 | 状态: {{ q.status }}</p>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card>
            <template #header>快速操作</template>
            <el-button type="primary" @click="$router.push('/ask')" style="width: 100%; margin-bottom: 10px">
              <el-icon><Plus /></el-icon> 提问
            </el-button>
            <el-button @click="$router.push('/questions')" style="width: 100%">
              <el-icon><Search /></el-icon> 浏览问题
            </el-button>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getQuestionList } from '../api/question'
import AppHeader from '../components/AppHeader.vue'

const router = useRouter()
const questions = ref([])

onMounted(async () => {
  questions.value = await getQuestionList()
})

const viewQuestion = (id) => {
  router.push(`/question/${id}`)
}
</script>

<style scoped>
.home-container {
  max-width: 1200px;
  margin: 20px auto;
  padding: 0 20px;
}
.question-item {
  padding: 15px 0;
  border-bottom: 1px solid #eee;
  cursor: pointer;
}
.question-item:hover {
  background: #f5f7fa;
}
.question-item h4 {
  color: #333;
  margin-bottom: 5px;
}
.meta {
  color: #999;
  font-size: 13px;
}
</style>
