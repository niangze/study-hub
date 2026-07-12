<template>
  <div>
    <AppHeader />
    <div class="home-container">
      <el-row :gutter="20">
        <el-col :span="16">
          <!-- 热门问题 -->
          <el-card style="margin-bottom: 20px">
            <template #header>
              <span>热门问题</span>
            </template>
            <el-empty v-if="hotQuestions.length === 0" description="暂无热门问题" />
            <div v-for="q in hotQuestions" :key="q.id" class="question-item" @click="viewQuestion(q.id)">
              <h4>{{ q.title }}</h4>
              <p class="meta">
                <span>悬赏: {{ q.pointsReward }} 积分</span>
                <span> | 回答: {{ q.answerCount }} | 浏览: {{ q.viewCount }}</span>
              </p>
            </div>
          </el-card>
          <!-- 最新问题 -->
          <el-card>
            <template #header>
              <span>最新问题</span>
              <el-button link type="primary" @click="$router.push('/questions')">查看更多</el-button>
            </template>
            <el-empty v-if="latestQuestions.length === 0" description="暂无问题" />
            <div v-for="q in latestQuestions" :key="q.id" class="question-item" @click="viewQuestion(q.id)">
              <h4>{{ q.title }}</h4>
              <p class="meta">
                <span>悬赏: {{ q.pointsReward }} 积分</span>
                <span> | 状态: {{ q.status }} | 作者: {{ q.username }}</span>
              </p>
            </div>
          </el-card>
        </el-col>
        <el-col :span="8">
          <el-card style="margin-bottom: 20px">
            <template #header>快速操作</template>
            <el-button type="primary" @click="$router.push('/ask')" style="width: 100%; margin-bottom: 10px">
              <el-icon><Plus /></el-icon> 提问
            </el-button>
            <el-button @click="$router.push('/questions')" style="width: 100%; margin-bottom: 10px">
              <el-icon><Search /></el-icon> 浏览问题
            </el-button>
            <el-button @click="$router.push('/resources')" style="width: 100%">
              <el-icon><Document /></el-icon> 浏览资源
            </el-button>
          </el-card>
          <!-- 分类 -->
          <el-card>
            <template #header>分类</template>
            <el-empty v-if="categories.length === 0" description="暂无分类" />
            <div v-for="c in categories" :key="c.id" class="category-item" @click="$router.push(`/questions?category=${c.id}`)">
              {{ c.name }}
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getHotQuestions, getLatestQuestions } from '../api/question'
import { getCategoryList } from '../api/category'
import AppHeader from '../components/AddHeader.vue'

const router = useRouter()
const hotQuestions = ref([])
const latestQuestions = ref([])
const categories = ref([])

onMounted(async () => {
  try {
    const [hot, latest, cats] = await Promise.all([
      getHotQuestions(5),
      getLatestQuestions(5),
      getCategoryList()
    ])
    hotQuestions.value = hot || []
    latestQuestions.value = latest || []
    categories.value = cats || []
  } catch (e) {
    console.error('首页数据加载失败:', e)
  }
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
.category-item {
  padding: 10px;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  color: #606266;
}
.category-item:hover {
  color: #409eff;
  background: #f5f7fa;
}
</style>
