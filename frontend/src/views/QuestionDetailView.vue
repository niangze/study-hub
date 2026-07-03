<template>
  <div>
    <AppHeader />
    <div class="page-container" v-if="question">
      <el-card>
        <h2>{{ question.title }}</h2>
        <p class="content">{{ question.content }}</p>
        <div class="meta">
          <el-tag>悬赏 {{ question.pointsReward }} 积分</el-tag>
          <el-tag :type="question.status === 'OPEN' ? 'success' : 'info'">{{ question.status }}</el-tag>
        </div>
      </el-card>
      
      <el-card style="margin-top: 20px">
        <template #header>回答 ({{ answers.length }})</template>
        <el-empty v-if="answers.length === 0" description="暂无回答，来做第一个回答者吧！" />
        <div v-for="a in answers" :key="a.id" class="answer-item">
          <p>{{ a.content }}</p>
          <el-tag v-if="a.isAccepted" type="success">已采纳</el-tag>
          <el-button v-else-if="isQuestionOwner" size="small" type="primary" @click="handleAccept(a.id)">采纳</el-button>
        </div>
      </el-card>
      
      <el-card style="margin-top: 20px">
        <template #header>提交回答</template>
        <el-input v-model="answerContent" type="textarea" :rows="4" placeholder="写下你的回答..." />
        <el-button type="primary" @click="submitAnswer" style="margin-top: 10px">提交</el-button>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getAnswersByQuestion, createAnswer } from '../api/answer'
import { acceptAnswer } from '../api/question'
import AppHeader from '../components/AppHeader.vue'

const route = useRoute()
const question = ref(null)
const answers = ref([])
const answerContent = ref('')
const isQuestionOwner = ref(false)

onMounted(async () => {
  // TODO: 获取问题详情
  question.value = { id: route.params.id, title: '示例问题', content: '问题内容...', pointsReward: 10, status: 'OPEN' }
  answers.value = await getAnswersByQuestion(route.params.id)
})

const handleAccept = async (answerId) => {
  await acceptAnswer(answerId)
  answers.value = await getAnswersByQuestion(route.params.id)
}

const submitAnswer = async () => {
  await createAnswer({
    content: answerContent.value,
    questionId: route.params.id
  })
  answerContent.value = ''
  answers.value = await getAnswersByQuestion(route.params.id)
}
</script>

<style scoped>
.page-container {
  max-width: 1000px;
  margin: 20px auto;
  padding: 0 20px;
}
.content {
  margin: 15px 0;
  line-height: 1.8;
  color: #333;
}
.meta {
  display: flex;
  gap: 10px;
  margin-top: 10px;
}
.answer-item {
  padding: 15px;
  border-bottom: 1px solid #eee;
}
</style>
