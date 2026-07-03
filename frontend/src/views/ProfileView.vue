<template>
  <div>
    <AppHeader />
    <div class="page-container">
      <el-card>
        <template #header><h3>个人中心</h3></template>
        <el-descriptions :column="1" border>
          <el-descriptions-item label="用户名">demo_user</el-descriptions-item>
          <el-descriptions-item label="邮箱">user@example.com</el-descriptions-item>
          <el-descriptions-item label="积分">100</el-descriptions-item>
          <el-descriptions-item label="角色">普通用户</el-descriptions-item>
        </el-descriptions>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import AppHeader from '../components/AppHeader.vue'
</script>

<style scoped>
.page-container {
  max-width: 800px;
  margin: 20px auto;
  padding: 0 20px;
}
</style>
