<template>
  <div>
    <AppHeader />
    <div class="page-container">
      <h2>后台管理</h2>
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
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getDashboardStats } from '../api/admin'
import AppHeader from '../components/AddHeader.vue'

const stats = ref({})

onMounted(async () => {
  try {
    const data = await getDashboardStats()
    stats.value = data || {}
  } catch (e) {
    console.error('获取统计数据失败:', e)
  }
})
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
</style>
