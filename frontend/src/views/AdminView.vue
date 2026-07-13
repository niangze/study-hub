<template>
  <div>
    <AppHeader />
    <div class="page-container">
      <h2>后台管理</h2>
      
      <!-- 统计卡片 -->
      <el-row :gutter="20" style="margin-top: 20px">
        <el-col :span="6">
          <el-card>
            <div class="stat">
              <div class="number">{{ stats.userCount || 0 }}</div>
              <div class="label">注册用户</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card>
            <div class="stat">
              <div class="number">{{ stats.questionCount || 0 }}</div>
              <div class="label">问题总数</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card>
            <div class="stat">
              <div class="number">{{ stats.answerCount || 0 }}</div>
              <div class="label">回答总数</div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card>
            <div class="stat">
              <div class="number">{{ stats.resourceCount || 0 }}</div>
              <div class="label">资源数量</div>
            </div>
          </el-card>
        </el-col>
      </el-row>

      <!-- 活跃用户统计 -->
      <el-card style="margin-top: 20px">
        <template #header>
          <span>活跃用户统计</span>
        </template>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="active-stat">
              <div class="active-number">{{ questions.length > 0 ? new Set(questions.map(q => q.username)).size : 0 }}</div>
              <div class="active-label">活跃提问用户</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="active-stat">
              <div class="active-number">{{ questions.filter(q => q.status === 'OPEN').length }}</div>
              <div class="active-label">待解决问题</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="active-stat">
              <div class="active-number">{{ questions.filter(q => q.status === 'CLOSED').length }}</div>
              <div class="active-label">已解决问题</div>
            </div>
          </el-col>
        </el-row>
      </el-card>

      <!-- 分类维护 -->
      <el-card style="margin-top: 20px">
        <template #header>
          <div class="section-header">
            <span>课程分类维护</span>
            <el-button type="primary" size="small" @click="showCategoryDialog = true">新增分类</el-button>
          </div>
        </template>
        <el-table :data="categories" style="width: 100%">
          <el-table-column prop="id" label="ID" width="60" />
          <el-table-column prop="name" label="名称" width="120" />
          <el-table-column prop="description" label="描述" min-width="200" show-overflow-tooltip />
          <el-table-column prop="sortOrder" label="排序" width="70" />
          <el-table-column label="操作" width="120">
            <template #default="scope">
              <el-button size="small" type="danger" @click="deleteCategory(scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>

      <!-- 问题管理 -->
      <el-card style="margin-top: 20px">
        <template #header>
          <div class="section-header">
            <span>问题管理</span>
            <el-input v-model="questionKeyword" placeholder="搜索问题" style="width: 200px" size="small" @keyup.enter="fetchQuestions">
              <template #append>
                <el-button size="small" @click="fetchQuestions"><el-icon><Search /></el-icon></el-button>
              </template>
            </el-input>
          </div>
        </template>
        <el-table :data="questions" style="width: 100%" v-loading="questionLoading">
          <el-table-column prop="id" label="ID" width="60" />
          <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip />
          <el-table-column prop="username" label="作者" width="100" />
          <el-table-column prop="status" label="状态" width="80">
            <template #default="scope">
              <el-tag :type="scope.row.status === 'OPEN' ? 'success' : 'info'" size="small">{{ scope.row.status }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="answerCount" label="回答" width="70" />
          <el-table-column prop="viewCount" label="浏览" width="70" />
          <el-table-column label="操作" width="100">
            <template #default="scope">
              <el-button size="small" type="danger" @click="deleteQuestion(scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          v-if="questionTotal > 0"
          v-model:current-page="questionPage"
          v-model:page-size="questionSize"
          :total="questionTotal"
          layout="total, prev, pager, next"
          @current-change="fetchQuestions"
          style="margin-top: 10px; justify-content: center"
        />
      </el-card>

      <!-- 资源管理 -->
      <el-card style="margin-top: 20px">
        <template #header>
          <div class="section-header">
            <span>资源管理</span>
            <el-input v-model="resourceKeyword" placeholder="搜索资源" style="width: 200px" size="small" @keyup.enter="fetchResources">
              <template #append>
                <el-button size="small" @click="fetchResources"><el-icon><Search /></el-icon></el-button>
              </template>
            </el-input>
          </div>
        </template>
        <el-table :data="resources" style="width: 100%" v-loading="resourceLoading">
          <el-table-column prop="id" label="ID" width="60" />
          <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip />
          <el-table-column prop="category" label="分类" width="100" />
          <el-table-column prop="downloadCount" label="下载" width="80" />
          <el-table-column prop="pointsCost" label="积分" width="70">
            <template #default="scope">
              <span :style="scope.row.pointsCost > 0 ? 'color: #e6a23c' : 'color: #67c23a'">
                {{ scope.row.pointsCost > 0 ? scope.row.pointsCost : '免费' }}
              </span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="100">
            <template #default="scope">
              <el-button size="small" type="danger" @click="deleteResource(scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
          v-if="resourceTotal > 0"
          v-model:current-page="resourcePage"
          v-model:page-size="resourceSize"
          :total="resourceTotal"
          layout="total, prev, pager, next"
          @current-change="fetchResources"
          style="margin-top: 10px; justify-content: center"
        />
      </el-card>
    </div>

    <!-- 新增分类弹窗 -->
    <el-dialog v-model="showCategoryDialog" title="新增分类" width="400px">
      <el-form :model="categoryForm" label-width="60px">
        <el-form-item label="名称" required>
          <el-input v-model="categoryForm.name" placeholder="分类名称" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="categoryForm.description" type="textarea" :rows="3" placeholder="分类描述" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="categoryForm.sortOrder" :min="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showCategoryDialog = false">取消</el-button>
        <el-button type="primary" @click="addCategory">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getDashboardStats } from '../api/admin'
import { getQuestionList, deleteQuestion as apiDeleteQuestion } from '../api/question'
import { getResourceList, deleteResource as apiDeleteResource } from '../api/resource'
import { getCategoryList } from '../api/category'
import AppHeader from '../components/AddHeader.vue'

// 统计
const stats = ref({})
const categories = ref([])
const showCategoryDialog = ref(false)
const categoryForm = ref({ name: '', description: '', sortOrder: 0 })

// 问题管理
const questions = ref([])
const questionKeyword = ref('')
const questionPage = ref(1)
const questionSize = ref(10)
const questionTotal = ref(0)
const questionLoading = ref(false)

// 资源管理
const resources = ref([])
const resourceKeyword = ref('')
const resourcePage = ref(1)
const resourceSize = ref(10)
const resourceTotal = ref(0)
const resourceLoading = ref(false)

onMounted(() => {
  fetchStats()
  fetchCategories()
  fetchQuestions()
  fetchResources()
})

const fetchStats = async () => {
  try {
    const data = await getDashboardStats()
    stats.value = data || {}
  } catch (e) {
    console.error('获取统计数据失败:', e)
  }
}

const fetchCategories = async () => {
  try {
    categories.value = await getCategoryList() || []
  } catch (e) {
    console.error('获取分类失败:', e)
  }
}

const fetchQuestions = async () => {
  questionLoading.value = true
  try {
    const params = {
      page: questionPage.value,
      size: questionSize.value,
      keyword: questionKeyword.value || undefined
    }
    const res = await getQuestionList(params)
    questions.value = res.list || []
    questionTotal.value = res.total || 0
  } catch (e) {
    console.error('获取问题列表失败:', e)
  } finally {
    questionLoading.value = false
  }
}

const fetchResources = async () => {
  resourceLoading.value = true
  try {
    const params = {
      page: resourcePage.value,
      size: resourceSize.value,
      keyword: resourceKeyword.value || undefined
    }
    const res = await getResourceList(params)
    resources.value = res.list || []
    resourceTotal.value = res.total || 0
  } catch (e) {
    console.error('获取资源列表失败:', e)
  } finally {
    resourceLoading.value = false
  }
}

const deleteQuestion = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除这个问题吗？', '提示', { type: 'warning' })
    await apiDeleteQuestion(id)
    ElMessage.success('删除成功')
    fetchQuestions()
    fetchStats()
  } catch (e) {
    if (e !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const deleteResource = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除这个资源吗？', '提示', { type: 'warning' })
    await apiDeleteResource(id)
    ElMessage.success('删除成功')
    fetchResources()
    fetchStats()
  } catch (e) {
    if (e !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const deleteCategory = async (id) => {
  try {
    await ElMessageBox.confirm('确定要删除这个分类吗？', '提示', { type: 'warning' })
    ElMessage.info('分类删除功能需后端支持，当前仅展示')
  } catch (e) {
    // cancelled
  }
}

const addCategory = () => {
  if (!categoryForm.value.name.trim()) {
    ElMessage.warning('请输入分类名称')
    return
  }
  ElMessage.info('分类新增功能需后端支持，当前仅展示')
  showCategoryDialog.value = false
  categoryForm.value = { name: '', description: '', sortOrder: 0 }
}
</script>

<style scoped>
.page-container {
  max-width: 1200px;
  margin: 20px auto;
  padding: 0 20px;
}
.stat {
  text-align: center;
}
.number {
  font-size: 36px;
  font-weight: bold;
  color: #409eff;
}
.label {
  color: #999;
  margin-top: 5px;
}
.active-stat {
  text-align: center;
  padding: 15px;
}
.active-number {
  font-size: 28px;
  font-weight: bold;
  color: #67c23a;
}
.active-label {
  color: #999;
  margin-top: 5px;
  font-size: 14px;
}
.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
