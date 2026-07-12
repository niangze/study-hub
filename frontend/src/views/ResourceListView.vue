<template>
  <div>
    <AppHeader />
    <div class="page-container">
      <el-card>
        <template #header>
          <div class="header-flex">
            <span>资源分享</span>
            <div class="action-bar">
              <el-input v-model="keyword" placeholder="搜索资源" style="width: 250px; margin-right: 10px" @keyup.enter="handleSearch">
                <template #append>
                  <el-button @click="handleSearch"><el-icon><Search /></el-icon></el-button>
                </template>
              </el-input>
              <el-button v-if="userStore.isLoggedIn" type="primary" @click="showUpload = true">
                <el-icon><Upload /></el-icon> 上传资源
              </el-button>
            </div>
          </div>
        </template>

        <!-- 资源列表 -->
        <el-empty v-if="resources.length === 0" description="暂无资源" />
        <el-row v-else :gutter="20">
          <el-col v-for="r in resources" :key="r.id" :span="8" style="margin-bottom: 20px">
            <el-card shadow="hover" class="resource-card">
              <div class="resource-title">{{ r.title }}</div>
              <div class="resource-desc">{{ r.description }}</div>
              <div class="resource-meta">
                <el-tag size="small">{{ r.category }}</el-tag>
                <span class="points" v-if="r.pointsCost > 0">{{ r.pointsCost }} 积分</span>
                <span class="points free" v-else>免费</span>
              </div>
              <div class="resource-footer">
                <span class="download-count">
                  <el-icon><Download /></el-icon> {{ r.downloadCount }} 次下载
                </span>
                <el-button size="small" type="primary" @click="handleDownload(r.id)">下载</el-button>
              </div>
            </el-card>
          </el-col>
        </el-row>

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

    <!-- 上传弹窗 -->
    <el-dialog v-model="showUpload" title="上传资源" width="500px" :close-on-click-modal="false">
      <el-form :model="uploadForm" label-width="80px" ref="uploadFormRef" :rules="uploadRules">
        <el-form-item label="标题" prop="title">
          <el-input v-model="uploadForm.title" placeholder="资源标题" maxlength="200" show-word-limit />
        </el-form-item>
        <el-form-item label="分类" prop="category">
          <el-select v-model="uploadForm.category" placeholder="选择分类" style="width: 100%">
            <el-option label="Java" value="Java" />
            <el-option label="数据库" value="数据库" />
            <el-option label="前端" value="前端" />
            <el-option label="算法" value="算法" />
            <el-option label="Spring" value="Spring" />
            <el-option label="运维" value="运维" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="积分" prop="pointsCost">
          <el-input-number v-model="uploadForm.pointsCost" :min="0" :max="50" />
        </el-form-item>
        <el-form-item label="文件链接" prop="fileUrl">
          <el-input v-model="uploadForm.fileUrl" placeholder="输入文件下载链接（如百度网盘、GitHub等）" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="uploadForm.description" type="textarea" :rows="4" placeholder="资源描述..." />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showUpload = false">取消</el-button>
        <el-button type="primary" @click="handleUpload" :loading="uploading">上传</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getResourceList, uploadResource, downloadResource } from '../api/resource'
import { useUserStore } from '../store/user'
import AppHeader from '../components/AddHeader.vue'

const userStore = useUserStore()
const resources = ref([])
const keyword = ref('')
const page = ref(1)
const size = ref(9)
const total = ref(0)
const showUpload = ref(false)
const uploading = ref(false)
const uploadFormRef = ref(null)

const uploadForm = reactive({
  title: '',
  description: '',
  category: '',
  fileUrl: '',
  pointsCost: 0
})

const uploadRules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  category: [{ required: true, message: '请选择分类', trigger: 'change' }],
  fileUrl: [{ required: true, message: '请输入文件链接', trigger: 'blur' }]
}

onMounted(() => {
  fetchResources()
})

const fetchResources = async () => {
  try {
    const params = {
      page: page.value,
      size: size.value,
      keyword: keyword.value || undefined
    }
    const res = await getResourceList(params)
    resources.value = res.list || []
    total.value = res.total || 0
  } catch (e) {
    console.error('获取资源列表失败:', e)
  }
}

const handleSearch = () => {
  page.value = 1
  fetchResources()
}

const handlePageChange = () => {
  fetchResources()
}

const handleDownload = async (id) => {
  try {
    await downloadResource(id)
    ElMessage.success('下载计数已增加')
    fetchResources()
  } catch (e) {
    ElMessage.error('下载失败')
  }
}

const handleUpload = async () => {
  if (!uploadFormRef.value) return
  try {
    await uploadFormRef.value.validate()
    uploading.value = true
    await uploadResource({
      title: uploadForm.title,
      description: uploadForm.description,
      category: uploadForm.category,
      fileUrl: uploadForm.fileUrl,
      pointsCost: uploadForm.pointsCost
    })
    ElMessage.success('上传成功')
    showUpload.value = false
    // 重置表单
    uploadForm.title = ''
    uploadForm.description = ''
    uploadForm.category = ''
    uploadForm.fileUrl = ''
    uploadForm.pointsCost = 0
    fetchResources()
  } catch (e) {
    if (e.message) ElMessage.error(e.message || '上传失败')
  } finally {
    uploading.value = false
  }
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
.action-bar {
  display: flex;
  align-items: center;
}
.resource-card {
  height: 100%;
  display: flex;
  flex-direction: column;
}
.resource-title {
  font-size: 16px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 10px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.resource-desc {
  color: #606266;
  font-size: 14px;
  line-height: 1.6;
  margin-bottom: 12px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  min-height: 42px;
}
.resource-meta {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 12px;
}
.points {
  color: #e6a23c;
  font-size: 13px;
  font-weight: bold;
}
.points.free {
  color: #67c23a;
}
.resource-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: auto;
  padding-top: 10px;
  border-top: 1px solid #f0f0f0;
}
.download-count {
  color: #909399;
  font-size: 13px;
  display: flex;
  align-items: center;
  gap: 4px;
}
</style>
