<template>
  <div>
    <AppHeader />
    <div class="page-container" v-if="question">
      <!-- 问题卡片 -->
      <el-card>
        <h2>{{ question.title }}</h2>
        <p class="content">{{ question.content }}</p>
        <div class="meta">
          <el-tag>悬赏 {{ question.pointsReward }} 积分</el-tag>
          <el-tag :type="question.status === 'OPEN' ? 'success' : 'info'">{{ question.status }}</el-tag>
          <span class="author">
            作者: {{ question.username }} | 回答: {{ question.answerCount }} | 浏览: {{ question.viewCount }}
          </span>
          <el-button
            v-if="userStore.isLoggedIn"
            :type="questionLike.isLiked ? 'danger' : 'default'"
            size="small"
            @click="toggleQuestionLike"
          >
            <el-icon><Pointer /></el-icon> {{ questionLike.likeCount || 0 }}
          </el-button>
          <span v-else class="like-count"><el-icon><Pointer /></el-icon> {{ questionLike.likeCount || 0 }}</span>
        </div>
      </el-card>
      
      <!-- 回答列表 -->
      <el-card style="margin-top: 20px">
        <template #header>回答 ({{ answers.length }})</template>
        <el-empty v-if="answers.length === 0" description="暂无回答，来做第一个回答者吧！" />
        <div v-for="a in answers" :key="a.id" class="answer-item">
          <p>{{ a.content }}</p>
          <div class="answer-meta">
            <span>回答者: {{ a.username }}</span>
            <el-tag v-if="a.isAccepted" type="success">已采纳</el-tag>
            <el-button v-else-if="isQuestionOwner" size="small" type="primary" @click="handleAccept(a.id)">采纳</el-button>
            <el-button
              v-if="userStore.isLoggedIn"
              :type="answerLikes[a.id]?.isLiked ? 'danger' : 'default'"
              size="small"
              @click="toggleAnswerLike(a.id)"
            >
              <el-icon><Pointer /></el-icon> {{ answerLikes[a.id]?.likeCount || 0 }}
            </el-button>
            <span v-else class="like-count"><el-icon><Pointer /></el-icon> {{ answerLikes[a.id]?.likeCount || 0 }}</span>
            <el-button v-if="userStore.isLoggedIn" size="small" link type="info" @click="showCommentInput = showCommentInput === a.id ? null : a.id">
              <el-icon><ChatDotRound /></el-icon> 评论
            </el-button>
          </div>
          
          <!-- 评论输入框 -->
          <div v-if="showCommentInput === a.id" style="margin-top: 10px">
            <el-input v-model="commentContent" type="textarea" :rows="2" placeholder="写下你的评论..." />
            <div style="margin-top: 5px; text-align: right">
              <el-button size="small" @click="showCommentInput = null">取消</el-button>
              <el-button size="small" type="primary" @click="submitComment(a.id)">发送</el-button>
            </div>
          </div>
          
          <!-- 评论列表 -->
          <div v-if="comments[a.id] && comments[a.id].length > 0" class="comment-list">
            <div v-for="c in comments[a.id]" :key="c.id" class="comment-item">
              <span class="comment-author">{{ c.username }}:</span>
              <span class="comment-content">{{ c.content }}</span>
            </div>
          </div>
        </div>
      </el-card>
      
      <!-- 提交回答 -->
      <el-card style="margin-top: 20px" v-if="userStore.isLoggedIn">
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
import { toggleLike, getLikeInfo } from '../api/like'
import { createComment, getCommentsByAnswer } from '../api/comment'
import { useUserStore } from '../store/user'
import { ElMessage } from 'element-plus'
import AppHeader from '../components/AddHeader.vue'

const route = useRoute()
const userStore = useUserStore()
const question = ref(null)
const answers = ref([])
const answerContent = ref('')
const questionLike = ref({ likeCount: 0, isLiked: false })
const answerLikes = ref({})
const comments = ref({})
const showCommentInput = ref(null)
const commentContent = ref('')

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
    answers.value = await getAnswersByQuestion(questionId)
    
    // 获取问题点赞信息
    const qLike = await getLikeInfo(questionId, 'QUESTION')
    questionLike.value = qLike || { likeCount: 0, isLiked: false }
    
    // 获取每条回答的点赞和评论
    for (const a of answers.value) {
      const aLike = await getLikeInfo(a.id, 'ANSWER')
      answerLikes.value[a.id] = aLike || { likeCount: 0, isLiked: false }
      const cList = await getCommentsByAnswer(a.id)
      comments.value[a.id] = cList || []
    }
  } catch (e) {
    console.error('获取问题详情失败:', e)
  }
}

const toggleQuestionLike = async () => {
  try {
    const res = await toggleLike({ targetId: question.value.id, targetType: 'QUESTION' })
    questionLike.value = res
  } catch (e) {
    ElMessage.error('操作失败')
  }
}

const toggleAnswerLike = async (answerId) => {
  try {
    const res = await toggleLike({ targetId: answerId, targetType: 'ANSWER' })
    answerLikes.value[answerId] = res
  } catch (e) {
    ElMessage.error('操作失败')
  }
}

const handleAccept = async (answerId) => {
  try {
    await acceptAnswer(answerId)
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
    ElMessage.success('回答成功')
  } catch (e) {
    console.error('提交回答失败:', e)
  }
}

const submitComment = async (answerId) => {
  if (!commentContent.value.trim()) return
  try {
    await createComment({ answerId, content: commentContent.value })
    commentContent.value = ''
    showCommentInput.value = null
    const cList = await getCommentsByAnswer(answerId)
    comments.value[answerId] = cList || []
    ElMessage.success('评论成功')
  } catch (e) {
    ElMessage.error('评论失败')
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
  flex-wrap: wrap;
}
.author {
  color: #999;
  font-size: 13px;
}
.like-count {
  color: #999;
  font-size: 13px;
  display: flex;
  align-items: center;
  gap: 4px;
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
  flex-wrap: wrap;
}
.comment-list {
  margin-top: 10px;
  padding: 10px;
  background: #f5f7fa;
  border-radius: 4px;
}
.comment-item {
  padding: 5px 0;
  font-size: 13px;
}
.comment-author {
  color: #409eff;
  font-weight: bold;
  margin-right: 5px;
}
.comment-content {
  color: #606266;
}
</style>
