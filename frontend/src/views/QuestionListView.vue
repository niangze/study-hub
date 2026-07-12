<template>
  <div>
    <AppHeader />
    <div class="page-container">
      <el-card>
        <template #header>
          <div class="header-flex">
            <div class="left-actions">
              <span style="font-weight: bold; margin-right: 15px">问题列表</span>
              <el-button type="primary" size="small" @click="$router.push('/ask')">
                <el-icon><Plus /></el-icon> 提问
              </el-button>
            </div>
            <div class="search-bar">
              <el-select v-model="categoryId" placeholder="分类" clearable style="width: 150px; margin-right: 10px" @change="handleCategoryChange">
                <el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.id" />
              </el-select>
              <el-input v-model="keyword" placeholder="搜索问题" style="width: 250px" @keyup.enter="handleSearch">
                <template #append>
                  <el-button @click="handleSearch"><el-icon><Search /></el-icon></el-button>
                </template>
              </el-input>
            </div>
          </div>
        </template>
        <el-empty v-if="questions.length === 0" description="暂无问题" />
        <div v-for="q in questions" :key="q.id" class="question-card" @click="viewQuestion(q.id)">
          <h3>{{ q.title }}</h3>
          <p>{{ q.content }}</p>
          <div class="meta">
            <el-tag size="small">悬赏 {{ q.pointsReward }} 积分</el-tag>
            <el-tag :type="q.status === 'OPEN' ? 'success' : 'info'" size="small">{{ q.status }}</el-tag>
            <span class="author">{{ q.username }} | 回答 {{ q.answerCount }} | 浏览 {{ q.viewCount }}</span>
          </div>
        </div>
        <!-- 分页 -->
        <el-pagination
          v-if="total > 0"
          v-model:current-page="page"
          v-model:page-size="size"
          :total="total"
          layout="total, prev, pager, next"
          @current-change="handlePageChange"
          style="margin-top: 20px; justify-content: center"
        />
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getQuestionList } from '../api/question'
import { getCategoryList } from '../api/category'
import AppHeader from '../components/AddHeader.vue'

const router = useRouter()
const questions = ref([])
const keyword = ref('')
const categoryId = ref('')
const categories = ref([])
const page = ref(1)
const size = ref(10)
const total = ref(0)

onMounted(async () => {
  await Promise.all([fetchQuestions(), fetchCategories()])
})

const fetchQuestions = async () => {
  try {
    const params = {
      page: page.value,
      size: size.value,
      keyword: keyword.value || undefined,
      categoryId: categoryId.value || undefined
    }
    const res = await getQuestionList(params)
    questions.value = res.list || []
    total.value = res.total || 0
  } catch (e) {
    console.error('获取问题列表失败:', e)
  }
}

const fetchCategories = async () => {
  try {
    categories.value = await getCategoryList() || []
  } catch (e) {
    console.error('获取分类失败:', e)
  }
}

const handleSearch = () => {
  page.value = 1
  fetchQuestions()
}

const handleCategoryChange = () => {
  page.value = 1
  fetchQuestions()
}

const handlePageChange = () => {
  fetchQuestions()
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
.left-actions {
  display: flex;
  align-items: center;
}
.search-bar {
  display: flex;
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
  align-items: center;
  gap: 10px;
}
.author {
  color: #999;
  font-size: 13px;
}
</style>
