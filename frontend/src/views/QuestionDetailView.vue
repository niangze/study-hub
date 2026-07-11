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
          <span class="author">
            作者: {{ question.username }} | 回答: {{ question.answerCount }} | 浏览: {{ question.viewCount }}
          </span>
        </div>
      </el-card>
      
      <el-card style="margin-top: 20px">
        <template #header>回答 ({{ answers.length }})</template>
        <el-empty v-if="answers.length === 0" description="暂无回答，来做第一个回答者吧！" />
        <div v-for="a in answers" :key="a.id" class="answer-item">
          <p>{{ a.content }}</p>
          <div class="answer-meta">
            <span>回答者: {{ a.username }}</span>
            <el-tag v-if="a.isAccepted" type="success">已采纳</el-tag>
            <el-button v-else-if="isQuestionOwner" size="small" type="primary" @click="handleAccept(a.id)">采纳</el-button>
          </div>
        </div>
      </el-card>
      
      <el-card style="margin-top: 20px">
        <template #header>提交回答</template>
        <el-input v-model="answerContent" type="textarea" :rows="4" placeholder="写下你的回答..." />
        <el-button type="primary" @click="submitAnswer" style="margin-top: 10px">提交</el-button>
      </el-card>
    </div>
    <div v-else class="page-container">
      <el-empty description="问题加载中..." />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { getQuestionDetail } from '../api/question'
import { getAnswersByQuestion, createAnswer, acceptAnswer } from '../api/answer'
import { useUserStore } from '../store/user'
import AppHeader from '../components/AddHeader.vue'

const route = useRoute()
const userStore = useUserStore()
const question = ref(null)
const answers = ref([])
const answerContent = ref('')

const isQuestionOwner = computed(() => {
  return userStore.isLoggedIn && question.value && userStore.userInfo && 
         question.value.userId === userStore.userInfo.id
})

onMounted(async () => {
  await fetchData()
})

const fetchData = async () => {
  try {
    const questionId = route.params.id
    question.value = await getQuestionDetail(questionId)
    // 如果问题详情已包含回答列表，直接使用
    if (question.value && question.value.answers) {
      answers.value = question.value.answers
    } else {
      answers.value = await getAnswersByQuestion(questionId)
    }
  } catch (e) {
    console.error('获取问题详情失败:', e)
  }
}

const handleAccept = async (answerId) => {
  try {
    await acceptAnswer(answerId)
    // 刷新数据
    await fetchData()
  } catch (e) {
    console.error('采纳回答失败:', e)
  }
}

const submitAnswer = async () => {
  if (!answerContent.value.trim()) return
  try {
    await createAnswer({
      content: answerContent.value,
      questionId: route.params.id
    })
    answerContent.value = ''
    await fetchData()
  } catch (e) {
    console.error('提交回答失败:', e)
  }
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
  align-items: center;
  gap: 10px;
  margin-top: 10px;
}
.author {
  color: #999;
  font-size: 13px;
}
.answer-item {
  padding: 15px;
  border-bottom: 1px solid #eee;
}
.answer-meta {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-top: 10px;
  color: #999;
  font-size: 13px;
}
</style>
